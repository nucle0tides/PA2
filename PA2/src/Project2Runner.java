import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class Project2Runner {

	public static void main(String[] args) throws InterruptedException, IOException {
		ArrayList<String> topics = new ArrayList<>();
		WikiCrawler w = new WikiCrawler("/wiki/Computer_Science", 200, topics, "WikiCS.txt");
		w.crawl();
		
		GraphProcessor gp = new GraphProcessor("WikiCS.txt");
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
