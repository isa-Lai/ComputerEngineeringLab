import os
import sys

schename = ["io","ooo"]
sizevalue = [1,4,8,16,64,128,256,512]

def main():
 result = [[],[]]
 print(result)
 
 for nameindex in range(2):
  for sizeindex in range(len(sizevalue)):
   #edit params.in
   paramtemplate = open("params52.template","r")
   paramin = open("params.in","w")
   paramdata = paramtemplate.readlines()
   for lineindex in range(len(paramdata)):
    if "large_core_schedule" in paramdata[lineindex]:
     paramdata[lineindex] = "large_core_schedule" + " " + str(schename[nameindex])+"\n"
    if "rob_large_size" in paramdata[lineindex]:
     paramdata[lineindex] = "rob_large_size" + " " + str(sizevalue[sizeindex])+"\n"
   paramtemplate.close()
   paramin.writelines(paramdata)
   paramin.close()
   
   # running macsim and grep value
   os.system("macsim > "+str(nameindex) + str(sizeindex)+".debug")
   os.system("cat params.in > params52_" + str(schename[nameindex]) + str(sizevalue[sizeindex]) +".in")
   
   macsimout = open(str(nameindex) + str(sizeindex)+".debug","r")
   for line in macsimout.readlines():
    if "Core_Total  Finished" in line:
     print(line)
     lineData = line.split()
     result[nameindex].append(int(lineData[5].split(":")[1]))
     print(result)
     print(str(nameindex) +" "+ str(sizeindex)+ " " + lineData[5].split(":")[1])
   macsimout.close()

 # write to file
 resultFile = os.getcwd()+"/cycle32.csv"
 print("write to " + resultFile)
 with open(resultFile,"w") as f:
  #write first line
  f.write("ROB Size; IO execution time; OOO execution time")
  
  # write data
  for sizeindex in range(len(sizevalue)):
   f.write("%d"%(sizevalue[sizeindex]))
   for nameindex in range(2):
    f.write("; %d"%(result[nameindex][sizeindex]))
   f.write("\n")
   
  
 f.close() 
   

    

if __name__ == "__main__":
 main()
