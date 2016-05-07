package Application;

import java.util.ArrayList;

/**
 * Taszkokat reprezentáló osztály.
 * Tárolja az erõforrás-foglalások
 * és -felszabadítások sorrendjét,
 * valamint azt, hogy mely erõforrásokra
 * várakozik ez a taszk.
 * 
 * @author zsigatibor
 */
public class Task {
	String name = "";
	ArrayList<String>commands = new ArrayList<>();
	ArrayList<Resource> waitingForTheseResources = new ArrayList<>();
	
	/**
	 * Nem töröljük a parancsot végrehajtásakor,
	 * így egy számlálóra van szükség, hogy tudjuk, melyik
	 * parancs lesz a soron következõ. Ez kell ráadásul a
	 * kimenethez is, ezért tároljuk el. 
	 * A lista 0-tól, ez a számláló 1-tõl számolja a 
	 * parancsokat, így a lista következõ elemének
	 * kivételekor nextCommand-1 -el kell indexelni.
	 */
	int nextCommand = 1;
	
	public Task(String taskName){
		name = taskName;
	}
}
