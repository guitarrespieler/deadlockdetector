package Application;

import java.util.ArrayList;
import java.util.Iterator;

public class Manager {
	ArrayList<Task> listOfTasks = new ArrayList<>();
	ArrayList<Resource>listOfResources = new ArrayList<>();
	ArrayList<String> output = new ArrayList<>();
	
	public void runAll(){
		while(listOfTasks.size() != 0){
			for(int i = 0; i < listOfTasks.size(); i++){
				run(listOfTasks.get(i));
			}
			
			
		}
	}
	
	private void run(Task T){
		if(T.commands.size() == 0){			
			freeAllocatedResources(T);//A taszkhoz tartozó erõforrás(oka)t felszabadítja
			listOfTasks.remove(T); //kivesszük a listából
			return;
		}
		String cmd = T.commands.get(T.nextCommand);
		if(cmd.equals("0"))
			return;
		String[] pieces = cmd.split("");
		
		if(pieces[0].equals("-")){
			freeResource(pieces[1] + pieces[2]);
		}
		allocateResource(T, pieces[1]+pieces[2]);
		
	}
	/**
	 * Lefoglalja a T taszk számára a kívánt nevû erõforrást.
	 * Ha nem létezik, létrehozzuk.
	 * Ha létezik, megnézzük, hogy használatban van-e.
	 * Ha nincs, lefoglaljuk. 
	 * Ha használatban van, megnézzük, hogy a lefoglalása
	 * okozna-e holtpontot
	 * Ha nem okozna, lefoglaljuk.
	 * Ha okozna, töröljük a foglalást.
	 */
	private void allocateResource(Task T, String resourceName){
		Resource R;		
		if((R= getResourceByName(resourceName)) == null){
			R = new Resource(resourceName);
			R.addWaitingTask(T);//hozzáadja R megfelelõ listájához
			R.nextUserTask();//errõl a listáról behúzza a következõ taszkot
			listOfResources.add(R);
			T.nextCommand++; //növeljük a számlálót, ez a parancs nem blokkolódik, nem kell már, hogy fusson
			return;
		}
		//Ha R != null, akkor van erõforrás ilyen néven
		if(R.getWhichTaskusesThisResource() == null){ //ha nem használja senki, akkor a lefoglalása
			R.addWaitingTask(T);			 // nem okoz deadlockot, így lefoglalhatjuk.
			R.nextUserTask();
			T.nextCommand++;						//növeljük a számlálót
			return;
		}
		//ha ez az erõforrás használatban van, megnézzük, hogy okoz-e
		//holtpontot a lefoglalása
		
		boolean causesDeadlock = false;
		causesDeadlock = checkForDeadlock(null,T,R);
		if(causesDeadlock){
			output.add(T.name + "," + T.nextCommand + "," + R.name);
			T.nextCommand++;//növeljük a számlálót, ez a rész nem futhat le
			return;
		}
		if(!causesDeadlock){
			if(!T.waitingForTheseResources.contains(R)){
				R.addWaitingTask(T);
				T.waitingForTheseResources.add(R);//ha nem tartalmazza, hozzáadjuk a listához
				//itt nem növeljük a számlálót
			}				
		}		
	}
	
	/**
	 * Ellenõrzi, hogy az erõforrás lefoglalásával létrejön-e holtpont.
	 * @return true ha létrejön holtpont, false egyébként
	 */
	private boolean checkForDeadlock(Task T, Task searchedTask,Resource wantedResource){
		if(T == searchedTask)
			return true;
		if(wantedResource.getWhichTaskusesThisResource() == null)//ha nem használja senki, akkor a lefoglalása
			return false;										// nem okoz deadlockot, így lefoglalhatjuk.
		if(T == null){//legelsõ meghíváskor fog ez az ág lefutni
			return checkForDeadlock(wantedResource.getWhichTaskusesThisResource(),
					searchedTask, wantedResource);
		}
		boolean value = false;
		for(int i = 0; i < T.waitingForTheseResources.size(); i++){
			Resource tempRes = T.waitingForTheseResources.get(i);
			Task tempTask = tempRes.getWhichTaskusesThisResource();
			if(tempTask == null)
				continue;
			value = checkForDeadlock(tempTask, searchedTask, wantedResource);
			if(value) // ha igaz, akkor megtaláltuk, térjünk vissza vele!
				return value;			
		}			
		return false;
	}
	
	private void freeResource(String resourceName){
		Resource R = getResourceByName(resourceName);
		R.nextUserTask();
	}
	
	/**
	 * A taszk által használt erõforrások felszabadítása
	 * @param T
	 */
	private void freeAllocatedResources(Task T){
		Iterator<Resource> it = listOfResources.iterator();
		while(it.hasNext()){
			Resource temp = it.next();
			if(temp.getWhichTaskusesThisResource() == T) //ha ez a taszk használja
				temp.deleteUserTask();					//felszabadítjuk
		}		
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
