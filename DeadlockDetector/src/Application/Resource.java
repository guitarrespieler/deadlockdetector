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
			thisTaskUsesMe = theseTasksWaitingForMe.get(0); //a következõ taszk megkapja õt
			thisTaskUsesMe.waitingForTheseResources.remove(this);//kitöröljük a taszk várakozó listájáról
			return;
		}
		thisTaskUsesMe = null; //ha nem várakoznak rá taszkok,akkor legyen null		
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
