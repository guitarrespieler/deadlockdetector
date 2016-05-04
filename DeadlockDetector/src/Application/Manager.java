package Application;

import java.util.ArrayList;

public class Manager {
	ArrayList<Task> listOfTasks = new ArrayList<>();
	ArrayList<Resource>listOfResources = new ArrayList<>();
	ArrayList<String> outpus = new ArrayList<>();
	
	public void addTask(Task t){
		listOfTasks.add(t);
	}
	public Resource askForResource(Task T, String resourceName){
		return new Resource();
	}
	
	public void freeResource(Resource R){
		
	}
	
}
