package constructives;

import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.tools.RandomManager;
import structure.Cluster;
import structure.DividerInstance;
import structure.DividerSolution;

import java.util.*;

public class ConstDivider implements Constructive<DividerInstance, DividerSolution> {

    @Override
    public DividerSolution constructSolution(DividerInstance instance) {

        DividerSolution sol = new DividerSolution(instance);
        sol.startDestructive();

        int cluster = 0;
        Set<Integer> computedNodes = new HashSet<>();

        DividerSolution bestSolution = sol;

        do{

            // GET WORST NODE
            int worstConnectedNode = bestSolution.getWorstConnected(cluster);

            // ADD NODE TO A NEW SOLUTION
            DividerSolution auxSolution = new DividerSolution(bestSolution);
            auxSolution.createNewCluster();
            auxSolution.moveToCluster(worstConnectedNode, cluster, cluster+1);

            // GET ADJACENTS
            Queue<Integer> nodes = new ArrayDeque<>(instance.getAdjacents(worstConnectedNode));

            // FOR EACH NODE, IF IT IMPROVE THE SOLUTION, SET A NEW SOLUTION
            while(nodes.size() > 0){

                int node = nodes.poll();
                computedNodes.add(node);
                auxSolution.moveToCluster(node, cluster, cluster+1);

                if(auxSolution.getModularity() > bestSolution.getModularity()){
                    bestSolution = new DividerSolution(auxSolution);
                }

                // ADD ADJACENTS TO NODES IF ARE CONTAINED IN CLUSTER WHICH IS DIVIDED

                for(int toAddNode: instance.getAdjacents(node)){

                    Cluster c = auxSolution.getCluster(cluster);
                    if(c.contains(toAddNode) && !computedNodes.contains(toAddNode)){
                        nodes.offer(toAddNode);
                        computedNodes.add(toAddNode);
                    }
                }

            }

            if(bestSolution.getModularity() > sol.getModularity()){
                sol = bestSolution;
            }else{
                cluster++;
            }

            computedNodes = new HashSet<>();

        }while(cluster < bestSolution.getClusterSize() - 1);

        sol.removeEmptyClusters();

        return sol;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
