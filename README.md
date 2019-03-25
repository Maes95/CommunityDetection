# CommunityDetection

### Requeriments

Java 8

### Build

IntelliJ IDE:
```
Build > Build Project
```

Command line:
```
mkdir out && find . -name "*.java" -print | xargs javac -d out/ -cp GrafoOptiLib.jar 
```
### Run

IntelliJ IDE:
```
Run > Run Main
```

Command line:
```
java -Dfile.encoding=UTF-8 -cp out/:GrafoOptiLib.jar main.Main
```