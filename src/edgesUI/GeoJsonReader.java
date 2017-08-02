package edgesUI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


/**
 * this is the most fucking hard coded fuckng piece of shit i will ever fucking do. 
 * @author YewOnn
 *
 */
public class GeoJsonReader {
	FileReader f; 
	BufferedReader buff; 
	int readGeoJsonCounter = 0; 
	int readPropertiesCounter = 0;
	int readEdgeCounter = 0; 
	String currLine   = "";
	GeoJson geoJson = new GeoJson(); 
	Edge edge = new Edge(); 
	ArrayList<GeoJson> jsonList = new ArrayList<GeoJson>(); 
	public GeoJsonReader(String fileName){
		try{
			f = new FileReader(fileName);
			buff = new BufferedReader(f);
			currLine = buff.readLine(); 
		
			while(currLine != null){
				//System.out.println("count = "+count);
				if(currLine.trim().equals("{") || currLine.trim().equals("\"features\": [{")){
					readGeoJson(); //will be at }, once done
					if(currLine.trim().equals("}")){
						System.out.println("You're done, line 6054 of txt file");
					}
					jsonList.add(geoJson);
				}
				currLine = buff.readLine(); 
				//System.out.println(currLine); 
			}//end of while(line != null);
			buff.close(); 
		}catch(IOException e){
			e.printStackTrace(); 
		}		
	}

	public void readGeoJson(){
		geoJson = new GeoJson(); 
		readGeoJsonCounter = 0; 
		while(readGeoJsonCounter < 4){
			//System.out.println("readGeoJson()");
			try{
				currLine = buff.readLine().trim(); 
				//System.out.println(currLine); 
			}catch(IOException e){
				e.printStackTrace(); 
			}
			if(currLine.trim().equals("{")){
				System.out.println("fucking big problem");
			}
			//type
			if(currLine.contains("type")){
				String type = currLine.split("\"")[3];
				geoJson.type = type;
				readGeoJsonCounter++; 
			}	

			//properties
			if(currLine.contains("properties")){
				readProperties(); 
				readGeoJsonCounter++;
			}

			//geometry
			if(currLine.contains("geometry")){
				readGeometry(); 
				readGeoJsonCounter++; 
				//currLine will be }, as in line 47 after completion of readGeomtry(); 
				//if buff.readLine one more time, it goes to id line. 
			}

			//id
			if(currLine.contains("\"id\":")){  //"id": "25d99516c468044d597259f218ca8702"
				String id = currLine.split("\"")[3];
				geoJson.id = id;
				readGeoJsonCounter++;	
			}
		}//end of while(readGeoJsonCounter < 4)
	}

	public void readGeometry(){
		if(!currLine.contains("geometry")){
			System.out.println("Error: currLine do not have geometry when it is suppose to");
			return; 
		}

		try {
			currLine = buff.readLine(); //  "coordinates": [
			//System.out.println(currLine); 
			if(!currLine.contains("coordinates")){
				System.out.println("Error: no coordinates");
				return; 
			}
			currLine = buff.readLine(); //  103.77039,
			//System.out.println(currLine); 
			String sLong = currLine.trim().split(",")[0];
			currLine = buff.readLine(); //  1.296891
			//System.out.println(currLine); 
			String sLat = currLine.trim(); 
			double longitude = Double.parseDouble(sLong);
			double latitude = Double.parseDouble(sLat);
			geoJson.geometries.longitude = longitude; 
			geoJson.geometries.latitude = latitude;
			currLine = buff.readLine(); // ],
			//System.out.println(currLine); 
			if(!currLine.contains("],")){
				System.out.println("Error: no ],");
				return; 
			}
			currLine = buff.readLine(); //     "type": "Point"
			//System.out.println(currLine); 
			geoJson.geometries.type = currLine.split("\"")[3];
			currLine = buff.readLine(); // },
			//System.out.println(currLine); 

		} catch (IOException e) {
			e.printStackTrace();
		} 

	}

	public void readProperties(){
		//currLine  =            "properties": { 
		try{ 
			readPropertiesCounter = 0; 

			while(readPropertiesCounter < 6){
				currLine = buff.readLine();  
				if(currLine.contains("locationName")){//      "locationName": "MPSH 1, MPSH 2, MPSH 3, MPSH 4",
					geoJson.properties.locationName = currLine.split("\"")[3];
					readPropertiesCounter++; 
				}
				if(currLine.contains("locationType")){ //  "locationType": "building",
					geoJson.properties.locationType = currLine.split("\"")[3]; 
					readPropertiesCounter++; 
				}
				if(currLine.contains("faculty")){ // "faculty": "",
					geoJson.properties.faculty = currLine.split("\"")[3];
					readPropertiesCounter++; 
				}
				if(currLine.contains("rooms")){
					readRooms(); 
					readPropertiesCounter++; 
				}
				if(currLine.contains("edgeList")){
					readEdgeList(); 
					readPropertiesCounter++; 
				}
				if(currLine.contains("locationImageUrl")){ //  "locationImageUrl": ""
					if(currLine.split("\"").length >= 4){
						geoJson.properties.locationImageUrl = currLine.split("\"")[3];
					}
					readPropertiesCounter++; 
					
				}
			}//end of while(readPropertiesCounter < 6) while Loop
		}catch(IOException e){
			e.printStackTrace(); 
		}
	}

	public void readEdgeList(){ //"edgeList": {}, OR       "edgeList": {
		if(currLine.contains("}")){ //"edgeList": {} HOORAY EASY GAME
			return; 
		}else{ //"edgeList": {
			while(!currLine.trim().equals("}")){
				readEdge(); 
				edge.origin = geoJson.properties.locationName; 
				geoJson.properties.edgeList.add(edge); //after currLine should be }, as in line 297.
			}
			//currLine has to be "}" here. 
		}


	}

	public Edge readEdge(){ //  "edgeList": { as per line 287; 
		edge = new Edge(); 
		readEdgeCounter = 0; 
		try{
			if(currLine.contains("edgeList")){
				currLine = buff.readLine(); 
				//System.out.println(currLine); 
			}
			while(readEdgeCounter < 9){
				if(currLine.contains("edgeList")){
					System.out.println("Something is wrong with readEdge Counter, come to line 181");
					return null; 
				}
				if(currLine.contains(": {")){// "E5": {
					edge.name = currLine.split("\"")[1];
					readEdgeCounter++; 
				}
				if(currLine.contains("time")){ //  "time": 1.4285714285714286, as in line 309; 
					String line = currLine.split(":")[1].trim().split(",")[0];
					edge.time = Double.parseDouble(line); 
					readEdgeCounter++; 
				}
				if(currLine.contains("distance")){ //    "distance": 120,
					String line = currLine.split(":")[1].trim().split(",")[0];
					edge.distance = Double.parseDouble(line);
					readEdgeCounter++; 
				}
				if(currLine.contains("stairsDirection")){ // 
					String line = currLine.split(":")[1].trim().split(",")[0];
					edge.stairsDirection = Double.parseDouble(line);
					readEdgeCounter++; 
				}	
				if(currLine.contains("stairsWeight")){
					String line = currLine.split(":")[1].trim().split(",")[0];
					edge.stairsWeight = Double.parseDouble(line); 
					readEdgeCounter++; 
				}
				if(currLine.contains("shelterWeight")){
					String line = currLine.split(":")[1].trim().split(",")[0];
					edge.shelterWeight = Double.parseDouble(line); 
					readEdgeCounter++; 
				}
				if(currLine.contains("shelterMultiplier")){
					String line = currLine.split(":")[1].trim().split(",")[0];
					edge.shelterMultiplier = Double.parseDouble(line);
					readEdgeCounter++; 
				}
				if(currLine.contains("directions")){
					String line = currLine.split(":")[1].trim().split(",")[0];
					edge.directions = line; 
					readEdgeCounter++; 
				}
				if(currLine.contains("imageLinks")){
					edge.imageLinks = currLine.split(":")[1];
					readEdgeCounter++; 
					if(currLine.contains(",")){ //"imageLinks": [],
						//contains smooth curve
						currLine = buff.readLine();  // “smoothCurve”:[
						if(currLine.contains("[]")){ // "smoothCurve":[]
							//do nothing; no smoothCurve found
						}else{ //"smoothCurve":[
							boolean toContinue = true; 
							while(toContinue){
								currLine = buff.readLine(); //{1.111, 100.1111},
								if(currLine.contains("]")){
									toContinue = false; 
								}
								String sLat = currLine.split(",")[0];
								String sLat2 = sLat.split("\\{")[1];
								double latitude = Double.parseDouble(sLat2);
								String sLong = currLine.split(",")[1].trim();
								String sLong2 = sLong.split("\\}")[0];
								double longitude = Double.parseDouble(sLong2);
								Point tempPoint = new Point(longitude, latitude);
								edge.smoothRoute.add(tempPoint);
							}
						}
					}
				}
				currLine = buff.readLine(); //end at } as per line 317;
				//System.out.println(currLine); 
			}//end of while(readEdgeCounter < 9){ currline should be either }, or }
			/**
            "directions": "Data yet to be collected",
            "imageLinks": []
			 */
		}catch(IOException e){
			e.printStackTrace(); 
		}
		return null; 
	}

	public void readRooms(){ // "rooms": [    OR  "rooms": [],
		try{
			if(currLine.contains("]")){ // "rooms": [],
				return; 
			} else { //  "rooms": [  
				currLine = buff.readLine(); 
				//System.out.println(currLine); 
				while(!currLine.contains("]")){ //looing out for       ], as in line 113
					geoJson.properties.rooms.add(currLine.split("\"")[1]); //   "Adaptive Computing Research Lab",
					currLine = buff.readLine(); 
					//System.out.println(currLine); 
				}
			}
		} catch (IOException e){
			e.printStackTrace(); 
		}

	}//finish at  ], as in line 113; 

	public ArrayList<GeoJson> getJsonList(){
		return this.jsonList; 
	}
	public static String outputEdgeList(int index, ArrayList<GeoJson> outJsonList){
		String line = null; 
		GeoJson geoJson = outJsonList.get(index);
		ArrayList<Edge> edgeList = geoJson.properties.edgeList; 
		if(edgeList.size() == 0){
			 line = "\"edgeList\": {},\n";
		}else{
			line =  "\"edgeList\": {\n";
			for(int i = 0; i < edgeList.size(); i++){
				Edge edge = edgeList.get(i);
				line = line +  "\""+edge.name+"\": {\n" 
						+"\"time\": "+Double.toString(edge.time)+",\n"
						+"\"distance\": "+Double.toString(edge.distance)+",\n"
						+"\"stairsDirection\": "+edge.stairsDirection+",\n"
						+"\"stairsWeight\": "+edge.stairsWeight+",\n"
						+"\"shelterWeight\": "+edge.shelterWeight+",\n"
						+"\"shelterMultiplier\": "+edge.shelterMultiplier+",\n"
						+"\"directions\": "+edge.directions+",\n"
						+"\"imageLinks\": "+edge.imageLinks+",\n";
				if(edge.smoothRoute.size() == 0){
					line = line + "\"smoothRoute\": []\n";
				}else{
					line = line + "\"smoothRoute\": [\n";
					for(int j = 0; j < edge.smoothRoute.size(); j++){
						String longitude = Double.toString(edge.smoothRoute.get(j).getLong());
						String latitude = Double.toString(edge.smoothRoute.get(j).getLat());
						if(j == edge.smoothRoute.size() - 1){
							//{3.333, 333.333}]
							line = line + "{" + latitude + ", " + longitude + "}]\n" ;
						}else{
							//{1.111, 100.1111},
							line = line + "{" + latitude + ", " + longitude + "},\n" ;
						}
					}
				}
				if(i < edgeList.size() - 1){
					line = line + "},\n"; 
				}else{
					line = line + "}\n},\n";
				}
			}
		}
		return line; 
	}

	/**
	 * "rooms": [
          "Centre for Biomedical Ethics",
          "Department of Pharmacology",
          "MD11",
          "National University Medical Institutes",
          "School of Medicine (Dean's Office)",
          "Starbucks (MD11)"
        ],


           "rooms": [],
	 * @param index
	 * @return
	 */
	public static String outputRoom(int index, ArrayList<GeoJson> outJsonList){
		String line = null; 
		GeoJson geoJson = outJsonList.get(index);
		ArrayList<String> rooms = geoJson.properties.rooms; 
		if(rooms.size() == 0){
			line = "\"rooms\": [],\n";
		}else{
			line = "\"rooms\": [\n";
			for(int i = 0; i < rooms.size(); i++){
				if(i < rooms.size() - 1){
					line = line + "\""+rooms.get(i)+"\",\n";
				}else{
					line = line + "\""+rooms.get(i)+"\"\n],\n";
				}
			}
		}
		
		return line; 
	}
	/**
	 *  "properties": {
    "locationName": "MD11",
    "locationType": "building",
    "faculty": "",
    "rooms": [
      "Centre for Biomedical Ethics",
      "Department of Pharmacology",
      "MD11",
      "National University Medical Institutes",
      "School of Medicine (Dean's Office)",
      "Starbucks (MD11)"
    ],
    "edgeList": {},
    "locationImageUrl": ""
  },*/
	public static String outputProperties(int index, ArrayList<GeoJson> outJsonList){
		String line = null; 
		line = "\"properties\": {\n"
				+ "\"locationName\": \""+outJsonList.get(index).properties.locationName+"\",\n" 
				+ "\"locationType\": \""+outJsonList.get(index).properties.locationType+"\",\n"
				+ "\"faculty\": \""+outJsonList.get(index).properties.faculty+"\",\n";
		line = line + outputRoom(index, outJsonList); 
		line = line + outputEdgeList(index, outJsonList);
		if(outJsonList.get(index).properties.locationImageUrl != null){
			line = line +  "\"locationImageUrl\": \""+outJsonList.get(index).properties.locationImageUrl+"\"\n";
		} else {
			line = line + "\"locationImageUrl\": \"\"\n";
		}
		line = line + "},\n";

		return line; 
	}

	public static String outputGeometries(int index, ArrayList<GeoJson> outJsonList){
		String line ="\"geometry\": {\n";
		line = line + "\"coordinates\": [\n"+Double.toString(outJsonList.get(index).getLong())+
				",\n"+Double.toString(outJsonList.get(index).getLat())+"\n],\n";	
		line = line + "\"type\": \""+outJsonList.get(index).geometries.type+"\"\n},\n";
		return line; 
	}
	
	
	public static String outputInFormat(int index, ArrayList<GeoJson> outJsonList){
		GeoJson geoJson = outJsonList.get(index);
		//start from line 24; 
		String line = null ;
		line = "{" + "\n" + "\"type\": "+"\""+geoJson.type+"\",\n";
		line = line + outputProperties(index, outJsonList);
		line = line + outputGeometries(index, outJsonList); 
		line = line + "\"id\": \""+geoJson.id+"\"\n";
		if(index <outJsonList.size() - 1){
			line = line + "},\n";
		}else{
			line = line + "}\n";
		}
		return line; 
	}
	
	public static void outTxt(String fileName, ArrayList<GeoJson> outJsonList){
		FileWriter f;
		try {
			f = new FileWriter(fileName);
			BufferedWriter buff = new BufferedWriter(f);
			String line = "var geojson = {\n\"features\": [";
			buff.write(line);
			for(int i = 0; i < outJsonList.size(); i++){
				buff.write(outputInFormat(i, outJsonList)); 
			}
			buff.write("],\n\"type\": \"FeatureCollection\"\n}\n"); 
			buff.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
	}
	
	public static void main (String [] args){
		GeoJsonReader jsonReader = new GeoJsonReader("getJson.txt"); 
		ArrayList<GeoJson> jsonList = jsonReader.getJsonList(); 
		System.out.println("jsonList.size() = "+jsonList.size()); 
		GeoJsonReader.outTxt("bcd.txt", jsonList);
	}
}
