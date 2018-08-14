package constructives;

import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.tools.RandomManager;
import structure.DividerInstance;
import structure.DividerSolution;

import java.util.Random;

public class ConstRandom implements Constructive<DividerInstance, DividerSolution> {

    @Override
    public DividerSolution constructSolution(DividerInstance instance) {
        Random rnd = RandomManager.getRandom();
        DividerSolution sol = new DividerSolution(instance);
        int nclusters = 1+rnd.nextInt(instance.getN()-1);
        for (int i = 0; i < nclusters; i++) {
            sol.createNewCluster();
        }
        int n = instance.getN();
        for (int i = 0; i < n; i++) {
            int c = rnd.nextInt(nclusters);
            sol.addToCluster(c, i);
        }
        return sol;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
