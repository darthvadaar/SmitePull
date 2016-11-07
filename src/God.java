import java.util.*;

public class God{ 
	public String url, name;
	public ArrayList<String> guides;
	
	public God(String name, String url){
		this.guides = new ArrayList<String>();
		this.url = url;
		this.name = name;
	}
	
	//getters and setters
	public String getName(){return name;}
	public String getURL(){return url;}
	public ArrayList<String> getGuides(){return guides;}
	
	
	
}