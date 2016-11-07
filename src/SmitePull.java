import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.*;

public class SmitePull {
	
    public static void main(String[] args){
    	ArrayList<God> gods;
    	ArrayList<Guide> guides;
    	gods = extractGods();
    	God selectedGod = selectGod(gods);
    	
    	guides = extractGuides(selectedGod);
    	Guide selectedGuide = selectGuide(guides);
    
    }
    
    public static Document webpage(String url){
    	//returns a Document with the given url
    	Document doc = null;
    	try{
    		doc = Jsoup.connect(url).userAgent("Mozilla").get();	//if userAgent isnt used to disguise, page won't let me access its contents
    	}catch (IOException e){
    		System.out.println(e);
    	}
    	return doc;
    }
    
    public static God selectGod(ArrayList<God> gods){
    	Scanner kb = new Scanner(System.in);
    	int choice = 0;
    	System.out.println("Select a god by number:");
    	for (int i = 0; i < gods.size(); i++){
    		System.out.printf("|%3d| %s\n",i + 1, gods.get(i).getName());
    	}
    	choice = kb.nextInt();
    	return gods.get(choice - 1);
    }
    
    public static Guide selectGuide(ArrayList<Guide> guides){
    	Scanner kb = new Scanner(System.in);
    	int choice = 0;
    	System.out.println("Select a guide by number:");
    	for (int i = 0; i < guides.size(); i++){
    		System.out.printf("|%3d| %s\n",i + 1, guides.get(i).getName());
    	}
    	choice = kb.nextInt();
    	return guides.get(choice - 1);
    }
    
    public static ArrayList<Guide> extractGuides( God selected){
    	ArrayList<Guide> guides = new ArrayList<Guide>();
    	Document doc = webpage(selected.getURL());
    	Elements glinks = doc.select("a[href]");
    	for (Element link : glinks){
    		if (link.attr("href").contains("/smite/guide/")){
    			String guideLink = link.attr("href");
    			String name = guideLink.substring(13,guideLink.lastIndexOf("-"));
				guides.add(new Guide(name, "http://www.smitefire.com/smite/guide" + guideLink));
    		}
    	}
    	return guides;
    }
   
    public static ArrayList<God> extractGods(){
    	//looks for all the url additions that lead to god pages
    	//and returns them as an ArrayList<God>
    	ArrayList<God> gods = new ArrayList<God>();
    	Document doc = webpage("http://www.smitefire.com/smite/gods");
    	
    	Elements links = doc.select("a[href]");
    	for (Element link: links){
    		if (link.attr("href").contains("/smite/god/")){
    			String godLink = link.attr("href");
    			String name = godLink.substring(11,godLink.lastIndexOf("-"));
				gods.add(new God(name, "http://www.smitefire.com" + godLink));
    		}
    	}
    	return gods;
    }
    
    
    
    
    
    
    
    
}
