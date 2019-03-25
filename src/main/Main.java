package main;

import algorithms.AlgorithmExecutor;
import constructives.*;
import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.tools.RandomManager;
import structure.DividerInstance;
import structure.DividerSolution;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws Exception {
        RandomManager.setSeed(123);

        DividerInstance instance = new DividerInstance("graphs/example3.txt");

        System.out.println("--- RANDOM ---");
        Constructive<DividerInstance, DividerSolution> c = new ConstRandom();
        DividerSolution s1 = c.constructSolution(instance);
        System.out.println(s1.getModularity());

        System.out.println("--- DIVIDER ---");
        Constructive<DividerInstance, DividerSolution> d = new ConstDivider();
        DividerSolution s2 = d.constructSolution(instance);
        System.out.println(s2.getModularity());

        /*System.out.println("--- PERFECT ---");
        Constructive<DividerInstance, DividerSolution> d = new ConstPerfect();
        DividerSolution s0 = d.constructSolution(instance);
        System.out.println(s0.toString());*/

        /*System.out.println("--- PERFECT SERGIO ---");
        Constructive<DividerInstance, DividerSolution> s = new ConstPerfectSergio();
        DividerSolution sS = s.constructSolution(instance);
        System.out.println(sS.toString());*/

        double[] alphas = {0.0, 0.25, 0.5, 0.75, 1};

        int iters = 1000;
        AlgorithmExecutor.EXECUTOR_TYPE t = AlgorithmExecutor.EXECUTOR_TYPE.MULTI;

        System.out.println("--- GREEDY ---");
        for(double alpha: alphas){
            AlgorithmExecutor aex = new AlgorithmExecutor(t);
            DividerSolution s = aex.calculateBestSolution(new ConstDividerGreedy(alpha), instance, iters);
            System.out.println("ALPHA: "+alpha+" MOD: "+s.getModularity());
        }

        System.out.println("--- GREEDY LOCAL SEARCH ---");
        for(double alpha: alphas){
            AlgorithmExecutor aex = new AlgorithmExecutor(t);
            DividerSolution s = aex.calculateBestSolution(new ConstDividerGreedyLS(alpha), instance, iters);
            System.out.println("ALPHA: "+alpha+" MOD: "+s.getModularity());
        }


    }
}
