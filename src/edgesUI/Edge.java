package edgesUI;

import java.util.ArrayList;

public class Edge {
	final double WALKING_SPEED = 100; //100 meter per seconds
	String name = null;  //set by user; 
	double time = -1; //set by me; 
	double distance = -1;  //calculated by me; 
	double stairsDirection = 1; //default for flat ground; only change; 
	double stairsWeight = -1; //calculated by me, distance * stairsDirection; 
	double shelterWeight = 0; //calculated by me = distance * shelter multiplier
	double shelterMultiplier = 0; //user input. //0 means got shleter
	String directions = "Data yet to be collected"; //permanently set as this;
	String imageLinks = "[]"; //permanently set as "[]"; 
	
	String origin = null; //set by me; no output
	String[] propertyList = new String[10]; //set  by me; no output
	ArrayList<Point> smoothRoute = new ArrayList<Point>();  //does not include the source and destination itself; 
	
	public Edge(){
	}
	
	
	public String getName(){
		return this.name; 
	}
	
	public void update(boolean updateDistance){
		if(updateDistance){
			int originIndex = GeoJsonList.getVertexIndexByName(origin);
			int edgeIndex = GeoJsonList.getVertexIndexByName(name); 
			Point point = GeoJsonList.getPoint(originIndex);
			Point other = GeoJsonList.getPoint(edgeIndex);
			distance = other.meterDistance(point);
		}
		time = distance/WALKING_SPEED; //assuming walking speed of 100 meters per minute; 
		//0 will be 1, 1 will be 1, 2 will be 2; 
		stairsWeight = distance*(((int)stairsDirection)/2 + 1);
		
		shelterWeight = shelterMultiplier*distance; 
		
		this.setList(); 
	}
	
	public void setList(){
		propertyList[0] = name; 
		propertyList[1] = Double.toString(time); 
		propertyList[2] = Double.toString(distance); 
		propertyList[3] = Double.toString(stairsDirection); 
		propertyList[4] = Double.toString(stairsWeight);
		propertyList[5] = Double.toString(shelterWeight);
		propertyList[6] = Double.toString(shelterMultiplier); 
		propertyList[7] = directions; 
		propertyList[8] = imageLinks; 
		
		propertyList[9] = origin; 
	}
	
	public String get(int index){
		return propertyList[index];
	}
}
