import java.io.File;
import java.net.*;
import java.util.*;
public class WikiCrawler {
	
	private static final String BASE_URL = "https://en.wikipedia.org";
	private String article_url; 
	private int max_pages;
	private String file_name; 
	private ArrayList<String> topics; 
	
	public WikiCrawler(String article_url, int max_pages, ArrayList<String> topics, String file_name) { 
		this.article_url = article_url;
		this.max_pages = max_pages; 
		this.file_name = file_name; 
		this.topics = topics;
	}
	
	public ArrayList<String> extractLinks(String doc) { 
	
		return null;
	}
	
	/**
	 * Construct a WebGraph object
	 */
	public void crawl() { 
		WebGraph graph = new WebGraph();
	}
}
