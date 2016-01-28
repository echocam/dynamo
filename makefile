CC=javac
SOURCE=src
BIN=bin
CFLAGS=-sourcepath $(SOURCE) -classpath $(BIN) -Xlint -d $(BIN)
SOURCES=$(wildcard src/android/os/*.java)
CLASSES=$(SOURCES:.java=.class)
TESTSOURCE=testsrc
TESTBIN=testbin
TESTCFLAGS=-sourcepath $(TESTSOURCE) -classpath $(TESTBIN) -Xlint -d $(TESTBIN)
TESTSOURCES=testsrc/*.java

all: classes
.PHONY: all clean

#Compile all classes
classes: $(BIN)
	
	$(CC) $(CFLAGS) $(SOURCES)

$(BIN):
	mkdir -p $(BIN)


#Clean the bin directory
clean:
	rm -rf $(BIN)/*

compiletest:
	mkdir -p $(TESTBIN)
	#$(CC) $(TESTCFLAGS) $(TESTSOURCES) #TODO: Compile test here!

test:
	echo "Let's run the test here!"
	make all
	make compiletest
  #make runtest!!!
	echo "testing!!!"
