package structure;

import grafo.optilib.structure.Instance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class DividerInstance implements Instance {

    private String name;
    private Set<Integer>[] graph;
    private Map<Integer, Integer> nodeIds;
    private int n;
    private int m;

    public DividerInstance(String path) {
        readInstance(path);
    }

    @Override
    public void readInstance(String path) {
        try {
            name = path.substring(path.lastIndexOf('/')+1);
            BufferedReader bf = new BufferedReader(new FileReader(path));
            String line = bf.readLine();
            String[] tokens = line.split("\\s+");
            n = Integer.parseInt(tokens[0]);
            nodeIds = new HashMap<>();
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

    public int getNodeID(int node) {
        return nodeIds.get(node);
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

