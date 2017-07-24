package edgesUI;

import java.util.ArrayList;

/**
 * This class only draws the point and edges. 
 * No listener function
 * No panel update
 * @author YewOnn
 *
 */
public class PointEdgeUI {

	/**
	 * Display the vertex with name.equals(vertexName)
	 */
	public static void showVertex(String vertexName, double innerRadius, double outerRadius){
		int vertexIndex = GeoJsonList.getVertexIndexByName(vertexName);
		showVertex(vertexIndex, innerRadius, outerRadius);
	}

	/**
	 * Display the vertex which index in arrayList == vertexIndex
	 */
	public static void showVertex(int vertexIndex, double innerRadius, double outerRadius){
		Point point = GeoJsonList.get(vertexIndex).getPoint(); 
		Display.showPoint(point, innerRadius, outerRadius);
	}

	/**
	 * Display all the edges connected to vertex specified by vertexIndex. 
	 * @param vertexIndex
	 * @param radius
	 */
	public static void displayEdges(int vertexIndex, double radius){
		GeoJson currGeoJson = GeoJsonList.get(vertexIndex);
		Point currVertex = currGeoJson.getPoint();

		if(radius > 0){
			Display.showTarget(currVertex, radius);
		}

		//displayEdges(i); 
		ArrayList<Edge> edgeList = currGeoJson.getEdges(); 
		for(int j = 0; j < edgeList.size(); j++){
			int index = GeoJsonList.getVertexIndexByName(edgeList.get(j).getName()); 
			Point edgeVertex = GeoJsonList.get(index).getPoint(); 
			Display.drawLine(currVertex, edgeVertex);
			if(radius > 0){
				Display.showTarget(edgeVertex, radius);
			}
		}
	}

	public static void displayEdges(String vertexName, double radius){
		int vertexIndex = GeoJsonList.getVertexIndexByName(vertexName);
		displayEdges(vertexIndex, radius);
	}
	
	public static void displayVertex(int vertexIndex){
		StdDraw.setPenColor(StdDraw.BLUE); 
		PointEdgeUI.showVertex(vertexIndex, 5, 10);
		PointEdgeUI.displayEdges(vertexIndex, 10);
	}

	/**
	 * Display all vertex and edges. 
	 * @param radius
	 */
	public static void displayWeb(double radius){
		for(int i = 0; i < GeoJsonList.jsonList.size(); i++){
			GeoJson currGeoJson = GeoJsonList.jsonList.get(i); 
			Point currVertex = currGeoJson.getPoint();

			//showPoint
			System.out.println("i = "+i); 
			System.out.println("currGeoJson.getName() = "+currGeoJson.getName());
			System.out.println("currVertex.getLong() = "+currVertex.getLong());
			System.out.println("currVertex.getLat() = "+currVertex.getLat()); 
			Display.showTarget(currVertex, radius);

			//display all edges; 0 because vertex not redrawn.
			PointEdgeUI.displayEdges(i, 0);
		}
	}
}
