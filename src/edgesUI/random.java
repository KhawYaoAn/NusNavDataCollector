package edgesUI;

public class random {
	public static void main (String[] args){
		String dog = "{1.111, 100.1111},";
		String[] apple = dog.split("\\{");
		for(int i = 0; i < apple.length; i++){
			System.out.println(i+"= "+apple[i]);
		}
	}
}
