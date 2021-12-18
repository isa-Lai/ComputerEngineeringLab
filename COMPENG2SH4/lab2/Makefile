CC=gcc
CFLAGS=-I. -lm
DEPS = *.h
OBJ = AllTests.o testCases.o CuTest.o Question1.o Question2.o Question3.o Question4.o Question5.o Question6.o Question7.o

%.o: %.c $(DEPS)
	$(CC) -c -o $@ $< $(CFLAGS)

L2: $(OBJ)
	$(CC) -o $@ $^ $(CFLAGS)

#testCompile: AllTests.c testCases.c CuTest.c Question1.c
#	gcc -o testLab0 AllTests.c testCases.c CuTest.c Question1.c

test:	testL2
testL2:
# next line must start with a real TAB (ASCII 9)
	./run.sh

.PHONY: clean

clean:
	$(RM) *.o 
