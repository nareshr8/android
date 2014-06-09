package in.nrsh.augumentedreality;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AugumentedReality extends Application {

	// Volley request queue that we needs to be singleton across the
	// application. Any request must be added to this queue so that the network
	// is accessed through a single Process/Thread.
	RequestQueue mRequestQueue;

	@Override
	public void onCreate() {
		super.onCreate();
		// Initialing the requestQueue for the application.
		mRequestQueue = Volley.newRequestQueue(getApplicationContext());
	}

	// Method to be called when a request is to be made.
	public void addToRequestQueue(Request request) {
		mRequestQueue.add(request);
	}
}
