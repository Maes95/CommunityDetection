
# coding: utf-8

# In[1]:


from igraph import *
from os import listdir, makedirs, path
from os.path import isfile, join
import time
import csv


# In[2]:


instances=[]
for file in [f for f in listdir("graphs/test/") if isfile(join("graphs/test/", f))]:
    G = Graph()
    filepath="graphs/test/"+file
    with open(filepath) as fp:  
        line = fp.readline()
        num_vertex = int(line.strip().split(" ")[0])
        for i in range(0,num_vertex):
            G.add_vertex(i)
        while line:
            line = fp.readline()
            if not line:
                break
            edge = line.strip().split(" ")
            A = int(edge[0])
            B = int(edge[1])
            if not G.are_connected(A,B):
                G.add_edge(A,B)
    instances.append({ "file": file, "graph": G})


# In[3]:


def fast_greedy(g):
    result = []
    for i in range(0,g.vcount()):
        result.append(0)
    start = round(time.time()* 1000)
    communities = g.community_fastgreedy().as_clustering()
    exec_time = start = round(time.time()* 1000) - start
    for i in range(0,len(communities)):
        for j in communities[i]:
            result[j] = i
    return "fast_greedy", result, exec_time


# In[4]:


def eigenvector(g):
    result = []
    for i in range(0,g.vcount()):
        result.append(0)
    start = round(time.time()* 1000)
    communities = g.community_leading_eigenvector()
    exec_time = start = round(time.time()* 1000) - start
    for i in range(0,len(communities)):
        for j in communities[i]:
            result[j] = i
    return "eigenvector", result, exec_time


# In[5]:


def label_propagation(g):
    result = []
    for i in range(0,g.vcount()):
        result.append(0)
    start = round(time.time()* 1000)
    communities = g.community_label_propagation()
    exec_time = start = round(time.time()* 1000) - start
    for i in range(0,len(communities)):
        for j in communities[i]:
            result[j] = i
    return "label_propagation", result, exec_time


# In[6]:


def multilevel(g):
    result = []
    for i in range(0,g.vcount()):
        result.append(0)
    start = round(time.time()* 1000)
    communities = g.community_multilevel()
    exec_time = start = round(time.time()* 1000) - start
    for i in range(0,len(communities)):
        for j in communities[i]:
            result[j] = i
    return "multilevel", result, exec_time


# In[7]:


def edge_betweenness(g):
    result = []
    for i in range(0,g.vcount()):
        result.append(0)
    start = round(time.time()* 1000)
    communities = g.community_edge_betweenness().as_clustering()
    exec_time = start = round(time.time()* 1000) - start
    for i in range(0,len(communities)):
        for j in communities[i]:
            result[j] = i
    return "edge_betweenness", result, exec_time


# In[8]:


def spinglass(g):
    result = []
    for i in range(0,g.vcount()):
        result.append(0)
    start = round(time.time()* 1000)
    communities = g.community_spinglass()
    exec_time = start = round(time.time()* 1000) - start
    for i in range(0,len(communities)):
        for j in communities[i]:
            result[j] = i
    return "spinglass", result, exec_time


# In[9]:


def walktrap(g):
    result = []
    for i in range(0,g.vcount()):
        result.append(0)
    start = round(time.time()* 1000)
    communities = g.community_walktrap().as_clustering()
    exec_time = start = round(time.time()* 1000) - start
    for i in range(0,len(communities)):
        for j in communities[i]:
            result[j] = i
    return "walktrap", result, exec_time


# In[10]:


algorithms = [fast_greedy, eigenvector, label_propagation, multilevel, edge_betweenness, spinglass, walktrap]
times = []
for instance in instances:
    for algorithm in algorithms:
        name, result, exec_time = algorithm(instance["graph"])
        dir_="results/algorithms_results/%s"%(name)
        if not path.exists(dir_): makedirs(dir_)
        with open("%s/community_%s"%(dir_, instance['file']), "w+") as fp:
            fp.write(str(result))
            times.append((name, instance["file"],exec_time))
with open("results/algorithms_results/times.txt", "w+") as fp:
    writer = csv.writer(fp, delimiter=',')
    writer.writerow(["Algorithm","Instance","Time(ms)"])
    for t in times:
        writer.writerow(t)


# In[11]:


times

