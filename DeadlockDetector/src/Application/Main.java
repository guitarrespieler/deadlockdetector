package Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

/**
 * Ez az osztály felelõs a parancsok
 * beolvasásáért a szabványos bemenetrõl.
 * Ha beolvasta, átadja az ebbõl elõállított
 * taszk objektumot a Manager-nek.
 * Ha a manager végzett, kiírja a szabványos
 * kimenetre a holtpontdetektálás eredményét.
 * 
 * @author zsigatibor
 */
public class Main {

	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Manager M = new Manager();
		String line;		
		try {
			while((line = br.readLine()) != null && line.length() != 0){
				String[] strings = line.split(",");
				String taskname = strings[0];
				Task T = new Task(taskname);
				for(int i = 1; i < strings.length; i++) //adding commands to the task
					T.commands.add(strings[i]);				
				
				M.addTask(T);				
			}
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}		
		M.runAll();
		//eredmények kiírása
		Iterator<String> it = M.output.iterator();
		while(it.hasNext())
			System.out.println(it.next());		
	}
}
