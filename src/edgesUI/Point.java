package edgesUI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Point {
	double longitude; 
	double latitude; 
	Time time; 
	//The scaling factor from longitude,latitude coordinates to corresponding length in meter unit.
	//only works near equator
	private final double COORDINATE_TO_METER_SCALE = 111190; 
	/**
	 * Takes in a string which format is similar with that given from comfort delgro, 
	 * and fill the point object with is longitude, latitude, time as specified by the string. 
	 * @param string
	 */
	Point(String string){
		String[] data = string.split(",");
		for(int i = 0; i < data.length; i++){
			data[i] = data[i].trim(); 
		}
		/*
		 * If data.length = 8, it means that it is from Comfort Delgro's data. 
		 */
		if(data.length == 8){
			this.latitude = Double.parseDouble(data[3]);
			this.longitude = Double.parseDouble(data[4]); 
			this.time = new Time(data[1]);
			return;
		}
		/*
		 * If data.length is 3, it means that it is taken from the old gps file that
		 * me(yew onn), siva and haojing collected using our custom android studio app. 
		 */
		if(data.length == 3){
			this.latitude = Double.parseDouble(data[0]);
			this.longitude = Double.parseDouble(data[1]);
			this.time = new Time(Double.parseDouble(data[2]));
		}
	}

	Point(Point other){
		this(other.getLong(), other.getLat(), other.getTime());
	}

	Point(double latitude, double longitude, boolean lazy){
		this(longitude, latitude, null);
		if(lazy != true){
			System.out.println("ERROR!, Point.java, line 51");
		}
	}

	Point(double longitude, double latitude){
		this(longitude, latitude, null);
	}

	Point(double longitude, double latitude, double time){
		this(longitude, latitude, new Time(time));
	}

	Point(double longitude, double latitude, Time time){
		this.longitude = longitude; 
		this.latitude = latitude; 
		this.time = time; 
	}

	public double getLong(){
		return this.longitude;
	}

	public double getLat(){
		return this.latitude; 
	}

	public Time getTime(){
		return this.time; 
	}

	public String toStringCoordinate(){
		return latitude+","+longitude;
	}
	public void printCoordinates(){
		System.out.println(latitude+","+longitude);
	}
	/**
	 * Determine if two Points are within a distance specified by cutoff. 
	 * @param pointTwo
	 * @param cutOff is the maximum distance between two points that are still considered close enough. 
	 * @return
	 */
	public boolean isClose(Point pointTwo, double cutOff){
		if(this.meterDistance(pointTwo) <= cutOff){
			return true; 
		} else{
			return false; 
		}
	}
	public double meterDistance(Point other){
		double longTwo = other.getLong(); 
		double latTwo = other.getLat(); 
		double longDiff = (longTwo - this.longitude)*COORDINATE_TO_METER_SCALE; 
		double latDiff = (latTwo - this.latitude)*COORDINATE_TO_METER_SCALE; 
		return Math.sqrt(longDiff*longDiff  + latDiff*latDiff); 
	}

	public void set(double longitude, double latitude){
		this.latitude = latitude; 
		this.longitude = longitude; 
	}

	public void set(String string){
		String[] data = string.split(",");
		for(int i = 0; i < data.length; i++){
			data[i] = data[i].trim(); 
		}
		if(data.length == 8){
			this.latitude = Double.parseDouble(data[3]);
			this.longitude = Double.parseDouble(data[4]); 
			this.time = new Time(data[1]);
			return;
		}
		if(data.length == 3){
			this.latitude = Double.parseDouble(data[0]);
			this.longitude = Double.parseDouble(data[1]);
			this.time = new Time(Double.parseDouble(data[2]));
		}
	}


	/**
	 * Determine the perpendicular distance from point object to straight line
	 * form by connecting pointOne and pointTwo. 
	 * 
	 * @param pointOne
	 * @param pointTwo
	 * 
	 * @return negative value if the object is not within pointOne and pointTwo; 
	 */

	public double meterDistance(Point pointOne,Point pointTwo){
		//get equation connecting pointOne and pointTwo
		double gradientOne = (pointTwo.getLat() - pointTwo.getLat())/(pointOne.getLong() - pointTwo.getLong());
		double gradientTwo = 1/gradientOne * -1; 
		double constantOne = pointOne.getLat() - gradientOne*getLong(); 
		double constantTwo = this.latitude - this.longitude*gradientTwo; 

		//find intersection between perpendicular and parallel, then find the distance; 
		double longIntersect = (constantTwo - constantOne)/(gradientOne - gradientTwo);
		double latIntersect = longIntersect*gradientTwo + constantTwo; 
		Point intersect = new Point(longIntersect, latIntersect, null);
		double distance = meterDistance(intersect);

		//check if intersection point is in between pointOne and pointTwo
		double smallerLong = 0; 
		double largerLong = 0; 
		if(pointOne.getLong() > pointTwo.getLong()){
			largerLong = pointOne.getLong(); 
			smallerLong = pointTwo.getLong(); 
		} else {
			largerLong = pointTwo.getLong(); 
			smallerLong = pointOne.getLong(); 
		}

		if(longIntersect < smallerLong || longIntersect > largerLong){
			//point of intersection does not lie within pointOne and pointTwo; 
			/*distance = meterDistance(pointOne);
			if(distance < meterDistance(pointTwo)){
				distance = meterDistance(pointTwo);
			}*/
			distance = distance * -1; 
		}
		//return negative value if does not lie with pointOne and pointTwo. 
		//negative value is equal to closest distance between either pointOne or pointTwo. 
		return distance; 
	}

	public boolean isWithin(Point pointOne, Point pointTwo){
		if(meterDistance(pointOne, pointTwo) < 0){
			return false; 
		} else {
			return true; 
		}
	}

	public boolean isSingapore(Point point){
		double longitude = point.getLong(); 
		double latitude = point.getLat(); 
		double singaporeMinLong = 103.59031826761881;
		double singaporeMinLat = 1.2070852141342034;
		double singaporeMaxLong = 104.05713892258476;
		double singaporeMaxLat = 1.4891059540533795;
		if(longitude < singaporeMinLong || longitude > singaporeMaxLong){
			return false; 
		} 
		if (latitude < singaporeMinLat || latitude > singaporeMaxLat){
			return false; 
		}
		return true; 
	}

	public boolean isNus(Point point){
		double longitude = point.getLong(); 
		double latitude = point.getLat(); 
		double nusMinLong = 103.76868753689519;
		double nusMinLat = 1.2894896700163732;
		double nusMaxLong = 103.78586577720426; 
		double nusMaxLat = 1.3058590694140482;
		if(longitude < nusMinLong || longitude > nusMaxLong){
			return false; 
		} else if(latitude < nusMinLat || latitude > nusMaxLat){
			return false; 
		} else {
			return true; 
		}
	}

	/**
	 * Sample usage
	 * 
	 * @param args
	 */
	public static void main (String[] args){
		try{
			FileReader f = new FileReader("PA 0002A.txt");
			BufferedReader buff = new BufferedReader(f);
			String line = buff.readLine(); 
			System.out.println(line);
			line = buff.readLine(); 
			System.out.println(line);
			line = buff.readLine();
			System.out.println(line); 
			System.out.println(line.split(",").length);
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}
