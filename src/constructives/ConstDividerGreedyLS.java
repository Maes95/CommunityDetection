package constructives;

import grafo.optilib.metaheuristics.Constructive;
import structure.Cluster;
import structure.DividerInstance;
import structure.DividerSolution;
import structure.RestrictedList;

import java.util.*;

public class ConstDividerGreedyLS extends ConstDividerGreedy {

    private LocalSearch local_search;

    public ConstDividerGreedyLS(double alpha){
        this.alpha = alpha;
        this.mu = 0.0;
        this.local_search  = new LocalSearch();
    }

    @Override
    public DividerSolution constructSolution(DividerInstance instance) {
        DividerSolution newSolution = getASolution(instance, new Random());
        local_search.improve(newSolution);
        return newSolution;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
