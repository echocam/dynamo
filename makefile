CC=javac
SOURCE=src
BIN=bin
CFLAGS=-sourcepath $(SOURCE) -classpath $(BIN) -Xlint -d $(BIN)
SOURCES=$(wildcard src/android/os/*.java)

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
