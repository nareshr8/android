package in.nrsh.augumentedreality.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable{
	private double lat;
	private double lng;
	private double angle;
	private int distance;
	
	public Location(Parcel in) {
		readFromParcel(in);
	}
	
	
	public double getLat() {
		return lat;
	}
	
	public void setLat(double lat) {
		this.lat = lat;
	}
	
	public double getLng() {
		return lng;
	}
	
	public void setLng(double lng) {
		this.lng = lng;
	}
	
	public void setAngle(double angle) {
		this.angle = angle;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public int getDistance() {
		return distance;
	}
	
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(lat);
		dest.writeDouble(lng);
		dest.writeDouble(angle);
		dest.writeInt(distance);
	}
	
	public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {  
		
		public Location createFromParcel(Parcel in) {  
			return new Location(in);  
		}  
		
		public Location[] newArray(int size) {  
			return new Location[size];  
		}  
		
	};  
	
	
	private void readFromParcel(Parcel in) {
		lat=in.readDouble();
		lng=in.readDouble();
		angle=in.readDouble();
		distance=in.readInt();
	}


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}

