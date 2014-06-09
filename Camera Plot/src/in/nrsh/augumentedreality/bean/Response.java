package in.nrsh.augumentedreality.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Response implements Parcelable {

	private String[] html_attributions;
	private String next_page_token;
	private Results[] results;

	public Response(Parcel in) {
		readFromParcel(in);
	}

	public Results[] getResults() {
		return results;
	}

	public void setResults(Results[] results) {
		this.results = results;
	}

	public String getNext_page_token() {
		return next_page_token;
	}

	public void setNext_page_token(String next_page_token) {
		this.next_page_token = next_page_token;
	}

	public String[] getHtml_attributions() {
		return html_attributions;
	}

	public void setHtml_attributions(String[] html_attributions) {
		this.html_attributions = html_attributions;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(html_attributions);
		dest.writeString(next_page_token);
		dest.writeTypedArray(results, 0);
	}

	public static final Parcelable.Creator<Response> CREATOR = new Parcelable.Creator<Response>() {

		public Response createFromParcel(Parcel in) {
			return new Response(in);
		}

		public Response[] newArray(int size) {
			return new Response[size];
		}

	};

	private void readFromParcel(Parcel in) {
		html_attributions = in.createStringArray();
		next_page_token = in.readString();
		results = (Results[]) in.createTypedArray(Results.CREATOR);
//		if (parcelableArray != null) {
//		    results = Arrays.copyOf(parcelableArray, parcelableArray.length, Results[].class);
//		}
	}

}
