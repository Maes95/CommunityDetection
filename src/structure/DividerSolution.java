package structure;

import grafo.optilib.structure.Solution;
import java.util.*;

public class DividerSolution implements Solution {

    private DividerInstance instance;
    private List<Cluster> clusters;

    public DividerSolution(DividerInstance instance){
        this.instance = instance;
        this.clusters = new ArrayList<>();
    }

    public DividerSolution(DividerSolution solution){
        this.instance = solution.instance;
        this.clusters = new ArrayList<>(solution.clusters.size());
        for(Cluster cluster: solution.clusters){
            this.clusters.add(new Cluster(cluster));
        }
    }

    public void createNewCluster() {
        clusters.add(new Cluster(this.instance));
    }

    public void moveToCluster(int node, int src, int dest){
        clusters.get(src).removeFromCluster(node);
        clusters.get(dest).addToCluster(node);
    }

    public double costToMove(int node, int src, int dest){
        clusters.get(src).removeFromCluster(node);
        clusters.get(dest).addToCluster(node);
        double new_ = this.getModularity();
        clusters.get(dest).removeFromCluster(node);
        clusters.get(src).addToCluster(node);
        return new_;
    }

    public void addToCluster(int cluster, int node){
        clusters.get(cluster).addToCluster(node);
    }

    public void removeFromCluster(int cluster, int node){
        clusters.get(cluster).removeFromCluster(node);
    }

    public Cluster getCluster(int cluster){
        return this.clusters.get(cluster);
    }

    public void startDestructive() {
        createNewCluster();
        int n = instance.getN();
        for (int i = 0; i < n; i++) {
            addToCluster(0, i);
        }
    }

    public double getModularity() {
        double modularity = 0;
        for(Cluster cluster: clusters){
            modularity+=cluster.getModularity();
        }
        return modularity;
    }

    public int getWorstConnected(int cluster){
        return clusters.get(cluster).getWorstConnected();
    }

    public int getClusterSize(){
        return this.clusters.size();
    }

    public void removeEmptyClusters(){
        for(int i=0; i < clusters.size(); i++){
            Cluster cluster = clusters.get(i);
            if(cluster.size() == 0) clusters.remove(cluster);
        }
    }

    @Override
    public String toString() {
        String s = "";
        for(Cluster cluster: clusters){
            s += clusters.indexOf(cluster) + "->" + cluster.toString()+"\n";
        }
        s+="Modularity: "+getModularity();
        return s;
    }
}
