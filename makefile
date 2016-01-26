CC=javac
SOURCE=src
BIN=bin
CFLAGS=-sourcepath $(SOURCE) -classpath $(BIN) -Xlint -d $(BIN)
SOURCES=src/android/os/*.java
CLASSES=$(SOURCES:.java=.class)
TESTSOURCE=testsrc
TESTBIN=testbin
TESTCFLAGS=-sourcepath $(TESTSOURCE) -classpath $(TESTBIN) -Xlint -d $(TESTBIN)
TESTSOURCES=testsrc/*.java

all: classes

classes: 
	mkdir -p $(BIN)
	$(CC) $(CFLAGS) $(SOURCES)

clean:
	rm -rf $(BIN)/*

compiletest:
	mkdir -p $(TESTBIN)
	$(CC) $(TESTCFLAGS) $(TESTSOURCES)

test:
	echo "Let's run the test here!"
	make all
	make compiletest
	echo "testing!!!"
