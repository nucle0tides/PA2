import java.lang.reflect.Array;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class GraphProcessor {

	public WebGraph graph;
	private int num_vertices;

	public GraphProcessor(String graphData) throws FileNotFoundException { 
		this.graph = new WebGraph();
		constructGraph(graphData);
	}
	
	/**
	 * Return the out degree of vertex v.
	 * @param v - input vertex
	 * @return out degree of vertex v.
	 */
	public int outDegree(String v) {
		//If the vertex is in the matrix
        if(graph.getAdjacencyMatrix().get(v) != null){
        	return graph.getAdjacencyMatrix().get(v).size();
		}
        return 0;
	}
	
	/**
	 * Calculate the BFS path from vertex u to vertex v.
	 * 
	 * @param u, start vertex
	 * @param v, finish vertex
	 * @return ArrayList of strings repr bfs path from vertex u to vertex v.
	 */
	public ArrayList<String> bfsPath(String u, String v) { 
	    Queue<String> bfs_queue = new ArrayDeque<>();
	    HashSet<String> visited = new HashSet<String>();
	    HashMap<String, String> parent = new HashMap<String, String>();
	    
	    bfs_queue.add(u);
	    visited.add(u);
	    parent.put(u, null);

		if(u.equals(v)) {
			ArrayList<String> path = new ArrayList<String>();
			path.add(v);
	    	return path;
	    }
	    
	    // while queue is not empty
	    while(!bfs_queue.isEmpty()) { 
	    	String current = bfs_queue.remove();
	    	ArrayList<String> edges = graph.getAdjacencyMatrix().get(current);
	    	// for each edge, w, current -> w
			if(edges != null) {
				for (String edge : edges) {
					// if edge is unvisited
					if (!visited.contains(edge)) {
						// keeping track of parent to mimic a tree like structure
						parent.put(edge, current);
						bfs_queue.add(edge);
						visited.add(edge);
					}
				}
			}
	    }

	    String path_vertex = parent.get(v);
	    if(path_vertex == null) { 
	    	return new ArrayList<String>();
	    }
	    
	    // create reverse of the path
	    ArrayList<String> rev_path = new ArrayList<String>();
	    rev_path.add(v);
	    while(path_vertex != null) { 
	    	rev_path.add(path_vertex);
	    	path_vertex = parent.get(path_vertex);
	    }
	    
	    ArrayList<String> path = new ArrayList<String>();
	    for(int i = rev_path.size() - 1; i >= 0; i--) { 
	    	path.add(rev_path.get(i));
	    }
	    return path;
	}
	
	/**
	 * For strongly connected graphs, the diameter is the smallest number, d, 
	 * such that there is a path of length <= d between any pair of 
	 * vertices. 
	 * 
	 * For graphs that are not strongly connected
	 * the diameter is 2n. 
	 * @return diameter of the graph. 
	 */
	public int diameter() { 
		int longest_path = 0;
		ArrayList<String> vertices = graph.getVertices();
		for (String v1: vertices) {
			for (String v2: vertices) {
				ArrayList<String> path = bfsPath(v1, v2);
				if(path.isEmpty()){
					return 2 * this.num_vertices;
				}
				if(path.size() > longest_path) {
					longest_path = path.size();
				}
			}
		}
		return longest_path;
	}
	
	/**
	 * Given a vertex x in V, the centrality of x is the number 
	 * of shortest paths that go via x. 
	 * 
	 * {<x, y> | x, y in V, at least one shortest path from x to y via v}
	 * @param v
	 * @return
	 */
	public int centrality(String v) { 
		ArrayList<ArrayList<String>> paths = new ArrayList<ArrayList<String>>();
		ArrayList<String> vertices = graph.getVertices();
		int path_counts = 0;
		for (String v1 : vertices) { 
			for (String v2 : vertices) {
				ArrayList<String> pathresult = bfsPath(v1, v2);
				if(pathresult.contains(v)){
					if(pathresult.size() == 1 && pathresult.get(0).equals(v)){
						break;
					}else {
						path_counts++;
					}
				}
				paths.add(pathresult);
			}
		}

		return path_counts;
	}
	
	/**
	 * Given a filename, graphData, fills a WebGraph object with vertices and edges. 
	 * @param graphData
	 * @throws FileNotFoundException
	 */
	private void constructGraph(String graphData) throws FileNotFoundException { 
		File f = new File(graphData);
		Scanner s = new Scanner(f);
		
		this.num_vertices = s.nextInt();
		s.nextLine();
		
		while(s.hasNextLine()) { 
			String vertex_edge = s.nextLine();
			processVertexEdgeLine(vertex_edge);
		}
		s.close();
	}
	
	/**
	 * Method to process a line of graphData.
	 * Given a line 'v1 v2' where v1 has an edge to v2
	 * Adds two vertices v1 and v2, and creates an edge from v1 to v2
	 * @param vertex_edge
	 */
	private void processVertexEdgeLine(String vertex_edge) { 
		Scanner line_scanner = new Scanner(vertex_edge);
		String vertex = line_scanner.next();
		String edge = line_scanner.next();
		
		this.graph.addVertex(vertex);
		this.graph.addVertex(edge);
		this.graph.addEdge(vertex, edge);
		line_scanner.close();
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		GraphProcessor gp = new GraphProcessor("/home/nick/Documents/Github/PA2/WikiCS.txt");
		System.out.println(gp.num_vertices);
		int num_edges=0;
		Iterator itr = gp.graph.getAdjacencyMatrix().values().iterator();
		while(itr.hasNext()){
			ArrayList<String> edges = (ArrayList<String>) itr.next();
			num_edges = num_edges + edges.size();
		}
		System.out.println("Edges: "+num_edges);
		Set<String> collection = gp.graph.getAdjacencyMatrix().keySet();
		Iterator iterator = collection.iterator();
		int out_degree = 0;
		String vertex_with_largest_out_degree = "A";
		while(iterator.hasNext()) {
			String current_vertex = (String)iterator.next();
			int current_degree = gp.outDegree(current_vertex);
			if(current_degree > out_degree){
				out_degree = current_degree;
				vertex_with_largest_out_degree = current_vertex;
			}
		}
		System.out.println("Largest out degree: " + out_degree+ ". vertex: "+ vertex_with_largest_out_degree);

		Set<String> collection2 = gp.graph.getAdjacencyMatrix().keySet();
		Iterator iterator2 = collection2.iterator();
		int centrality = 0;
		String vertex_with_largest_centrality = "A";
		while(iterator2.hasNext()) {
			String current_vertex = (String)iterator2.next();
			int current_centrality= gp.centrality(current_vertex);
			if(current_centrality > centrality){
				centrality = current_centrality;
				vertex_with_largest_centrality = current_vertex;
			}
		}

		System.out.println("Largest centrality: " + centrality+ ". vertex: "+ vertex_with_largest_centrality);

		System.out.println("Diameter: "+gp.diameter());


	}
}
