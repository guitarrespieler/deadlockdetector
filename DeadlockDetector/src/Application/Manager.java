package Application;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Ez az osztály szimulálja a holtpontdetektálást.
 * Futtatja a taszkokat, erõforrásokat lefoglal, 
 * felszabadít, holtpontot ellenõriz.
 * 
 * @author zsigatibor
 */
public class Manager {
	ArrayList<Task> listOfTasks = new ArrayList<>();
	ArrayList<Resource>listOfResources = new ArrayList<>();
	ArrayList<String> output = new ArrayList<>();
	
	/**
	 * Taszkokat futtatja.
	 */
	public void runAll(){
		int x = 0;
		while(x++ < 5000){
			for(int i = 0; i < listOfTasks.size(); i++){
				run(listOfTasks.get(i));
			}
		}
	}
	
	private void run(Task T){
		if(T.nextCommand > T.commands.size()){			
			freeAllocatedResources(T);//A taszkhoz tartozó erõforrás(oka)t felszabadítja
//			listOfTasks.remove(T); //kivesszük a listából
			return;
		}
		String nev = T.name;//teszteléshez kell
		String cmd = T.commands.get(T.nextCommand-1);
		if(cmd.equals("0")){
			T.nextCommand++;
			return;
		}
			
		String[] pieces = cmd.split("");
		
		if(pieces[0].equals("-")){
			freeResource(T,pieces[1] + pieces[2]);
			T.nextCommand++;
			return;
		}
		if(pieces[0].equals("+")){
			allocateResource(T, pieces[1]+pieces[2]);
		}
		
	}
	/**
	 * Lefoglalja a T taszk számára a kívánt nevû erõforrást.
	 * Ha nem létezik, létrehozzuk.
	 * Ha létezik, megnézzük, hogy használatban van-e: 
	 * ha nincs, lefoglaljuk. 
	 * Ha használatban van, megnézzük, hogy a lefoglalása
	 * okozna-e holtpontot
	 * Ha nem okozna, lefoglaljuk.
	 * Ha okozna, töröljük a foglalást.
	 */
	private void allocateResource(Task T, String resourceName){
		Resource R = getResourceByName(resourceName);		
		if(R == null){
			R = new Resource(resourceName);
			R.addWaitingTask(T);//hozzáadja R megfelelõ listájához
			listOfResources.add(R);
			return;
		}
		//Ha R != null, akkor van erõforrás ilyen néven
		if(R.getWhichTaskusesThisResource() == null){ 	//ha nem használja senki, akkor a lefoglalása
			R.addWaitingTask(T);			 			// nem okoz deadlockot, így lefoglalhatjuk.
//			T.nextCommand++;							//növeljük a számlálót
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
	 * Rekurzívan ellenõrzi, hogy az erõforrás lefoglalásával létrejönne-e holtpont.
	 * @return true ha létrejön holtpont, false egyébként
	 */
	private boolean checkForDeadlock(Task T, Task searchedTask,Resource wantedResource){
		if(T == searchedTask)
			return true;
		if(wantedResource.getWhichTaskusesThisResource() == null)	//ha nem használja senki, akkor a lefoglalása
			return false;											// nem okoz deadlockot, így lefoglalhatjuk.
		if(T == null){												//legelsõ meghíváskor fog ez az ág lefutni
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
			if(value) 												// ha igaz, akkor megtaláltuk, térjünk vissza vele!
				return value;			
		}			
		return false;
	}
	
	/**
	 * Adott erõforrás felszabadítása.
	 */
	private void freeResource(Task T,String resourceName){
		Resource R = getResourceByName(resourceName);
		//lehet, hogy holtpont miatt kilõttük, de fel akarja szabadítani... Ezt kezeli le a második feltétel
		if(R != null &&(R.getWhichTaskusesThisResource() == T)) 
			R.deleteUserTask();		
	}
	
	/**
	 * A taszk által használt összes erõforrás felszabadítása
	 */
	private void freeAllocatedResources(Task T){
		Iterator<Resource> it = listOfResources.iterator();
		while(it.hasNext()){
			Resource temp = it.next();
			if(temp.getWhichTaskusesThisResource() == T) 			//ha ez a taszk használja
				temp.deleteUserTask();								//felszabadítjuk
		}		
	}
	/**
	 * @return null, ha nincs erõforrás a paraméterül kapott névvel, 
	 * egyébként az adott nevû erõforrást adja vissza.
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
	
	/**
	 * A paraméterül kapott taszkot hozzáadja a Manager listájához
	 */
	public void addTask(Task T){
		listOfTasks.add(T);
	}
	
}
