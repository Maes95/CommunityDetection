
# coding: utf-8

# In[54]:


import sklearn as sk
import sklearn.metrics
import pandas as pd
import ast
import os
import re
from os import listdir
from os.path import isfile, join, isdir
from collections import defaultdict
regex = re.compile('community_*.*.dat')
dir_ = "results/"
ground_truth_dir=dir_+"ground_truth/"
alg_folder=dir_+"algorithms_results/"
print(os.getcwd())


# In[55]:


df_to_save = {"Instances":[]}
for fn in sorted([f for f in listdir(ground_truth_dir) if isfile(join(ground_truth_dir, f))]):
    filepath=ground_truth_dir+fn
    print('Reading "{name}"...'.format(name=fn))
    size = int(fn.split("_")[1])
    nodes_in_community_gt = [0]*size
    df = pd.read_csv(filepath, sep='\t', header=None, engine = 'python')

    for i in range(0, df.shape[0]):
        nodes_in_community_gt[df.loc[i, 0]-1] = df.loc[i,1]
    
    df_to_save["Instances"].append(fn.split(".dat")[0])
    
    for alg, alg_dir in [(f,join(alg_folder,f)) for f in listdir(alg_folder) if isdir(join(alg_folder, f))]:
        fileToCompare = join(alg_dir,fn.split(".dat")[0]+"network.txt")
        if not alg in df_to_save:
            df_to_save[alg] = []
        nmi = -1.0
        if isfile(fileToCompare):
            maxNMI = 0.0;
            with open(fileToCompare, 'r') as f:
                sol = f.readline()
                while sol:
                    mysol = ast.literal_eval(sol)
                    #print("Evaluating "+str(mysol)+" with gt "+str(nodes_in_community_gt))
                    actNMI = sk.metrics.normalized_mutual_info_score(nodes_in_community_gt, mysol)
                    print("   |-> "+alg+"-> NMI: "+str(actNMI))
                    if(actNMI > maxNMI):
                        maxNMI = actNMI
                        bestSol = mysol
                    sol = f.readline()
            nmi = maxNMI
            #fileToSave = alg_dir+fn.split(".dat")[0]+"_MyBestSol.txt"
            #with open(fileToSave, 'w+') as fts:
            #    fts.write(str(bestSol)+" NMI: "+str(maxNMI))
            #nodes_in_community_gt.clear()
        else:
            print("   |-> No %s network for %s"%(alg,fn))
        df_to_save[alg].append(nmi)
pd.DataFrame(df_to_save).to_csv(dir_+"NMI_results.csv", sep=",", index=False)

