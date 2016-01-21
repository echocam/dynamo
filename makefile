CC=javac
SOURCE=src
BIN=bin
CFLAGS=-sourcepath $(SOURCE) -classpath $(BIN) -Xlint -d $(BIN)
SOURCES=src/android/os/*.java
CLASSES=$(SOURCES:.java=.class)

all: classes

classes: 
	mkdir -p $(BIN)
	$(CC) $(CFLAGS) $(SOURCES)

clean:
	rm -rf $(BIN)/*
