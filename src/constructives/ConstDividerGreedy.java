package constructives;

import grafo.optilib.metaheuristics.Constructive;
import structure.Cluster;
import structure.DividerInstance;
import structure.DividerSolution;
import structure.RestrictedList;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class ConstDividerGreedy implements Constructive<DividerInstance, DividerSolution> {

    protected double alpha;
    protected double mu;

    public ConstDividerGreedy(){}

    public ConstDividerGreedy(double alpha){
        this.alpha = alpha;
        this.mu = 0.0;
    }

    @Override
    public DividerSolution constructSolution(DividerInstance instance) {
        return this.getASolution(instance, new Random());
    }

    protected DividerSolution getASolution(DividerInstance instance, Random rnd){

        DividerSolution finalSolution = new DividerSolution(instance);
        finalSolution.startDestructive();

        // GET MU
        int min = instance.getMinEdges();
        int max = instance.getMaxEdges();
        this.mu = max - this.alpha * (max-min);

        int cluster_index = 0;
        Set<Integer> computedNodes = new HashSet<>();

        DividerSolution bestSolution = finalSolution;
        boolean firstIter = true;
        Cluster c = null;

        int refactor_iters = 10;


        // FOR EACH CLUSTER (CAN BE REPEATED IF PERFORMS)

        // GET RANDOM NODE (FIRST TIME)
        int baseNode;

        while(cluster_index < instance.getN() - 1 && refactor_iters > 0) {

            // ADD NODE TO A NEW SOLUTION
            DividerSolution auxSolution = new DividerSolution(bestSolution);
            int next_cluster = auxSolution.createNewCluster();
            c = auxSolution.getCluster(cluster_index);

            if(firstIter){
                baseNode = c.getRandomNode(rnd);
                firstIter = false;
            }else{
                RestrictedList candidates = new RestrictedList(instance, auxSolution.getCluster(cluster_index), mu, rnd);
                if(!candidates.isEmpty()){
                    baseNode = candidates.getNode();
                }else{
                    baseNode = c.getRandomNode(rnd);
                }

            }

            if(baseNode == -1){
                cluster_index = 0;
                refactor_iters--;
                continue;
            };

            auxSolution.moveToCluster(baseNode, cluster_index, next_cluster);

            // GET ADJACENT
            Queue<Integer> adjacents = new ArrayDeque<>(instance.getAdjacents(baseNode));

            while(adjacents.size() > 0){

                int node = adjacents.poll();

                if(!auxSolution.getCluster(cluster_index).contains(node)) continue;

                computedNodes.add(node);
                auxSolution.moveToCluster(node, cluster_index, next_cluster);

                // IF ADD NODE (OR A SET OF NODES) IMPROVE THE MODULARITY, REPLACE SOLUTION
                if(auxSolution.getModularity() > bestSolution.getModularity()){
                    bestSolution = new DividerSolution(auxSolution);
                }

                // ADD ADJACENT TO CHECK (ONLY IF THEY ARE NOT CHECKED BEFORE AND THEY ARE CONTAINED BY THE CLUSTER)
                for(int toAddNode: instance.getAdjacents(node)){
                    if(c.contains(toAddNode) && !computedNodes.contains(toAddNode)){
                        adjacents.offer(toAddNode);
                        computedNodes.add(toAddNode);
                    }
                }

            }

            if(bestSolution.getModularity() > finalSolution.getModularity()){
                // IF SOLUTION IMPROVES
                finalSolution = new DividerSolution(bestSolution);

            }

            cluster_index++;

            computedNodes = new HashSet<>();

        }

        finalSolution.removeEmptyClusters();

        return finalSolution;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
