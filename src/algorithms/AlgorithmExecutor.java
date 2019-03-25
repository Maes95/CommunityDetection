package algorithms;

import grafo.optilib.metaheuristics.Constructive;
import structure.DividerInstance;
import structure.DividerSolution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class AlgorithmExecutor {

    private ExecutorService executor;

    public enum EXECUTOR_TYPE { SINGLE, MULTI };

    public AlgorithmExecutor(EXECUTOR_TYPE t){
        if (t == EXECUTOR_TYPE.SINGLE){
            this.executor = Executors.newSingleThreadExecutor();
        }else if (t == EXECUTOR_TYPE.MULTI){
            this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);
        }
    }

    public DividerSolution calculateBestSolution(Constructive<DividerInstance, DividerSolution> alg, DividerInstance instance, int iters) throws Exception {

        long start = System.currentTimeMillis();
        List<Future<DividerSolution>> results = new ArrayList(iters);

        for(int i = 0; i < iters; i++) {
            Future<DividerSolution> result = executor.submit(()-> alg.constructSolution(instance));
            results.add(result);
        }
        executor.shutdown();
        executor.awaitTermination(5 * iters, TimeUnit.MINUTES);

        long total = System.currentTimeMillis() - start;
        System.out.println("TOTAL TIME: "+total +" ms");

        return results.stream()
                .map((Future<DividerSolution> f )->{
                    DividerSolution s = null;
                    try {
                        s = f.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    return s;
                })
                .sorted(Comparator.comparing(DividerSolution::getModularity))
                .collect(Collectors.toList())
                .get(iters-1);
    }

}
