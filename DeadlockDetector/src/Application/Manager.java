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
			freeAllocatedResources(T);//A taszkhoz tartoz� er�forr�s(oka)t felszabad�tja
			listOfTasks.remove(T); //kivessz�k a list�b�l
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
	 * Lefoglalja a T taszk sz�m�ra a k�v�nt nev� er�forr�st.
	 * Ha nem l�tezik, l�trehozzuk.
	 * Ha l�tezik, megn�zz�k, hogy haszn�latban van-e.
	 * Ha nincs, lefoglaljuk. 
	 * Ha haszn�latban van, megn�zz�k, hogy a lefoglal�sa
	 * okozna-e holtpontot
	 * Ha nem okozna, lefoglaljuk.
	 * Ha okozna, t�r�lj�k a foglal�st.
	 */
	private void allocateResource(Task T, String resourceName){
		Resource R;		
		if((R= getResourceByName(resourceName)) == null){
			R = new Resource(resourceName);
			R.addWaitingTask(T);//hozz�adja R megfelel� list�j�hoz
			R.nextUserTask();//err�l a list�r�l beh�zza a k�vetkez� taszkot
			listOfResources.add(R);
			T.nextCommand++; //n�velj�k a sz�ml�l�t, ez a parancs nem blokkol�dik, nem kell m�r, hogy fusson
			return;
		}
		//Ha R != null, akkor van er�forr�s ilyen n�ven
		if(R.getWhichTaskusesThisResource() == null){ //ha nem haszn�lja senki, akkor a lefoglal�sa
			R.addWaitingTask(T);			 // nem okoz deadlockot, �gy lefoglalhatjuk.
			R.nextUserTask();
			T.nextCommand++;						//n�velj�k a sz�ml�l�t
			return;
		}
		//ha ez az er�forr�s haszn�latban van, megn�zz�k, hogy okoz-e
		//holtpontot a lefoglal�sa
		
		boolean causesDeadlock = false;
		causesDeadlock = checkForDeadlock(null,T,R);
		if(causesDeadlock){
			output.add(T.name + "," + T.nextCommand + "," + R.name);
			T.nextCommand++;//n�velj�k a sz�ml�l�t, ez a r�sz nem futhat le
			return;
		}
		if(!causesDeadlock){
			if(!T.waitingForTheseResources.contains(R)){
				R.addWaitingTask(T);
				T.waitingForTheseResources.add(R);//ha nem tartalmazza, hozz�adjuk a list�hoz
				//itt nem n�velj�k a sz�ml�l�t
			}				
		}		
	}
	
	/**
	 * Ellen�rzi, hogy az er�forr�s lefoglal�s�val l�trej�n-e holtpont.
	 * @return true ha l�trej�n holtpont, false egy�bk�nt
	 */
	private boolean checkForDeadlock(Task T, Task searchedTask,Resource wantedResource){
		if(T == searchedTask)
			return true;
		if(wantedResource.getWhichTaskusesThisResource() == null)//ha nem haszn�lja senki, akkor a lefoglal�sa
			return false;										// nem okoz deadlockot, �gy lefoglalhatjuk.
		if(T == null){//legels� megh�v�skor fog ez az �g lefutni
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
			if(value) // ha igaz, akkor megtal�ltuk, t�rj�nk vissza vele!
				return value;			
		}			
		return false;
	}
	
	private void freeResource(String resourceName){
		Resource R = getResourceByName(resourceName);
		R.nextUserTask();
	}
	
	/**
	 * A taszk �ltal haszn�lt er�forr�sok felszabad�t�sa
	 * @param T
	 */
	private void freeAllocatedResources(Task T){
		Iterator<Resource> it = listOfResources.iterator();
		while(it.hasNext()){
			Resource temp = it.next();
			if(temp.getWhichTaskusesThisResource() == T) //ha ez a taszk haszn�lja
				temp.deleteUserTask();					//felszabad�tjuk
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
