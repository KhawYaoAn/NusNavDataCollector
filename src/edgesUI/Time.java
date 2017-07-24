package edgesUI;

public class Time {
	private int year; 
	private int month; 
	private int day; 
	private int hour; 
	private int minute; 
	private double seconds; 

	/**
	 * takes in a string representing time as formatted in comfort delgro's 
	 * data file and fill up the informations in time object. 
	 * @param time
	 */
	Time(String time){
		String date = time.split(" ")[0];
		this.year = Integer.parseInt(date.split("-")[0]);
		this.month = Integer.parseInt(date.split("-")[1]);
		this.day = Integer.parseInt(date.split("-")[2]);
		String instance = time.split(" ")[1];
		this.hour = Integer.parseInt(instance.split(":")[0]);
		this.minute = Integer.parseInt(instance.split(":")[1]);
		this.seconds = Integer.parseInt(instance.split(":")[2]);
	}
	
	/**
	 * takes in a second value and convert it to its corresponding
	 * Day, hour, minute and seconds value. Month and year value are set as zero. 
	 * @param seconds
	 */
	Time(double seconds){
		this.seconds = seconds; 
	}
	
	public double getSecondTime(){
		double time = this.hour*3600 + this.minute*60 + this.seconds;  
		return time; 
	}
	
	public int getYear(){
		return this.year; 
	}
	
	public int getMonth(){
		return this.month; 
	}
	
	public int getDay(){
		return this.day; 
	}
	
	public int getHour(){
		return this.hour; 
	}
	
	public int getMinute(){
		return this.minute; 
	}
	
	public double getSecond(){
		return this.seconds; 
	}
}
