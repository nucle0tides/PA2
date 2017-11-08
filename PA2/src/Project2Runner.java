import java.util.ArrayList;

public class Project2Runner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WikiCrawler w = new WikiCrawler("/wiki/Iowa_State_University", 1, new ArrayList<String>(2),"ji");
		w.crawl();
	}

}
