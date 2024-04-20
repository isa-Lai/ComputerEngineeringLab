import os
import sys

def main():
 instCount = {}
 
 #reading
 BMdir = sys.argv[1]
 print("Open BenchMark dir " + BMdir)
 for subdir, dirs, files in os.walk(BMdir):
  for file in files:
   if "_inscount" in file:
    filePath = os.path.join(subdir,file)
    print("reading "+ filePath)
    dirNames = subdir.split("/")
    stateCheck = False
    for dirName in dirNames:
     if "benchmarks" in dirName:
      stateCheck = True
      continue
     if stateCheck == True:
      fileData = open(filePath,"r")
      for num, line in enumerate(fileData):
       instCount[dirName] = int(line.split()[1])
       print(dirName + ": " + line.split()[1])
      fileData.close()
      break;
      
 #write
 resultFile = os.getcwd()+"/lab1-exercise1-inscount.csv"
 print("write to " + resultFile)
 with open(resultFile,"w") as f:
  for key in instCount:
   f.write("%s; %d\n"%(key,instCount[key]))
 f.close()
    

if __name__ == "__main__":
 main()
