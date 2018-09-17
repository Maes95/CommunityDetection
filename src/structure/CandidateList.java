package structure;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

public class CandidateList {

    LinkedList<Candidate> candidates = new LinkedList<>();

    public CandidateList(DividerInstance instance, Cluster cluster){
        for(int nodeID = 0; nodeID < instance.getN(); nodeID++){
            if(!cluster.contains(nodeID)) continue;
            Iterable<Integer> adjacent = instance.getAdjacents(nodeID);
            int numExtEdges = 0;
            for(int adj: adjacent){
                if(!cluster.contains(adj)) numExtEdges++;
            }
            candidates.add(new Candidate(nodeID, numExtEdges));
        }
        Collections.sort(candidates, Comparator.comparing(Candidate::getNumExtEdges));
    }

    public int getNode(){
        return candidates.pop().node;
    }

    public boolean isEmpty(){
        return candidates.isEmpty();
    }

    @Override
    public String toString() {
        return candidates.toString();
    }

    private class Candidate{
        int node;
        int numExtEdges;
        public Candidate(int node, int numExtEdges){
            this.node = node;
            this.numExtEdges = numExtEdges;
        }

        public int getNumExtEdges(){
            return this.numExtEdges;
        }
    }
}
