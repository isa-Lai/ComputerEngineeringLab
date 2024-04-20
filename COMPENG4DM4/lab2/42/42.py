import os
import sys

namelookup = ["use_branch_prediction","perfect_bp"]
ratevalue = [[0,0],[1,1],[1,0]]

def main():
 result = [0.0,0.0,0.0]
 print(result)
 
 for rateindex in range(3):
  # for nameindex in range(2):
   #edit params.in
  paramtemplate = open("params42.tem","r")
  paramin = open("params42.in","w")
  paramdata = paramtemplate.readlines()
  for lineindex in range(len(paramdata)):
    if namelookup[0] in paramdata[lineindex]:
      paramdata[lineindex] = namelookup[0] + " " + str(ratevalue[rateindex][0])+"\n"
    if namelookup[1] in paramdata[lineindex]:
      paramdata[lineindex] = namelookup[1] + " " + str(ratevalue[rateindex][1])+"\n"

  paramtemplate.close()
  paramin.writelines(paramdata)
  paramin.close()
  # return
   
   # running macsim and grep value
  os.system("macsim > " + str(rateindex)+".debug")
  macsimout = open(str(rateindex)+".debug","r")
  for line in macsimout.readlines():
   if "Core_Total  Finished" in line:
    print(line)
    lineData = line.split()
    result[rateindex] = int(lineData[5].split(":")[1])
    print(result)
    print(str(rateindex)+ " " + lineData[5].split(":")[1])
  macsimout.close()
 
 # write to file
 resultFile = os.getcwd()+"/BP_impact.csv"
 print("write to " + resultFile)
 with open(resultFile,"w") as f:
  #write first line
  f.write("BP; Execution Time\n")
  # for name in namelookup:
  #  f.write("; %s"%(name))
  # f.write("\n")
  
  # write data
  # for index in range(3):
  #  f.write("%d"%(ratevalue[rateindex]))
  #  for nameindex in range(4):
  #   f.write("; %d"%(result[nameindex][rateindex]))
  f.write("Disabled; %f\n"%result[0])
  f.write("Perfected; %f\n"%result[1])
  f.write("NonPerfected; %f\n"%result[2])
  
 f.close() 
   

    

if __name__ == "__main__":
 main()
