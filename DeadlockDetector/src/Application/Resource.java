package Application;

import java.util.ArrayList;

/**
 * Ez az oszt�ly reprezent�lja az er�forr�sokat.
 * T�rolja, hogy mely taszkok v�rakoznak erre
 * az er�forr�sra, menedzseli, ha �jabb taszk
 * szeretn� haszn�lni (v�rakoz�si sorba teszi), 
 * valamint ha felszabad�tj�k, akkor a soron 
 * k�vetkez� taszkhoz ker�l automatikusan.
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
	 * Az er�forr�st felszabad�tja. Ha vannak
	 * r� v�rakoz� taszkok, a soron k�vetkez�
	 * taszk kapja meg.
	 */
	public void deleteUserTask(){
		if (theseTasksWaitingForMe.size() != 0){
			nextUserTask();
			return;
		}
		thisTaskUsesMe = null; 										//ha nem v�rakoznak r� taszkok,akkor legyen null		
	}
	
	/**
	 * @return Az er�forr�st �ppen haszn�l� taszk referenci�j�t adja vissza.
	 */
	public Task getWhichTaskusesThisResource(){
		return thisTaskUsesMe;
	}
	
	/**
	 * Az er�forr�sra v�rakoz� taszkok sor�ba
	 * teszi a param�ter�l kapott taszkot.
	 */
	public void addWaitingTask(Task T){
		theseTasksWaitingForMe.add(T);
		if (thisTaskUsesMe == null)
			nextUserTask();
	}
	
	/**
	 * a soron k�vetkez� taszknak �tadja
	 * ezt az er�forr�st.
	 */
	public void nextUserTask(){
		if(theseTasksWaitingForMe.size() != 0){
			thisTaskUsesMe = theseTasksWaitingForMe.remove(0); 		//a k�vetkez� taszk megkapja �t
			thisTaskUsesMe.waitingForTheseResources.remove(this);	//kit�r�lj�k a taszk v�rakoz� list�j�r�l
			thisTaskUsesMe.nextCommand++;
			return;
		}		
		thisTaskUsesMe = null;
	}
}
