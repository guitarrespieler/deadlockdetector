package Application;

import java.util.ArrayList;
import java.util.Iterator;

public class Manager {
	ArrayList<Task> listOfTasks = new ArrayList<>();
	ArrayList<Resource>listOfResources = new ArrayList<>();
	ArrayList<String> outpus = new ArrayList<>();
	
	public void RunAll(){
		while(listOfTasks.size() != 0){
			for(int i = 0; i < listOfTasks.size(); i++){
				Task temp = listOfTasks.get(i);
				run(temp);
				
			}
		}
			
	}
	
	public void run(Task T){
		String nextCmd = T.nextCommand();
		String[] chars = nextCmd.split("");
		if(chars.length == 1)  //NO-OP method
			return;
		if(chars[1].equals("-")){
			freeResource(R);
		}
		
		
	}
	
	private Resource askForResource(Task T, String resourceName){
		return new Resource();
	}
	
	private void freeResource(Resource R){
		
	}
	
	/**
	 * @return null if there's no Resource with the given name.
	 */
	private Resource getResourceByName(String resName){
		Iterator<Resource> iterator = listOfResources.iterator();
		while (iterator.hasNext()) {
			Resource R = iterator.next();
			if(R.getResoureName().equals(resName))
				return R;			
		}
		return null;
	}
	
	public void addTask(Task t){
		listOfTasks.add(t);
	}
	
}
