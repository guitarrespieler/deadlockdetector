package Application;

public class Resource {
	public String name = "";
	public Task thisTaskUsesMe = null;	
	
	public Resource(String resName) {
		name = resName;
	}
}
