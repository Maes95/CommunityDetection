package constructives;

import grafo.optilib.metaheuristics.Constructive;
import structure.Cluster;
import structure.DividerInstance;
import structure.DividerSolution;
import structure.RestrictedList;

import java.util.*;

public class ConstDividerGreedy2 implements Constructive<DividerInstance, DividerSolution> {

    private double alpha;
    private double mu;

    public ConstDividerGreedy2(double alpha){
        this.alpha = alpha;
        this.mu = 0.0;
    }

    @Override
    public DividerSolution constructSolution(DividerInstance instance) {

        DividerSolution bestSolution = new DividerSolution(instance);
        bestSolution.startDestructive();

        for(int i = 0; i < 1; i++){
            Random rnd = new Random();
            rnd.setSeed(649);
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
        boolean firstIter = true;
        boolean refreshCandidates = true;
        Cluster c = null;


        // FOR EACH CLUSTER (CAN BE REPEATED IF PERFORMS)

        // GET RANDOM NODE (FIRST TIME)
        int baseNode = bestSolution.getRandomNode(cluster_index, rnd);

        while(cluster_index < instance.getN() - 1 && baseNode != -1) {

            //System.out.println("##### ITER "+cluster_index);

            // ADD NODE TO A NEW SOLUTION
            DividerSolution auxSolution = new DividerSolution(bestSolution);
            int next_cluster = auxSolution.createNewCluster();
            c = auxSolution.getCluster(cluster_index);

            System.out.println(auxSolution);
            System.out.println(cluster_index);
            System.out.println("CLUSTER: "+auxSolution.getCluster(cluster_index));

            if(firstIter){
                baseNode = c.getRandomNode(rnd);
                System.out.println("RANDOM NODE FI "+baseNode);
                firstIter = false;
            }else{
                if(refreshCandidates) candidates = new RestrictedList(instance, auxSolution.getCluster(cluster_index), mu, rnd);
                //refreshCandidates = false;

                System.out.println("CANDIDATES:"+candidates);
                if(!candidates.isEmpty()){
                    baseNode = candidates.getNode();
                    System.out.println("GET NODE "+ baseNode);
                }else{
                    baseNode = c.getRandomNode(rnd);
                    System.out.println("RANDOM NODE NOC "+baseNode);
                }

            }

            if(baseNode == -1) break;

            auxSolution.moveToCluster(baseNode, cluster_index, next_cluster);

            // GET ADJACENT
            Queue<Integer> adjacents = new ArrayDeque<>(instance.getAdjacents(baseNode));

            System.out.println(c);
            System.out.println(baseNode + adjacents.toString());

            while(adjacents.size() > 0){

                int node = adjacents.poll();

                if(!auxSolution.getCluster(cluster_index).contains(node)) continue;

                computedNodes.add(node);
                auxSolution.moveToCluster(node, cluster_index, cluster_index+1);

                // IF ADD NODE (OR A SET OF NODES) IMPROVE THE MODULARITY, REPLACE SOLUTION
                System.out.println(auxSolution.getModularity() +">"+ bestSolution.getModularity());
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

            /*if(candidates == null){
                cluster_index++;
            }else{
                if(candidates.isEmpty()){
                    refreshCandidates = true;
                    cluster_index++;
                }else{

                }
            }*/



            /*if(candidates != null && !candidates.isEmpty()){
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


            }*/

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
