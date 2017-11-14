import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

public class WikiCrawler {
	
	private static final String BASE_URL = "https://en.wikipedia.org";
	private String seed_url;
	private int max_pages;
	private String file_name; 
	private ArrayList<String> topics;
    private Queue<String> page_queue = new ArrayDeque<>();
    private HashSet<String> visited = new HashSet<String>();
	// only make 50 requests at a time. 
	private static final int MAX_REQUESTS = 50;
    private int requests = 0;

	
	public WikiCrawler(String seed_url, int max_pages, ArrayList<String> topics, String file_name) { 
		this.seed_url = seed_url;
		this.max_pages = max_pages; 
		this.file_name = file_name; 
		this.topics = topics;
	}

	public ArrayList<String> extractLinks(String doc) {
		ArrayList<String> page_links = new ArrayList<String>();
		/*
		 * From the Assignment PDF:
		 * The “actual text content” of the page starts immediately after 
		 * the first occurrence of the html tag <p>. 
		 * So, your program should extract links (pages) 
		 * that appear after the first occurrence of<p>.
		 */
		int first_para_tag = doc.indexOf("<p>");
		doc = doc.substring(first_para_tag, doc.length());
		
		// get links in the format of href="/wiki/..."
		Pattern link_pattern = Pattern.compile("href=\"(/wiki/([^:#]*?))\"",  Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
		Matcher matcher = link_pattern.matcher(doc);
		while(matcher.find()){
			page_links.add(matcher.group(1));
		}
		return page_links;
	}
	
	/**
	 * Construct a WebGraph object
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void crawl() throws InterruptedException, IOException{
		WebGraph graph = new WebGraph();
		// if seed url contains all topics
		if(verifyURL(this.seed_url)) { 
			//initialize Queue Q and list visited 
			// place seed url in Q and visited 
			page_queue.add(this.seed_url);
			visited.add(this.seed_url);
			// while the queue is not empty
			while(!page_queue.isEmpty()) { 
				String current_page = page_queue.remove();
				// request current
				String current_page_html = getPageAsString(current_page);
				// extract all links
				ArrayList<String> links = extractLinks(current_page_html);
				graph.addVertex(current_page);
				for(String link : links) {
					if(graph.getVertices().size() < this.max_pages) {
						if(verifyURL(link)) { 
							graph.addVertex(link);
							if(!visited.contains(link)) { 
								page_queue.add(link);
								visited.add(link);
								graph.addEdge(current_page, link);
							}
						}
					}else{
						if(visited.contains(link)){
							graph.addEdge(current_page, link);
						}
						//break;
					}
				}
			}
		}
		graphToFile(graph);
	}
	
	/**
	 * If seedUrl does not contain all words from topics, 
	 * then the graph constructed is empty graph(0 vertices and 0 edges).
	 * @return true if seed url contains all topics, false otherwise
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	private boolean verifyURL(String url) throws InterruptedException, IOException { 
		String page = getPageAsString(url);
		return pageHasTopics(page);
	}
	
	/**
	 * 
	 * @param current_url
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private String getPageAsString(String current_url) throws InterruptedException, IOException{
		// do not do more than 50 requests at a time.
		if(this.requests >= MAX_REQUESTS) { 
			Thread.sleep(3000);
			this.requests = 0;
		}
		
		// connect!
		URL wiki_url = new URL(BASE_URL + current_url);
		InputStream input_stream = wiki_url.openStream();
		BufferedReader page_reader = new BufferedReader(new InputStreamReader(input_stream));
		
		// StringBuilders don't have to re-create a String object with each concatenation
		// https://stackoverflow.com/questions/1532461/stringbuilder-vs-string-concatenation-in-tostring-in-java
		StringBuilder builder = new StringBuilder();
		String page_html = "";
		
		// read page
		while((page_html = page_reader.readLine()) != null) { 
			builder.append(page_html);
		}
		
		String final_page = builder.toString();
		this.requests += 1; 
		return final_page;
	}

	/**
	 * Method to check that topics is a subset of page_html
	 * @param page_html - page to check for topics
	 * @return true if page contains all topics, false otherwise
	 */
	private boolean pageHasTopics(String page_html){
		for (String topic : this.topics) { 
			if(!page_html.contains(topic)) { 
				return false; 
			}
			
		}
		return true;
	}
	
	/**
	 * 
	 * @param g
	 * @throws IOException 
	 */
	private void graphToFile(WebGraph g) throws IOException { 
		File f = new File(this.file_name);
		// https://docs.oracle.com/javase/7/docs/api/java/io/FileWriter.html
		FileWriter writer = new FileWriter(f);
        ArrayList<String> graph_vertices = g.getVertices();
        HashMap<String, ArrayList<String>> graph_adj = g.getAdjacencyMatrix();
        writer.append(g.getVertices().size() + "\n");
        for(int i = 0; i < graph_vertices.size(); i++) { 
        	String current_vertex = graph_vertices.get(i);
        	ArrayList<String> vertex_edges = graph_adj.get(current_vertex);
        	for(int j = 0; j < vertex_edges.size(); j++) { 
        		String current_edge = vertex_edges.get(j); 
        		writer.append(current_vertex + " " + current_edge + "\n");
        	}
        }
        writer.flush();
        writer.close();
	}
}
