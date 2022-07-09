# this makefile is intended for g++ on Linux

CC = g++
CFLAGS = -c -Wall -Wextra
# CFLAGS = -c
LDFLAGS =
SOURCES = experiment.cpp iofunc.cpp filter.cpp  fourier.cpp  genfunc.cpp  logfunc.cpp
OBJECTS = $(SOURCES:.cpp=.o)
EXECUTABLE = experiment

all: $(EXECUTABLE) clean

debug: CXXFLAGS += -DDEBUG -g
debug: CCFLAGS += -DDEBUG -g
debug: experiment

$(EXECUTABLE): $(OBJECTS)
	$(CC) $(OBJECTS) -o $@ $(LDFLAGS)

.cpp.o:
	$(CC) $(CFLAGS) $< -o $@

clean:
	-rm $(OBJECTS)
