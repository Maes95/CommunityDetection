package main;

import constructives.ConstDivider;
import constructives.ConstPerfect;
import constructives.ConstRandom;
import constructives.ConstDividerGreedy;
import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.structure.Solution;
import grafo.optilib.tools.RandomManager;
import structure.DividerInstance;
import grafo.optilib.structure.Instance;
import structure.DividerSolution;

public class Main {

    public static void main(String[] args) {
        RandomManager.setSeed(1234);

        DividerInstance instance = new DividerInstance("example2.txt");

        System.out.println("--- RANDOM ---");
        Constructive<DividerInstance, DividerSolution> c = new ConstRandom();
        DividerSolution s1 = c.constructSolution(instance);
        System.out.println(s1.toString());

        System.out.println("--- DIVIDER ---");
        Constructive<DividerInstance, DividerSolution> d = new ConstDivider();
        DividerSolution s2 = d.constructSolution(instance);
        System.out.println(s2.toString());

        System.out.println("--- GREEDY ---");
        Constructive<DividerInstance, DividerSolution> g = new ConstDividerGreedy(0.1);
        DividerSolution s3 = g.constructSolution(instance);
        System.out.println(s3.toString());

        System.out.println("--- PERFECT ---");
        Constructive<DividerInstance, DividerSolution> p = new ConstPerfect();
        DividerSolution s4 = p.constructSolution(instance);
        System.out.println(s4.toString());
    }
}
