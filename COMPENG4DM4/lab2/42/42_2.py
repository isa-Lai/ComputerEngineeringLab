import os
import sys

namelookup = "bp_hist_length"
ratevalue = [1,2,4,8,16]

def main():
 result = [0,0,0,0,0]
 print(result)
 
 for rateindex in range(5):
  # for nameindex in range(2):
   #edit params.in
  paramtemplate = open("params42_2.tem","r")
  paramin = open("params42_2.in","w")
  paramdata = paramtemplate.readlines()
  for lineindex in range(len(paramdata)):
    if namelookup in paramdata[lineindex]:
      paramdata[lineindex] = namelookup + " " + str(ratevalue[rateindex])+"\n"
  #  paramtemplate.close()


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
 resultFile = os.getcwd()+"/sweep_bp_length.csv"
 print("write to " + resultFile)
 with open(resultFile,"w") as f:
  #write first line
  f.write("BP Length; Execution Time\n")
  # for name in namelookup:
  #  f.write("; %s"%(name))
  # f.write("\n")
  for count in range(5):
    f.write("%d"%ratevalue[count])
    f.write("; %d\n"%result[count])


  # write data
  # for index in range(3):
  #  f.write("%d"%(ratevalue[rateindex]))
  #  for nameindex in range(4):
  #   f.write("; %d"%(result[nameindex][rateindex]))
  # f.write("Disabled; %d\n"%result[0])
  # f.write("Perfected; %d\n"%result[1])
  # f.write("NonPerfected; %d\n"%result[2])
  
 f.close() 
   

    

if __name__ == "__main__":
 main()
