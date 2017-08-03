package edgesUI;

public class PanelDisplay {

	public static void refreshPanel(){
		UserInterface.setDefaultColor(); 
		UserInterface.createHorizontalOne(); 
		UserInterface.createHorizontalTwo(); 
		UserInterface.createVertical(); 
	}

	public static void resetVertical(){
		UserInterface.setDefaultColor();
		UserInterface.createVertical(); 
	}

	public static void homePagePanel(){
		int[] numList = {9,10,11};		
		UserInterface.setClickColor();
		UserInterface.highlightBox(numList);
		UserInterface.colorVertical(12, "Zoom in");
		UserInterface.colorVertical(13, "Zoom out");
	}

	public static void donePanel(){
		refreshPanel(); 
		UserInterface.setBrightColor(); 
		UserInterface.colorHorizontalTwo(1, "outputFile called :"+UIFunction.outputFileName);
		UserInterface.colorVertical(9, "DONE!/GOHOME!");
		//UserInterface.colorHorizontalTwo(1, "Please enter file name at CMD");
	}

	//Function 10, include refresh
	public static void addVertexPanel(){
		refreshPanel(); 
		UserInterface.setBrightColor(); 
		UserInterface.colorVertical(10, "choose/location");
	}
	//Function 10. remember to trim the name. 
	public static void addVertexDetail(String coordinates, String name, String edgeList){
		UserInterface.setDisplayColor(); 
		UserInterface.colorHorizontalOne(1, coordinates);
		if(name!= null){
			UserInterface.setDisplayColor(); 
			UserInterface.colorHorizontalOne(0, name);
		}else{
			UserInterface.setBrightColor(); 
			UserInterface.colorHorizontalOne(0, "Please Enter Name at Command Prompt");
		}
	}
	//function 11, vertex not chosen. 
	public static void selectVertexPanel(){
		refreshPanel(); 
		UserInterface.setBrightColor(); 
		UserInterface.colorVertical(11, "Please/SelectVertex");
	}
	//function 11 part b, vertex chosen; called from MoreFunction.selectVertex();
	public static void selectVertexPanel(int vertexIndex){
		refreshPanel(); 
		PointEdgeUI.displayVertex(vertexIndex);
		updateVertexPanel(vertexIndex); 

		UserInterface.setClickColor(); 
		UserInterface.colorVertical(12, null);
		UserInterface.colorVertical(13, null);
		UserInterface.colorVertical(14, null);
		UserInterface.colorVertical(15, null);
		UserInterface.colorVertical(16, null);
		UserInterface.colorVertical(17, null);
		UserInterface.colorVertical(18, null);
	}

	public static void editVertexPanel(int vertexIndex, boolean hasEdge){
		//UserInterface.UIreset(); 
		refreshPanel(); 
		updateVertexPanel(vertexIndex);
		PointEdgeUI.displayVertex(vertexIndex);
		UserInterface.setClickColor();
		UserInterface.colorVertical(13, null);
		UserInterface.colorVertical(17, null);
		if(hasEdge){
			UserInterface.colorVertical(18, null);
		}
	}

	//function15 UI, only got one
	public static void editVertexNamePanel(int vertexIndex, boolean isConfirmed){
		updateVertexPanel(vertexIndex);
		if(isConfirmed){
			UserInterface.setBrightColor(); 
			UserInterface.colorHorizontalTwo(1, "Go to CMD to type new Name");
		}
		else{
			UserInterface.setClickColor();
			UserInterface.colorVertical(15,"Proceed/To Edit");
			UserInterface.colorVertical(13, null);
		}
	}

	//function 16
	public static void editVertexLocationPanel(int vertexIndex, boolean isConfirmed){
		if(isConfirmed){
			updateVertexPanel(vertexIndex);
			UserInterface.setBrightColor(); 
			UserInterface.colorHorizontalTwo(1, "Please Select New Location");
		}
		else{
			updateVertexPanel(vertexIndex);
			UserInterface.setClickColor();
			UserInterface.colorVertical(16,"Proceed/To Edit");
			UserInterface.colorVertical(13, null);
		}
	}

	public static void addEdgePanel(int vertexIndex){
		refreshPanel(); 
		updateVertexPanel(vertexIndex);
		UserInterface.setBrightColor();
		UserInterface.colorHorizontalTwo(1, "Select A Vertex TO Connect");
	}

	//function 18
	public static void selectEdgePanel(int vertexIndex){
		restartEdgePanel(); 
		refreshPanel(); 
		UserInterface.setBrightColor(); 
		UserInterface.colorHorizontalTwo(1, "Select an edge");
	}

	//pre function 19
	public static void deleteEdgePanel(int vertexIndex, int edgeIndex, Edge edge){
		PanelDisplay.resetVertical(); 
		PanelDisplay.updateEdgePanel(edge); 
		UserInterface.setClickColor(); 
		UserInterface.colorVertical(18, "18.confirm/delete?");
		UserInterface.colorVertical(19, "19.abort/delete?");
	}

	public static void selectEdgePanel(int vertexIndex, int edgeIndex, Edge edge){
		//UserInterface.UIreset(); 
		System.out.println("PanelDisplay.selectEdgePanel");
		refreshPanel(); 
		updateEdgePanel(edge);
		updateVertexPanel(vertexIndex);
		Point source = GeoJsonList.getPoint(vertexIndex);
		Point destination = GeoJsonList.getPoint(edgeIndex);
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.setPenRadius(0.008); 
		if(edge.smoothRoute.size() == 0){
			Display.drawLine(source, destination);
		}else{
			PointEdgeUI.showRoute(vertexIndex, edgeIndex, 3);
		}
		UserInterface.setDisplayColor(); 
		//UserInterface.colorVertical(15, "destination/"+GeoJsonList.getName(edgeIndex));
		//UserInterface.colorVertical(17, "source/"+GeoJsonList.getName(vertexIndex));
		//UserInterface.colorVertical(18, "destination/"+edge.name);
		UserInterface.setClickColor();
		UserInterface.colorVertical(3, null);
		UserInterface.colorVertical(6, null);
		UserInterface.colorVertical(13, null); 
		UserInterface.colorVertical(17, null);
		UserInterface.colorVertical(18, "returnTo/selectEdge");
		UserInterface.colorVertical(19, null);
		UserInterface.colorVertical(20, null);
		System.out.println("PanelDisplay.selectEdgePanel.end");
	}

	//function 20 display. 
	public static void editSmoothRoutePanel(int sourceIndex, int destIndex, Edge edge, Edge otherEdge,
			boolean noRoute){
		refreshPanel(); 
		updateEdgePanel(edge);
		updateVertexPanel(sourceIndex);
		UserInterface.setClickColor();
		UserInterface.colorVertical(13, null); 
		UserInterface.colorVertical(14, "14.Add/Point");//click a point to add after
		UserInterface.colorVertical(17, "17.return/toEdge");
		if(!noRoute){
			UserInterface.colorVertical(16, "16.delete/Point");//ask user to select a point to delete;
			UserInterface.colorVertical(15, "15.Move/Point");//ask user to select point to move
		}
	}


	public static void addEdgeDetail(Edge edge, int vertexIndex, int otherIndex){
		//add detail for display
		refreshPanel(); 
		updateVertexPanel(vertexIndex);
		updateEdgePanel(edge);
		//draw the point
		StdDraw.setPenColor(StdDraw.RED);
		Point point = GeoJsonList.getPoint(vertexIndex); 
		Point edgeVertex = GeoJsonList.getPoint(otherIndex);
		//Display.showPoint(edgeVertex, 5, 10);
		StdDraw.setPenRadius(0.005);
		Display.drawLine(point, edgeVertex);
		StdDraw.setPenRadius(); 
		//userClickOption	
		UserInterface.setClickColor();
		UserInterface.colorVertical(3, null); //to edit stairsDirection
		UserInterface.colorVertical(6, null); //to edit shelterMultiplier
		UserInterface.colorVertical(13, null); 
		UserInterface.colorVertical(17, "Confirm/AddEdge");
	}

	public static void isSatisfied(int satisfied, int notSatisfied){
		refreshPanel(); 
		UserInterface.setClickColor(); 
		UserInterface.colorVertical(13, null); 
		UserInterface.colorVertical(satisfied, "satisfied?");
		UserInterface.colorVertical(notSatisfied, "notsatisfied?");
	}

	/**
	 * Update all information related to the vertex whose index within
	 * GeoJsonList.jsonList == vertexIndex;
	 * Do not refresh entire panel 
	 * @param vertexIndex
	 */
	public static void updateVertexPanel(int vertexIndex){
		//find information about that vertex, and update every related field; 
		//Name
		String name = GeoJsonList.get(vertexIndex).getName(); 
		UserInterface.horizontalDisplayOne[0] = "Current Vertex = " +name; 
		//GPS Location
		String coordinates = GeoJsonList.get(vertexIndex).getCoordinates();
		UserInterface.horizontalDisplayOne[1] = "Coordinate = "+coordinates;
		//List of edges
		String edgeList = GeoJsonList.get(vertexIndex).getEdgeString(); 
		UserInterface.horizontalDisplayTwo[0] = "EdgeList: "+edgeList;

		//refreshPanel(); 
		UserInterface.setDisplayColor(); 
		UserInterface.colorHorizontalOne(0, null);
		UserInterface.colorHorizontalOne(1, null);
		UserInterface.colorHorizontalTwo(0, null);
		//int ySize = UserInterface.verticalDisplay.length; 
		//int x1Size = UserInterface.horizontalDisplayOne.length; 
		//int[] panels = {ySize, ySize + 1, ySize + x1Size};
		//UserInterface.highlightBox(panels);

		//draw the web; 
		/**StdDraw.setPenRadius(0.006);
		StdDraw.setPenColor(StdDraw.BLUE);
		PointEdgeUI.displayEdges(vertexIndex, 10);
		StdDraw.setPenRadius(); 
		StdDraw.setPenColor(); */
	}

	public static void updateEdgePanel(Edge edge){
		edge.update(true);
		edge.setList(); 
		for(int i = 0; i < 9; i++){
			UserInterface.verticalDisplay[i] = UserInterface.verticalDisplay[i].split("/")[0] +"/"+edge.get(i);
			//updateUI; 
			UserInterface.setDisplayColor(); 
			UserInterface.colorVertical(i, null); 
		}
		if(edge.origin != null){
			UserInterface.verticalDisplay[9] = "9.origin/"+edge.origin;
		}
	}

	public static void restartEdgePanel(){
		UserInterface.restartEdgePanel(); 
	}


}
