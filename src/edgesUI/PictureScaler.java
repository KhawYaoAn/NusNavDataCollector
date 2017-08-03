package edgesUI;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class PictureScaler {
	public static String imageFile = null;
	public static double xCenter = -1; 
	public static double yCenter = -1; 
	public static double tempXCenter = -1; 
	public static double tempYCenter = -1; 
	public static double resize = 1; 
	public static double imageWidth = 1440; 
	public static double imageHeight = 900;
	public static double minLat = -1; 
	public static double minLong = -1; 
	public static double scale = -1; 
	public static ArrayList<Point> userPoint = new ArrayList<Point>(); 
	public static ArrayList<Point> gpsPoint = new ArrayList<Point>(); 
	public static ArrayList<Double> minLatList = new ArrayList<Double>(); 
	public static ArrayList<Double> minLongList = new ArrayList<Double>(); 
	public static ArrayList<Double> scaleList = new ArrayList<Double>(); 
	public static Scanner sc = new Scanner(System.in);

	public static void init(double xCenter, double yCenter, String imageFile){
		PictureScaler.imageFile = imageFile; 
		PictureScaler.xCenter = xCenter; 
		PictureScaler.tempXCenter = xCenter; 
		PictureScaler.yCenter = yCenter; 
		PictureScaler.tempYCenter = yCenter;
		PictureScaler.resize = 1; 
		BufferedImage bimg;
		/**try {
			bimg = ImageIO.read(new File("src/edgesUI/"+imageFile));
			int width = bimg.getWidth();
			int height = bimg.getHeight();
			PictureScaler.imageWidth = width; 
			PictureScaler.imageHeight = height; 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		resetImage();
	}

	public static void resetImage(){
		StdDraw.picture(tempXCenter, tempYCenter, imageFile, imageWidth * resize, imageHeight * resize);
	}

	public static void zoom(double scaleFactor, double restTime){
		Point oldCenter = new Point(tempXCenter, tempYCenter);
		double radius = 10; 
		StdDraw.circle(oldCenter.getLong(), oldCenter.getLat(), radius);
		StdDraw.line(oldCenter.getLong() - radius, oldCenter.getLat(), oldCenter.getLong() + radius, oldCenter.getLat());
		StdDraw.line(oldCenter.getLong(), oldCenter.getLat() + radius, oldCenter.getLong(), oldCenter.getLat() - radius);
		System.out.println("click a point");
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
		resize = resize * scaleFactor; 
		
		tempXCenter = (tempXCenter - x)*scaleFactor + x; 
		tempYCenter = (tempYCenter - y)*scaleFactor + y; 
		resetImage(); 
	}

	public static void run(){
		boolean imHappy = true; 
		while(imHappy){
			System.out.println("Please enter scaleFactor: ");
			double scaleFactor = sc.nextDouble(); 
			sc.nextLine(); 
			PictureScaler.zoom(scaleFactor, 300);
			System.out.println("Press y to select a point:");
			String line = sc.nextLine();
			if(line.equals("y")){//allow user to select a point
				Point point = Display.listener(300, false);
				//need to put point back to original
				drawCircle(point, 10); 
				
				
				Point point2 = new Point(xCenter, yCenter);	
				Point point3 = new Point(tempXCenter, tempYCenter);
				
				System.out.println("okay with this point?");
				String line2 = sc.nextLine(); 
				if(line2.equals("y")){
					Point scaledPoint = revertToOriginal(point);
					userPoint.add(revertToOriginal(scaledPoint));
					PictureScaler.init(xCenter, yCenter, imageFile);
					StdDraw.setPenColor(StdDraw.BLUE);
					//drawCircle(point, 10); //original
					drawCircle(scaledPoint, 30); //scaled
					StdDraw.setPenColor(StdDraw.RED);
					//drawCircle(point2, 15); //orginalCenter
					StdDraw.setPenColor(StdDraw.BLACK);
					//drawCircle(point3, 20);
					System.out.println("x = "+point.getLat()+", y = "+point.getLong());
					System.out.println("Please enter actual coordinate,");
					System.out.println("longitude = ");
					double longitude = sc.nextDouble(); 
					System.out.println("longitude = "+longitude);
					System.out.println("latitude = ");
					double latitude = sc.nextDouble(); 
					System.out.println("latitude = " +latitude);
					System.out.println("toContinue?");
					sc.nextLine(); 
					gpsPoint.add(new Point(longitude, latitude));
					String input = sc.nextLine(); 
					if(input.equals("n")){
						imHappy = false; 
					}
				}
			}
		}
	}
	
	public static Point revertToOriginal(Point point){
		double xOld = point.getLong(); 
		double yOld = point.getLat(); 
		double xNew = (xOld - tempXCenter)/resize + xCenter; 
		double yNew = (yOld - tempYCenter)/resize + yCenter; 
		return new Point(xNew, yNew); 
	}

	public static void computeScale(String fileName){
		if(fileName != null){
			
			try {
				FileReader f = new FileReader(fileName);
				BufferedReader buff = new BufferedReader(f);
				String line;
				line = buff.readLine();
				int count = 1; 
				while(line != null){
					if(count % 2 == 1){ //userPoint
						double x = Double.parseDouble(line.split(",")[0]);
						double y = Double.parseDouble(line.split(",")[1]);
						userPoint.add(new Point(x, y));
					}
					if(count % 2 == 0){ //gpsPoint
						double longitude = Double.parseDouble(line.split(",")[1]);
						double latitude = Double.parseDouble(line.split(",")[0]);
						gpsPoint.add(new Point(longitude, latitude));
					}
					line = buff.readLine(); 
					count++; 
				}
				buff.close();
				f.close(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
		for(int i = 0; i < userPoint.size() - 1; i++){
			double gpsXOne = gpsPoint.get(i).getLong();
			double gpsYOne = gpsPoint.get(i).getLat(); 
			double gpsXTwo = gpsPoint.get(i + 1).getLong(); 
			double gpsYTwo = gpsPoint.get(i + 1).getLat(); 

			double userXOne = userPoint.get(i).getLong(); 
			double userYOne = userPoint.get(i).getLat(); 
			double userXTwo = userPoint.get(i + 1).getLong(); 
			double userYTwo = userPoint.get(i + 1).getLat(); 

			double minLong = (gpsXOne - gpsXTwo*(userXOne/userXTwo))/(1 - userXOne/userXTwo);
			double scaleLong  = userXTwo/(gpsXTwo - minLong);
			double minLat = (gpsYOne - gpsYTwo*(userYOne/userYTwo))/(1 - userYOne/userYTwo);
			double scaleLat = userYTwo/(gpsYTwo - minLat);
			double scale = (scaleLong + scaleLat)/2; 
			minLongList.add(minLong);
			minLatList.add(minLat);
			scaleList.add(scale);
		}
		double minLongSum = 0; 
		double minLatSum = 0; 
		double scaleSum = 0; 
		for(int i = 0; i < minLongList.size(); i++){
			minLongSum += minLongList.get(i);
			minLatSum += minLatList.get(i);
			scaleSum += scaleList.get(i);
		}
		minLong = minLongSum / minLongList.size();
		minLat = minLatSum / minLatList.size(); 
		scale = scaleSum / scaleList.size(); 
		System.out.println("minLong = "+minLong+";");
		System.out.println("minLat = "+minLat+";");
		System.out.println("scale = "+scale+";");
	}

	public static void outData(String fileName){
		FileWriter f = null;
		try {
			f = new FileWriter(fileName);
			BufferedWriter buff = new BufferedWriter(f);
			for(int i = 0; i < userPoint.size(); i++){
				String x = Double.toString(userPoint.get(i).getLong());
				String y = Double.toString(userPoint.get(i).getLat());
				String longitude = Double.toString(gpsPoint.get(i).getLong());
				String latitude = Double.toString(gpsPoint.get(i).getLat());
				buff.write(x+","+y);
				buff.write("\n");
				buff.write(latitude+", "+longitude);
				if(i < userPoint.size() - 1){
					buff.write("\n");
				}
			}
			buff.close(); 
			f.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void testRun(){
		PictureScaler.init(xCenter, yCenter, imageFile);
		for(int i = 0; i < userPoint.size(); i++){
			System.out.println("printing points");
			StdDraw.setPenColor(StdDraw.BLACK);
			drawCircle(userPoint.get(i), 30);
			StdDraw.setPenColor(StdDraw.RED); 
			drawCircle(gpsPoint.get(i), 15);
			StdDraw.setPenColor(StdDraw.BLUE);
			drawCircle(scaleToDisplay(gpsPoint.get(i)), 20); 
		}
		
		boolean toContinue = true; 

		while(toContinue){
			boolean happy = true; 
			while(happy){
				System.out.println("enter scale factor");
				double scaleFactor = sc.nextDouble(); 
				sc.nextLine(); 
				zoom(scaleFactor, 300); 
				System.out.println("done?");
				String line = sc.nextLine();
				if(line.equals("y")){
					happy = false; 
				}
			}
			System.out.println("Please click a point on the screen");
			Point point = Display.listener(300, false);
			drawCircle(point, 20);
			point = revertToOriginal(point);
			double longitude = (point.getLong()/scale) + minLong;
			double latitude = (point.getLat()/scale) + minLat;
			System.out.println(latitude +","+longitude);
			System.out.println("toContinue");
			String line2 = sc.nextLine(); 
			if(line2.equals("n")){
				toContinue = false; 
			}
		}
	}
	
	public static Point scaleToDisplay(Point point){
		double longitude = (point.getLong() - minLong)*scale; 
		double latitude = (point.getLat() - minLat)*scale;
		return new Point(longitude, latitude);
	}
	
	public static void drawCircle(Point point, double radius){
		StdDraw.circle(point.getLong(), point.getLat(), radius);
		StdDraw.line(point.getLong() - radius, point.getLat(), point.getLong() + radius, point.getLat());
		StdDraw.line(point.getLong(), point.getLat() + radius, point.getLong(), point.getLat() - radius);
	}

	public static void main (String[] args){
		StdDraw.setCanvasSize(1000, 600); 
		StdDraw.setXscale(0, 1000);
		StdDraw.setYscale(0, 600);
		//minLong = 103.77282122909119;
		//minLat = 1.2912204598768084;
		//scale = 51361.147903481295;
		PictureScaler.init(300, 300, "NusLarge.png");
		PictureScaler.run();
		PictureScaler.outData("dataStore.txt");
		PictureScaler.computeScale(null);
		PictureScaler.testRun(); 
	}
}
