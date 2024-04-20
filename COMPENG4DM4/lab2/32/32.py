import os
import sys

namelookup = ["fsched_large_rate","isched_large_rate","msched_large_rate","ssched_large_rate"]
ratevalue = [1,2,4,8,16]

def main():
 result = [[0,0,0,0,0],[0,0,0,0,0],[0,0,0,0,0],[0,0,0,0,0]]
 print(result)
 
 for nameindex in range(4):
  for rateindex in range(5):
   #edit params.in
   paramtemplate = open("params32.template","r")
   paramin = open("params.in","w")
   paramdata = paramtemplate.readlines()
   for lineindex in range(len(paramdata)):
    if namelookup[nameindex] in paramdata[lineindex]:
     paramdata[lineindex] = namelookup[nameindex] + " " + str(ratevalue[rateindex])+"\n"
   paramtemplate.close()
   paramin.writelines(paramdata)
   paramin.close()
   
   # running macsim and grep value
   os.system("macsim > "+str(nameindex) + str(rateindex)+".debug")
   os.system("cat params.in > params32_" + str(namelookup[nameindex]) + str(ratevalue[rateindex]) +".in")
   
   macsimout = open(str(nameindex) + str(rateindex)+".debug","r")
   for line in macsimout.readlines():
    if "Core_Total  Finished" in line:
     print(line)
     lineData = line.split()
     result[nameindex][rateindex] = int(lineData[5].split(":")[1])
     print(result)
     print(str(nameindex) +" "+ str(rateindex)+ " " + lineData[5].split(":")[1])
   macsimout.close()
 
 # write to file
 resultFile = os.getcwd()+"/sched_rate_impact.csv"
 print("write to " + resultFile)
 with open(resultFile,"w") as f:
  #write first line
  f.write("Cycles")
  for name in namelookup:
   f.write("; %s"%(name))
  f.write("\n")
  
  # write data
  for rateindex in range(5):
   f.write("%d"%(ratevalue[rateindex]))
   for nameindex in range(4):
    f.write("; %d"%(result[nameindex][rateindex]))
   f.write("\n")
  
 f.close() 
   

    

if __name__ == "__main__":
 main()
