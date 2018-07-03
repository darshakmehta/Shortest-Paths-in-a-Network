/***

@name Darshak Mehta
@studentID 801020115

***/

import java.util.*;
import java.lang.*;
import java.io.*;

// Used to signal violations of preconditions for shortest path algorithms.
class GraphException extends RuntimeException {
	public GraphException(String name) {
		super(name);
	}
}

 /**
  * Vertex class is used to represent vertex of the graph.
  */
class Vertex {
	public String name; // Vertex name
	public List<Edge> adj; // Adjacent vertices
	public Vertex prev; // Previous vertex on shortest path
	public Double dist; // Distance 
	public String status; // status
	
	public Vertex(String nm) {
		name = nm;
		adj = new LinkedList<Edge>();
		status = "up"; //initialize to "UP" status (default status)
		reset();
	}
	public void reset() {
		dist = (double) Graph.INFINITY; // Resets the distance of Vertex to infinite 
		prev = null; //And set the prev pointer to null
	}
}

/**
 * Edge class is used to represent edges of the graph.
 */
class Edge {
	public Vertex destName; // Destination Node Name
	public double distance; // Distance of path
	public Edge(Vertex w, double d) {
		destName = w;
		distance = d;
	}
}
/*
 * Path class stores name and distance of path
 */
class Path {
	String str;
	double dist;
	public Path(String s, Double d) {
		str = s;
		dist = d;
	}
}

/*
 * Pair class represents the String pairs
 */
class Pair {
	String str1;
	String str2;

	public Pair(String s1, String s2) {
		str1 = s1;
		str2 = s2;
	}
	public String key() {
		return str1;
	}
	public String value() {
		return str2;
	}
}

/**
 * Heap Class is use to efficiently design Dijkstra's Algorithm using concept of minimum priority queue
*/
class Heap {
	Path[] path;
	int heap_size; 
	int numberofvertices; // number of vertices

	public Heap(int numberofvertices) {
		this.numberofvertices = numberofvertices; //maximum heap size
		this.heap_size = 0;
		path = new Path[this.numberofvertices + 1];
		path[0] = new Path("null", (double) Integer.MIN_VALUE); //initialize first path element to null with minimum value
	}
	public int size() {
		return heap_size; //return the size of the Heap
	}
	private int parent(int i) {
		return i >> 1; //right shift by 1 ==> i >> 1
	}
	public int left(int i) {
		return i <<  1; //left shift by 1 ==> i << 1
	}
	public int right(int i) {
		return (2*i + 1);
	}
	private void swap(int i, int j) {
		Path temp;
		temp = path[i];
		path[i] = path[j];
		path[j] = temp;
	}
	private void minHeapify(int i) {
		int l = left(i);
		int r = right(i);
		int smallest;
		boolean flag = false;

		//check if i is leaf node
		if (i - 1 >= (heap_size / 2) && i <= heap_size) {
			flag = true;
		} else { 
			flag = false;
		}
		if (!flag && heap_size > 0) {
			if(path[l].dist < path[i].dist)
				smallest = l;
			else
				smallest = i;
			if(path[r].dist < path[smallest].dist)
				smallest = r;
			if(smallest !=i) {
				swap(i, smallest);
				minHeapify(smallest);
			}
		}
	}
	public void insert(Path p) {
		path[++heap_size] = p;
		int i = heap_size;
		int parent = parent(i);
		while (path[i].dist < path[parent].dist) {
			swap(i, parent);
			i = parent;
		}
	}
	public Path extractMin() {
		Path root = path[1];
		path[1] = path[heap_size--];
		minHeapify(1);
		return root;
	}
}

/**
 * Build the network Graph and perform operations on the network
 */
public class Graph {

	public static final int INFINITY = Integer.MAX_VALUE; //Initialize to maximum value
	private Map<String, Vertex> vertexMap = new HashMap<String, Vertex>(); //to store information of the vertices
	private LinkedList<Pair> edgeList = new LinkedList<Pair>(); //add edges to the linked list
	private TreeSet<String> visited = new TreeSet<String>(); //store visited nodes and sort alphabetically

	/**
	 * If vertexName is not present, add it to vertexMap. In either case, return the Vertex.
	 */
	private Vertex getVertex(String vertexName) {
		Vertex v = vertexMap.get(vertexName);
		if (v == null) {
			v = new Vertex(vertexName);
			vertexMap.put(vertexName, v); //Key is the Vertex Name and vertex reference of that vertex
		}
		return v;
	}

	/**
     * Add a new edge to the graph.
     */
	public void addEdge(String sourceName, String destinationName, double distance) {
		Vertex v = getVertex(sourceName);  // create source vertex if does not exist 
		Vertex w = getVertex(destinationName);    // create destination vertex if does not exist

		Iterator<Edge> listIterator = v.adj.listIterator();
		int i = 0;
		int flag = 0;
		while (listIterator.hasNext()) {  //first time source adjacency list does not have the adj so flag remains 0
			Edge edge = listIterator.next();
			if (w.name.equals(edge.destName.name)) {   //check for destName of current edge getting added is equal to the adjacency list of the current source
				v.adj.get(i).distance = distance;
				flag = 1;
			}
			i++; //couldnt find the destName in the adjacency list
		}
		if (flag == 0)
			v.adj.add(new Edge(w, distance));  //add edge to adjacency list of source vertex 
	}

	/**
	 * Check for down or disabled edge
	 */
	public Boolean isEdgeDown(String source, String destination) {
		Iterator<Pair> iterator = edgeList.iterator();
		int flag = 0;
		if (edgeList.size() != 0) {
			while (iterator.hasNext()) {
				Pair pair = iterator.next();
				if (pair.key().equals(source) && pair.value().equals(destination)) {
					flag = 1;
					break;
				}
			}
		}
		if (flag == 0)
			return false;
		else
			return true; //edge is down/disabled
	}

	/**
	 * This function will print all the contents of the graph in Alphabetical Order.
	 * If a vertex or an edge is down then it will append the string "DOWN" near it.
	 */
	private void print() {
		TreeSet<String> treeSet = new TreeSet<String>();
		System.out.println();
		for (String key : vertexMap.keySet()) { //Iterate throught all keys in hashmap and add to the treeset
			treeSet.add(key);
		}
		Iterator<String> iterator = treeSet.iterator();
		while (iterator.hasNext()) {
				String adjacentNode = iterator.next();
				System.out.print(adjacentNode); //print the node in hand
				if (vertexMap.get(adjacentNode).status != "up")
					System.out.print(" down");
				System.out.println();
				TreeSet<String> adjacent = new TreeSet<String>(); // Unique vertices in alphabetical order
				HashMap<String, Double> hashMap = new HashMap<String, Double>(); 
				//Adding adjacent destination vertex and hashmap to put key vertex and value as distance of vertex
				for (Edge key : vertexMap.get(adjacentNode).adj) {
					adjacent.add(key.destName.name); //All destination vertex from adjaceny list in alphabetical order
					hashMap.put(key.destName.name, key.distance);
				}
				Iterator<String> adjacentIterator = adjacent.iterator(); //Iterator for unique vertices
				while (adjacentIterator.hasNext()) {
					String node = adjacentIterator.next();
					System.out.print("  " + node + " " + hashMap.get(node)); //print all the adjacent node in the current vertex adjacency list
					if (isEdgeDown(adjacentNode, node))
						System.out.print(" down");
					System.out.println();
				}
		}
	}

	/**
	 * Process a request entered by user, To exist please enter "quit" query
	 */
	public static boolean processRequest(Scanner sc, Graph g) {
		try {
			String query = sc.nextLine();
			String[] a = query.split(" "); //First word will decide what kind of action we should process
			switch (a[0]) {
				case "print":
					g.print();
					break;
				case "path":
						g.dijkstras(a[1]);
						g.printPath(a[2]);
					break;
				case "edgedown":
						g.edgeDown(a[1], a[2]);
					break;
				case "vertexdown":
						g.vertexDown(a[1]);
					break;
				case "reachable":
					g.checkReachable();
					break;
				case "edgeup":
						g.edgeUp(a[1], a[2]);
					break;
				case "vertexup":
						g.vertexUp(a[1]);
					break;
				case "deleteedge":
						g.deleteEdge(a[1], a[2]);
					break;
				case "addedge":
						g.addEdge(a[1], a[2], Double.parseDouble(a[3]));
					break;
				case "quit": 
					return false; //exit query
				default:
					System.out.println("Not valid query");
					break;
			}
		} catch (NoSuchElementException e) {
			System.out.println("Not valid query");
		} catch (GraphException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
     * Initializes the vertex output info prior to running any shortest path algorithm.
     */
    private void clearAll( )
    {
        for( Vertex v : vertexMap.values( ) )
            v.reset( );
    }

	/**
	 * Dijkstra's: shortest-path algorithm using MinHeap.
	 */
	public void dijkstras(String startVertex) {
		clearAll(); //Initialize the distance to infinite and previous to null
		Heap minPriorityQueue = new Heap(vertexMap.size()); //number of vertices in hashmap, initialize that much size of heap
		Vertex initialVertex = vertexMap.get(startVertex);
		if (initialVertex == null){
			System.out.println("Initial vertex is null");
			return; //stop dijkstras
		}
		if (initialVertex.status != "up"){ //If vertex is disabled, return
			System.out.println("Vertex is Down");
			return;
		}
		initialVertex.dist = 0.0; //initialize start vertex to 0
		minPriorityQueue.insert(new Path(initialVertex.name, initialVertex.dist)); //add to queue
		while (minPriorityQueue.size() != 0) {
			Vertex v = getVertex(minPriorityQueue.extractMin().str); //extract the element at root
			for (Edge edge : v.adj) {  //for every edge in the adjacency list of current vertex in hand
				Vertex w = edge.destName;
				if (w.status != "up") //check for disabled vertices
					continue;
				if (isEdgeDown(v.name, w.name)) //check for disabled edges
					continue;
				double weight = edge.distance;
				if (weight < 0) 
					throw new GraphException("Graph has negative edge");
				if (w.dist > v.dist + weight) {
					w.dist = v.dist + weight;
					w.prev = v; //set previous of destination to source
					minPriorityQueue.insert(new Path(w.name, w.dist));
				}
			}
		}
	}

	/**
     * Recursive routine to print shortest path.
     */
	private void printPath(Vertex dest) {
		Vertex previous = dest.prev;
		String name = dest.name;
		if (previous != null) {
			printPath(previous);
			System.out.print(" ");
		}
		System.out.print(name);
	}

	/**
     * Driver routine to print total distance.
     */
	public void printPath(String destinationName) {
		Vertex w = vertexMap.get(destinationName);
		if (w == null){
			System.out.println("Destination vertex not found");
			return;
		}
		else if (w.dist == INFINITY)
			System.out.println(destinationName + " is unreachable");
		else {
			System.out.println();
			printPath(w);
			System.out.printf(" %.2f", w.dist);
			System.out.println();
		}
	}

	/**
	 * Disable the edge and we cannot further traverse through that route
	 */
	public void edgeDown(String source, String destination) {
		Iterator<Pair> iterator = edgeList.iterator();
		int flag = 1;
		if (edgeList.size() != 0) {
			while (iterator.hasNext()) {
				Pair pair = iterator.next();
				if (pair.key() == source && pair.value() == destination) {
					flag = 0;
					break;
				}
			}
		}
		if (flag == 1)
			edgeList.add(new Pair(source, destination));
	}

	/**
	 * Enable the edge and we can further traverse through that route
	 */
	public void edgeUp(String source, String destination) {
		Iterator<Pair> iterator = edgeList.iterator();
		int flag = 0;
		Pair pair = null;
		if (edgeList.size() != 0) {
			while (iterator.hasNext()) {
				pair = iterator.next();
				if (source.equals(pair.key()) && destination.equals(pair.value())) {
					flag = 1;
					break;
				}
			}
		}
		if (flag == 1)
			edgeList.remove(pair);
	}

	/**
	 * Update status of vertext to down.
	 * If vertex is made down, it will disable all the Edges connected to it.
	 */
	public void vertexDown(String s) {
		Vertex v = vertexMap.get(s);
		v.status = "down";
	}
	
	/**
	 * Update status of vertext to up.
	 * If vertex is made up, it will enable all the Edges connected to it.
	 */
	public void vertexUp(String s) {
		Vertex v = vertexMap.get(s);
		v.status = "up";
	}

	/**
	 * Delete edge from the Graph.
	 */
	public void deleteEdge(String sourceName, String destinationName) {
		Vertex v = getVertex(sourceName);	// create source vertex if does not exist 
		Vertex w = getVertex(destinationName);	// create destination vertex if does not exist
		int flag = 0;
		Iterator<Edge> listIterator = v.adj.listIterator();
		int i = 0;
		while (listIterator.hasNext()) {
			Edge edge = listIterator.next();
			if (w.name.equals(edge.destName.name)) {
				flag = 1;
				break;
			}
			i++;
		}
		if (flag == 1)
			v.adj.remove(i);
	}	

	/**
	 * Recursive routine called by checkReachable() method
	 */
	private void reachable(String node) {
		TreeSet<String> adjacent = new TreeSet<String>(); // add all unique vertex name in alphabetical order // treeset removes duplicate 
		for (Edge key : vertexMap.get(node).adj) { 
			if (key.destName.status == "down")
				continue;
			if (isEdgeDown(node, key.destName.name))
				continue;
			adjacent.add(key.destName.name);
		}
		for (String s : adjacent) {
			if (visited.contains(s))
				continue;
			else {
				visited.add(s);
				reachable(s);
			}
		}
	}
	
	/**
	* It displays the vertices accessible from each of the vertex in alphabetical order.
	*/
	private void checkReachable() {
		TreeSet<String> treeSet = new TreeSet<String>();
		System.out.println();
		for (String key : vertexMap.keySet()) {
			if (vertexMap.get(key).status == "down")
				continue;
			treeSet.add(key);
		}
		Iterator<String> iterator = treeSet.iterator();
		while (iterator.hasNext()) {
			String adjacentNode = iterator.next();
			System.out.print(adjacentNode); //print the node in hand
			System.out.println();
			visited.clear();
			visited.add(adjacentNode);
			reachable(adjacentNode);
			Iterator<String> adjacentIterator = visited.iterator(); //Iterator for unique vertices
			while (adjacentIterator.hasNext()) {
				String node = adjacentIterator.next();
				if (node != adjacentNode)
					System.out.println("  " + node);
			}
		}
	}

	/**
     * A main routine that:
     * 1. Reads a file containing edges (supplied as a command-line parameter);
     * 2. Builds the graph;
     * 3. Repeatedly prompts for queries (stops at "quit" query)
     */
	public static void main(String[] args) {
		Graph g = new Graph();
		try {
			FileReader fin = new FileReader(args[0]);
			Scanner graphFile = new Scanner(fin);
			// Read the edges and insert
			String line;
			while (graphFile.hasNextLine()) 
			{
				line = graphFile.nextLine();
				StringTokenizer st = new StringTokenizer(line);
				try 
				{
					if ( st.countTokens() != 3) 
					{
						System.err.println("Skipping ill-formatted line " + line);
						continue;
					}
					String source = st.nextToken();
					String dest = st.nextToken();
					double dist = Double.parseDouble(st.nextToken());
	
					g.addEdge(source, dest, dist);
					g.addEdge(dest, source, dist);
				} catch (NumberFormatException e) 
				   { System.err.println("Skipping ill-formatted line " + line); }
			}
					graphFile.close();
		} catch (IOException e) 
			{ System.err.println( e ); }
		Scanner sc = new Scanner( System.in );
         while( processRequest( sc, g ) )
             ;
         sc.close();
	}
}