package structure;

import grafo.optilib.structure.Instance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class DividerInstance implements Instance {

    private String name;
    private Set<Integer>[] graph;
    private int n;
    private int m;
    private int minEdges = Integer.MAX_VALUE;
    private int maxEdges = 0;

    public DividerInstance(String path) {
        readInstance(path);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void readInstance(String path) {
        try {
            name = path.substring(path.lastIndexOf('/')+1);
            BufferedReader bf = new BufferedReader(new FileReader(path));
            String line = bf.readLine();
            String[] tokens = line.split("\\s+");
            n = Integer.parseInt(tokens[0]);
            graph = (HashSet<Integer>[]) new HashSet[n];
            for (int i=0;i<n;i++) {
                graph[i] = new HashSet<>(n);
            }
            while ((line = bf.readLine()) != null) {
                tokens = line.split("\\s+");
                int src = Integer.parseInt(tokens[0]);
                int dst = Integer.parseInt(tokens[1]);
                if (src == dst) continue;
                if(!graph[src].contains(dst)) m++;
                graph[src].add(dst);
                graph[dst].add(src);
            }

            for(Set<Integer> adjacents: graph){
                int size = adjacents.size();
                if(size > this.maxEdges)
                    this.maxEdges = size;
                else if(size < this.minEdges)
                    this.minEdges = size;
            }
            bf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    public int getMaxEdges() {
        return maxEdges;
    }

    public int getMinEdges() {
        return minEdges;
    }

    public boolean areAdyacents(int v, int u) {
        return graph[v].contains(u);
    }

    public Set<Integer> getAdjacents(int v) {
        return graph[v];
    }

    public int nAdjacents(int v) { return graph[v].size(); }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder stb = new StringBuilder();
        for (int i = 0; i < graph.length; i++) {
            stb.append(i).append(" --> ").append(graph[i]).append("\n");
        }
        return stb.toString();
    }
}

