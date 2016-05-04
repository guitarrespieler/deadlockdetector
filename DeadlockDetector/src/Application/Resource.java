package Application;

public class Resource {
	private String name = "";
	private Task thisTaskUsesMe = null;	
	
	public String getResoureName(){
		return name;
	}
	public Task getWhoUsesThisResource(){
		return thisTaskUsesMe;
	}
}
