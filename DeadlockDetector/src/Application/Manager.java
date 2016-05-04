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
		if(T.commands.size() == 0 && T.waitingForTheseResources.size() == 0){
			listOfTasks.remove(T);
			return;
		}		
		String nextCmd = T.commands.get(0);
		String[] chars = nextCmd.split("");
		if(chars.length == 1){  //NO-OP method
			T.commands.remove(0);
			return;
		}
		if(chars[0].equals("-")){
			freeResource(getResourceByName(chars[1] + chars[2]));
		}
		if(chars[0].equals("+")){
			askForResource(T,chars[1] + chars[2]);
		}
		
		
	}
	
	private void askForResource(Task T, String resourceName){
		Resource R = getResourceByName(resourceName);
		if(R != null){
			if(R.thisTaskUsesMe != null){
				T.waitingForTheseResources.add(R);
			}
			else if(R.thisTaskUsesMe == null){
				R.thisTaskUsesMe = T;
			}
			return;
		}
		//if there was no resource like this before
		R = new Resource(resourceName);
		listOfResources.add(R);
		R.thisTaskUsesMe = T;	
	}
	
	private void freeResource(Resource R){
		if(R == null)
			return;
		R.thisTaskUsesMe = null;		
	}
	
	/**
	 * @return null if there's no Resource with the given name.
	 */
	private Resource getResourceByName(String resName){
		Iterator<Resource> iterator = listOfResources.iterator();
		while (iterator.hasNext()) {
			Resource R = iterator.next();
			if(R.name.equals(resName))
				return R;			
		}
		return null;
	}
	
	public void addTask(Task t){
		listOfTasks.add(t);
	}
	
}
