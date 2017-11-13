import java.io.IOException;
import java.util.ArrayList;

public class Project2Runner {

	public static void main(String[] args) throws InterruptedException, IOException {
		ArrayList<String> topics = new ArrayList<>();
		WikiCrawler w = new WikiCrawler("/wiki/Complexity_theory", 20, topics, "WikiISU.txt");
		w.crawl();
	}
}
