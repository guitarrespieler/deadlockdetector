package Application;

import java.util.ArrayList;

/**
 * Ez az osztály reprezentálja az erõforrásokat.
 * Tárolja, hogy mely taszkok várakoznak erre
 * az erõforrásra, menedzseli, ha újabb taszk
 * szeretné használni (várakozási sorba teszi), 
 * valamint ha felszabadítják, akkor a soron 
 * következõ taszkhoz kerül automatikusan.
 * 
 * @author zsigatibor
 */
public class Resource {
	public String name = "";
	private Task thisTaskUsesMe = null;	
	public ArrayList<Task> theseTasksWaitingForMe = new ArrayList<>();
	
	public Resource(String resName) {
		name = resName;
	}
	
	/**
	 * Az erõforrást felszabadítja. Ha vannak
	 * rá várakozó taszkok, a soron következõ
	 * taszk kapja meg.
	 */
	public void deleteUserTask(){
		if (theseTasksWaitingForMe.size() != 0){
			nextUserTask();
			return;
		}
		thisTaskUsesMe = null; 										//ha nem várakoznak rá taszkok,akkor legyen null		
	}
	
	/**
	 * @return Az erõforrást éppen használó taszk referenciáját adja vissza.
	 */
	public Task getWhichTaskusesThisResource(){
		return thisTaskUsesMe;
	}
	
	/**
	 * Az erõforrásra várakozó taszkok sorába
	 * teszi a paraméterül kapott taszkot.
	 */
	public void addWaitingTask(Task T){
		theseTasksWaitingForMe.add(T);
		if (thisTaskUsesMe == null)
			nextUserTask();
	}
	
	/**
	 * a soron következõ taszknak átadja
	 * ezt az erõforrást.
	 */
	public void nextUserTask(){
		if(theseTasksWaitingForMe.size() != 0){
			thisTaskUsesMe = theseTasksWaitingForMe.remove(0); 		//a következõ taszk megkapja õt
			thisTaskUsesMe.waitingForTheseResources.remove(this);	//kitöröljük a taszk várakozó listájáról
			thisTaskUsesMe.nextCommand++;
			return;
		}		
		thisTaskUsesMe = null;
	}
}
