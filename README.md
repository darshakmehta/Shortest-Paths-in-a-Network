/***

@name Darshak Mehta

@studentID 801020115

***/

				ITCS 6114/8114: Algorithms and Data Structures
				Programming Project 2: Shortest Paths in a Network

# Introduction:

Consider a data communication network that must route data packets (email, MP3 files, or video
files, for example). Such a network consists of routers connected by physical cables or links. A
router can act as a source, a destination, or a forwarder of data packets. We can model a network
as a graph with each router corresponding to a vertex and the link or physical connection between
two routers corresponding to a pair of directed edges between the vertices.

A network that follows the OSPF (Open Shortest Path First:) protocol routes packets using
Dijkstra's shortest path algorithm. The criteria used to compute the weight corresponding to a
link can include the time taken for data transmission, reliability of the link, transmission cost, and available bandwidth. Typically each router has a complete representation of the network graph and associated information available to it.

For the purposes of this project, each link has associated with it the transmission time taken
for data to get from the vertex at one end to the vertex at the other end. You will compute the
best path using the criterion of minimizing the total time taken for data to reach the destination.

The shortest time path minimizes the sum of the transmission times of the links along the path. The network topology can change dynamically based on the state of the links and the routers. For example, a link may go down when the corresponding cable is cut, and a vertex may go down when the corresponding router crashes. In addition to these transient changes, changes to a network occur when a link is added or removed.


# Application of Shortest Path problem:

1. Given a number of cities with highways connecting them, find the shortest path from New York to Chicago. The traffic and length of the highways are path weights.
2. Word Ladder puzzles


#### Pseudocode of Dijkstra's using Priority Queue:

~~~
1  function Dijkstra(Graph, source):
2      dist[source] ← 0                                    // Initialization
3
4      create vertex set Q
5
6      for each vertex v in Graph:           
7          if v ≠ source
8              dist[v] ← INFINITY                          // Unknown distance from source to v
9              prev[v] ← UNDEFINED                         // Predecessor of v
10
11         Q.add_with_priority(v, dist[v])
12
13
14     while Q is not empty:                              // The main loop
15         u ← Q.extract_min()                            // Remove and return best vertex
16         for each neighbor v of u:                      // only v that is still in Q
17             alt ← dist[u] + length(u, v) 
18             if alt < dist[v]
19                 dist[v] ← alt
20                 prev[v] ← u
21                 Q.decrease_priority(v, alt)
22
23     return dist, prev
~~~


# Program Description:
Programming Language : JAVA
Compiler Version: JDK 1.8.0_151
OS: Windows X System [Not tested for UNIX system]

# Compile the program:
Before building the java class file make sure java(i.e jdk1.8) is installed in the system.

##### Command to build java class files:
___
~~~~
javac Graph.java
~~~~

Once the above command is executed, a class file named filename.class is created

# Run the Program:
Arguments passed are
~~~
InputfileName - The file which has the nodes of the network with weights
~~~

##### Command to run java program:
___
~~~
java ClassName InputfileName < QueryfileName > OutputfileName
Example: java Graph network.txt <  queries.txt > output.txt
~~~
Note: ClassName has the main function

# Data Structure:
1. **Heap data structure** is used to implement the algorithm and have design min binary heap (priority queue) where smallest element has the highest priority (root node in tree shape)
2. **HashMap data structure** is used to store information about the vertex 
3. **LinkedList** data structure is used to add all the edges of the graph
4. **TreeSet** data structure stores the visited nodes  [Sorted sort, no duplicate values]. It sorts the vertex in alphabetical order useful while printing the graph.

# Implementation:

Implementation code is in single file Graph.java which has complete 5 tasks required for the project. The algorithm implemented is an algorithm used to find the shortest path between nodes in graph called as Dijkstra's Algorithm. Adding to the above task, the reachable vertices task is accomplish using recursive algorithm (not using the shortest path algorithm described above) to find the vertices connected to each vertex. The task of reachable is achieved in O(n logn) time, where n = Number of vertices.

Classes used to achieve the implementation are:

1. **Graph:** Build the network Graph using input file and perform operations based on query file on the network. 
2. **Vertex:** Vertex class is used to represent vertex of the graph.
3. **Edge:** Edge class is used to represent edges of the graph.
4. **Path:** Path class stores name and distance of path.
5. **Pair:** Pair class represents the String pairs.
6. **Heap:** Heap Class is use to efficiently design Dijkstra's Algorithm using concept of minimum priority queue.
7. **GraphException:** Used to signal violations of preconditions for shortest path algorithms.

# Task:
1. **Building the Graph:** Using network input file we will build our graph. We have use double to handle the floating point distance. Each input link is represented by adding two edges, one in each direction
2. **Changes to the Graph:** All the operations for the changes of the Graph are present in the Graph class.
3. **Finding the Shortest Path:** Using Dijkstra's algorithm we will compute the shortest path distance, and we have implemented using Heap Data structure (Min priority queue).
4. **Print Graph:** We have used TreeSet datastructure to obtain the vertex in alphabetical order, and no duplicates.
5. **Reachable Vertices:** For every vertex, we have find the reachable vertices inn alphabetical order and we have not use the shortest path algorithm to compute that.
### The algorithm used is describe as below
___
~~~~
    ALGORITHM checkReachable( )
  	1. Sorting all the vertices in alphabetical order
  	2. FOR each vertex which is "up" in the list
  	3. 		PRINT vertex_Name
  	4.		BUMP vertex to visited_node list
  	5.		CALL reachable(vertex)
  	6.	END FOR
  	7.	PRINT all the Vertices from visited_nodes in alphabetical order

  	ALGORITHM reachable(vertex)
  	1. FOR each adjacent vertices
  	2.		IF edge is down OR vertex is down THEN
  	3.			CONTINUE;
  	4.		END IF
  	5.		IF Vertex exists in visited_node list THEN
  	6.			CONTINUE
  	7.		ELSE
  	8.			BUMP vertex to visited node list
  	9.			CALL reachable(vertex)
  	10.		END IF
  	11.	END FOR
~~~~

## **Complexity:**
The algorithm checkReachable() will be called for every n vertices. So it will take O(n) running time. The recursive call to reachable() will call recursively for every adjacent vertices. So it will take O(logn) running time. Therefore, the overall running time of the algorithm will be O(n logn).

# What works and fails?
1. Input file is expected to be precise format as required by the correct execution of the program.
2. Query file should have "quit" as the last query, else the program will run indefinitely.
3. Not tested on UNIX OS. Works well on windows X
4. The building graph, operations on graph, printing the entire graph, printing shortest path and finding the reachable vertices from every vertex all the 5 task expected for the project works well.


# References:
1. https://stackoverflow.com/questions/4412031/what-are-the-applications-of-the-shortest-path-algorithm 
2. https://en.wikipedia.org/wiki/Shortest_path_problem 
3. https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
4. https://gcbenison.wordpress.com/2012/03/28/the-nerdy-stuff-matters/
5. https://en.wikipedia.org/wiki/Open_Shortest_Path_First
6. https://users.csc.calpoly.edu/~jdalbey/SWE/pdl_std.html
