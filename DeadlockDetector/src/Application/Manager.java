package Application;

import java.util.ArrayList;
import java.util.Iterator;

public class Manager {
	ArrayList<Task> listOfTasks = new ArrayList<>();
	ArrayList<Resource>listOfResources = new ArrayList<>();
	ArrayList<String> outpus = new ArrayList<>();
	
	public void RunAll(){
		while(listOfTasks.size() != 0){
			for(int i = 0; i < listOfTasks.size(); i++){// 1 lépés
				Task temp = listOfTasks.get(i);
				run(temp);				
			}
		}
			
	}
	
	public void run(Task T){
		if(T.commands.size() == 0){
			freeAllResources(T);
			listOfTasks.remove(T);
			return;
		}	
		boolean successful = false;
		String nextCmd = T.commands.get(0);
		String[] chars = nextCmd.split("");
		if(chars.length == 1){  //NO-OP method
			T.commands.remove(0);
			return;
		}
		if(chars[0].equals("-")){//free resource ( for example "-R1" )
			freeResource(getResourceByName(chars[1] + chars[2]));
		}
		if(chars[0].equals("+")){//ask for resource ( for example "+R5" )
			successful = askForResource(T,chars[1] + chars[2]);
		}
		if(!successful){
			System.out.println(T.name + "," + );
			
		}
	}
	
	private boolean checkForDeadLock(/*Task actualTask,Task searchedTask, Resource actualRes, Resource wantedRes*/){
		//T szeretné használni R-t. El kell indulni és meg kell nézni, hogy T-t megtaláljuk-e benne.
		//ide kéne valami rekurzív függvény, mélységében be kellene járni a fát.
		//Sok sikert hozzá, Tibor.
		//Ha ez meglesz, valószínûleg a feladat oroszlánrészével meg leszel.
		return false;
	}
	/**
	 * @return false if asking was not successful
	 */
	private boolean askForResource(Task T, String resourceName){
		Resource R = getResourceByName(resourceName);
		if(R != null){
			if(R.thisTaskUsesMe != null && !checkForDeadLock()){
				T.waitingForTheseResources.add(R);
			}
			else if(R.thisTaskUsesMe != null && checkForDeadLock()){
				T.commands.remove(0);//töröljük a parancsot, holtpontot okozna
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
	private void freeAllResources(Task T){
		Iterator<Resource> it = listOfResources.iterator();
		while(it.hasNext()){
			Resource temp = it.next();
			if(temp.thisTaskUsesMe == T)
				freeResource(temp);
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
