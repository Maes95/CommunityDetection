package main;

import algorithms.AlgorithmExecutor;
import constructives.*;
import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.tools.RandomManager;
import structure.DividerInstance;
import structure.DividerSolution;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    private static final int ITERS = 100;
    private static final String PATH_TO_GRAPHS_TRAIN = "graphs/train/";
    private static final String PATH_TO_GRAPHS_TEST = "graphs/test/";
    private static final double[] ALPHAS = {0.25, 0.5, 0.75};
    private static final int N_ALGORITHMS = 8+1;
    private static final AlgorithmExecutor.EXECUTOR_TYPE EXECUTOR_TYPE = AlgorithmExecutor.EXECUTOR_TYPE.MULTI;

    public static void main(String[] args) throws Exception {
        RandomManager.setSeed(123);
        test();
        //train();
    }

    public static void test() throws Exception {
        FileWriter csvWriter = new FileWriter("results/results-test.csv");
        csvWriter.append("Instance, N, M, DividerGreedyLS 0.25,,,,\n");
        csvWriter.append(",,,Mod,T(ms), Dev(%), # Best\n");

        File[] files = new File(PATH_TO_GRAPHS_TEST).listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt")) {

                DividerInstance instance = new DividerInstance(PATH_TO_GRAPHS_TEST+file.getName());

                System.out.println("--- GREEDY LOCAL SEARCH ---");
                double alpha = 0.25;
                AlgorithmExecutor aex = new AlgorithmExecutor(EXECUTOR_TYPE);
                DividerSolution greedyLSSolution = aex.calculateBestSolution(new ConstDividerGreedyLS(alpha), instance, 100);
                System.out.println("ALPHA: "+alpha+" MOD: "+greedyLSSolution.getModularity());

                String[] row = {
                        file.getName(), // Instance
                        Integer.toString(instance.getN()), // N
                        Integer.toString(instance.getM()) // M
                };

                String[] aux = getRow(greedyLSSolution,0,false);
                row = concat(row, aux);
                csvWriter.append(String.join(",", row)+"\n");
                csvWriter.flush();

                String solution = greedyLSSolution.getSolutionAsList().toString();
                String filename = "results/community_"+file.getName();
                BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
                bw.write(solution);
                bw.close();
            }
        }

    }

    public static void train() throws Exception {

        FileWriter csvWriter = new FileWriter("results/results-train.csv");
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
        for(int i = 0; i <N_ALGORITHMS; i++){
            header2 += ",Mod,T(ms), Dev(%), # Best";
        }
        csvWriter.append(header1+"\n");
        csvWriter.append(header2+"\n");

        // Describe instances

        File[] files = new File(PATH_TO_GRAPHS_TRAIN).listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                DividerInstance instance = new DividerInstance(PATH_TO_GRAPHS_TRAIN+file.getName());
                System.out.println(file.getName()+" "+instance.getN()+" "+instance.getM());

                String[] row = {
                        file.getName(), // Instance
                        Integer.toString(instance.getN()), // N
                        Integer.toString(instance.getM()) // M
                };

                List<DividerSolution> solutions = new ArrayList<>(N_ALGORITHMS-1);


                System.out.println("--- RANDOM ---");
                Constructive<DividerInstance, DividerSolution> c = new ConstRandom();
                DividerSolution randomSolucion = c.constructSolution(instance);
                System.out.println(randomSolucion.getModularity());

                DividerSolution best = randomSolucion;
                solutions.add(randomSolucion);

                System.out.println("--- DIVIDER ---");
                Constructive<DividerInstance, DividerSolution> d = new ConstDivider();
                DividerSolution dividerSolution = d.constructSolution(instance);
                System.out.println(dividerSolution.getModularity());

                if(dividerSolution.isBetterThan(best)) best = dividerSolution;
                solutions.add(dividerSolution);

                System.out.println("--- GREEDY ---");
                for(double alpha: ALPHAS){
                    AlgorithmExecutor aex = new AlgorithmExecutor(EXECUTOR_TYPE);
                    DividerSolution greedySolution = aex.calculateBestSolution(new ConstDividerGreedy(alpha), instance, ITERS);
                    System.out.println("ALPHA: "+alpha+" MOD: "+greedySolution.getModularity());
                    if(greedySolution.isBetterThan(best)) best = greedySolution;
                    solutions.add(greedySolution);
                }

                System.out.println("--- GREEDY LOCAL SEARCH ---");
                for(double alpha: ALPHAS){
                    AlgorithmExecutor aex = new AlgorithmExecutor(EXECUTOR_TYPE);
                    DividerSolution greedyLSSolution = aex.calculateBestSolution(new ConstDividerGreedyLS(alpha), instance, ITERS);
                    System.out.println("ALPHA: "+alpha+" MOD: "+greedyLSSolution.getModularity());
                    if(greedyLSSolution.isBetterThan(best)) best = greedyLSSolution;
                    solutions.add(greedyLSSolution);
                }


                double bestMod = best.getModularity();
                for(DividerSolution s: solutions){
                    String[] aux = getRow(s,(bestMod-s.getModularity())/bestMod,bestMod==s.getModularity());
                    row = concat(row, aux);
                }

                row = concat(row, getRow(best,0,true));
                csvWriter.append(String.join(",", row)+"\n");
                csvWriter.flush();
            }
        }
    }

    public static String[] concat(String[] ...arrays) {
        return Stream.of(arrays)
                .flatMap(Arrays::stream)
                .toArray(String[]::new);
    }

    public static String[] getRow(DividerSolution dividerSolution, double dev, boolean isBest){
        String[] row = {
                Double.toString(dividerSolution.getModularity()),
                Long.toString(dividerSolution.getExecTime()),
                Double.toString(dev),
                isBest ? "1" : "0"
        };
        return row;
    }

}
