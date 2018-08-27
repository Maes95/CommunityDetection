package constructives;

import grafo.optilib.metaheuristics.Constructive;
import structure.Cluster;
import structure.DividerInstance;
import structure.DividerSolution;

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

        DividerSolution sol = new DividerSolution(instance);
        sol.startDestructive();

        // GET MU
        int min = instance.getMinEdges();
        int max = instance.getMaxEdges();
        this.mu = max - this.alpha * (max-min);

        int cluster = 0;
        Set<Integer> computedNodes = new HashSet<>();

        DividerSolution bestSolution = sol;

        // FOR EACH CLUSTER (CAN BE REPEATED IF PERFORMS)

        // GET RANDOM NODE (FIRST TIME)
        int baseNode = bestSolution.getRandomNode(cluster);

        do{

            // ADD NODE TO A NEW SOLUTION
            DividerSolution auxSolution = new DividerSolution(bestSolution);
            auxSolution.createNewCluster();
            Cluster c = auxSolution.getCluster(cluster);
            auxSolution.moveToCluster(baseNode, cluster, cluster+1);

            // GET ADJACENT
            Queue<Integer> adjacents = new ArrayDeque<>(instance.getAdjacents(baseNode));

            while(adjacents.size() > 0){

                int node = adjacents.poll();
                computedNodes.add(node);
                auxSolution.moveToCluster(node, cluster, cluster+1);

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

            if(bestSolution.getModularity() > sol.getModularity()){
                // IF SOLUTION IMPROVES
                sol = bestSolution;
                // GET NEXT NODE TO MOVE (FROM RESTRICTED LIST)
                baseNode = this.getNodeFromRestrictedList(instance, c);
            }else{
                // IF NOT
                cluster++;
                // GET NEXT NODE TO MOVE (RANDOM)
                baseNode = bestSolution.getRandomNode(cluster);
            }



            computedNodes = new HashSet<>();

        }while(cluster < bestSolution.getClusterSize() - 1);

        sol.removeEmptyClusters();

        return sol;
    }

    private int getNodeFromRestrictedList(DividerInstance instance, Cluster cluster){
        List<Integer> candidates = this.getRestrictedList(instance, cluster);
        Random rand = new Random();
        return candidates.get(rand.nextInt(candidates.size()));
    }

    private List<Integer> getRestrictedList(DividerInstance instance, Cluster cluster){
        List<Integer> candidates = new LinkedList<>();
        for(int nodeID = 0; nodeID < instance.getN(); nodeID++){
            Iterable<Integer> adjacents = instance.getAdjacents(nodeID);
            int numExtEdges = 0;
            for(int adj: adjacents){
                if(!cluster.contains(adj)) numExtEdges++;
            }
            if(numExtEdges >= this.mu){
                candidates.add(nodeID);
            }
        }
        return candidates;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public class Candidate{
        int node;
        int numExtEdges;
        public Candidate(int node, int numExtEdges){
            this.node = node;
            this.numExtEdges = numExtEdges;
        }
    }

}
