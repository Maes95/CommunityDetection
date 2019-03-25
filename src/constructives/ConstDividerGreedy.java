package constructives;

import grafo.optilib.metaheuristics.Constructive;
import structure.Cluster;
import structure.DividerInstance;
import structure.DividerSolution;
import structure.RestrictedList;

import java.util.*;

public class ConstDividerGreedy implements Constructive<DividerInstance, DividerSolution> {

    private double alpha;
    private double mu;

    public ConstDividerGreedy(double alpha){
        this.alpha = alpha;
        this.mu = 0.0;
    }

    @Override
    public DividerSolution constructSolution(DividerInstance instance) {

        DividerSolution bestSolution = new DividerSolution(instance);
        bestSolution.startDestructive();

        for(int i = 0; i < 100; i++){
            Random rnd = new Random();
            rnd.setSeed(i);
            DividerSolution newSolution = getASolution(instance, rnd);
            if(newSolution.getModularity() > bestSolution.getModularity()){
                bestSolution = newSolution;
            }
        }

        return  bestSolution;
    }

    private DividerSolution getASolution(DividerInstance instance, Random rnd){

        DividerSolution finalSolution = new DividerSolution(instance);
        finalSolution.startDestructive();

        // GET MU
        int min = instance.getMinEdges();
        int max = instance.getMaxEdges();
        this.mu = max - this.alpha * (max-min);

        int cluster_index = 0;
        Set<Integer> computedNodes = new HashSet<>();

        DividerSolution bestSolution = finalSolution;
        RestrictedList candidates = null;
        Cluster c = null;


        // FOR EACH CLUSTER (CAN BE REPEATED IF PERFORMS)

        // GET RANDOM NODE (FIRST TIME)
        int baseNode = bestSolution.getRandomNode(cluster_index, rnd);

        while(cluster_index < instance.getN() - 1 && baseNode != -1) {

            // ADD NODE TO A NEW SOLUTION
            DividerSolution auxSolution = new DividerSolution(bestSolution);
            int next_cluster = auxSolution.createNewCluster();
            c = auxSolution.getCluster(cluster_index);

            auxSolution.moveToCluster(baseNode, cluster_index, next_cluster);

            // GET ADJACENT
            Queue<Integer> adjacents = new ArrayDeque<>(instance.getAdjacents(baseNode));

            while(adjacents.size() > 0){

                int node = adjacents.poll();

                if(!c.contains(node)) continue;

                computedNodes.add(node);
                auxSolution.moveToCluster(node, cluster_index, cluster_index+1);

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

            if(candidates != null && !candidates.isEmpty()){
                // GET NEXT NODE TO MOVE (FROM RESTRICTED LIST)
                System.out.println("GET NODE");
                baseNode = candidates.getNode();
            }else{
                // IF EMPTY, CHECK NEXT CLUSTER AND GET RANDOM NODE
                cluster_index++;
                Cluster x = bestSolution.getCluster(cluster_index);
                if(x == null){
                    // TODAS ACABAN EN UN CLUSTER INNACESIBLE, ¿POR QUÉ? -> NO MEJORA
                    //cluster_index--; -> SE QUEDA EN BUCLE
                    // CAMBIAR AL CLUSTER MÁS GRANDE?
                    break;
                }else{
                    baseNode = x.getRandomNode(rnd);
                }


            }

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
