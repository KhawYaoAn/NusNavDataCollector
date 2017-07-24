package edgesUI;

import java.awt.Color;

public class UserInterface {
	private static final double BOXWIDTH = 0.15;
	private static final double TOP_BORDER = 0.04;
	private static final double BORDER_PEN_RADIUS = 0.004; 

	//After Scaler.prepareCanvas //Never Change
	private static String imageFile; 
	private static int frameWidth; 
	private static int frameHeight; 

	//The Boxes UI; 
	//functionStorage() to initialise this. 
	static String[] verticalDisplay; 
	static String[] horizontalDisplayOne; 
	static String[] horizontalDisplayTwo; 
	static double verticalBoxWidth; 
	static double verticalBoxHeight; 

	//Changes as per user's happiness; 
	private static Color fillColor = StdDraw.BLACK; 
	private static Color outlineColor = StdDraw.RED; 
	private static Color wordColor = StdDraw.WHITE; 

	
	public static void setDefaultColor(){
		fillColor = StdDraw.BLACK; 
		outlineColor = StdDraw.RED; 
		wordColor = StdDraw.WHITE; 
		StdDraw.setPenColor(StdDraw.BLACK); 
		StdDraw.setPenRadius();
	}
	
	public static void setClickColor(){
		fillColor = StdDraw.GREEN; 
		outlineColor = StdDraw.RED; 
		wordColor = StdDraw.BLACK; 
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius();
	}
	
	/**
	 * Use only for the most important panel, such as 
	 * a message panel. 
	 */
	public static void setBrightColor(){
		fillColor = StdDraw.YELLOW; 
		outlineColor = StdDraw.RED; 
		wordColor = StdDraw.BLACK; 
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius();
	}

	/**
	 * Relevant Information but not clickable
	 */
	public static void setDisplayColor(){
		fillColor = StdDraw.WHITE; 
		outlineColor = StdDraw.RED; 
		wordColor = StdDraw.BLACK; 
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius();
	}
	
	/**
	 * i = 0 for the topMost box; 
	 * @param i
	 */
	private static void colorBox(String word, double xMin, double yMin, 
			double xMax, double yMax){		

		//color the box; 
		double[] x = {xMax, xMax, xMin, xMin}; 
		double[] y = {yMax, yMin, yMin, yMax}; 
		StdDraw.setPenColor(fillColor); 
		StdDraw.filledPolygon(x, y);

		//draw the outline; 
		StdDraw.setPenColor(outlineColor);
		StdDraw.setPenRadius(BORDER_PEN_RADIUS);
		StdDraw.line(xMin, yMin, xMax, yMin);
		StdDraw.line(xMin, yMax, xMin, yMin);
		StdDraw.line(xMin, yMax, xMax, yMax);
		StdDraw.line(xMax, yMax, xMax, yMin);
		StdDraw.setPenRadius(); 
		//insert the words
		StdDraw.setPenColor(wordColor);
		String[] wordArray = word.split("/");
		for(int j = 0; j < wordArray.length; j++){
			double yCurr = yMax - 10 - 17* j ; 
			StdDraw.text(xMin/2 + xMax/2, yCurr, wordArray[j]);
		}
		StdDraw.setPenColor(StdDraw.BLACK); 
	}

	protected static void colorVertical(int i, String word){
		if(word == null){
			word = verticalDisplay[i];
		}
		verticalBoxHeight = (double)frameHeight/ (double)verticalDisplay.length; 
		verticalBoxWidth = (double)frameWidth* BOXWIDTH; 
		double xMin = frameWidth - verticalBoxWidth; 
		double xMax = frameWidth; 
		double yMax = frameHeight - i*verticalBoxHeight; 
		double yMin = frameHeight - (i+1)*verticalBoxHeight; 

		//color the box; 
		colorBox(word, xMin, yMin, xMax, yMax); 
	}

	protected static void colorHorizontalOne(int i, String word){
		if(word == null){
			word = horizontalDisplayOne[i];
		}
		double xMin = i*(frameWidth - frameWidth*BOXWIDTH)/horizontalDisplayOne.length; 
		double xMax = (i+1)*(frameWidth - frameWidth*BOXWIDTH)/horizontalDisplayOne.length; 
		double yMin = frameHeight - TOP_BORDER*frameHeight; 
		double yMax = frameHeight;
		colorBox(word, xMin, yMin, xMax, yMax);
	}

	static void colorHorizontalTwo(int i, String word){
		if(word == null){
			word = horizontalDisplayTwo[i];
		}
		double xMin = i*(frameWidth - frameWidth*BOXWIDTH)/horizontalDisplayTwo.length; 
		double xMax = (i+1)*(frameWidth - frameWidth*BOXWIDTH)/horizontalDisplayTwo.length; 
		double yMin = frameHeight - TOP_BORDER*2*frameHeight; 
		double yMax = frameHeight - TOP_BORDER*frameHeight;
		colorBox(word, xMin, yMin, xMax, yMax);
	}

	static void createVertical(){
		for(int i = 0; i < verticalDisplay.length; i++){
			colorVertical(i, verticalDisplay[i]);
		}
	}

	static void createHorizontalOne(){
		for(int i = 0; i < horizontalDisplayOne.length; i++){
			colorHorizontalOne(i, horizontalDisplayOne[i]);
		}
	}

	static void createHorizontalTwo(){
		for(int i = 0; i < horizontalDisplayTwo.length; i++){
			colorHorizontalTwo(i, horizontalDisplayTwo[i]);
		}
	}

	static void highlightBox(int[] boxIndex){
		for(int i = 0; i < boxIndex.length; i++){
			int index = boxIndex[i];
			if(index < verticalDisplay.length){
				colorVertical(index, null);
			}else if(index < verticalDisplay.length + horizontalDisplayOne.length){
				colorHorizontalOne(index - verticalDisplay.length, null);
			}else if(index < verticalDisplay.length + horizontalDisplayOne.length+horizontalDisplayTwo.length){
				colorHorizontalTwo(index - verticalDisplay.length - horizontalDisplayOne.length, null);
			}else{
				System.out.println("Error: UserInterface, line 138");; 
			}	
		}
	}
	
	/**
	 * identify the function clicked by the user. 
	 * @return -1 if no function is being selected. return function number
	 * with 0 being the topmost. 
	 */
	public static int functionListener(){
		Point point = Display.listener(500, false);
		double x = point.getLong(); 
		double y = point.getLat();
		int functionNumber = -1; 
		if(x > frameWidth - frameWidth*BOXWIDTH){
			if(x > frameWidth){
				System.out.println("qwe Error: out of bound listener failed: x = "+x+
						" > frameWidth = "+frameWidth);
				return -100; 
			}else{
				double value = y/verticalBoxHeight; 
				functionNumber = verticalDisplay.length - 1 - (int)(value); 
				//System.out.println("functionNumber = "+functionNumber);
			}
		}else{
			if(y > frameHeight - frameHeight*TOP_BORDER*2){
				if(y < frameHeight - frameHeight*TOP_BORDER){//secondLine
					double horizontalTwoWidth = (frameWidth - 
							frameWidth*BOXWIDTH)/horizontalDisplayTwo.length;
					double value = y/horizontalTwoWidth;
					functionNumber = verticalDisplay.length + horizontalDisplayOne.length + (int)(value); 
				} else {
					double horizontalOneWidth = (frameWidth - 
							frameWidth*BOXWIDTH)/horizontalDisplayOne.length;
					double value = y/horizontalOneWidth;
					functionNumber = verticalDisplay.length + (int)(value);
				}
			}else{
				System.out.println("Error: public static int FunctionListener()");
				System.out.println("No function selected"); 
				functionNumber = -1; 
			}
		}


		if(functionNumber < 0){
			System.out.println("no function chosen, do again"); 
			return functionListener(); 
		}
		return functionNumber; 
	}

	public static void restartEdgePanel(){
		UserInterface.verticalDisplay[0] = "0.name:/NA";
		UserInterface.verticalDisplay[1] = "1.time:/NA";
		UserInterface.verticalDisplay[2] = "2.distance:/NA";
		UserInterface.verticalDisplay[3] = "3.stairsDirection:/NA";
		UserInterface.verticalDisplay[4] = "4.stairsWeight:/NA";
		UserInterface.verticalDisplay[5] = "5.shelterWeight:/NA";
		UserInterface.verticalDisplay[6] = "6.shelterMultiplier:/NA";
		UserInterface.verticalDisplay[7] = "7.directions:/NA";
		UserInterface.verticalDisplay[8] = "8.ImageLink:/NA";
		UserInterface.verticalDisplay[9] = "9.Done/ouput.txt";
	}
	
	private static void functionStorage(){
		UserInterface.verticalDisplay = new String[21];
		UserInterface.verticalDisplay[0] = "0.name:/NA";
		UserInterface.verticalDisplay[1] = "1.time:/NA";
		UserInterface.verticalDisplay[2] = "2.distance:/NA";
		UserInterface.verticalDisplay[3] = "3.stairsDirection:/NA";
		UserInterface.verticalDisplay[4] = "4.stairsWeight:/NA";
		UserInterface.verticalDisplay[5] = "5.shelterWeight:/NA";
		UserInterface.verticalDisplay[6] = "6.shelterMultiplier:/NA";
		UserInterface.verticalDisplay[7] = "7.directions:/NA";
		UserInterface.verticalDisplay[8] = "8.ImageLink:/NA";
		UserInterface.verticalDisplay[9] = "9.Done/ouput.txt";
		UserInterface.verticalDisplay[10] = "10.Add/Vertex";
		UserInterface.verticalDisplay[11] = "11.Select/Vertex";
		UserInterface.verticalDisplay[12] = "12.Delete/Vertex";
		UserInterface.verticalDisplay[13] = "13.Abort/BackToOrigin";
		UserInterface.verticalDisplay[14] = "14.Edit/Vertex";
		UserInterface.verticalDisplay[15] = "15.Edit/VertexName";
		UserInterface.verticalDisplay[16] = "16.Edit/VertexLocation";		
		UserInterface.verticalDisplay[17] = "17.Add/Edge";
		UserInterface.verticalDisplay[18] = "18.Select/Edge";
		UserInterface.verticalDisplay[19] = "19.Delete/Edge";
		UserInterface.verticalDisplay[20] = "20.Edit/Edge";

		UserInterface.horizontalDisplayOne = new String[2];
		UserInterface.horizontalDisplayOne[0] = "0.Current Vertex = Name";
		UserInterface.horizontalDisplayOne[1] = "1.Coordinate = someshit, someshit2";

		UserInterface.horizontalDisplayTwo = new String[2];
		UserInterface.horizontalDisplayTwo[0] = "2.EdgeList: shit1, shit2, shit3, shit4";
		UserInterface.horizontalDisplayTwo[1] = "2.Yellow When Done";

	}
	
	/**
	 * Reset everything to original state
	 * Panel that display's specific instance's info will show giberish
	 * All vertex and edge will be drawn
	 */
	public static void UIreset(){
		functionStorage(); 
		Scaler.prepareCanvass(imageFile, false);
		PanelDisplay.refreshPanel(); 
		PointEdgeUI.displayWeb(5);
		setBrightColor(); 
		colorHorizontalTwo(1, null);
		setDefaultColor(); 
	}

	/**
	 * Create the UI, with the function box. 
	 * @param functionName
	 * @param imageFile
	 */
	public static void initialiseUI(String geoJsonFile, String imageFile){
		GeoJsonList.initialise(geoJsonFile); 
		UserInterface.imageFile = imageFile; 
		Scaler.prepareCanvass(imageFile, true);
		frameHeight = StdDraw.height; 
		frameWidth = StdDraw.width; 
		verticalBoxWidth = frameWidth * BOXWIDTH; 
		verticalBoxHeight = frameHeight * TOP_BORDER; 
		//reset();
	}
	
	public static void run(String geoJsonFile, String imageFile){
		initialiseUI(geoJsonFile, imageFile);
		UIreset(); 
	}
	
	public void run(){
		String geoJsonFile = "getJson.txt";
		String imageFile = "FoeSmall.png";
		run(geoJsonFile, imageFile);
	}

}
