package Application;

import java.util.ArrayList;

public class Task {
	String name = "";
	ArrayList<String>commands = new ArrayList<>();
	ArrayList<Resource> waitingForTheseResources = new ArrayList<>();
	
	public Task(String taskname){
		name = taskname;
	}
	public void addCommand(String cmd){
		commands.add(cmd);
	}
	public String nextCommand(){
		if(commands.size() != 0)
			return commands.remove(0);
		return null;
	}
}
