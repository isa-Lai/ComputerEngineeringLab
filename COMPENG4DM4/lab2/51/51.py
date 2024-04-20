import os
import sys

ordervalue = ["io","ooo"]

def main():
 result = [[],[]]
 
 # grep BM path
 BMpath = []
 BMName = []
 BMdir = sys.argv[1]
 print("Open BenchMark dir " + BMdir)
 for subdir, dirs, files in os.walk(BMdir):
  for file in files:
   if "BM.txt" in file:
    filePath = os.path.join(subdir,file)
    print("found "+ filePath)
    dirNames = subdir.split("/")
    stateCheck = False
    for dirName in dirNames:
     if "benchmarks" in dirName:
      stateCheck = True
      continue
     if stateCheck == True:
      BMpath.append(filePath)
      BMName.append(dirName)
      break;
 print(BMpath)
 print(BMName)
 
 for scheindex in range(2):
  #modify params.in
  paramtemplate = open("params51.template","r")
  paramin = open("params.in","w")
  paramdata = paramtemplate.readlines()
  for lineindex in range(len(paramdata)):
   if "large_core_schedule" in paramdata[lineindex]:
    paramdata[lineindex] = "large_core_schedule" + " " + str(ordervalue[scheindex])+"\n"
  paramtemplate.close()
  paramin.writelines(paramdata)
  paramin.close()
  
  for bmIndex in range(13):
   #modify trace_file_list
   f = open(os.getcwd()+"/trace_file_list","w")
   f.write("1\n%s\n"%(BMpath[bmIndex]))
   f.close()
   
   # run macsim
   os.system("macsim > " + BMName[bmIndex]+str(ordervalue[scheindex]) +".debug")
   os.system("cat params.in > params51_" + str(ordervalue[scheindex]) +".in")
   
   # grep value
   macsimout = open(BMName[bmIndex]+str(ordervalue[scheindex])+".debug","r")
   for line in macsimout.readlines():
    if "Core_Total  Finished" in line:
     print(line)
     lineData = line.split()
     result[scheindex].append(int(lineData[5].split(":")[1]))
     #print(result)
     print(str(scheindex) +" "+ str(bmIndex)+ " " + lineData[5].split(":")[1])
   macsimout.close()
 
 print(result)
 # write to file
 resultFile = os.getcwd()+"/cycle31.csv"
 print("write to " + resultFile)
 with open(resultFile,"w") as f:
  #write first line
  f.write("Benchmark; IO execution time; OOO execution time")
  
  # write data
  for nameindex in range(13):
   f.write("%s"%(BMName[nameindex]))
   for scheindex in range(2):
    f.write("; %d"%(result[scheindex][nameindex]))
   f.write("\n")
   
 f.close()
  

    

if __name__ == "__main__":
 main()
