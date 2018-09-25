package constructives;

import grafo.optilib.metaheuristics.Constructive;
import grafo.optilib.tools.RandomManager;
import structure.DividerInstance;
import structure.DividerSolution;

import java.util.Random;

public class ConstPerfect implements Constructive<DividerInstance, DividerSolution> {

    @Override
    public DividerSolution constructSolution(DividerInstance instance) {
        DividerSolution sol = new DividerSolution(instance);
        sol.createNewCluster();
        sol.createNewCluster();
        sol.createNewCluster();
        sol.createNewCluster();
        sol.createNewCluster();
        sol.createNewCluster();
        sol.createNewCluster();
        sol.createNewCluster();
        sol.createNewCluster();
        sol.createNewCluster();

        int[] _0 = {197, 198, 188, 158};
        for(int node: _0) sol.addToCluster(0,node);

        int[] _1 = {131, 135, 136, 137, 10, 11, 141, 142, 144, 145, 149, 22, 150, 23, 151, 155, 156, 157, 30, 33, 161, 34, 162, 166, 169, 170, 171, 174, 176, 177, 179, 180, 183, 184, 56, 57, 186, 189, 191, 193, 66, 196, 71, 74, 204, 205, 207, 209, 211, 212, 94, 107, 112, 113, 119, 120, 121, 124, 125, 126, 127};
        for(int node: _1) sol.addToCluster(1,node);

        int[] _2 = {0, 1, 130, 6, 8, 9, 12, 13, 20, 21, 24, 25, 154, 26, 163, 38, 168, 43, 44, 173, 47, 49, 181, 53, 182, 185, 58, 59, 61, 63, 64, 194, 69, 70, 73, 75, 79, 80, 210, 82, 83, 84, 213, 90, 91, 92, 97, 101, 102, 104, 106, 109, 115, 118, 122, 123};
        for(int node: _2) sol.addToCluster(2,node);

        int[] _3 = {192, 65, 2, 3, 67, 195, 134, 203, 140, 14, 15, 16, 146, 19, 148, 27, 93, 31, 99, 103, 172, 46, 175, 48, 52, 117, 62};
        for(int node: _3) sol.addToCluster(3,node);

        int[] _4 = {114, 42, 143};
        for(int node: _4) sol.addToCluster(4,node);

        int[] _5 = {7, 167, 139};
        for(int node: _5) sol.addToCluster(5,node);

        int[] _6 = {208, 50, 164, 86, 187, 108};
        for(int node: _6) sol.addToCluster(6,node);

        int[] _7 = {128, 129, 132, 4, 68, 133, 5, 199, 72, 200, 138, 76, 77, 17, 147, 85, 87, 88, 152, 89, 153, 28, 95, 160, 32, 98, 35, 100, 36, 165, 37, 39, 40, 41, 105, 45, 110, 111, 51, 116, 54};
        for(int node: _7) sol.addToCluster(7,node);

        int[] _8 = {201, 202};
        for(int node: _8) sol.addToCluster(8,node);

        int[] _9 = {96, 81, 178, 18, 55, 60, 29, 78, 206, 190, 159};
        for(int node: _9) sol.addToCluster(9,node);

        return sol;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}