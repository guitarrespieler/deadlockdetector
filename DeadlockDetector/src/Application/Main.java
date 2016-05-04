package Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
				
				for(int i = 1; i  < strings.length; i++){
					T.addCommand(strings[i]);
				}
				M.addTask(T);
				
			}
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
	}

}
