package structure;

import grafo.optilib.tools.RandomManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Cluster {

    private final Map<Integer, Set<Integer>> nodesInCluster;
    private final DividerInstance instance;
    private double currentModularity;
    private double currentConductance;
    private boolean needUpdate;

    public Cluster(DividerInstance instance){
        this.nodesInCluster = new HashMap<>();
        this.instance = instance;
        this.currentModularity = 0;
        this.currentConductance = 0;
        this.needUpdate = false;
    }

    public Cluster(Cluster cluster){
        this.nodesInCluster = new HashMap<>();
        this.instance = cluster.instance;
        this.currentModularity = cluster.currentModularity;
        this.needUpdate = cluster.needUpdate;
        for(int node: cluster.nodesInCluster.keySet()){
            this.nodesInCluster.put(node, this.instance.getAdjacents(node));
        }
    }

    public void addToCluster(int node){
        this.nodesInCluster.put(node, this.instance.getAdjacents(node));
        this.needUpdate = true;
    }

    public void removeFromCluster(int node){
        this.nodesInCluster.remove(node);
        this.needUpdate = true;
    }

    public double getModularity(){
        updateAll();
        return this.currentModularity;
    }

    public double getConductance(){
        int oneBindingEdges = 0;
        int twoBindingEdges = 0;
        for(Set<Integer> adjacent: this.nodesInCluster.values())
            for(int node : adjacent)
                if(!this.nodesInCluster.containsKey(node))
                    oneBindingEdges++;
                else
                    twoBindingEdges++;

        twoBindingEdges/=2;

        // The number of edges that connect vertices of different clusters
        int conectedWithOtherClusters = oneBindingEdges;

        // Number of edges with an endpoint in the cluster
        int withEndpoint = oneBindingEdges + twoBindingEdges;

        // Number of edges with no endpoint in the cluster
        int withoutEndpoint = this.instance.getM() - oneBindingEdges;

        this.currentConductance = conectedWithOtherClusters / Double.min(withEndpoint, withoutEndpoint);

        return Double.isNaN(this.currentConductance) ? 0.0 : this.currentConductance;
    }


    public double getCoverage(){
        int twoBindingEdges = 0;
        for(Set<Integer> adjacent: this.nodesInCluster.values())
            for(int node : adjacent)
                if(this.nodesInCluster.containsKey(node))
                    twoBindingEdges++;
        return twoBindingEdges/2;
    }

    private void updateAll(){
        if(this.needUpdate){
            setModularity();
            this.needUpdate = false;
        }
    }

    private void setModularity(){

        double twoBindingEdges = 0;
        double oneBindingEdges = 0;
        for(Integer key: this.nodesInCluster.keySet()){
            Set<Integer> adjacent = this.nodesInCluster.get(key);
            for(int node : adjacent){
                if(this.nodesInCluster.containsKey(node))
                    twoBindingEdges++;
                else
                    oneBindingEdges++;
            }
        }

        // Total edges of cluster nodes
        double totalEdges = oneBindingEdges + twoBindingEdges;

        // Fraction of the edges that fall within the given community
        double inEndEdgeFraction = twoBindingEdges / (2*instance.getM());

        // Fraction of ends of edges that are attached to vertices in this community
        double outEndEdgeFraction = totalEdges / (2*instance.getM());

        this.currentModularity = inEndEdgeFraction - (outEndEdgeFraction*outEndEdgeFraction);
    }

    public boolean contains(int node){
        return this.nodesInCluster.containsKey(node);
    }

    public int getRandomNode(Random rnd){
        Object[] nodes = nodesInCluster.keySet().toArray();
        if(nodes.length == 0){
            return -1;
        }else{
            return (Integer) nodes[rnd.nextInt(nodes.length)];
        }
    }

    public int getWorstConnected(){
        int worst = -1;
        int worstConnectionNumber = Integer.MAX_VALUE;
        for(int node: this.nodesInCluster.keySet()){
            if(this.nodesInCluster.get(node).size() < worstConnectionNumber){
                worst = node;
                worstConnectionNumber = this.nodesInCluster.get(node).size();
                if(worstConnectionNumber == 1) break;
            }
        }
        return worst;
    }

    public int size(){
        return this.nodesInCluster.size();
    }

    public String toString(){
        return this.nodesInCluster.keySet().toString();
    }

}
