import java.io.IOException;
import java.util.ArrayList;

public class Project2Runner {

	public static void main(String[] args) throws InterruptedException, IOException {
		ArrayList<String> topics = new ArrayList<>();
		WikiCrawler w = new WikiCrawler("/wiki/Complexity_theory", 20, topics, "WikiComplexity.txt");
		w.crawl();
		
//		ArrayList<String> topics = new ArrayList<>();
//		topics.add("Iowa State");
//		topics.add("Cyclones");
//		WikiCrawler w = new WikiCrawler("/wiki/Iowa_State_University", 100, topics, "WikiISU.txt");
//		w.crawl();
	}
}
