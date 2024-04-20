import os
import sys

def main():
 instCount = {}
 
 #reading
 BMdir = sys.argv[1]
 print("Open BenchMark dir " + BMdir)
 for subdir, dirs, files in os.walk(BMdir):
  for file in files:
   if "BM.txt" in file:
    filePath = os.path.join(subdir,file)
    print("analysis "+ filePath)
    dirNames = subdir.split("/")
    stateCheck = False
    for dirName in dirNames:
     if "benchmarks" in dirName:
      stateCheck = True
      continue
     if stateCheck == True:
   # analysis of all ---------------
      #add file pointer
      f = open(os.getcwd()+"/trace_file_list","w")
      f.write("1\n%s\n"%(filePath))
      f.close()
      
      #run macsim (make sure macsim bin is in PATH
      os.system("macsim > " + dirName +".debug")
      os.system("grep done_exec "+ dirName +".debug > " + dirName +"_done.debug")
      
   # end of anaylysis --------------
      break;
      
 #write

    

if __name__ == "__main__":
 main()
