package main;

import algorithms.AlgorithmExecutor;
import constructives.*;
import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.tools.RandomManager;
import structure.DividerInstance;
import structure.DividerSolution;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.stream.Stream;

public class Main {

    private static final int ITERS = 1;
    private static final String PATH_TO_GRAPHS = "graphs/";
    private static final double[] ALPHAS = {0.25, 0.5, 0.75};
    private static final AlgorithmExecutor.EXECUTOR_TYPE EXECUTOR_TYPE = AlgorithmExecutor.EXECUTOR_TYPE.MULTI;

    public static void main(String[] args) throws Exception {
        RandomManager.setSeed(123);


        FileWriter csvWriter = new FileWriter("results.csv");
        String header1 = "Instance, N, M, " +
                "Random,,,,"+
                "Divider,,,,"+
                "DividerGreedy 0.25,,,,"+
                "DividerGreedy 0.5,,,,"+
                "DividerGreedy 0.75,,,,"+
                "DividerGreedyLS 0.25,,,,"+
                "DividerGreedyLS 0.5,,,,"+
                "DividerGreedyLS 0.75,,,,"+
                "Best,,,,";


        String header2 = ",,";
        for(int i = 0; i <9; i++){
            header2 += ",Mod,T(ms), Dev(%), # Best";
        }
        csvWriter.append(header1+"\n");
        csvWriter.append(header2+"\n");

        // Describe instances

        File[] files = new File(PATH_TO_GRAPHS).listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                DividerInstance instance = new DividerInstance(PATH_TO_GRAPHS+file.getName());
                System.out.println(file.getName()+" "+instance.getN()+" "+instance.getM());

                String[] row = {
                        file.getName(), // Instance
                        Integer.toString(instance.getN()), // N
                        Integer.toString(instance.getM()), // M
                };


                System.out.println("--- RANDOM ---");
                Constructive<DividerInstance, DividerSolution> c = new ConstRandom();
                DividerSolution randomSolucion = c.constructSolution(instance);
                System.out.println(randomSolucion.getModularity());

                DividerSolution best = randomSolucion;
                row = concat(row, getRow(randomSolucion));

                System.out.println("--- DIVIDER ---");
                Constructive<DividerInstance, DividerSolution> d = new ConstDivider();
                DividerSolution dividerSolution = d.constructSolution(instance);
                System.out.println(dividerSolution.getModularity());

                if(dividerSolution.isBetterThan(best)) best = dividerSolution;
                row = concat(row, getRow(dividerSolution));

                System.out.println("--- GREEDY ---");
                String[] greedyResults = new String[ALPHAS.length];
                int i = 0;
                for(double alpha: ALPHAS){
                    AlgorithmExecutor aex = new AlgorithmExecutor(EXECUTOR_TYPE);
                    DividerSolution greedySolution = aex.calculateBestSolution(new ConstDividerGreedy(alpha), instance, ITERS);
                    System.out.println("ALPHA: "+alpha+" MOD: "+greedySolution.getModularity());
                    greedyResults[i] = Double.toString(greedySolution.getModularity());
                    if(greedySolution.isBetterThan(best)) best = greedySolution;
                    row = concat(row, getRow(greedySolution));
                    i++;
                }

                System.out.println("--- GREEDY LOCAL SEARCH ---");
                String[] greedyLSResults = new String[ALPHAS.length];
                int j = 0;
                for(double alpha: ALPHAS){
                    AlgorithmExecutor aex = new AlgorithmExecutor(EXECUTOR_TYPE);
                    DividerSolution greedyLSSolution = aex.calculateBestSolution(new ConstDividerGreedyLS(alpha), instance, ITERS);
                    System.out.println("ALPHA: "+alpha+" MOD: "+greedyLSSolution.getModularity());
                    greedyLSResults[j] = Double.toString(greedyLSSolution.getModularity());
                    if(greedyLSSolution.isBetterThan(best)) best = greedyLSSolution;
                    row = concat(row, getRow(greedyLSSolution));
                    j++;
                }

                row = concat(row, getRow(best));
                csvWriter.append(String.join(",", row)+"\n");
                csvWriter.flush();
                break;
            }
        }


    }

    public static String[] concat(String[] ...arrays) {
        return Stream.of(arrays)
                .flatMap(Arrays::stream)
                .toArray(String[]::new);
    }

    public static String[] getRow(DividerSolution dividerSolution){
        String[] row = {
                Double.toString(dividerSolution.getModularity()),
                Long.toString(dividerSolution.getExecTime()),
                "-1",
                "0"
        };
        return row;
    }

}
