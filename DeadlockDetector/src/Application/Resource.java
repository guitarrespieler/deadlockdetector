package Application;

import java.util.ArrayList;

public class Resource {
	public String name = "";
	private Task thisTaskUsesMe = null;	
	public ArrayList<Task> theseTasksWaitingForMe = new ArrayList<>();
	
	public Resource(String resName) {
		name = resName;
	}
	public void deleteUserTask(){
		if (theseTasksWaitingForMe.size() != 0){
			thisTaskUsesMe = theseTasksWaitingForMe.get(0); //a k�vetkez� taszk megkapja �t
			thisTaskUsesMe.waitingForTheseResources.remove(this);//kit�r�lj�k a taszk v�rakoz� list�j�r�l
			return;
		}
		thisTaskUsesMe = null; //ha nem v�rakoznak r� taszkok,akkor legyen null		
	}
	public Task getWhichTaskusesThisResource(){
		return thisTaskUsesMe;
	}
	public void addWaitingTask(Task T){
		theseTasksWaitingForMe.add(T);
	}
	public void nextUserTask(){
		thisTaskUsesMe = theseTasksWaitingForMe.get(0);
		thisTaskUsesMe.waitingForTheseResources.remove(this);
	}
}
