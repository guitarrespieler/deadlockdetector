package Application;

import java.util.ArrayList;

/**
 * Taszkokat reprezent�l� oszt�ly.
 * T�rolja az er�forr�s-foglal�sok
 * �s -felszabad�t�sok sorrendj�t,
 * valamint azt, hogy mely er�forr�sokra
 * v�rakozik ez a taszk.
 * 
 * @author zsigatibor
 */
public class Task {
	String name = "";
	ArrayList<String>commands = new ArrayList<>();
	ArrayList<Resource> waitingForTheseResources = new ArrayList<>();
	
	/**
	 * Nem t�r�lj�k a parancsot v�grehajt�sakor,
	 * �gy egy sz�ml�l�ra van sz�ks�g, hogy tudjuk, melyik
	 * parancs lesz a soron k�vetkez�. Ez kell r�ad�sul a
	 * kimenethez is, ez�rt t�roljuk el. 
	 * A lista 0-t�l, ez a sz�ml�l� 1-t�l sz�molja a 
	 * parancsokat, �gy a lista k�vetkez� elem�nek
	 * kiv�telekor nextCommand-1 -el kell indexelni.
	 */
	int nextCommand = 1;
	
	public Task(String taskName){
		name = taskName;
	}
}
