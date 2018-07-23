package metrics;

import graph.Instance;

public interface Metric {
    double calculate(Instance instance);
}
