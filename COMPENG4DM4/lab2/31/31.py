import os
import sys

widthvalue = [1,2,3,4]

def main():
 result = [[],[],[],[]]
 
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
 
 for widthindex in range(4):
  #modify params.in
  paramtemplate = open("params31.template","r")
  paramin = open("params.in","w")
  paramdata = paramtemplate.readlines()
  for lineindex in range(len(paramdata)):
   if "large_width" in paramdata[lineindex]:
    paramdata[lineindex] = "large_width" + " " + str(widthvalue[widthindex])+"\n"
  paramtemplate.close()
  paramin.writelines(paramdata)
  paramin.close()
  
  for bmIndex in range(13):
   #modify trace_file_list
   f = open(os.getcwd()+"/trace_file_list","w")
   f.write("1\n%s\n"%(BMpath[bmIndex]))
   f.close()
   
   # run macsim
   os.system("macsim > " + BMName[bmIndex]+str(widthvalue[widthindex]) +".debug")
   os.system("cat params.in > params31_" + str(widthvalue[widthindex]) +".in")
   
   # grep value
   macsimout = open(BMName[bmIndex]+str(widthvalue[widthindex])+".debug","r")
   for line in macsimout.readlines():
    if "Core_Total  Finished" in line:
     print(line)
     lineData = line.split()
     result[widthindex].append(int(lineData[5].split(":")[1]))
     #print(result)
     print(str(widthindex) +" "+ str(bmIndex)+ " " + lineData[5].split(":")[1])
   macsimout.close()
  
 print(result)
 # write to file
 resultFile = os.getcwd()+"/large_width_impact.csv"
 print("write to " + resultFile)
 with open(resultFile,"w") as f:
  #write first line
  f.write("Cycles")
  for name in BMName:
   f.write("; %s"%(name))
  f.write("\n")
  
  # write data
  for widthindex in range(4):
   f.write("%d"%(widthvalue[widthindex]))
   for nameindex in range(13):
    f.write("; %d"%(result[widthindex][nameindex]))
   f.write("\n")
   
 f.close()
  

    

if __name__ == "__main__":
 main()
