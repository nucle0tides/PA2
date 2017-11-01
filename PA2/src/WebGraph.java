import java.util.*;
public class WebGraph {

	private List<String> vertices = new ArrayList<String>();
	
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
}
