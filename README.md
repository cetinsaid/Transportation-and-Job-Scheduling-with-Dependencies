# Transportation-and-Job-Scheduling-with-Dependencies
1. Job Scheduling with Dependencies
Objective: Compute the minimum time required to complete all jobs while respecting their dependencies.

Approach: The problem is modeled as a Directed Acyclic Graph (DAG), where nodes represent jobs, and edges represent prerequisites. The solution uses topological sorting and dynamic programming to calculate the earliest finish times of all jobs.

2. Shortest Path Finding
Objective: Find the shortest path between train stations, considering:

Train connections (e.g., from station 2, you can go to stations 3 and 4 but not directly to 5).
Walking paths from each station directly towards the target.

Approach: The problem is modeled as a weighted graph, where:

Stations are nodes.
Train paths and walking paths are weighted edges representing travel times.
The algorithm uses Dijkstra's algorithm to compute the minimal travel time to the target station, considering both train and walking paths.
