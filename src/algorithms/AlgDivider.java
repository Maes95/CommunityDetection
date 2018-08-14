package algorithms;

import grafo.optilib.results.Result;
import grafo.optilib.structure.Solution;
import structure.DividerInstance;
import grafo.optilib.metaheuristics.Algorithm;
import structure.DividerSolution;
import grafo.optilib.metaheuristics.Constructive;

public class AlgDivider implements Algorithm<DividerInstance>{

    private Constructive<DividerInstance, DividerSolution> divider;
    private int iters;
    private DividerSolution solution;

    public AlgDivider(Constructive<DividerInstance, DividerSolution> d, int iters){
        this.divider = d;
        this.iters = iters;
        this.solution = null;
    }

    @Override
    public Result execute(DividerInstance dividerInstance) {

        for (int i = 0; i < iters; i++) {
            DividerSolution newSol = divider.constructSolution(dividerInstance);
            if (solution == null || solution.getModularity() < newSol.getModularity()) {
                solution = newSol;
            }
        }
        return null;
    }

    @Override
    public Solution getBestSolution() {
        return null;
    }

}
