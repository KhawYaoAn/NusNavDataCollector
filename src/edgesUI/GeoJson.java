package edgesUI;

import java.util.ArrayList;

public class GeoJson implements Comparable<GeoJson>{
	String type = "Feature"; //done; 
	Properties properties = null; //done; 
	Geometries geometries = null; //done; 
	String id = "ANY_RANDOM_NUMBER"; //done
	
	public GeoJson(){
		properties = new Properties(); 
		geometries = new Geometries(); 
	}
	
	class Geometries{
		double longitude = -1;  //set by me
		double latitude = -1;  //set by me
		String type = "Point"; 
	}
	
	class Properties{
		String locationName = null; //set by user
		String locationType = "building" ; 
		String faculty = "" ; //done
		ArrayList<String> rooms = new ArrayList<String>(); //leave as null
		ArrayList<Edge> edgeList = new ArrayList<Edge>(); //leave as null
		String locationImageUrl = ""; //leave as ""; 
	}

	
	
	
	
	
	
	
	
	
	
	@Override
	public int compareTo(GeoJson other) {
		String name = this.properties.locationName; 
		String oName = other.properties.locationName; 
		
		return name.compareTo(oName);
	}
	
	public String getName(){
		return this.properties.locationName; 
	}
	
	public double getLong(){
		return this.geometries.longitude; 
	}
	
	public double getLat(){
		return this.geometries.latitude; 
	}
	
	public String getCoordinates(){
		String longitude = Double.toString(this.getLong());
		String latitude = Double.toString(this.getLat());
		return (latitude +"," +longitude);
	}
	
	public Point getPoint(){
		return new Point(this.getLong(), this.getLat());
	}
	
	public ArrayList<Edge> getEdges(){
		return this.properties.edgeList; 
	}
	
	public String[] getEdgesName(){
		String[] edgeList = new String[this.properties.edgeList.size()];
		for(int i = 0; i < this.properties.edgeList.size(); i++){
			edgeList[i] = this.properties.edgeList.get(i).getName(); 
		}
		return edgeList; 
	}
	
	public String getEdgeString(){
		String[] edgeList = new String[this.properties.edgeList.size()];
		String edgeString = "";
		for(int i = 0; i < this.properties.edgeList.size(); i++){
			edgeList[i] = this.properties.edgeList.get(i).getName(); 
			edgeString = edgeString + edgeList[i];
			if(i != this.properties.edgeList.size() - 1){
				edgeString = edgeString + ",";
			}
		}
		
		return edgeString; 
	}
	
	
	
	
	
	
	
	
	
	
	
	public boolean hasEdge(int otherIndex){
		return hasEdge(GeoJsonList.get(otherIndex).getName());
	}

	public boolean hasEdge(String edgeName){
		ArrayList<Edge> edgeList = this.getEdges(); 
		for(int i = 0; i < edgeList.size(); i++){
			String name = edgeList.get(i).name; 
			if(edgeName.equals(name)){
				return true; 
			}
		}
		return false; 
	}
}
