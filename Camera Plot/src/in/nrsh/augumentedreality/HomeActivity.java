package in.nrsh.augumentedreality;

import in.nrsh.augumentedreality.bean.Response;
import in.nrsh.augumentedreality.bean.Results;
import in.nrsh.augumentedreality.utils.GridAdapter;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.hardware.GeomagneticField;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public class HomeActivity extends Activity implements LocationListener,
		OnItemClickListener {

	GridView gridView;
	private GridAdapter adapter;
	private ProgressDialog mProgressDialog;

	/**
	 * The url to find the nearby places of any location/ Read the documentation
	 * to know what parameters can be sent.
	 */
	private static final String NEARBY_LOCATION_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

	private static final long MIN_TIME_BW_UPDATES = 10000;

	private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1000;

	protected static final String TAG = "MainActivity";

	/**
	 * Using the location manager to get the current location so that we can
	 * find the nearby places such as ATM
	 */
	private LocationManager mLocationMgr;

	private ProgressDialog mLocationProgress;

	/**
	 * Application Object. Used to get values stored in the Application Object
	 */
	private AugumentedReality app;

	/**
	 * Location object that stored the current location.
	 */
	private Location location;

	/**
	 * The GeoMagnetic Field to determine the magnetic declination.
	 */
	private GeomagneticField geoField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		gridView = (GridView) findViewById(R.id.gridView);
		adapter = new GridAdapter(this, 0);
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle("Loading...");

		gridView.setAdapter(adapter);
		gridView.setEnabled(false);
		gridView.setOnItemClickListener(this);
		mLocationProgress = new ProgressDialog(this);
		mLocationProgress.setTitle(getString(R.string.gathering_location));

		/**
		 * Initializing the values
		 */
		app = (AugumentedReality) getApplication();
		mLocationMgr = (LocationManager) getSystemService(LOCATION_SERVICE);

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!isLocationEnabled(this)) {
			// This means that location settings is turned off. Prompt to turn
			// it on.

			new AlertDialog.Builder(this)
					.setTitle(R.string.location_access_required)
					.setMessage(
							R.string.accessing_the_location_is_required_to_process_further_)
					.setCancelable(false)
					.setPositiveButton(R.string.go_to_settings,
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									startActivity(new Intent(
											Settings.ACTION_LOCATION_SOURCE_SETTINGS));
								}
							}).show();

		} else {
			location = getLocation();
			if (location != null) {
				gridView.setEnabled(true);
			} else {
				mLocationProgress.show();
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	public Location getLocation() {
		try {
			mLocationMgr = (LocationManager) getSystemService(LOCATION_SERVICE);

			// getting GPS status
			boolean isGPSEnabled = mLocationMgr
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			boolean isNetworkEnabled = mLocationMgr
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
			} else {
				if (isNetworkEnabled) {
					mLocationMgr.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network Enabled");
					if (mLocationMgr != null) {
						location = mLocationMgr
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						mLocationMgr.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS", "GPS Enabled");
						if (mLocationMgr != null) {
							location = mLocationMgr
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onLocationChanged(Location location) {
		this.location = location;
		mLocationProgress.dismiss();
		gridView.setEnabled(true);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String place = null;
		switch (position) {
		case 0:
			place = "airport";
			break;
		case 1:
			place = "amusement_park";
			break;
		case 2:
			place = "atm";
			break;
		case 3:
			place = "bank";
			break;
		case 4:
			place = "bus_station";
			break;
		case 5:
			place = "hospital";
			break;
		}
		Log.d(TEXT_SERVICES_MANAGER_SERVICE,
				"Lat Lng : " + location.getLatitude() + ","
						+ location.getLongitude());

		/**
		 * Building the web-service URL to retrieve the nearby places
		 */
		StringBuilder queryHolder = new StringBuilder(NEARBY_LOCATION_URL);
		queryHolder.append("location=" + location.getLatitude() + ","
				+ location.getLongitude());
		queryHolder.append("&radius=" + CameraActivity.DISTANCE_COVERED);
		queryHolder.append("&types=" + place);
		queryHolder.append("&sensor=true");
		//TODO: Add your API Key here.
		// Key is mentioned here. See the documentation to get an API Key.
		queryHolder.append("&key=YourAPIKeyHere");

		// Detecting the Magnetic declination of the current location for
		// the
		// current time
		geoField = new GeomagneticField((float) location.getLatitude(),
				(float) location.getLongitude(),
				(float) location.getAltitude(), Calendar.getInstance()
						.getTimeInMillis());

		/**
		 * Using the Volley error handler and showing a Toast message about the
		 * error.
		 */
		ErrorListener errorListener = new ErrorListener() {

			/**
			 * Determines what to show when an inappropriate response is got.
			 */
			@Override
			public void onErrorResponse(VolleyError error) {
				error.printStackTrace();
				mProgressDialog.dismiss();
				Toast.makeText(HomeActivity.this,
						R.string.please_check_your_internet_connection,
						Toast.LENGTH_SHORT).show();
			}
		};

		// Listener that gets called on succesful response and parsing of
		// JSON
		// data. The parsing is done using the GSON library.
		Listener<Response> listener = new Listener<Response>() {

			@Override
			public void onResponse(Response response) {
				mProgressDialog.dismiss();
				// Determining the Location's current angle from the current
				// location with respect to north pole and the distance
				// between
				// the current location and the places got in the response.
				for (int i = 0; i < response.getResults().length; i++) {
					Results result = response.getResults()[i];
					double currentLat = result.getGeometry().getLocation()
							.getLat();
					double currentLng = result.getGeometry().getLocation()
							.getLng();
					double adj = currentLat - location.getLatitude();
					double opp = currentLng - location.getLongitude();

					// Applying Pythegorous theorem to get the distance
					// between the two points.
					Double distanceDouble = Math.sqrt(Math.pow(
							Math.abs(opp) * 1000, 2)
							+ Math.pow(Math.abs(adj) * 1000, 2));

					// Multiply by 111 to get the distance in meters
					distanceDouble *= 111;
					int distance = distanceDouble.intValue();

					// Calculation to determine the angle. See the PPT/
					// Documentation for details about this calculation.
					double angle = Math.toDegrees(Math.atan((Math.abs(opp))
							/ Math.abs(adj)));
					angle -= geoField.getDeclination();
					if (opp < 0 && adj < 0) {
						// South West alone
						angle = 180 + angle;
					} else if (adj < 0) {
						// South alone
						angle = 180 - angle;
					} else if (opp < 0) {
						// West alone
						angle = 360 - angle;
					}

					// Regulation to put angle in [0,360]
					if (angle < 0) {
						angle += 360;
					}
					Log.d(TAG, "The" + result.getName() + " is in your "
							+ ((opp > 0) ? "E" : "W") + ((adj > 0) ? "N" : "S")
							+ " angle : " + angle);
					result.getGeometry().getLocation().setAngle(angle);
					result.getGeometry().getLocation().setDistance(distance);
				}

				// After all these calculations are done, Open up the Camera
				// and
				// place the image over it.
				Intent intent = new Intent(HomeActivity.this,
						CameraActivity.class);
				intent.putExtra(CameraActivity.EXTRA_RESPONSE, response);
				startActivity(intent);
			}
		};

		// The web-service request is being set using the volley library,
		GsonRequest<Response> request = new GsonRequest<Response>(
				queryHolder.toString(), Response.class, null, listener,
				errorListener);

		// Adding the request to the Volley queue to process this request.
		app.addToRequestQueue(request);
		mProgressDialog.show();
	}

	public static boolean isLocationEnabled(Context context) {
		int locationMode = 0;
		String locationProviders;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			try {
				locationMode = Settings.Secure.getInt(
						context.getContentResolver(),
						Settings.Secure.LOCATION_MODE);

			} catch (SettingNotFoundException e) {
				e.printStackTrace();
			}

			return locationMode != Settings.Secure.LOCATION_MODE_OFF;

		} else {
			locationProviders = Settings.Secure.getString(
					context.getContentResolver(),
					Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			return !TextUtils.isEmpty(locationProviders);
		}

	}

}
