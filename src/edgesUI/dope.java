package edgesUI;

import java.util.ArrayList;

public class dope {
	public static void main (String[] args){
		ArrayList<Integer> dogShit = new ArrayList<Integer>();
		for(int i = 0; i < 5; i++){
			dogShit.add(i);
		}
		
		dogShit.add(5, 100);
		
		for(int i = 0; i < dogShit.size(); i++){
			System.out.println("dogShit.get("+i+")" + dogShit.get(i));
		}
	}
}
