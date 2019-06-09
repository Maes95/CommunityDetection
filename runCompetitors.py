# import networkx as nx
# from networkx.algorithms import community
# import matplotlib.pyplot as plt

# filepath="graphs/test/1000_0.3network.txt"

# G=nx.Graph() 
# with open(filepath) as fp:  
#     line = fp.readline()
#     for i in range(0,int(line.strip().split(" ")[0])-1):
#         G.add_node(i)
#     while line:
#         line = fp.readline()
#         if not line:
#             break
#         edge = line.strip().split(" ")
#         G.add_edge(int(edge[0]),int(edge[1]))

# print(nx.is_connected(G))

# EDGE BETWEENESS

# communities_generator = community.girvan_newman(G)
# top_level_communities = next(communities_generator)
# next_level_communities = next(communities_generator)
# communities=sorted(map(sorted, top_level_communities))
# print(len(communities))

# GREEDY MODULARITY (CONSTRUCTIVE)

# communities = community.greedy_modularity_communities(G)
# communities=sorted(map(sorted, communities))
# print(len(communities))

from igraph import *
G = Graph()

filepath="graphs/test/1000_0.3network.txt"

with open(filepath) as fp:  
    line = fp.readline()
    for i in range(0,int(line.strip().split(" ")[0])-1):
        G.add_vertices(i)
    while line:
        line = fp.readline()
        if not line:
            break
        edge = line.strip().split(" ")

        edge = ( int(edge[0]), int(edge[1]) )
        G.add_edges([edge])

fast_greedy_result = G.community_fastgreedy()
print(fast_greedy_result)