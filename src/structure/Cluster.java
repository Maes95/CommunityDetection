package structure;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Cluster {

    private final Map<Integer, Set<Integer>> nodesInCluster;
    private final DividerInstance instance;
    private double currentModularity;
    private boolean needUpdate;

    public Cluster(DividerInstance instance){
        this.nodesInCluster = new HashMap<>();
        this.instance = instance;
        this.currentModularity = 0;
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
        if(this.needUpdate){
            double twoBindingEdges = 0;
            double oneBindingEdges = 0;
            for(Set<Integer> adjacent: this.nodesInCluster.values())
                for(int node : adjacent)
                    if(this.nodesInCluster.containsKey(node))
                        twoBindingEdges++;
                    else
                        oneBindingEdges++;

            // Total edges of cluster nodes
            double totalEdges = oneBindingEdges + twoBindingEdges;

            // Fraction of the edges that fall within the given community
            double inEndEdgeFraction = twoBindingEdges / (2*instance.getM());

            // Fraction of ends of edges that are attached to vertices in this community
            double outEndEdgeFraction = totalEdges / (2*instance.getM());

            this.currentModularity = inEndEdgeFraction - (outEndEdgeFraction*outEndEdgeFraction);
            this.needUpdate = false;
        }

        return this.currentModularity;
    }

    public boolean contains(int node){
        return this.nodesInCluster.containsKey(node);
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
