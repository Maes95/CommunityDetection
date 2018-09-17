package constructives;

import grafo.optilib.metaheuristics.Improvement;
import structure.CandidateList;
import structure.Cluster;
import structure.DividerSolution;

public class LocalSearch implements Improvement<DividerSolution> {
    @Override
    public void improve(DividerSolution dividerSolution) {
        DividerSolution best = new DividerSolution(dividerSolution);

        int cluster_index = 0;
        while(cluster_index < best.getClusterSize()){
            Cluster cluster = best.getCluster(cluster_index);
            CandidateList candidates = new CandidateList(best.getInstance(), cluster);

            while(!candidates.isEmpty()){
                int candidate = candidates.getNode();
                DividerSolution aux = new DividerSolution(best);
                aux.createNewCluster();
                int cluster_to_move_index = 0;
                while(cluster_to_move_index < aux.getClusterSize()){

                    // JUMP TO NEXT ITER IF WE ARE IN SAME CLUSTER
                    if(cluster_to_move_index == cluster_index){
                        cluster_to_move_index++;
                        continue;
                    };

                    // MOVE CANDIDATE TO NEW CLUSTER
                    aux.moveToCluster(candidate, cluster_index, cluster_to_move_index);

                    // CHECK IF MOVE IMPROVES MODULARITY (SAVE SOLUTION IF IMPROVES MODULARITY)
                    if(aux.getModularity() > best.getModularity()){
                        best = aux;
                        aux = new DividerSolution(best);
                    }

                    // UNDO MOVE TO CHECK OTHER WAYS
                    aux.moveToCluster(candidate, cluster_to_move_index, cluster_index);

                    cluster_to_move_index++;
                }
            }


            cluster_index++;
        }

        best.removeEmptyClusters();
        dividerSolution.copy(best);
    }
}
