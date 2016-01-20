CC=javac
SOURCE=src
BIN=bin
CFLAGS=-sourcepath $(SOURCE) -classpath bin -Xlint -d $(BIN)
SOURCES=src/android/os/*.java
CLASSES=$(SOURCES:.java=.class)

all: classes

classes: 
	$(CC) $(CFLAGS) $(SOURCES)

clean:
	rm -rf $(BIN)/*
