package edgesUI;

import java.util.ArrayList;
import java.util.Scanner;


/**
 * All the function that doesn't involve UI change
 * @author YewOnn
 *
 */
public class UIFunction {
	private static boolean isDone; 

	public static String inputFileName = "edited.txt";
	public static String outputFileName = "edited.txt";

	public static void done(){//function 9; 
		isDone = true; 
		PanelDisplay.donePanel(); 
		/**Scanner sc = new Scanner(System.in); 
		System.out.print("enter output file name:");
		String name = sc.nextLine(); 
		if(!name.contains(".txt")){
			name = name + ".txt";
		}*/
		String name = UIFunction.outputFileName;
		GeoJsonReader.outTxt(name, GeoJsonList.jsonList);
		System.out.println("Done");
		System.out.println("Output a file called: "+name);
	}

	public static void addVertex(){ //function 10; Listener
		//include instruction to select panel; 
		PanelDisplay.addVertexPanel(); 
		Point point = Display.listener(200);
		double innerRadius = 5; 
		double outerRadius = 10; 
		StdDraw.setPenColor(StdDraw.YELLOW);
		Display.showPoint(point, innerRadius, outerRadius);
		PanelDisplay.isSatisfied(9, 10);
		int function = -1; 
		UserInterface.setDisplayColor(); 
		UserInterface.colorHorizontalOne(1, point.toStringCoordinate());
		while(function != 9 && function != 10 && function != 13){
			function = UserInterface.functionListener(); 
		}
		if(function == 9){ //user is satisfied. 
			MoreFunction.getVertexDetailFromUser(point); 
		}
		if(function == 10){
			addVertex(); 
		}
		if(function == 13){
			returnToOrigin(); 
		}
	}

	public static void selectVertex(){ //function 11; Listener
		PanelDisplay.selectVertexPanel(); 
		Point vertex = null;
		int vertexIndex = -1;
		vertex = Display.listener(300);
		vertexIndex = GeoJsonList.getClosestPointIndex(vertex);
		StdDraw.setPenColor(StdDraw.YELLOW);
		PointEdgeUI.showVertex(vertexIndex, 5, 10);
		MoreFunction.selectVertex(vertexIndex);
	}

	public static void deleteVertex(int vertexIndex){ //function 12, from 11
		//ask user to confirm; 
		PanelDisplay.refreshPanel(); 
		PanelDisplay.updateVertexPanel(vertexIndex);
		UserInterface.setDisplayColor(); 
		UserInterface.colorVertical(12, "Are You/Sure?");
		UserInterface.colorHorizontalTwo(1, "all edges related will be deleted as well");
		UserInterface.setClickColor(); 
		UserInterface.colorVertical(13, "yes");
		UserInterface.colorVertical(14, "no");	
		int function = -1; 
		while(function != 13 && function != 14){
			function = UserInterface.functionListener(); 
		}
		if(function == 13){
			MoreFunction.deleteVertex(vertexIndex); 
		}
		if(function == 14){
			MoreFunction.selectVertex(vertexIndex);
			//UIFunction.returnToOrigin(); 
		}
	}
	//13
	public static void returnToOrigin(){ //function 13, from everywhere
		//do nothing, it will return to origin. 
		//while loop will refresh the page; 
	}

	//14
	public static void editVertex(int vertexIndex){ //function 14, from 11, to 17 & 18
		//check if this vertex has any edge; 
		boolean hasEdge = true; 
		int numEdge = GeoJsonList.get(vertexIndex).getEdges().size(); 
		if(numEdge < 1){
			hasEdge = false; 
		}

		PanelDisplay.editVertexPanel(vertexIndex, hasEdge);
		int function = -1; 
		while(function != 13 && function != 17 && (function != 18 || !hasEdge)){
			function = UserInterface.functionListener(); 
		}
		if(function == 13){ //my favourite
			returnToOrigin(); 
		}
		if(function == 17){
			//UserInterface.UIreset(); 
			//PanelDisplay.updateVertexPanel(vertexIndex);
			addEdge(vertexIndex); 
		}
		if(function == 18){
			selectEdge(vertexIndex); 
		}
	}

	public static void editVertexName(int vertexIndex){ //function 15, from 11
		PanelDisplay.editVertexNamePanel(vertexIndex, false);
		int function = -1; 
		while(function != 15 && function != 13){
			function = UserInterface.functionListener(); 
		}
		if(function == 13){
			returnToOrigin(); 
		}
		if(function == 15){
			PanelDisplay.editVertexNamePanel(vertexIndex, true);
			MoreFunction.editVertexName(vertexIndex);  //getNameFromUser; 
		}
	}

	/** 
	 * Function 16, reach from function selectVertex, function 11; 
	 * @param vertexIndex
	 */
	public static void editVertexLocation(int vertexIndex){
		PanelDisplay.editVertexLocationPanel(vertexIndex, false);
		int function = -1; 
		while(function != 16 && function != 13){
			function = UserInterface.functionListener(); 
		}
		if(function == 13){
			returnToOrigin(); 
		}
		if(function == 16){
			MoreFunction.editVertexLocation(vertexIndex);
		}
	}

	public static void addEdge(int vertexIndex){ //funciton 17, from 14
		PanelDisplay.addEdgePanel(vertexIndex);
		//let the user choose the edge here; 
		Point point = Display.listener(200);
		int edgeIndex = GeoJsonList.getClosestPointIndex(point);
		Point edgePoint = GeoJsonList.getPoint(edgeIndex);
		StdDraw.setPenColor(StdDraw.RED);
		Display.showPoint(edgePoint, 5, 10);
		PanelDisplay.isSatisfied(17, 18);
		int function = -1; 
		while(function != 17 && function != 18 && function != 13){
			function = UserInterface.functionListener();
		}
		if(function == 17){
			if(GeoJsonList.get(vertexIndex).hasEdge(edgeIndex)){
				UserInterface.setBrightColor(); 
				UserInterface.colorHorizontalTwo(1, "Edge already exists");
				UserInterface.setClickColor(); 
				UserInterface.colorVertical(17, "17. ClickHere/SelectAgain");
				UserInterface.colorVertical(13, null);
				int currFunction = -1; 
				while(currFunction != 13 && currFunction != 17){
					currFunction = UserInterface.functionListener(); 
				}
				if(currFunction == 13){ //user abandone ship
					returnToOrigin(); 
				}
				if(currFunction == 17){ //user want to repeat again
					addEdge(vertexIndex);
				}
			}else{
				//add this edge inside now. 
				Edge edge = MoreFunction.addEdge(vertexIndex, edgeIndex);
			}
		}

		if(function == 18){
			//check if that vertex is already an edge; 
			addEdge(vertexIndex);
		}
		if(function == 13){
			returnToOrigin(); 
		}
	}

	//function 18; 
	public static void selectEdge(int vertexIndex){
		PanelDisplay.selectEdgePanel(vertexIndex); 
		Point point = Display.listener(200);
		Edge edge = MoreFunction.getNearestEdge(point, vertexIndex);
		System.out.println("edge.Name = "+edge.name);
		int edgeIndex = GeoJsonList.getVertexIndexByName(edge.name);
		int function = -1; 
		Point edgePoint = GeoJsonList.getPoint(edgeIndex);
		StdDraw.setPenColor(StdDraw.RED);
		Display.showPoint(edgePoint, 10, 15);
		PanelDisplay.isSatisfied(17, 18);
		PanelDisplay.updateEdgePanel(edge);

		while(function != 13 && function != 17 && function != 18){
			function = UserInterface.functionListener();
		}
		if(function == 13){
			returnToOrigin(); 
		}
		if(function == 17){ //satisfied; 
			Edge otherEdge = MoreFunction.getEdgeByName(edgeIndex, GeoJsonList.getName(vertexIndex)); 
			MoreFunction.selectEdge(vertexIndex, edgeIndex, edge, otherEdge);
		}
		if(function == 18){ //not satisfied; 
			selectEdge(vertexIndex); 
		}
	}

	//function 19
	public static void deleteEdge(int sourceIndex, int destIndex, Edge edge, Edge otherEdge){
		PanelDisplay.deleteEdgePanel(sourceIndex, destIndex, edge); 

		int function = -1; 
		while(function != 18 && function != 19){
			System.out.println("Wrong function");
			function = UserInterface.functionListener(); 
		}
		if(function == 18){ //confirmDelete
			//proceed to below; 
			//ignore edge; 
			//just delete, don't fucking ask; 
			ArrayList<Edge> sourceEdges = GeoJsonList.get(sourceIndex).getEdges(); 
			ArrayList<Edge> destEdges = GeoJsonList.get(destIndex).getEdges(); 
			for(int i = 0; i < sourceEdges.size(); i++){
				Edge edge2 = sourceEdges.get(i);
				if(edge2.name.equals(GeoJsonList.getName(destIndex))){
					GeoJsonList.get(sourceIndex).properties.edgeList.remove(i);
					i = destEdges.size() + 1;  //exit the for loop
				}
			}

			for(int i = 0; i < destEdges.size(); i++){
				Edge edge2 = destEdges.get(i);
				if(edge2.name.equals(GeoJsonList.getName(sourceIndex))){
					GeoJsonList.get(destIndex).properties.edgeList.remove(i);
					i = destEdges.size() + 1; //exit the for loop
				}
			}
		}
		if(function == 19){ //abortDelete
			UserInterface.setBrightColor(); 
			UserInterface.colorVertical(18, "deleteAborted/ChooseAgain");
			MoreFunction.selectEdge(sourceIndex, destIndex, edge, otherEdge); 
			//UIFunction.deleteEdge(sourceIndex, destIndex, edge);
		}

	}

	//function 20 reach from selectEdge
	public static void editSmoothRoute(int sourceIndex, int destIndex, Edge edge, Edge otherEdge){
		int function = -1; 
		boolean noRoute = true; 
		if(edge.smoothRoute.size() > 0){
			noRoute = false; 
		}

		PanelDisplay.editSmoothRoutePanel(sourceIndex, destIndex, edge, otherEdge, noRoute); 
		while(function != 13 && function != 14 && (function != 15 || noRoute) && 
				(function != 16 || noRoute) && function != 17){
			System.out.println("Wrong function");
			function = UserInterface.functionListener(); 
		}
		if(function == 13){ //confirmDelete
			returnToOrigin(); 
		}
		if(function == 14){//create the array of point here. 
			MoreFunction.smoothRouteAddPoint(sourceIndex, destIndex, edge, otherEdge);
		}
		if(function == 15){ // move point //disable if not possible
			MoreFunction.smoothRouteMovePoint(sourceIndex, destIndex, edge, otherEdge);
		}
		if(function == 16){ //deletePoint //disable if not possible
			MoreFunction.smoothRouteDeletePoint(sourceIndex, destIndex, edge, otherEdge);
		}
		if(function == 17){ //return to "selectEdge
			MoreFunction.selectEdge(sourceIndex, destIndex, edge, otherEdge);
		}

	}



	/**
	 * includes Everything. This function has a listener. 
	 * @param imageFile
	 * @param geoJsonFile
	 */
	public static void run(String geoJsonFile, String imageFile){
		// initialiseUI makes everything complete
		UserInterface.initialiseUI(geoJsonFile, imageFile);

		while(!isDone){
			//Color the three possible function; 
			UserInterface.UIreset(); 
			PanelDisplay.homePagePanel(); 
			int function = UserInterface.functionListener(); 	
			while(function!= 9 && function != 10 && function != 11 && function != 12 && function != 13){
				function = UserInterface.functionListener(); 
			}

			if(function == 9){ //User want to output .txt file; 				
				done(); 
			}

			if(function == 10){  //User want to output .txt File; 
				addVertex(); 
			}

			if(function == 11){ //User want to select vertex; 
				selectVertex(); 
			}
			if(function == 12){
				//please select a point; 
				PanelDisplay.refreshPanel(); 
				UserInterface.setBrightColor(); 
				UserInterface.colorHorizontalOne(1, "Select A Point");
				Point point = Display.listener(100, false);
				Scaler.resize(Scaler.resize*1.5, point);
			}
			if(function == 13){
				PanelDisplay.refreshPanel(); 
				UserInterface.setBrightColor(); 
				UserInterface.colorHorizontalOne(1, "Select A Point");
				Point point = Display.listener(100, false);
				Scaler.resize(Scaler.resize*(1/1.5), point);
			}
		}
	}

	public static void main (String[] args){
		String geoJsonFile = "abc.txt";
		outputFileName = "abc.txt";
		//String geoJsonFile = UIFunction.outputFileName;
		String imageFile = "FoeSmall.png";
		run(geoJsonFile, imageFile);
	}
}
