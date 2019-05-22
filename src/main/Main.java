package main;

import algorithms.AlgorithmExecutor;
import constructives.*;
import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.tools.RandomManager;
import structure.DividerInstance;
import structure.DividerSolution;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws Exception {
        RandomManager.setSeed(123);
        String PATH_TO_GRAPHS = "graphs/";

        FileWriter csvWriter = new FileWriter("results.csv");
        csvWriter.append("Instance, N, M, Random, Divider, DividerGreedy, , , , , , DividerGreedyLS, , , , \n");
        csvWriter.append(" , , , , ,0.0, 0.25, 0.5, 0.75, 1, 0.0, 0.25, 0.5, 0.75, 1\n");

        // Describe instances

        File[] files = new File(PATH_TO_GRAPHS).listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                DividerInstance instance = new DividerInstance(PATH_TO_GRAPHS+file.getName());
                System.out.println(file.getName()+" "+instance.getN()+" "+instance.getM());

                System.out.println("--- RANDOM ---");
                Constructive<DividerInstance, DividerSolution> c = new ConstRandom();
                DividerSolution randomSolucion = c.constructSolution(instance);
                System.out.println(randomSolucion.getModularity());

                System.out.println("--- DIVIDER ---");
                Constructive<DividerInstance, DividerSolution> d = new ConstDivider();
                DividerSolution dividerSolution = d.constructSolution(instance);
                System.out.println(dividerSolution.getModularity());

                double[] alphas = {0.0, 0.25, 0.5, 0.75, 1};

                int iters = 1;
                AlgorithmExecutor.EXECUTOR_TYPE t = AlgorithmExecutor.EXECUTOR_TYPE.MULTI;

                System.out.println("--- GREEDY ---");
                String[] greedyResults = new String[5];
                int i = 0;
                for(double alpha: alphas){
                    AlgorithmExecutor aex = new AlgorithmExecutor(t);
                    DividerSolution greedySolution = aex.calculateBestSolution(new ConstDividerGreedy(alpha), instance, iters);
                    System.out.println("ALPHA: "+alpha+" MOD: "+greedySolution.getModularity());
                    greedyResults[i] = Double.toString(greedySolution.getModularity());
                    i++;
                }

                System.out.println("--- GREEDY LOCAL SEARCH ---");
                String[] greedyLSResults = new String[5];
                int j = 0;
                for(double alpha: alphas){
                    AlgorithmExecutor aex = new AlgorithmExecutor(t);
                    DividerSolution greedyLSSolution = aex.calculateBestSolution(new ConstDividerGreedyLS(alpha), instance, iters);
                    System.out.println("ALPHA: "+alpha+" MOD: "+greedyLSSolution.getModularity());
                    greedyLSResults[j] = Double.toString(greedyLSSolution.getModularity());
                    j++;
                }

                String[] row = {
                        file.getName(), // Instance
                        Integer.toString(instance.getN()), // N
                        Integer.toString(instance.getM()), // M
                        Double.toString(randomSolucion.getModularity()), // RANDOM
                        Double.toString(dividerSolution.getModularity()), // DIVIDER
                };

                String[] c1 = combine(greedyResults, greedyLSResults);
                row = combine(row, c1);
                csvWriter.append(String.join(",", row)+"\n");
                //break;
            }
        }

        csvWriter.flush();
        csvWriter.close();

        /*System.out.println("--- RANDOM ---");
        Constructive<DividerInstance, DividerSolution> c = new ConstRandom();
        DividerSolution s1 = c.constructSolution(instance);
        System.out.println(s1.getModularity());

        System.out.println("--- DIVIDER ---");
        Constructive<DividerInstance, DividerSolution> d = new ConstDivider();
        DividerSolution s2 = d.constructSolution(instance);
        System.out.println(s2.getModularity());

        *//*System.out.println("--- PERFECT ---");
        Constructive<DividerInstance, DividerSolution> d = new ConstPerfect();
        DividerSolution s0 = d.constructSolution(instance);
        System.out.println(s0.toString());*//*

        *//*System.out.println("--- PERFECT SERGIO ---");
        Constructive<DividerInstance, DividerSolution> s = new ConstPerfectSergio();
        DividerSolution sS = s.constructSolution(instance);
        System.out.println(sS.toString());*//*

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
        }*/


    }

    public static String[] combine(String[] a, String[] b){
        int length = a.length + b.length;
        String[] result = new String[length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

}
