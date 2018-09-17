package main;

import constructives.*;
import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.tools.RandomManager;
import structure.DividerInstance;
import structure.DividerSolution;

public class Main {

    public static void main(String[] args) {
        RandomManager.setSeed(123);

        DividerInstance instance = new DividerInstance("example3.txt");

        /*System.out.println("--- RANDOM ---");
        Constructive<DividerInstance, DividerSolution> c = new ConstRandom();
        DividerSolution s1 = c.constructSolution(instance);
        System.out.println(s1.toString());

        System.out.println("--- DIVIDER ---");
        Constructive<DividerInstance, DividerSolution> d = new ConstDivider();
        DividerSolution s2 = d.constructSolution(instance);
        System.out.println(s2.toString());

        System.out.println("--- GREEDY 1 ---");
        Constructive<DividerInstance, DividerSolution> g1 = new ConstDividerGreedy(0.01);
        DividerSolution s3 = g1.constructSolution(instance);
        System.out.println(s3.toString());

        System.out.println("--- GREEDY 2 ---");
        Constructive<DividerInstance, DividerSolution> g2 = new ConstDividerGreedy2(0.01);
        DividerSolution s4 = g2.constructSolution(instance);
        System.out.println(s4.toString());*/

        long start = System.currentTimeMillis();

        System.out.println("--- GREEDY LOCAL SEARCH ---");
        Constructive<DividerInstance, DividerSolution> ls = new ConstDividerGreedyLS(0.01);
        DividerSolution s5 = ls.constructSolution(instance);
        System.out.println(s5.toString());

        System.out.println("---------------");
        long total = System.currentTimeMillis() - start;
        System.out.println("TOTAL TIME: "+total);







    }
}
