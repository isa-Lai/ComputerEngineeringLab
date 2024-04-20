import os
import sys

def main():
 uopCount = {} #uopCount[uopType][bmName] = count
 bmNames = set()
 uopLatency = {} #uopLatency[uopType] = latency
 totalInst = {} #totalInst[bmName] = instcount
 totalCycle = {} #totalCycle[bmName] = cycle
 macsimCPI = {} #macsimCPI[bmName] = cycle/inst
 
 #reading
 curDir = os.getcwd()
 print("open current dir " + curDir)
 for filename in os.listdir(curDir):
  # read done_exec
  if filename.endswith("_done.debug"):
   bmName = filename.split("_")[0]
   bmNames.add(bmName)
   with open(os.path.join(curDir,filename)) as f:
    print("read file " + filename)
    for num, line in enumerate(f):
     lineData = line.split()
     uopType = lineData[9].split(":")[1]
     if uopType in uopCount:
      if bmName in uopCount[uopType]:
       uopCount[uopType][bmName] += 1
      else:
       uopCount[uopType][bmName] = 1
     else:
      uopCount[uopType] = {}
      uopCount[uopType][bmName] = 1
      
     #read latency
     if uopType in uopLatency:
      pass 
     else:
      if int(lineData[16].split(":")[1]) == -1:
       #see mem op as 10
       uopLatency[uopType] = 10
      else:
       uopLatency[uopType] = int(lineData[16].split(":")[1])
      
   f.close()

  #read total
  if filename.endswith(".debug") and not(filename.endswith("_done.debug")):
   bmName = filename.split(".")[0]
   with open(os.path.join(curDir,filename)) as f:
    print("read file " + filename)
    for num, line in enumerate(f):
     if "Core_Total  Finished" in line:
      lineData = line.split()
      totalInst[bmName] = int(lineData[4].split(":")[1])
      totalCycle[bmName] = int(lineData[5].split(":")[1])
      macsimCPI[bmName] = float(totalCycle[bmName])/float(totalInst[bmName])
   f.close()
 
 #do analysis total
 totalUOPs = {}
 for bm in bmNames:
  totalUOP = 0
  for key in uopCount:
   if bm in uopCount[key]:
    totalUOP += uopCount[key][bm]
   else:
    uopCount[key][bm] = 0
  totalUOPs[bm] = totalUOP
  
 #do analysis CPI
 CPI = {} #CPI[bmName] = cycle/inst
 for bm in bmNames:
  CPIcal = 0
  for key in uopCount:
   CPIcal += float(uopCount[key][bm]) / float(totalUOPs[bm]) * float(uopLatency[key]) 
  CPI[bm] = CPIcal
      
 #write
 print(bmNames)
 print(uopCount)
 print(totalUOPs)
 print(uopLatency)
 print(totalInst)
 print(totalCycle)
 print(macsimCPI)
 
 resultFile = os.getcwd()+"/UOP_No.csv"
 resultFile = os.getcwd()+"/avg_CPI.csv"
 print("write to " + resultFile)
 with open(resultFile,"w") as f:
  #write first line
  f.write("UOP; ")
  for bm in bmNames:
   f.write("%s; "%(bm))
  f.write("\n")
  
  # write data line
  for key in uopCount:
   f.write("%s; "%(key))
   for bm in bmNames:
    f.write("%d; "%(uopCount[key][bm]))
   f.write("\n")
   
  #write total uop
  f.write("total_UOPS; ")
  for bm in bmNames:
   f.write("%d; "%(totalUOPs[bm]))
  f.write("\n")
  
  #write CPI
  f.write("CPI; ")
  for bm in bmNames:
   f.write("%.3f; "%((CPI[bm])))
  f.write("\n")

  #write total inst
  f.write("Total_Instructions; ")
  for bm in bmNames:
   f.write("%d; "%(totalInst[bm]))
  f.write("\n")
  
  #write total cycles
  f.write("Total_cycles; ")
  for bm in bmNames:
   f.write("%d; "%(totalCycle[bm]))
  f.write("\n")
  
  #write macsim cpi
  f.write("CPI_macsim; ")
  for bm in bmNames:
   f.write("%.3f; "%((macsimCPI[bm])))
  f.write("\n")

 f.close()

if __name__ == "__main__":
 main()
