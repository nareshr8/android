package in.nrsh.augumentedreality.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Results implements Parcelable {

	private Geometry geometry;
	private String icon;
	private String id;
	private String name;
	private double rating;
	private String reference;
	private String vicinity;

	public Results(Parcel in) {
		readFromParcel(in);
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getVicinity() {
		return vicinity;
	}

	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(geometry);
		dest.writeString(icon);
		dest.writeString(id);
		dest.writeString(name);
		dest.writeDouble(rating);
		dest.writeString(reference);
		dest.writeString(vicinity);
	}

	public static final Parcelable.Creator<Results> CREATOR = new Parcelable.Creator<Results>() {

		public Results createFromParcel(Parcel in) {
			return new Results(in);
		}

		public Results[] newArray(int size) {
			return new Results[size];
		}

	};

	private void readFromParcel(Parcel in) {
		geometry = (Geometry) in.readValue(Geometry.class.getClassLoader());
		icon = in.readString();
		id = in.readString();
		name = in.readString();
		rating = in.readDouble();
		reference = in.readString();
		vicinity = in.readString();
	}

}
