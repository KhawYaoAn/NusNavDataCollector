package edgesUI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Display { 
	public Display(){

	}

	public static void showPoint(Point point, double innerRadius, double outerRadius){
		//10 is an arbitrary value; 
		Point scaledPoint = Scaler.scaleToDisplay(point, true);
		double x = scaledPoint.getLong(); 
		double y = scaledPoint.getLat(); 
		for(int i = (int)innerRadius; i <= outerRadius; i++){

			StdDraw.circle(x, y, i); 
		}
	}

	public static void showTarget(Point point, double outerRadius){
		//10 is an arbitrary value; 
		Point scaledPoint = Scaler.scaleToDisplay(point, true);
		double x = scaledPoint.getLong(); 
		double y = scaledPoint.getLat(); 
		StdDraw.circle(x, y, outerRadius); 
		StdDraw.line(x - outerRadius, y, x + outerRadius, y);
		StdDraw.line(x, y + outerRadius, x, y - outerRadius);
	}

	public static void showPoint(double longitude, double latitude, double innerRadius, 
			double outerRadius){
		showPoint(new Point(longitude, latitude), innerRadius, outerRadius);
	}

	public static void showRoute(String gpsFile){
		int count = 0; 
		try{
			FileReader f = new FileReader(gpsFile); 
			BufferedReader buff = new BufferedReader(f); 
			String line = buff.readLine(); 
			System.out.println("how");
			Point pointOne = null;
			Point pointTwo = null; 
			if(line.split(" ")[1].equals("Bus")){
				System.out.println("Bus Service: "+line.split(" ")[3]); 
				System.out.println("Comment: "+buff.readLine()); 
				line = buff.readLine(); 
				String lineTwo = buff.readLine(); 
				System.out.println();
				while(line != null && lineTwo != null){
					double yOne = Double.parseDouble(line.split(",")[0]);
					double xOne = Double.parseDouble(line.split(",")[1]);
					double yTwo = Double.parseDouble(lineTwo.split(",")[0]);
					double xTwo = Double.parseDouble(lineTwo.split(",")[1]);
					Point one = new Point(xOne, yOne);
					Point two = new Point(xTwo, yTwo);
					Display.drawLine(one, two);
					/*Point three = Scaler.scaleToDisplay(one);
					Point four = Scaler.scaleToDisplay(two); 
					StdDraw.circle(three.getLong(), three.getLat(), 5);
					StdDraw.line(three.getLong(), three.getLat(), 
							four.getLong(), four.getLat());*/
							line = lineTwo;  
							lineTwo = buff.readLine();  
				}
			} else if(line.split(",")[0].equals("veh_plate")){
				System.out.println("oops, not coded yet");
				System.out.println(line);
				line = buff.readLine(); 
				System.out.println(line);
				line = buff.readLine(); 
				System.out.println(line);
			} else {
				line = buff.readLine(); 
				String lineTwo = buff.readLine(); 
				while(line != null && lineTwo != null){
					double yOne = Double.parseDouble(line.split(",")[3]);
					double xOne = Double.parseDouble(line.split(",")[4]);
					double yTwo = Double.parseDouble(lineTwo.split(",")[3]);
					double xTwo = Double.parseDouble(lineTwo.split(",")[4]);
					pointOne = new Point(xOne, yOne);
					pointTwo = new Point(xTwo, yTwo);

					drawLine(pointOne, pointTwo);
					line = lineTwo; 
					lineTwo = buff.readLine();  
					count++; 
					if(count % 100 == 0){
						System.out.println("count = "+count);
					}
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	//Assumed that Scaler had already been "scaled". 
	public static void showRoute(ArrayList<Point> points){
		for(int i = 0; i < points.size() - 1; i++){
			drawLine(points.get(i), points.get(i+1));
		}
	}

	public static void drawLine(Point pointOne, Point pointTwo){
		Point scaledOne = Scaler.scaleToDisplay(pointOne); 
		Point scaledTwo = Scaler.scaleToDisplay(pointTwo);
		double x0 = scaledOne.getLong();
		double y0 = scaledOne.getLat(); 
		double x1 = scaledTwo.getLong(); 
		double y1 = scaledTwo.getLat(); 
		StdDraw.line(x0, y0, x1, y1);
	}

	public static void showPoints(ArrayList<Point> points, double innerRadius, 
			double outerRadius){
		for(int i = 0; i < points.size(); i++){
			showPoint(points.get(i), innerRadius, outerRadius);
		}
	}
	
	
	/** wait for a click and output a point corresponding to that click
	 * 
	 * @return a point corresponding to the location of click. 
	 */
	public static Point listener(double restTime, boolean toScale){
		try {
			Thread.sleep((long) restTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		while(StdDraw.mousePressed() != true){
			StdDraw.mousePressed(); 
		}
		double x = StdDraw.mouseX(); 
		double y = StdDraw.mouseY();
		Point point = new Point(x, y);
		if(toScale){
			return Scaler.scaleToActualGPS(point);
		} else {
			return point; 
		}
	}

	public static Point listener(double restTime){
		return listener(restTime, true);
	}

	public static void main (String[] args){
		Scaler.prepareCanvass("SingaporeSmall.png", true);
		Point point = Display.listener(500);
		Display.showPoint(point, 0, 10);
		System.out.println(point.getLat()+","+point.getLong());

		point = Display.listener(100);
		Display.showPoint(point, 0, 10);
		System.out.println(point.getLat()+","+point.getLong());
	}
}
