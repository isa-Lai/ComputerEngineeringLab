JFLAGS = -cp ./junit.jar:org.hamcrest.core_1.1.0.jar:.
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = TestSLLAll.java 

default: classes

classes: $(CLASSES:.java=.class)

test:
	java -cp ./junit.jar:org.hamcrest.core_1.1.0.jar:.  org.junit.runner.JUnitCore  TestSLLAll

clean:
	$(RM) *.class
