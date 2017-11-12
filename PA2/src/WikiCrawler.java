import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

public class WikiCrawler {
	
	private static final String BASE_URL = "https://en.wikipedia.org";
	private String article_url; 
	private int max_pages;
	private String file_name; 
	private ArrayList<String> topics;
	private String textToWrite;

	
	public WikiCrawler(String article_url, int max_pages, ArrayList<String> topics, String file_name) { 
		this.article_url = article_url;
		this.max_pages = max_pages; 
		this.file_name = file_name; 
		this.topics = topics;
	}

	public ArrayList<String> extractLinks(String doc) {
		Pattern linkPattern = Pattern.compile("href=\"(/wiki/([^:#]*?))\"",  Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
		String[] afterFirstParagraph = doc.split("<p>",2);
		Matcher pageMatcher = linkPattern.matcher(afterFirstParagraph[1]);
		ArrayList<String> links = new ArrayList<String>();
		while(pageMatcher.find()){
			links.add(pageMatcher.group(1));
		}
		return links;
	}
	
	/**
	 * Construct a WebGraph object
	 */
	public void crawl(){
		WebGraph graph = new WebGraph();
		int i = 0;

		List<String> visited = new LinkedList<>();
		Queue<String> q = new LinkedList<String>();
		q.add(article_url);
		while(!q.isEmpty() && visited.size() < max_pages - 1){
			String currentUrl = q.remove();
			String pageHTML = getPageAsString(currentUrl);
			i++;
			ArrayList<String> links = extractLinks(pageHTML);
			int numberOfLinksExtracted = 0;
			for (String link : links) {
				if (numberOfLinksExtracted >= max_pages - 1) {
					break;
				}
				if(pageHasTopics(getPageAsString(link))) {
					if (!visited.contains(link)) {
						numberOfLinksExtracted++;
						q.add(link);
						visited.add(link);
					}
				}
			}

			if(i % 50 == 0){
				try {
					Thread.sleep(3000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}

//		int count = 0;
		for(String link:visited){
//			System.out.print(count++ +" ");
			textToWrite = textToWrite + "\n" + article_url + " " + link;
		}

		getLinksBetweenPages(visited);

		appendToFile(textToWrite);
	}

	private void getLinksBetweenPages(List<String> visited){

		for(String link:visited){
			List<String> seen = new LinkedList<>();
			String pageHTML = getPageAsString(link);
			ArrayList<String> linksInPage = extractLinks(pageHTML);

			for(String linkInPage:linksInPage){
				if(visited.contains(linkInPage) && !seen.contains(linkInPage) && linkInPage.compareTo(link) != 0){
					seen.add(linkInPage);
					textToWrite = textToWrite + "\n" + link+ " " + linkInPage;
				}
			}
		}
	}

	private void appendToFile(String text) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file_name, true));
			bw.write(text);
			bw.newLine();
			bw.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getPageAsString(String currentUrl){
		try {
			URL uri = new URL(BASE_URL+currentUrl);
			URLConnection yc = uri.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String document = "";
			String line;

			while((line = br.readLine())!=null){
				document += line;
			}
			br.close();
			return document;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	private boolean pageHasTopics(String pageHTML){
		for(String topic:topics){
			if(!pageHTML.contains(topic)){
				return false;
			}
		}
		return true;
	}
}
