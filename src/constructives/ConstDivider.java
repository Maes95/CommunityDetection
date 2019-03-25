package constructives;

import grafo.optilib.metaheuristics.Constructive;
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

        while(cluster < bestSolution.getClusterSize()){

            // GET WORST NODE
            int worstConnectedNode = bestSolution.getWorstConnected(cluster);

            // ADD NODE TO A NEW SOLUTION
            DividerSolution auxSolution = new DividerSolution(bestSolution);
            auxSolution.createNewCluster();
            auxSolution.moveToCluster(worstConnectedNode, cluster, cluster+1);

            // GET ADJACENT
            Queue<Integer> nodes = new ArrayDeque<>(instance.getAdjacents(worstConnectedNode));

            while(nodes.size() > 0){

                int node = nodes.poll();
                computedNodes.add(node);
                if(!auxSolution.getCluster(cluster).contains(node)) continue;
                auxSolution.moveToCluster(node, cluster, cluster+1);

                // IF ADD NODE (OR A SET OF NODES) IMPROVE THE MODULARITY, REPLACE SOLUTION
                if(auxSolution.getModularity() > bestSolution.getModularity()){
                    bestSolution = new DividerSolution(auxSolution);
                }

                // ADD ADJACENT TO CHECK (ONLY IF THEY ARE NOT CHECKED BEFORE AND THEY ARE CONTAINED BY THE CLUSTER)

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

        }

        sol.removeEmptyClusters();

        return sol;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
