package test.rival;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

//import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.junit.Test;

import beast.core.Distribution;
import beast.core.Logger;
import beast.core.Logger.LOGMODE;
import beast.core.MCMC;
import beast.core.Operator;
import beast.core.parameter.RealParameter;
import beast.core.util.CompoundDistribution;
import beast.core.util.ESS;
import beast.evolution.alignment.Alignment;
import beast.evolution.likelihood.TreeLikelihood;
import beast.evolution.operators.ScaleOperator;
import beast.evolution.operators.UpDownOperator;
import beast.evolution.sitemodel.SiteModel;
import beast.evolution.substitutionmodel.Frequencies;
import beast.evolution.substitutionmodel.HKY;
import beast.evolution.tree.TreeHeightLogger;
import beast.evolution.tree.TreeWithMetaDataLogger;
import beast.evolution.tree.coalescent.Coalescent;
import beast.evolution.tree.coalescent.ConstantPopulation;
import beast.evolution.tree.coalescent.TreeIntervals;
import beast.math.distributions.LogNormalDistributionModel;
import beast.math.distributions.OneOnX;
import beast.math.distributions.Prior;
import rival.evolution.geo.dispersal.DispersalKernelRandom;
import rival.evolution.geo.distance.LocationDistance;
import rival.evolution.likelihood.GeoLikelihood;
import rival.evolution.operators.LocationLabelOperator;
import rival.evolution.tree.RivalTree;
import rival.io.ReadLocFile;
import util.io.DataImporter;

public class MCMCRivalTest  {

	@Test
	public void testMcmcRival() throws Exception {

		String userPath = System.getProperty("user.dir");
		userPath += "/data/";
		
		String locationFileName = userPath + "/Banza.loc";
		ReadLocFile locationFile = new ReadLocFile();
		try {
			locationFile.readFile(locationFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LocationDistance locDist = new LocationDistance();
		locDist.initByName("readLocFile", locationFile, "typeDist", "euclidean");


		String alignmentFile = userPath+"/Banza.nex";
		System.out.println("Input reads file: "+alignmentFile);
		DataImporter dataImporter = new DataImporter(userPath);
		Alignment alignment  = dataImporter.importAlignment(alignmentFile);

		//Setup lambda, tau ...etc
		RealParameter lambda = new RealParameter(new Double[]{0.5});
		lambda.setID("lambda");
		lambda.initByName("lower", 0.0);
		lambda.initByName("upper", 1.0);
		
		RealParameter tau = new RealParameter(new Double[]{0.25});
		
		tau.initAndValidate();
		tau.setID("tau");
		
		RealParameter sigma = new RealParameter(new Double[]{1.0});
		sigma.setID("sigma");
		
		//Setup dispersal kernel
		DispersalKernelRandom dispersalKernel = new DispersalKernelRandom();
		dispersalKernel.initByName("distMatrix", locDist.getDistMat(), "param", sigma);

//		DispersalKernelRandom dispersalKernel2 = new DispersalKernelRandom();
//		dispersalKernel.initByName("distMatrix", locDist2.getDistMat(), "param", sigma2);

		
		//Setup rival/random tree
		RealParameter popSize = new RealParameter(new Double[]{10.0});
		popSize.setID("popSize.t");
		ConstantPopulation popFunction = new ConstantPopulation();
        popFunction.setInputValue("popSize", popSize);
        popFunction.setID("ConstantPopulation");

		RivalTree rivalTree = new RivalTree();
		rivalTree.initByName("taxa", alignment, "populationModel", popFunction, 
				"locationFile", locationFile);
		rivalTree.setID("Tree.t");
		
		//Setup geolikelihood
		GeoLikelihood geoLikelihood = new GeoLikelihood();
		geoLikelihood.initByName("lambda", lambda, "tau", tau, "tree", rivalTree, "kernel", dispersalKernel);;
		geoLikelihood.setID("GeoLikelihood");
		
				
		
		//HKY Model
		RealParameter freqParameter = new RealParameter(new Double[]{0.25,0.25,0.25,0.25});
		freqParameter.setID("freqParameter.s");
		
		Frequencies estimatedFreqs = new Frequencies();
		estimatedFreqs.initByName("frequencies", freqParameter);
		estimatedFreqs.setID("estimatedFreqs.s");
//        RealParameter parameter = new RealParameter(new Double[] { 1., 1., 1., 1. });
//		DeltaExchangeOperator d = new DeltaExchangeOperator();
//			d.initByName("parameter", parameter);
        
        RealParameter kappa = new RealParameter(new Double[] {27.40259});
        kappa.setID("kappa.s");
        HKY hky = new HKY();
        hky.initByName("kappa", kappa, "frequencies", estimatedFreqs);
        
        SiteModel siteModel = new SiteModel();
        siteModel.initByName("mutationRate", "1.0", "gammaCategoryCount", 1, "substModel", hky);

        //TreeLikelihood
        TreeLikelihood likelihood = new TreeLikelihood();
        likelihood.initByName("data", alignment, "tree", rivalTree, "siteModel", siteModel);
        likelihood.setID("treeLikelihood");

        //Setup Prior
        Distribution kappaPrior = new Prior();
        LogNormalDistributionModel lnd = new LogNormalDistributionModel();
        lnd.initByName("M", new RealParameter("1.0"), "S", new RealParameter("1.25"));
        kappaPrior.initByName("x", kappa, "distr", lnd);
        kappaPrior.setID("KappaPrior.s");

        Distribution coalescentConstant = new Coalescent();
        coalescentConstant.initByName("populationModel", popFunction, "treeIntervals", new TreeIntervals(rivalTree));
        coalescentConstant.setID("CoalescentConstant.t");
        
        Distribution popSizePrior = new Prior();
        OneOnX oox = new OneOnX();
        popSizePrior.initByName("x", popSize, "distr", oox);
        popSizePrior.setID("PopSizePrior.t");
        
        CompoundDistribution prior = new CompoundDistribution();
        prior.initByName("distribution", Arrays.asList(new Distribution[]{kappaPrior, coalescentConstant, popSizePrior})); //Init multiple distrubitons
//        prior.initByName("distribution", Arrays.asList(new Distribution[]{coalescentConstant}));
        prior.setID("prior");

        //Setup Posterior
        ArrayList<Distribution> distributionList = new ArrayList<>();
        distributionList.add(likelihood);
        distributionList.add(prior);
        distributionList.add(geoLikelihood);
        
        
        CompoundDistribution posterior = new CompoundDistribution();
        posterior.initByName("distribution", distributionList);
        posterior.setID("posterior");
        
        //Operators
        ArrayList<Operator> opsList = new ArrayList<>();
        BeastTestUtils.addDefaultOperators(opsList, rivalTree, kappa, freqParameter);

        //Geo Operators
        Operator op = new LocationLabelOperator();
        op.initByName("tree", rivalTree, "weight", 5.0);
        op.setID("locationLabelOperators");
        opsList.add(op);
        
        op = new ScaleOperator();
        op.initByName("parameter", tau , "scaleFactor", 0.75, "weight", 5.0);
        op.setID("tau");
        opsList.add(op);
        
        op = new ScaleOperator();
        op.initByName("parameter", lambda , "scaleFactor", 0.75, "weight", 5.0);
        op.setID("lambda");
        opsList.add(op);
        
        op = new UpDownOperator();
        op.initByName("scaleFactor", 0.75, "up", tau, "down", lambda, "weight", 3.0);
        op.setID("Up/Down");
        opsList.add(op);
        
        //Setup Logger
        Logger logger = new Logger();
        logger.setInputValue("fileName", userPath+"/result/result.log");
        logger.setInputValue("log", likelihood);
        logger.setInputValue("log", geoLikelihood);
        logger.setInputValue("log", prior);

        TreeHeightLogger thl = new TreeHeightLogger();
        thl.initByName("tree", rivalTree);
        logger.setInputValue("log", thl);
        logger.setInputValue("log", kappa);
        logger.setInputValue("log", popSize);
        logger.setInputValue("log", tau);
        logger.setInputValue("log", lambda);
        logger.setInputValue("log", freqParameter);
        
        logger.setInputValue("mode", LOGMODE.compound);
        logger.setInputValue("sanitiseHeaders", true);
        logger.initByName("logEvery", 1000);

        Logger loggerScreen = new Logger();
        loggerScreen.setInputValue("log", posterior);
        ESS ess = new ESS();
        ess.initByName("arg", posterior);
        loggerScreen.setInputValue("log", ess);
        loggerScreen.setInputValue("log", likelihood);
        loggerScreen.setInputValue("log", geoLikelihood);
        loggerScreen.setInputValue("log", prior);
        loggerScreen.setInputValue("log", tau);
        loggerScreen.setInputValue("log", lambda);
        loggerScreen.setInputValue("mode", LOGMODE.compound);
        loggerScreen.setInputValue("sanitiseHeaders", false);
        loggerScreen.initByName("logEvery", 1000);
        
        Logger loggerTree = new Logger();
        TreeWithMetaDataLogger tlog = new TreeWithMetaDataLogger();
        tlog.initByName("tree", rivalTree);
        loggerTree.setInputValue("fileName", userPath+"/result/Result.trees");
        loggerTree.setInputValue("log", tlog);
        loggerTree.setInputValue("mode", LOGMODE.autodetect);
        loggerTree.setInputValue("logEvery", 1000);
        loggerTree.initAndValidate();
        
        Logger.FILE_MODE = Logger.LogFileMode.overwrite;
        MCMC mcmc = new MCMC();

        mcmc.initByName("chainLength", 1000000,
        				"storeEvery", 1000,
        				"preBurnin", 0,
        				"operator", opsList,
        				"distribution", posterior,
        				"logger", logger,
        				"logger", loggerScreen,
        				"logger", loggerTree
        				);
        
    	mcmc.run();
		
	}
}
