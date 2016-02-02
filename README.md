# Dynamic Narrative
A system architecture and tools that allow developers to track the logic of player decisions and alter the state of the game based on those decisions. 

This application is in development by James Riordan, Angus Hammond, Elise Xue, Robin McFarland, Tim Ringland and Tom Read-Cutting.


Building, Running and Testing
=============================

You must have Java8 installed or above. The project is built using [Gradle](http://gradle.org/).

Linux
-----
To build the project and/or testing open the root directory in the terminal.
```bash
./gradlew assemble
```

To run the GUI simply use the following:
```bash
./gradlew runGui
```

```
To test:
```bash
./gradlew check
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

Project (Java) Style Guide
--------------------------

### Indentation
Spaces or tabs?

### Functions
Functions must

### Class Member Ordering
It is advised that class members are ordered as follows:
 - Fields
 - Constructors
 - Methods
Then the next level of subordering must be:
 - static members
 - instance members
Finally, within these catagories order things as follows:
 - public
 - protected
 - package
 - private

Refer to the following example:
```java
class TestClass {
    public static int publicStaticField = 0;
    protected static int protectedStaticField = 0;
    
    public int publicInstanceMember;
    private int privateInstanceMember;

    public TestClass() {
        privateInstanceMember = 0;
    }

    public static int publicStaticMethod() {
        return publicStaticField;
    }

    package int packageInstanceMethod() {
      return privateInstanceMember;
    }
}
```
Abiding by this is up to the programmer's discretion, but don't diverge from this format unless you have a good reason.

### Maximum Line Length
Lines must not be longer the 120 characters each.


