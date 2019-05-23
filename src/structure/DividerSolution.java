package structure;

import grafo.optilib.structure.Solution;
import java.util.*;

public class DividerSolution implements Solution {

    private DividerInstance instance;
    private ArrayList<Cluster> clusters;
    private long execTime = 0;

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

    public void copy(DividerSolution solution){
        this.instance = solution.instance;
        this.clusters = new ArrayList<>(solution.clusters.size());
        for(Cluster cluster: solution.clusters){
            this.clusters.add(new Cluster(cluster));
        }
    }

    public int createNewCluster() {
        clusters.add(new Cluster(this.instance));
        return clusters.size()-1;
    }

    public int whichClusterContains(int node){
        for(int i = 0; i < clusters.size(); i++){
            if(clusters.get(i).contains(node)) return i;
        }
        return -1;
    }

    public void moveToCluster(int node, int src, int dest){
        if(!clusters.get(src).contains(node)) throw new Error("Can't move node: "+node+" "+src+"->"+dest+'\n'+
                "SRC: "+src+ clusters.get(src) + '\n' +
                "DST: "+dest+clusters.get(dest));
        clusters.get(src).removeFromCluster(node);
        clusters.get(dest).addToCluster(node);
    }

    public double modularityIfMove(int node, int src, int dest){
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
        if(this.clusters.size() <= cluster) return null;
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

    public double getConductance() {
        double acum = 0;
        for(Cluster cluster: clusters){
            acum+=cluster.getConductance();
        }
        double conductance = 1 - ( acum / clusters.size() );
        return conductance;
    }

    public double getCoverage() {
        double coverage = 0;
        for(Cluster cluster: clusters){
            coverage+=cluster.getCoverage();
        }
        return coverage/this.instance.getM();
    }

    public int getWorstConnected(int cluster){
        return clusters.get(cluster).getWorstConnected();
    }

    public int getRandomNode(int cluster, Random random){
        return clusters.get(cluster).getRandomNode(random);
    }

    public int getClusterSize(){
        return this.clusters.size();
    }

    public void removeEmptyClusters(){
        int i = 0;
        while(i<clusters.size()){
            Cluster cluster = clusters.get(i);
            if(cluster.size() == 0){
                clusters.remove(i);
            }else{
                i++;
            }
        }
    }

    public boolean isBetterThan(DividerSolution other){
        return this.getModularity() >= other.getModularity();
    }

    public DividerInstance getInstance(){
        return this.instance;
    }

    public void setExecTime(long execTime){
        this.execTime = execTime;
    }
    public long getExecTime(){
        return execTime;
    }

    public List<Integer> getSolutionAsList(){
        List<Integer> partition = new ArrayList<>(this.instance.getN());
        for(int node = 0; node <this.instance.getN(); node++)
            partition.add(whichClusterContains(node));
        return partition;
    }


    @Override
    public String toString() {
        String s = "";
        for(Cluster cluster: clusters){
            s += clusters.indexOf(cluster) + "->" + cluster.toString()+"\n";
        }
        s+="Modularity: "+ getModularity()+"\n";
        s+="Conductance: "+getConductance()+"\n";
        s+="Coverage: "+getCoverage()+"\n";
        return s;
    }
}
