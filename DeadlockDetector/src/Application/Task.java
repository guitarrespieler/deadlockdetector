package Application;

import java.util.ArrayList;

public class Task {
	String name = "";
	ArrayList<String>commands = new ArrayList<>();
	ArrayList<Resource> waitingForTheseResources = new ArrayList<>();
	int nextCommand = 0;
	
	public Task(String taskName){
		name = taskName;
	}
}
