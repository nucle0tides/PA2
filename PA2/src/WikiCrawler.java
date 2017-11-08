import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;
import java.util.List;

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
	public void crawl(){
		WebGraph graph = new WebGraph();

		List<String> visited = new LinkedList<>();
		Queue<String> q = new LinkedList<String>();
		visited.add(article_url);
		q.add(article_url);
		while(!q.isEmpty()){
			String currentUrl = q.remove();
			try {
				URL uri = new URL(BASE_URL+currentUrl);
				URLConnection yc = uri.openConnection();
				BufferedReader br = new BufferedReader(new InputStreamReader(yc.getInputStream()));
				String document = "";
				String line;

				while((line = br.readLine())!=null){
					document += line;
				}
				System.out.println(document);
				ArrayList<String> links = extractLinks(document);
				br.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
