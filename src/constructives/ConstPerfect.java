package constructives;

import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.tools.RandomManager;
import structure.DividerInstance;
import structure.DividerSolution;

import java.util.Random;

public class ConstPerfect implements Constructive<DividerInstance, DividerSolution> {

    @Override
    public DividerSolution constructSolution(DividerInstance instance) {
        DividerSolution sol = new DividerSolution(instance);
        sol.createNewCluster();
        sol.createNewCluster();
        sol.createNewCluster();
        sol.addToCluster(0,0);
        sol.addToCluster(0,1);
        sol.addToCluster(2,2);
        sol.addToCluster(0,3);
        sol.addToCluster(0,8);
        sol.addToCluster(1,4);
        sol.addToCluster(1,5);
        sol.addToCluster(1,6);
        sol.addToCluster(1,7);
        sol.addToCluster(2,9);
        sol.addToCluster(2,10);
        sol.addToCluster(2,11);
        sol.addToCluster(2,12);
        return sol;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}