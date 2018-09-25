package constructives;

import grafo.optilib.metaheuristics.Constructive;
import structure.DividerInstance;
import structure.DividerSolution;

public class ConstPerfectSergio implements Constructive<DividerInstance, DividerSolution> {

    @Override
    public DividerSolution constructSolution(DividerInstance instance) {
        DividerSolution sol = new DividerSolution(instance);
        sol.createNewCluster();
        sol.createNewCluster();
        sol.createNewCluster();
        sol.createNewCluster();
        sol.createNewCluster();

        int[] _0 = {192, 65, 2, 67, 203, 140, 14, 78, 15, 16, 81, 18, 146, 19, 148, 27, 29, 93, 31, 96, 160, 99, 103, 106, 46, 175, 48, 52, 117, 55, 60, 190};
        for(int node: _0) sol.addToCluster(0,node);

        int[] _1 = {36, 164, 134, 72, 138, 108, 206, 208, 50, 178, 115, 147, 86, 25, 57, 187, 159};
        for(int node: _1) sol.addToCluster(1,node);

        int[] _2 = {128, 129, 4, 68, 132, 5, 133, 199, 76, 77, 143, 17, 145, 85, 87, 88, 152, 89, 153, 28, 95, 32, 98, 35, 100, 37, 39, 40, 41, 105, 45, 110, 111, 51, 116, 54, 183};
        for(int node: _2) sol.addToCluster(2,node);

        int[] _3 = {131, 135, 136, 137, 10, 11, 141, 142, 144, 149, 22, 150, 23, 151, 155, 156, 157, 30, 158, 33, 161, 34, 162, 166, 169, 170, 171, 174, 176, 177, 179, 180, 56, 184, 186, 188, 189, 191, 193, 66, 196, 197, 198, 71, 201, 74, 202, 204, 205, 207, 209, 212, 94, 107, 112, 113, 119, 120, 121, 124, 125, 126, 127};
        for(int node: _3) sol.addToCluster(3,node);

        int[] _4 = {0, 1, 130, 3, 6, 7, 8, 9, 139, 12, 13, 20, 21, 24, 26, 154, 163, 165, 38, 167, 168, 42, 43, 44, 172, 173, 47, 49, 53, 181, 182, 185, 58, 59, 61, 62, 63, 64, 194, 195, 69, 70, 200, 73, 75, 79, 80, 82, 210, 83, 211, 84, 213, 90, 91, 92, 97, 101, 102, 104, 109, 114, 118, 122, 123};
        for(int node: _4) sol.addToCluster(4,node);

        return sol;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}