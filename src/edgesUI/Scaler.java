package edgesUI;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class must be used along side display. 
 * Otherwise it would not work as expected. 
 * @author YewOnn
 *
 */
public class Scaler {

	static ArrayList<Point> pointList = new ArrayList<Point>();

	static double minLong = Double.MAX_VALUE; 
	static double minLat = Double.MAX_VALUE; 
	static double maxLong = -1; 
	static double maxLat = -1; 
	//CANVASS_SIZE & range are for old system. 
	static double CANVASS_SIZE = 500; 
	static double range = -1; 
	//Scale is for new system. scale = canvass_size/range; 
	static double scale = 1; 


	private static void inputPoint(String fileName){
		ArrayList<Point> pointList = new ArrayList<Point>();

		try {
			FileReader f = new FileReader(fileName);
			BufferedReader b = new BufferedReader(f);


			//the following four line are useless.
			//the two local variables won't be used. They are written because
			//the file format's first two lines are these information.
			String busService = b.readLine().split(" ")[3]; 
			String comment = b.readLine(); 
			busService = busService + "";
			comment = comment +"";

			String line = b.readLine(); 
			while(line != null){
				double latitude = Double.parseDouble(line.split(",")[0]);
				double longitude = Double.parseDouble(line.split(",")[1]);
				double time = Double.parseDouble(line.split(",")[2]);
				//updateMinMax(longitude, latitude);
				pointList.add(new Point(longitude, latitude, time));
				line = b.readLine();
			}
			b.close(); 
		} catch (FileNotFoundException e) {
			System.out.println("File not found. Try again...");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Exception. Failed to readLine. Try again...");
			e.printStackTrace();
		}
		Scaler.pointList = pointList; 

		return; 
	}
	/**
	 * function is only used when the scaling factor is not yet identified. 
	 * @param longitude
	 * @param latitude
	 */
	private static void updateMinMax(Double longitude, Double latitude){
		if(longitude > maxLong){
			maxLong = longitude; 
		}
		if(longitude < minLong){
			minLong = longitude; 
		}
		if(latitude > maxLat){
			maxLat = latitude; 
		}
		if(latitude < minLat){
			minLat = latitude; 
		}

		double latRange = maxLat - minLat; 
		double longRange = maxLong - minLong; 

		if(longRange >= latRange){
			range = longRange;
		}else{
			range = latRange; 
		}
	}

	public static void obtainScale(String fileName){
		//update the busRoute
		inputPoint(fileName);
		//updateScale("NusSmall.png");
		System.out.println("at obtainScale");
		for(int i = 0; i < pointList.size(); i++){
			Point pointOne = scaleToDisplay(pointList.get(i));

			if(i != pointList.size() - 1){
				Point pointTwo = scaleToDisplay(pointList.get(i + 1));
				StdDraw.line(pointOne.getLong(), pointOne.getLat(), 
						pointTwo.getLong(), pointTwo.getLat());
			}
		}
		/*Point[] points = new Point[3];
		points[0] = new Point(103.643783, 1.212248);
		points[1] = new Point(104.042080, 1.348857);
		points[2] = new Point(103.804522, 1.368464);
		//points[3] = new Point(103.78037687549245, 1.2919898173916589);
		//points[4] = new Point(103.633317, 1.350680);
		for(int i = 0; i < points.length; i++){
			Point point = scaleToDisplay(points[i]);
			double x = point.getLong(); 
			double y = point.getLat(); 
			double radius = 20; 
			double sradius = Math.sqrt(radius*radius/2);
			StdDraw.circle(x, y, radius);
			//StdDraw.line(x - radius, y, x + radius, y);
			//StdDraw.line(x, y + radius, x, y - radius);
			StdDraw.line(x - sradius, y - sradius, x + sradius, y + sradius);
			StdDraw.line(x - sradius, y + sradius, x + sradius, y - sradius);
		}*/

		System.out.println("minLong = "+minLong+";");
		System.out.println("minLat = "+minLat+";");
		System.out.println("range = "+range+";");
		System.out.println("CANVASS_SIZE = "+CANVASS_SIZE+";");
		scale = CANVASS_SIZE/range;
		System.out.println("scale = "+scale+";");
	}

	/**
	 * 
	 * @param point
	 * @param toMaintain equals true when your point object SHOUDL NOT BE MODIFIED
	 * @return
	 */
	public static Point scaleToDisplay(Point point, boolean toMaintain){
		if(toMaintain){
			double x = (point.getLong() - minLong)*CANVASS_SIZE/range; 
			double y = (point.getLat() - minLat)*CANVASS_SIZE/range; 
			return(new Point(x, y, point.getTime()));
		} else {
			double x = (point.getLong() - minLong)*CANVASS_SIZE/range; 
			double y = (point.getLat() - minLat)*CANVASS_SIZE/range; 
			point.set(x, y);
			return point; 
		}
	}

	public static Point scaleToDisplay(Point point){
		return scaleToDisplay(point, true);
	}

	public static Point scaleToActualGPS(Point point){
		double x = point.getLong()*(range/CANVASS_SIZE) + minLong; 
		double y = point.getLat()*(range/CANVASS_SIZE) + minLat; 
		return(new Point(x, y, point.getTime()));
	}
	public static void showPoint(Point point){

	}
	/**
	 * This method set provide the scaling for converting
	 * coordinates in to dimensions that fit the display units. 
	 * @param imageFile
	 */
	public static void prepareCanvass(String imageFile, Boolean prepareCanvass){
		/*StdDraw.setCanvasSize(700, 700);
		StdDraw.setScale(0, 700);
		StdDraw.picture(330, 380, "NusSmall.png"); 
		this.minLong = 103.76445;
		this.minLat = 1.2851;
		this.range = 0.014426080324795976;
		this.CANVASS_SIZE = 412.0;
		this.scale = 28559.38624519109;*/
		if(imageFile.equals("NusMedium.png")){
			if(prepareCanvass){
				StdDraw.setCanvasSize(700, 700);
				StdDraw.setScale(0, 700);
				StdDraw.picture(330, 380, "NusMedium.png"); 
			} 
			StdDraw.picture(330, 380, "NusMedium.png"); 
			minLong = 103.769168;
			minLat = 1.288457; 
			range = 1.0;
			CANVASS_SIZE = 39000; 
		}
		if(imageFile.equals("NusSmall.png")){
			if(prepareCanvass){
				StdDraw.setCanvasSize(500, 500);
				StdDraw.setScale(0, 500);
				StdDraw.picture(210, 300, "NusSmall.png"); 
			}
			StdDraw.picture(210, 300, "NusSmall.png"); 
			minLong = 103.767768;
			minLat = 1.286707; 
			range = 1.0;
			CANVASS_SIZE = 25600; 
		}
		if(imageFile.equals("NusLarge.png")){
			System.out.println("image file = NusLarge.png");
			if(prepareCanvass){
				StdDraw.setCanvasSize(1350, 700);
				StdDraw.setXscale(0, 1350);
				StdDraw.setYscale(0, 700);
				StdDraw.picture(650, 400, "NusLarge.png");
			}
			StdDraw.picture(650, 400, "NusLarge.png");
			minLong = 103.766000;
			minLat = 1.28925;
			range = 1.0;
			CANVASS_SIZE = 51500;
		}
		if(imageFile.equals("SingaporeLarge.png")){
			if(prepareCanvass){
				StdDraw.setCanvasSize(1400, 700);
				StdDraw.setYscale(0, 700);
				StdDraw.setXscale(0, 1400);
				StdDraw.picture(700, 300, "SingaporeLarge.png"); 
			}
			StdDraw.picture(700, 300, "SingaporeLarge.png"); 
			minLong = 103.62;
			minLat = 1.257;
			range = 0.014426080324795976;
			CANVASS_SIZE = 50.8;
			scale = 3521.4000515915227;
		}

		if(imageFile.equals("SingaporeSmall.png")){
			if(prepareCanvass){
				StdDraw.setCanvasSize(600, 400);
				StdDraw.setYscale(0, 400);
				StdDraw.setXscale(0, 600);
				StdDraw.picture(270, 200, imageFile); 
			}
			StdDraw.picture(270, 200, imageFile); 
			minLong = 103.605;
			minLat = 1.2;
			range = 0.014426080324795976;
			CANVASS_SIZE = 19.75;
			scale = 1369.0482484041845;
		}

		if(imageFile.equals("UniversityTown.png")){
			if(prepareCanvass){
				StdDraw.setCanvasSize(1300, 740);
				StdDraw.setXscale(0, 1300);
				StdDraw.setYscale(0, 740);
				StdDraw.picture(650, 342, "UniversityTown.png"); 
			}
			StdDraw.picture(650, 342, "UniversityTown.png"); 

			//String fileName = "A2.txt";
			//prepareCanvass("NusLarge.png", true);
			Scaler.minLong = 103.768554;
			Scaler.minLat = 1.30309;
			Scaler.range = 1;
			Scaler.CANVASS_SIZE = 102053.6568423504;
		}
		if(imageFile.equals("FASS.png")){
			if(prepareCanvass){
				StdDraw.setCanvasSize(1000, 700);
				StdDraw.setYscale(0, 700);
				StdDraw.setXscale(0, 1000);
			}
			StdDraw.picture(330, 380, "FASS.png"); 

			minLong = 103.770547;
			minLat = 1.293465;
			range = 1.0;
			CANVASS_SIZE = 204691;
		}
		if(imageFile.equals("FOE.png")){
			if(prepareCanvass){
				StdDraw.setCanvasSize(1000, 700);
				StdDraw.setYscale(0, 700);
				StdDraw.setXscale(0, 1000);
			}
			StdDraw.picture(330, 380, "FOE.png"); 


			minLong = 103.770063;
			minLat = 1.297050;
			range = 1.0;
			CANVASS_SIZE = 204691;
		}
		if(imageFile.equals("FoeSmall.png")){
			/**StdDraw.setCanvasSize(1000, 700);
			StdDraw.setYscale(0, 700);
			StdDraw.setXscale(0, 1000);
			StdDraw.picture(330, 380, "FOE.png"); 


			minLong = 103.770063;
			minLat = 1.297050;
			range = 1.0;
			CANVASS_SIZE = 204691;*/
			if(prepareCanvass){
				StdDraw.setCanvasSize(1000, 700);
				StdDraw.setYscale(0, 700);
				StdDraw.setXscale(0, 1000);
			}
			
			StdDraw.picture(330, 380, "FoeSmall.png"); 


			minLong = 103.769878;
			minLat = 1.295937;
			range = 1.0;
			CANVASS_SIZE = 101803.37405468141;
		}

	}

	/**
	 * beta mode, unstable. 
	 * @param args
	 */
	public static void findScale(double minLat, double minLong,
			double maxLat, double maxLong){
		double frameHeight = StdDraw.height; 
		double frameWidth = StdDraw.width; 
		double widthScale = StdDraw.width/(maxLong - minLong);
		double heightScale = StdDraw.height/(maxLat - minLat);
		double averageScale = (widthScale + heightScale)/2;
		System.out.println("heigthScale = "+heightScale);
		System.out.println("widthScale = "+widthScale);
		System.out.println("averageScale = "+averageScale);
		Scaler.minLong = minLong;
		Scaler.minLat = minLat;
		Scaler.range = 1;
		Scaler.CANVASS_SIZE = averageScale;
	}

	public static void main(String[] args){
		//Scaler.prepareCanvass("FASS.png", true);
		/**StdDraw.setCanvasSize(700, 700);
		StdDraw.setScale(0, 700);
		StdDraw.picture(330, 380, "NusMedium.png"); 
		minLong = 103.769168;
		minLat = 1.288457; 
		range = 1.0;
		CANVASS_SIZE = 39000; */



		/*String fileName = "A1.txt";
		Scaler.prepareCanvass("NusMedium.png", true);
		Display.showRoute(fileName);*/




		//1.293229, 103.768720
		//int outerRadius = 10;
		//obtainScale(fileName);
		//Display.showRoute(fileName);
		StdDraw.setCanvasSize(1000, 700);
		StdDraw.setYscale(0, 700);
		StdDraw.setXscale(0, 1000);
		StdDraw.picture(330, 380, "FoeSmall.png"); 


		minLong = 103.769878;
		minLat = 1.295937;
		range = 1.0;
		//heigthScale = 101803.37405468141
		//widthScale = 101978.38058334193
		//		averageScale = 101890.87731901166
		CANVASS_SIZE = 101803.37405468141;
		//Scaler.findScale(1.295977, 103.769878, 1.302853, 103.779684);
		int outerRadius = 10; 

		System.out.println("targetShown");
		Display.showTarget(new Point(1.296701, 103.771491, true), outerRadius);
		Display.showTarget(new Point(1.302112, 103.778504, true), outerRadius);
		Point point = Display.listener(500);
		Display.showTarget(point, 20);
		System.out.println(point.getLat()+","+point.getLong());
		Point point2 = Display.listener(500);
		Display.showTarget(point2, 20);
		System.out.println(point2.getLat()+","+point2.getLong());
		System.out.println("longDiff = "+(point2.getLong() - point.getLong()));
		System.out.println("latDiff = "+(point2.getLat() - point.getLat()));
		System.out.println("distance = "+point.meterDistance(point2));
		System.out.println("done");
	}
}
