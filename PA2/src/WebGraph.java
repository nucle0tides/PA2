import java.util.*;
public class WebGraph {

	private ArrayList<String> vertices = new ArrayList<String>();
	
	/*
	 * represent the vertices and edges as such
	 * {
	 * 	'A', ['A', 'C', 'D', 'Z'], 
	 * 	'Z', ['H', 'G', 'B']
	 * }
	 */
	private HashMap<String, ArrayList<String>> adjacency_matrix = new HashMap<String, ArrayList<String>>();
	
	public WebGraph() {
		
	}
	
	/**
	 * Method to add a vertex to the Web Graph
	 * @param toAdd - vertex to add
	 */
	public void addVertex(String toAdd) { 
		if(!vertices.contains(toAdd)) { 
			vertices.add(toAdd); 
			adjacency_matrix.put(toAdd, new ArrayList<String>());
		}
	}
	
	/**
	 * Method to add edge to a vertex
	 * @param vertex
	 * @param edge
	 */
	public void addEdge(String vertex, String edge) { 
		/*
		 * From Assignment PDF: 
		 * The graph constructed should not have self loops nor it should have multiple edges.
		 * (even though a page might refer to itself or refer to another page multiple times).
		 */
		if(!vertex.equals(edge)) { 
			// add edge to list of vertex edges
			if(this.adjacency_matrix.get(vertex) != null) {
				if(!this.adjacency_matrix.get(vertex).contains(edge)){
					this.adjacency_matrix.get(vertex).add(edge);
				}
			}
			else { 
				this.adjacency_matrix.put(vertex, new ArrayList<String>());
				this.adjacency_matrix.get(vertex).add(edge);
			}
		}
		
	}
	
	/**
	 * Method to get the adjacency matrix of a WebGraph object. 
	 * @return HashMap<String, ArrayList<String>> containing the vertex -> edge mappings
	 * for all vertices in a WebGraph object. 
	 */
	public HashMap<String, ArrayList<String>> getAdjacencyMatrix() { 
		return this.adjacency_matrix;
	}
	
	/**
	 * Method to get the vertices of WebGraph object. 
	 * @return ArrayList<String> containing all vertices. 
	 */
	public ArrayList<String> getVertices() { 
		return this.vertices;
	}
}
