# Dynamic Narrative
A system architecture and tools that allow developers to track the logic of player decisions and alter the state of the game based on those decisions. 

This application is in development by James Riordan, Angus Hammond, Elise Xue, Robin McFarland, Tim Ringland and Tom Read-Cutting.


Building, Running and Testing
====================

You must have Java8 installed or above. The project is built using [Gradle](http://gradle.org/).

Linux
-----
To build the project and/or testing open the root directory in the terminal.
```bash
./gradlew build
```

To run the GUI simply use the following:
```bash
./gradlew runGui
```

To test:
```bash
./gradlew check
```

To clean:
```bash
./gradlew clean
```

Windows
-------
Windows is similar to Linux, but without the "./".


Specifying JDK path
-------------------
If you wish to specify the jdk path for gradle, add the following to the gradle.properties file in the home directory.

```
org.gradle.java.home=/home/tom/Desktop/jdk1.8.0_72
```
