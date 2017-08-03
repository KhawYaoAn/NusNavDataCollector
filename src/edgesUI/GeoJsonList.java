package edgesUI;

import java.util.ArrayList;

/**
 * This class due everything with ArrayList<geoJson>, including
 * searching for vertex for a particular name. 
 * UpdateName should be done here as well. 
 * @author YewOnn
 *
 */
public class GeoJsonList {
	public static boolean isInitialized = false; 
	public static ArrayList<GeoJson> jsonList;

	/**
	 * jsonList will be updated after this call; 
	 * @param fileName
	 */
	public static void initialise(String fileName){
		GeoJsonReader geoJsonReader = new GeoJsonReader(fileName);
		jsonList = geoJsonReader.getJsonList(); 
		//to update origin information; 
		for(int i = 0; i < jsonList.size(); i++){
			String origin = jsonList.get(i).properties.locationName; 
			for(int j = 0; j < jsonList.get(i).properties.edgeList.size(); j++){
				jsonList.get(i).properties.edgeList.get(j).origin = origin; 
			}
		}
		isInitialized = true; 
	}


	public static GeoJson get(int index){
		return jsonList.get(index);
	}

	public static String getName(int index){
		return jsonList.get(index).getName(); 
	}

	public static Point getPoint(int index){
		return jsonList.get(index).getPoint(); 
	}














	//Line 50 onwards are only for functions that involve calculation; 
	/** 
	 * @param point
	 * @return the index of the vertex nearest to POINT.
	 */
	public static int getClosestPointIndex(Point point){
		int minIndex = -1; 
		double minDistance = Double.MAX_VALUE; 
		for(int i = 0; i < jsonList.size(); i++){
			double longitude = jsonList.get(i).geometries.longitude; 
			double latitude = jsonList.get(i).geometries.latitude;
			Point other = new Point(longitude, latitude);
			if(point.meterDistance(other) < minDistance){
				minDistance = point.meterDistance(other);
				minIndex = i; 
			}
		}
		return minIndex; 
	}

	/**
	 * Return index of vertex which locationName.equals(name); 
	 * return -1 if no such vertex is found; 
	 * @param name
	 * @return
	 */
	public static int getVertexIndexByName(String name){
		boolean isFound = false; 
		int vertexIndex = -1; 
		for(int i = 0; i < jsonList.size(); i++){
			String tempName = jsonList.get(i).getName(); 
			if(tempName.equals(name)){
				if(!isFound){
					isFound = true; 
					vertexIndex = i; 
				}else{
					System.out.println("Problem add GeoJsonList.getVertexIndexByName");
					System.out.println("Duplicated vertexName found");
				}
			}
		}
		return vertexIndex; 
	}

}
