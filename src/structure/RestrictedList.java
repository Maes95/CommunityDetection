package structure;

import grafo.optilib.tools.RandomManager;

import java.util.*;

public class RestrictedList{

    LinkedList<Integer> candidates = new LinkedList<>();

    public RestrictedList(DividerInstance instance, Cluster cluster, double mu){
        this(instance, cluster, mu, new Random());
    }

    public RestrictedList(DividerInstance instance, Cluster cluster, double mu, Random rnd){
        for(int nodeID = 0; nodeID < instance.getN(); nodeID++){
            if(!cluster.contains(nodeID)) continue;
            Iterable<Integer> adjacent = instance.getAdjacents(nodeID);
            int numExtEdges = 0;
            for(int adj: adjacent){
                if(!cluster.contains(adj)) numExtEdges++;
            }
            if(numExtEdges >= mu){
                candidates.add(nodeID);
            }
        }
        Collections.shuffle(candidates, rnd);
    }

    public int getNode(){
        return candidates.pop();
    }

    public boolean isEmpty(){
        return candidates.isEmpty();
    }

    @Override
    public String toString() {
        return candidates.toString();
    }
}
