# CommunityDetection

**All results are available in this repository. Execute the following commands OVERWRITE the results**

### Requeriments

Java 8
Conda 4.5.3

## 1. Reproduce experiment

This steps reproduce 
- The train phase to check which algorithm developed is the best whith train instances.
- The test phase to get results from our best algorithm with test instances.

### Build

IntelliJ IDE:
```
Build > Build Project
```

Command line:
```
mkdir out && find . -name "*.java" -print | xargs javac -d out/ -cp GrafoOptiLib.jar 
```
### Run (Train & Test)

IntelliJ IDE:
```
Run > Run Main
```

Command line:
```
java -Dfile.encoding=UTF-8 -cp out/:GrafoOptiLib.jar main.Main
```

### Output

```
results/results-train.csv
results/results-test.csv
```

## 2. Run competitors

This steps reproduce experiment for others community-detection algorithms (from Python `iGraph` library) with test instances

### Build

Command line:
```
pip install -r requirements.txt
```

### Run (Test)

Command line:
```
python RunCompetitors.py
```

### Output

```
results/algorithms_results/<algorithm>/<instance>network.txt
results/algorithms_results/times.txt
```

## 3. Calculate NMI

### Run (Test)

Command line:
```
python NMI.py
```

### Output

```
results/NMI_results.csv
```