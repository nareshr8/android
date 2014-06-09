package in.nrsh.augumentedreality.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Geometry implements Parcelable{

	Location location;

	
	public Geometry(Parcel in) {
		readFromParcel(in);
	}
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(location, 0);
	}

    public static final Parcelable.Creator<Geometry> CREATOR = new Parcelable.Creator<Geometry>() {  
        
        public Geometry createFromParcel(Parcel in) {  
            return new Geometry(in);  
        }  
   
        public Geometry[] newArray(int size) {  
            return new Geometry[size];  
        }  
          
    };  
  
	
	private void readFromParcel(Parcel in) {
		location = (Location) in.readParcelable(Location.class.getClassLoader());
	}

}
