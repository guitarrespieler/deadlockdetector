package Application;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Ez az oszt�ly szimul�lja a holtpontdetekt�l�st.
 * Futtatja a taszkokat, er�forr�sokat lefoglal, 
 * felszabad�t, holtpontot ellen�riz.
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
			freeAllocatedResources(T);//A taszkhoz tartoz� er�forr�s(oka)t felszabad�tja
//			listOfTasks.remove(T); //kivessz�k a list�b�l
			return;
		}
		String nev = T.name;//tesztel�shez kell
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
	 * Lefoglalja a T taszk sz�m�ra a k�v�nt nev� er�forr�st.
	 * Ha nem l�tezik, l�trehozzuk.
	 * Ha l�tezik, megn�zz�k, hogy haszn�latban van-e: 
	 * ha nincs, lefoglaljuk. 
	 * Ha haszn�latban van, megn�zz�k, hogy a lefoglal�sa
	 * okozna-e holtpontot
	 * Ha nem okozna, lefoglaljuk.
	 * Ha okozna, t�r�lj�k a foglal�st.
	 */
	private void allocateResource(Task T, String resourceName){
		Resource R = getResourceByName(resourceName);		
		if(R == null){
			R = new Resource(resourceName);
			R.addWaitingTask(T);//hozz�adja R megfelel� list�j�hoz
			listOfResources.add(R);
			return;
		}
		//Ha R != null, akkor van er�forr�s ilyen n�ven
		if(R.getWhichTaskusesThisResource() == null){ 	//ha nem haszn�lja senki, akkor a lefoglal�sa
			R.addWaitingTask(T);			 			// nem okoz deadlockot, �gy lefoglalhatjuk.
//			T.nextCommand++;							//n�velj�k a sz�ml�l�t
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
	 * Rekurz�van ellen�rzi, hogy az er�forr�s lefoglal�s�val l�trej�nne-e holtpont.
	 * @return true ha l�trej�n holtpont, false egy�bk�nt
	 */
	private boolean checkForDeadlock(Task T, Task searchedTask,Resource wantedResource){
		if(T == searchedTask)
			return true;
		if(wantedResource.getWhichTaskusesThisResource() == null)	//ha nem haszn�lja senki, akkor a lefoglal�sa
			return false;											// nem okoz deadlockot, �gy lefoglalhatjuk.
		if(T == null){												//legels� megh�v�skor fog ez az �g lefutni
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
			if(value) 												// ha igaz, akkor megtal�ltuk, t�rj�nk vissza vele!
				return value;			
		}			
		return false;
	}
	
	/**
	 * Adott er�forr�s felszabad�t�sa.
	 */
	private void freeResource(Task T,String resourceName){
		Resource R = getResourceByName(resourceName);
		//lehet, hogy holtpont miatt kil�tt�k, de fel akarja szabad�tani... Ezt kezeli le a m�sodik felt�tel
		if(R != null &&(R.getWhichTaskusesThisResource() == T)) 
			R.deleteUserTask();		
	}
	
	/**
	 * A taszk �ltal haszn�lt �sszes er�forr�s felszabad�t�sa
	 */
	private void freeAllocatedResources(Task T){
		Iterator<Resource> it = listOfResources.iterator();
		while(it.hasNext()){
			Resource temp = it.next();
			if(temp.getWhichTaskusesThisResource() == T) 			//ha ez a taszk haszn�lja
				temp.deleteUserTask();								//felszabad�tjuk
		}		
	}
	/**
	 * @return null, ha nincs er�forr�s a param�ter�l kapott n�vvel, 
	 * egy�bk�nt az adott nev� er�forr�st adja vissza.
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
	 * A param�ter�l kapott taszkot hozz�adja a Manager list�j�hoz
	 */
	public void addTask(Task T){
		listOfTasks.add(T);
	}
	
}
