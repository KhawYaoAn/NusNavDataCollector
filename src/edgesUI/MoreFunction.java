package edgesUI;

import java.util.ArrayList;
import java.util.Scanner;

public class MoreFunction {
	static Scanner sc = new Scanner(System.in); 

	public static void getVertexDetailFromUser(Point point){//for function 10, add vertex
		//update the panel with this points information
		double longitude = point.getLong(); 
		double latitude = point.getLat(); 
		String name = null; 
		String coordinates = Double.toString(longitude) + "," + Double.toString(latitude);
		PanelDisplay.addVertexDetail(coordinates, null, null);

		//get VertexName; 
		name = getNameFromUser(); 
		PanelDisplay.addVertexDetail(coordinates, name, null);
		GeoJson json = new GeoJson(); 
		json.properties.locationName = name; 
		json.geometries.latitude = point.getLat(); 
		json.geometries.longitude = point.getLong(); 
		System.out.println("Do you want to add this vertex? Enter y if yes, "
				+ "any other key otherwise to return");
		String input = sc.nextLine(); 
		if(input.equals("y")){
			System.out.println("GeoJsonList.jsonList.size() = "+GeoJsonList.jsonList.size()); 
			GeoJsonList.jsonList.add(json);
			System.out.println("GeoJsonList.jsonList.size() = "+GeoJsonList.jsonList.size()); 
			System.out.println("Location type is set as \"building\" by default");
			System.out.println("facult is set as \"\" by default");
			input = ""; 
			while(!input.equals("y") && !input.equals("n")){
				System.out.println("Would you like to add edge? Enter y for yes,"
						+ "n for no"); 
				input = sc.nextLine(); 
			}
			if(input.equals("n")){
				System.out.println("reseting page... ");
				System.out.println("please return to userInterface"); 
				return; 
			}else{
				System.out.println("please return to userInterface to add edges");
				UIFunction.addEdge(GeoJsonList.getVertexIndexByName(name)); 
			}

		}else{
			//do nothing. 
			//will reset display() by returning to run's while loop; 
		}
	}

	//Vertex already selected
	public static void selectVertex(int vertexIndex){ //function 11 part2b
		PanelDisplay.selectVertexPanel(vertexIndex); //update UI; 
		System.out.println("here");
		int function = -1; 
		while(function < 12 || function > 16){ // 12 to 16 are the function of interest. 
			function = UserInterface.functionListener(); 
		}
		if(function == 12){  //deleteVertex
			UIFunction.deleteVertex(vertexIndex); 
		}
		if(function == 13){ //abort
			UIFunction.returnToOrigin(); 
		}

		if(function == 14){ //editVertex
			UIFunction.editVertex(vertexIndex); 
		}
		if(function == 15){ //editVertexName
			UIFunction.editVertexName(vertexIndex); 
		}
		if(function == 16){
			UIFunction.editVertexLocation(vertexIndex); 
		}
		if(function == 17){
			//later
		}
		if(function == 18){
			//later
		}
	}

	/**
	 * Including deleting all the edges
	 * @param vertexIndex
	 */
	public static void deleteVertex(int vertexIndex){
		System.out.println("84ac");
		String vertexName = GeoJsonList.getName(vertexIndex);
		//Two ways of doing this. one is iterate through the edges in this vertex
		//then only go to those vertex . 
		GeoJson geoJson = GeoJsonList.get(vertexIndex);
		ArrayList<Edge> edgeList = geoJson.getEdges(); 
		for(int i = 0; i < edgeList.size(); i++){
			Edge edge = edgeList.get(i);
			String edgeName = edge.name; 
			int edgeIndex = GeoJsonList.getVertexIndexByName(edgeName); 
			ArrayList<Edge> otherEdgeList = GeoJsonList.get(edgeIndex).getEdges(); 
			boolean found = false; 
			for(int j = 0; j < otherEdgeList.size(); j++){
				String otherEdgeName = otherEdgeList.get(j).name; 
				if(otherEdgeName.equals(vertexName)){
					found = true; 
					otherEdgeList.remove(j);
					System.out.println("deleted");
					j = otherEdgeList.size(); //terminate from for loop; 
				}
			}
			if(!found){
				System.out.println("Error with deleteVertex"
						+ ", edge not found in partner vertex");
				System.out.println("vertexName = "+vertexName);
				System.out.println("edgeName = "+edgeName);
			}
		}
		GeoJsonList.jsonList.remove(vertexIndex);
	}

	public static void editVertexName(int vertexIndex){
		System.out.println("OrginalName = "+GeoJsonList.getName(vertexIndex));
		String newName = getNameFromUser(); 
		updateName(vertexIndex, newName);
	}

	//function 16
	public static void editVertexLocation(int vertexIndex){
		PanelDisplay.editVertexLocationPanel(vertexIndex, true);
		Point point = Display.listener(200);
		StdDraw.setPenColor(StdDraw.RED);
		Display.showTarget(point, 10);
		PanelDisplay.isSatisfied(15, 16);
		int functionChose = -1; 
		while(functionChose!= 15 && functionChose!= 16){
			functionChose = UserInterface.functionListener(); 
		}
		if(functionChose == 15){
			updateLocation(vertexIndex, point); 
		}
		if(functionChose == 16){
			editVertexLocation(vertexIndex);
		}
	}

	//function 18 edge has already be selected
	public static void selectEdge(int sourceIndex, int destIndex, Edge edge, Edge otherEdge){
		PanelDisplay.selectEdgePanel(sourceIndex, destIndex, edge);
		int function = -1; 
		while(function != 3 && function != 6 && function != 13 &&
				function != 17 && function != 19 && function != 20){
			function = UserInterface.functionListener(); 
		}

		if(function == 3){ //change stairs direction
			int stairsDirection = chooseStairsDirection(sourceIndex, destIndex, edge); 
			edge.stairsDirection = stairsDirection;
			edge.update(false);
			int otherDirection = 1; 
			if(stairsDirection == 0){
				otherDirection = 2; 
			}
			if(stairsDirection == 2){
				otherDirection = 2; 
			}
			otherEdge.stairsDirection = otherDirection; 
			otherEdge.update(false); 
			selectEdge(sourceIndex, destIndex, edge, otherEdge);
		}

		if(function == 6){ //change shelterMultiplier
			int shelterMultiplier = chooseShelterMultiplier(sourceIndex, destIndex, edge);
			edge.shelterMultiplier = shelterMultiplier; 
			otherEdge.shelterMultiplier = shelterMultiplier; 
			edge.update(false);
			otherEdge.update(false);
			selectEdge(sourceIndex, destIndex, edge, otherEdge);
		}

		if(function == 13){
			UIFunction.returnToOrigin(); 
		}

		if(function == 17){
			UIFunction.addEdge(sourceIndex);
		}

		if(function == 19){ //delete edge
			UIFunction.deleteEdge(sourceIndex, destIndex, edge, otherEdge); 
		}
		if(function == 20){
			UIFunction.editSmoothRoute(sourceIndex, destIndex, edge, otherEdge);
		}
	}

	public static void smoothRouteAddPoint(int sourceIndex, int destIndex, Edge edge, Edge otherEdge){
		//dig out the route list; 
		PanelDisplay.refreshPanel(); 
		PanelDisplay.updateEdgePanel(edge);
		PanelDisplay.updateVertexPanel(sourceIndex);
		Point sourcePoint = GeoJsonList.getPoint(sourceIndex);
		ArrayList<Point> routeList = edge.smoothRoute; 
		int selectedIndex = -1; 
		if(routeList == null){
			selectedIndex = -1;
		}else{
			if(routeList.size() == 0){
				selectedIndex = -1; 
			}else{
				UserInterface.setBrightColor(); 
				UserInterface.colorHorizontalTwo(1, "please click one of the route's point"); 
				Point userPoint = Display.listener(300);
				selectedIndex = smoothRouteGetNearestPoint(userPoint, routeList);
			}
		}
		UserInterface.setBrightColor(); 
		UserInterface.colorHorizontalTwo(1, "please click add your pointts"); 
		UserInterface.setClickColor();
		UserInterface.colorVertical(14, "stop/adding");
		boolean toExit = false; 
		int currIndex = selectedIndex + 1; 
		System.out.println("Entering while loop...");
		while(!toExit){
			System.out.println("What the hell");
			Point newPoint = Display.listener(200, false); 
			if(UserInterface.functionListener(newPoint, false) == 14){
				toExit = true; 
				System.out.println("newPoint == 14");
			}else{
				newPoint = Scaler.scaleToActualGPS(newPoint);
				System.out.println("newPoint = "+newPoint.getLat()+", "+newPoint.getLong());
				StdDraw.setPenColor(StdDraw.RED);
				Display.showPoint(newPoint, 0, 3);
				routeList.add(currIndex, newPoint);
				StdDraw.setPenColor(StdDraw.RED); 
				if(currIndex > 0){
					Display.drawLine(routeList.get(currIndex - 1), routeList.get(currIndex));
				}else{
					Display.drawLine(sourcePoint, newPoint);
				}
				currIndex++;
			}
		}		
		otherEdge.smoothRoute = reverseRouteList(routeList);
		MoreFunction.selectEdge(sourceIndex, destIndex, edge, otherEdge);
	}

	public static void smoothRouteMovePoint(int sourceIndex, int destIndex, Edge edge, Edge otherEdge){
		PanelDisplay.refreshPanel(); 
		PanelDisplay.updateEdgePanel(edge);
		PanelDisplay.updateVertexPanel(sourceIndex);
		Point sourcePoint = GeoJsonList.getPoint(sourceIndex);
		ArrayList<Point> routeList = edge.smoothRoute; 

		UserInterface.setBrightColor(); 
		UserInterface.colorHorizontalTwo(1, "please click one of the route's point"); 
		Point userPoint = Display.listener(300);
		int selectedIndex = smoothRouteGetNearestPoint(userPoint, routeList);
		Point selectedPoint = routeList.get(selectedIndex);
		StdDraw.setPenColor(StdDraw.GREEN);
		Display.showPoint(selectedPoint, 3, 6);
		UserInterface.colorHorizontalTwo(1, "please select new point"); 
		UserInterface.colorVertical(15, "abort");
		Point newPoint = Display.listener(200, false); 
		
		if(UserInterface.functionListener(newPoint, false) == 15){
			MoreFunction.selectEdge(sourceIndex, destIndex, edge, otherEdge);
		}else{
			newPoint = Scaler.scaleToActualGPS(newPoint);
			System.out.println("newPoint = "+newPoint.getLat()+", "+newPoint.getLong());
			StdDraw.setPenColor(StdDraw.ORANGE);
			Display.showPoint(newPoint, 0, 3);
			routeList.remove(selectedIndex);
			routeList.add(selectedIndex, newPoint);
			StdDraw.setPenColor(StdDraw.ORANGE); 
			if(selectedIndex > 0){
				Display.drawLine(routeList.get(selectedIndex - 1), routeList.get(selectedIndex));
			}else{
				Display.drawLine(sourcePoint, newPoint);
			}
		}
		otherEdge.smoothRoute = reverseRouteList(routeList);
		MoreFunction.selectEdge(sourceIndex, destIndex, edge, otherEdge);
	}

	public static void smoothRouteDeletePoint(int sourceIndex, int destIndex, Edge edge, Edge other){
		PanelDisplay.refreshPanel(); 
		PanelDisplay.updateEdgePanel(edge);
		PanelDisplay.updateVertexPanel(sourceIndex);
		ArrayList<Point> routeList = edge.smoothRoute; 

		UserInterface.setBrightColor(); 
		UserInterface.colorHorizontalTwo(1, "please click one of the route's point"); 
		Point userPoint = Display.listener(300);
		int selectedIndex = smoothRouteGetNearestPoint(userPoint, routeList);
		Point selectedPoint = routeList.get(selectedIndex);
		StdDraw.setPenColor(StdDraw.GREEN);
		Display.showPoint(selectedPoint, 3, 6);
		UserInterface.colorVertical(15, "confirm");
		UserInterface.colorVertical(16, "abort");
		int function = UserInterface.functionListener();
		while(function != 15 && function != 16){
			function = UserInterface.functionListener(); 
		}
		if(function == 15){ //confirm the deleter
			routeList.remove(selectedIndex);
		}else{ //abort the delete, aka do nothing here. 
			
		}
		other.smoothRoute = reverseRouteList(routeList);
		MoreFunction.selectEdge(sourceIndex, destIndex, edge, other);
	}

	public static ArrayList<Point> reverseRouteList(ArrayList<Point> routeList){
		ArrayList<Point> reverseList = new ArrayList<Point>(); 
		for(int i = 0; i < routeList.size(); i++){
			reverseList.add(routeList.get(routeList.size() - i - 1));
		}
		return reverseList; 
	}

	public static int smoothRouteGetNearestPoint(Point point, ArrayList<Point> pointList){
		double minDistance = Double.MAX_VALUE; 
		int index = -1; 
		for(int i = 0; i < pointList.size(); i++){
			double currDistance = point.meterDistance(pointList.get(i));
			if(currDistance < minDistance){
				minDistance = currDistance; 
				index = i; 
			}
		}
		return index; 
	}




	/**
	 * Including update all distance between two points; 
	 * @param vertexIndex
	 * @param point
	 */
	public static void updateLocation(int vertexIndex, Point point){
		//update location of vertex; 
		GeoJson geoJson = GeoJsonList.get(vertexIndex); 
		geoJson.geometries.longitude = point.getLong();
		geoJson.geometries.latitude = point.getLat(); 
		ArrayList<Edge> edgeList = geoJson.getEdges(); 
		for(int i = 0; i < edgeList.size(); i++){
			Edge edge = edgeList.get(i);
			String name = edge.getName(); 
			int otherIndex = GeoJsonList.getVertexIndexByName(name);
			Point other = GeoJsonList.get(otherIndex).getPoint(); 
			edgeList.get(i).distance = point.meterDistance(other);
		}

		//this is method two la; 
		//update distance of all related edges in other vertex; 
		for(int i = 0; i < GeoJsonList.jsonList.size(); i++){
			GeoJson geoJson2 = GeoJsonList.get(i);
			ArrayList<Edge> edgeList2 = geoJson.properties.edgeList; 

			//iterate through all edges. If name of edge match vertex's 
			//original name, change it to newName
			for(int j = 0; j < edgeList2.size(); j++){
				String tempName = edgeList2.get(j).getName(); 
				if(tempName.equals(geoJson.getName())){
					double longitude = geoJson2.geometries.longitude; 
					double latitude = geoJson2.geometries.latitude; 
					Point other = new Point(longitude, latitude);
					edgeList2.get(j).distance = point.meterDistance(other);
				}
			}
		}

	}

	/**
	 * Edit the name of the vertex which index is vertexIndex in the arrayList
	 * to newName and change all edge that has this original name into newName.
	 * name is stores in GeoJsonList.get(vertexIndex).properties.locationName. 
	 * @param vertexIndex
	 * @param name
	 */
	public static void updateName(int vertexIndex, String newName){
		//the name before editVertexName
		String iniName = GeoJsonList.getName(vertexIndex);
		//change the name of vertex to newName
		GeoJsonList.get(vertexIndex).properties.locationName = newName; 
		//iterate through all vertex
		for(int i = 0; i < GeoJsonList.jsonList.size(); i++){
			GeoJson geoJson = GeoJsonList.get(i);
			ArrayList<Edge> edgeList = geoJson.properties.edgeList; 

			//iterate through all edges. If name of edge match vertex's 
			//original name, change it to newName
			for(int j = 0; j < edgeList.size(); j++){
				String tempName = edgeList.get(j).getName(); 
				if(tempName.equals(iniName)){
					edgeList.get(j).name = newName; 
				}
			}
		}
	}



	/**
	 * This function can only be called if vertex with vertexIndex
	 * has at least one edge. 
	 * @return the edge within vertex specified by vertexIndex 
	 * which is nearest to point. 
	 */
	public static Edge getNearestEdge(Point point, int vertexIndex){
		//get the edgeList. 
		ArrayList<Edge> edgeList = GeoJsonList.get(vertexIndex).getEdges(); 
		//check length of edgeList
		double minDistance = Double.MAX_VALUE; 
		int edgeIndex = -1; 

		for(int i = 0; i < edgeList.size(); i++){
			int currEdgeIndex = GeoJsonList.getVertexIndexByName(edgeList.get(i).getName());
			Point currEdge = GeoJsonList.get(currEdgeIndex).getPoint(); 
			double distance = point.meterDistance(currEdge);
			if(distance < minDistance){
				minDistance = distance; 
				edgeIndex = i; 
			}
		}
		if(edgeIndex < 0){
			System.out.println("Error: UIFunction.getNearestEdge(Point point int vertexIndex");
			System.out.println("edgeList.size() = "+edgeList.size()+
					", edgeIndex ="+edgeIndex);
			return null; 
		}


		return edgeList.get(edgeIndex); 
	}

	//function 17 ... edge already selected
	public static Edge addEdge(int vertexIndex, int otherIndex){
		Edge edge = new Edge(); 
		edge.origin = GeoJsonList.getName(vertexIndex);
		edge.name = GeoJsonList.getName(otherIndex);
		edge.update(true);
		GeoJsonList.jsonList.get(vertexIndex).properties.edgeList.add(edge);
		Edge otherEdge = reverseEdge(edge);
		GeoJsonList.jsonList.get(otherIndex).properties.edgeList.add(otherEdge);
		MoreFunction.selectEdge(vertexIndex, otherIndex, edge, otherEdge);
		return edge; 
	}

	public static String getNameFromUser(){
		String name = null; 
		boolean hasName = false; 

		//get VertexName; 
		while(!hasName){
			System.out.println("Enter name:");
			name = sc.nextLine(); 
			UserInterface.setDisplayColor(); 
			UserInterface.colorHorizontalTwo(1, "Name Chosen:"+name); 
			name = name.trim(); 
			System.out.println("Chosen name:"+name);
			System.out.println("Enter y (lower case) to confirm, press other key to type again");
			String tempString = sc.nextLine(); 
			if(tempString.equals("y")){
				if(GeoJsonList.getVertexIndexByName(name) == -1){
					hasName = true; 
				}else{
					System.out.println("other point already has name = "+name);
					System.out.println("do again...");
				}			
			}
		}
		return name; 
	}

	public static boolean addEdgeQuestion(){
		System.out.println("Do you want to add edge:");
		System.out.println("press y if you want, if not, any other key"); 
		String tempString = sc.nextLine(); 
		if(tempString.equals("y")){

		}else{
			//do nothing
			return false; 
		}
		return true; 
	}

	public static Edge reverseEdge(Edge edge){
		Edge other = new Edge(); 
		edge.update(false);
		other.name = edge.origin; 
		other.origin = edge.name;
		other.shelterMultiplier = edge.shelterMultiplier; 
		double stairsDirection = edge.stairsDirection;
		double otherStairsDirection = stairsDirection; 
		if(stairsDirection == 0){
			otherStairsDirection  = 2; 
		}else if(stairsDirection == 2){
			otherStairsDirection = 0; 
		}else {
			otherStairsDirection = 1; 
		}
		other.stairsDirection = otherStairsDirection; 
		other.update(true);
		return other; 
	}

	public static int chooseStairsDirection(int sourceIndex, int destIndex, Edge edge){
		PanelDisplay.refreshPanel(); 
		PanelDisplay.updateVertexPanel(sourceIndex);
		UserInterface.setDisplayColor(); 
		PanelDisplay.updateEdgePanel(edge);
		UserInterface.setDisplayColor(); 
		UserInterface.colorHorizontalTwo(0, "source = "+GeoJsonList.getName(sourceIndex));
		UserInterface.colorHorizontalTwo(1, "destination = "+GeoJsonList.getName(destIndex));
		UserInterface.colorHorizontalOne(0, "Updating stairs Direction...");
		UserInterface.setBrightColor(); 
		UserInterface.colorVertical(9, "CHOOSE ONE");
		UserInterface.setClickColor(); 
		UserInterface.colorVertical(10, "0");
		UserInterface.colorVertical(11, "1");
		UserInterface.colorVertical(12, "2");
		int option = -1; 
		while(option != 10 && option != 11 && option != 12){
			option = UserInterface.functionListener(); 
		}
		if(option == 10){
			return 0; 
		}else if(option == 11){
			return 1; 
		}else {
			return 2; 
		}

	}

	public static int chooseShelterMultiplier(int sourceIndex, int destIndex, Edge edge){
		//UserInterface.createVertical(); 
		PanelDisplay.refreshPanel(); 
		PanelDisplay.updateVertexPanel(sourceIndex);
		PanelDisplay.updateEdgePanel(edge); 
		UserInterface.setDisplayColor(); 
		UserInterface.colorHorizontalTwo(1, "Updating shelterMultiplier");
		UserInterface.colorHorizontalTwo(0, "source = "+GeoJsonList.getName(sourceIndex));
		UserInterface.colorHorizontalTwo(1, "destination = "+GeoJsonList.getName(destIndex));
		UserInterface.setBrightColor(); 
		UserInterface.colorVertical(9, "CHOOSE ONE");
		UserInterface.setClickColor(); 
		UserInterface.colorVertical(10, "0");//got shelter
		UserInterface.colorVertical(11, "1");//no shelter
		int option = -1; 
		while(option != 10 && option != 11){
			option = UserInterface.functionListener(); 
		}
		if(option == 10){
			return 0; 
		}else {
			return 1; 
		}

	}

	public static Edge getEdgeByName(int vertexIndex, String edgeName){
		ArrayList<Edge> edgeList = GeoJsonList.get(vertexIndex).getEdges();
		for(int i = 0; i < edgeList.size(); i++){
			String tempName = edgeList.get(i).name; 
			if(tempName.equals(edgeName)){
				return edgeList.get(i);
			}
		}
		return null; 
	}
}
