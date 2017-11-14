import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class GraphProcessor {

	public WebGraph graph;
	private int num_vertices;
	private ArrayList<HashSet<String>> strongly_connected_components;
	private HashSet<String> scc_visited = new HashSet<String>();
	
	public GraphProcessor(String graphData) throws FileNotFoundException { 
		this.graph = new WebGraph();
		constructGraph(graphData);
		// construct SCCs
	}
	
	/**
	 * 	 * TODO: Double check this with a simpler example
	 * Return the out degree of vertex v.
	 * @param v
	 * @return out degree of vertex v.
	 */
	public int outDegree(String v) { 
        HashMap<String, ArrayList<String>> vertex_edges = graph.getAdjacencyMatrix();
        if(vertex_edges.containsKey(v)){
            return vertex_edges.get(v).size();
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
	    	path.add(u);
	    	path.add(v);
	    	return path;
	    }
	    
	    // while queue is not empty
	    while(!bfs_queue.isEmpty()) { 
	    	String current = bfs_queue.remove();
	    	ArrayList<String> edges = graph.getAdjacencyMatrix().get(current);
	    	// for each edge, w, current -> w
	    	for(String edge : edges) { 
	    		// if edge is unvisited
	    		if(!visited.contains(edge)) { 
	    			parent.put(edge, current);
	    			bfs_queue.add(edge);
	    			visited.add(edge);
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
		return 0; 
	}
	
	/**
	 * TODO: Double check this with a simpler example
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
				path_counts += 1;
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
		this.graph.addEdge(vertex, edge);
		line_scanner.close();
	}
	
	public static void main(String[] args) throws FileNotFoundException { 
		GraphProcessor gp = new GraphProcessor("WikiComplexity.txt");
		System.out.println(gp.outDegree("/wiki/Complexity"));
	}
}
