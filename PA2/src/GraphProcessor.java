import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class GraphProcessor {

	public WebGraph graph;
	private int num_vertices;
	private boolean is_strongly_connected;
	
	public GraphProcessor(String graphData) throws FileNotFoundException { 
		this.graph = new WebGraph();
		constructGraph(graphData);
		this.is_strongly_connected = isStronglyConnected();
	}
	
	/**
	 * Return the out degree of vertex v.
	 * @param v
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
		if(this.is_strongly_connected) { 
			int longest_path = 0;
			ArrayList<String> vertices = graph.getVertices();
			for (String v1: vertices) { 
				for (String v2: vertices) { 
					ArrayList<String> path = bfsPath(v1, v2);
					if(path.size() > longest_path) { 
						longest_path = path.size();
					}
				}
			}
			return longest_path;
		}
		else { 
			return 2 * this.num_vertices;
		}
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
		for (String v1 : vertices) { 
			for (String v2 : vertices) {
				paths.add(bfsPath(v1, v2));
			}
		}
		int path_counts = 0;
		for (ArrayList<String> path : paths) { 
			if(path.contains(v)) {
				//Checking for self-paths, if we have the
				//path start and end at the vertex,
				//and it is the only thing in the path, it is a self path.
				path.remove(v);
				if(path.size() != 0){
					path_counts += 1;
				}
			}
		}
		return path_counts;
	}
	
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
	
	private void processVertexEdgeLine(String vertex_edge) { 
		Scanner line_scanner = new Scanner(vertex_edge);
		String vertex = line_scanner.next();
		String edge = line_scanner.next();
		
		this.graph.addVertex(vertex);
		this.graph.addVertex(edge);
		this.graph.addEdge(vertex, edge);
		line_scanner.close();
	}
	
	/**
	 * Method to determine whether a graph is strongly connected 
	 * i.e., each node can reach every other node
	 * @return
	 */
	private boolean isStronglyConnected() { 
		ArrayList<String> vertices = graph.getVertices();
		for (String v1 : vertices) { 
			for (String v2 : vertices) { 
				if(!v1.equals(v2)) { 
					if(!bfsPathExists(v1, v2)) { 
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/** 
	 * Method to determine if there exists a path between 
	 * @param u
	 * @param v
	 * @return
	 */
	private boolean bfsPathExists(String u, String v) { 
	    Queue<String> bfs_queue = new ArrayDeque<>();
	    HashSet<String> visited = new HashSet<String>();
	    HashMap<String, String> parent = new HashMap<String, String>();
	    
	    bfs_queue.add(u);
	    visited.add(u);
	    parent.put(u, null);
	    
	    if(u.equals(v)) { 
	    	return false; 
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
						parent.put(edge, current);
						bfs_queue.add(edge);
						visited.add(edge);
					}
				}
			}
	    }
	    
	    String path_vertex = parent.get(v);
	    if(path_vertex == null) { 
	    	return false;
	    }
	    
	    return true;
	}
	
	public static void main(String[] args) throws FileNotFoundException { 
		GraphProcessor gp = new GraphProcessor("cities.txt");
		boolean path_exists = gp.bfsPathExists("Ames", "Chicago");
		System.out.println(path_exists);
		System.out.println(gp.isStronglyConnected());
		System.out.println(gp.diameter());
	}
}
