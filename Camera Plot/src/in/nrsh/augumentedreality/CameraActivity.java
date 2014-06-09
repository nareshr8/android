package in.nrsh.augumentedreality;

import in.nrsh.augumentedreality.bean.Response;
import in.nrsh.augumentedreality.bean.Results;

import java.util.Locale;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class CameraActivity extends Activity implements SensorEventListener,
		OnClickListener {

	/**
	 * The Distance from the current location that we look at (in meters).
	 */
	public static final int DISTANCE_COVERED = 1000;

	int COUNT;

	/**
	 * The key of the {@link Bundle} value to be passed to this activity. Would
	 * carry the the list of places that are needed to be shown.
	 */
	protected static final String EXTRA_RESPONSE = "ATMcenters";

	/**
	 * The camera instance.
	 */
	private Camera mCamera;

	/**
	 * The custom camera view.
	 */
	private CameraPreview mCameraPreview;

	/**
	 * The {@link Bundle} value that we extract to get the list of places.
	 */
	private Response placesNearby;

	/**
	 * {@link SensorManager} to determine the Phone angle with respect to the
	 * Magnetic north.
	 */
	private SensorManager mSensorMgr;

	/**
	 * The sensor that is used to get the angle between the current location and
	 * the magnetic north.
	 */
	private Sensor rotaionSensor;

	/**
	 * The FrameLayout over which the image is to be overlayed.
	 */
	private FrameLayout overlayFrame;

	/**
	 * The device height
	 */
	private int deviceHeight;

	/**
	 * The device width.
	 */
	private int deviceWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Initializing the activity
		super.onCreate(savedInstanceState);
		setCameraObject();
		mCameraPreview = new CameraPreview(this, mCamera);
		setContentView(R.layout.activity_camera);
		((FrameLayout) findViewById(R.id.activity_camera))
				.addView(mCameraPreview);

		// The overlayframe contains all the views that are dynamically added on
		// the top of the camera view.
		overlayFrame = ((FrameLayout) findViewById(R.id.overlayFrame));

		// Making sure that the images are above the camera (in Z-axis) so that
		// it becomes
		// visible
		overlayFrame.bringToFront();

		// Created a dummy brace to instruct Java that obj is no more needed and
		// can be destroyed after the end of the braces.
		{
			Object obj = (Response) getIntent().getExtras().get(EXTRA_RESPONSE);
			if (obj != null) {
				placesNearby = (Response) obj;
			}

			// Getting the device width and height
			DisplayMetrics outMetrics = getResources().getDisplayMetrics();
			deviceHeight = outMetrics.heightPixels - 20;
			deviceWidth = outMetrics.widthPixels - 20;
		}

		// Requesting for sensor that uses azmith angle. The angle with respect
		// to the Magnetic north of the earth
		mSensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
		rotaionSensor = mSensorMgr.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		if (rotaionSensor == null) {
			Toast.makeText(this,
					"Your phone is not smart enough to run this application",
					Toast.LENGTH_LONG).show();
			finish();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Registering for updates in the azimmuth angle with sampling rate
		// matching to UI delay. This helps in getting samples at an interval
		// that matches the UI refresh rate.
		// System.out.println("Sensor registered?? "
		mSensorMgr.registerListener(this, rotaionSensor,
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Unregister the sensor manager as soon as it is no more needed to
		// detect.This must be done as, the sampling is no more needed as soon
		// as the screen UI goes to the background.
		mSensorMgr.unregisterListener(this);
	}

	/**
	 * Setting up the camera that needs to be displayed
	 */
	private void setCameraObject() {
		if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			if (mCamera == null) {
				mCamera = Camera.open();
			}
		} else {
			Toast.makeText(this, getString(R.string.camera_unavailable),
					Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// Unregistering the sensor so that this method is not called the second
		// time be for the processing for the first time is finished. As the
		// time interval between subsequent updates is around 0.2 seconds
		mSensorMgr.unregisterListener(this);

		// Getting the azimmuth angle, the angle with respect to the north pole
		float azimuthAngleFloat = event.values[0];
		double azimmuthAngle = Double.parseDouble(String.format(Locale.ENGLISH,
				"%f", azimuthAngleFloat));
		Log.w(TEXT_SERVICES_MANAGER_SERVICE, "Azmirth angle: " + azimmuthAngle);
		overlayFrame.removeAllViews();
		// Getting the list of places and checking which place is covered in the
		// current coverage angle.
		for (Results result : placesNearby.getResults()) {

			Double angleDouble = (result.getGeometry().getLocation().getAngle() - azimmuthAngle);
			double angle = angleDouble.intValue();
			if (Math.abs(angle) < mCamera.getParameters()
					.getHorizontalViewAngle()) {
				LayoutParams wrapPrarams = new FrameLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

				TextView txtView = new TextView(this);
				txtView.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.pinpoint));
				txtView.setTextColor(Color.BLACK);
				txtView.setGravity(Gravity.CENTER);
				txtView.setText(result.getName().subSequence(0, 1));
				txtView.setOnClickListener(this);
				txtView.setTag(result);

				wrapPrarams.gravity = Gravity.LEFT;
				Double deviation = ((angle / mCamera.getParameters()
						.getHorizontalViewAngle()) * (deviceWidth / 2));
				wrapPrarams.gravity = Gravity.LEFT;
				wrapPrarams.leftMargin = (deviceWidth / 2)
						+ deviation.intValue() - (txtView.getWidth() / 2);

				int distance = result.getGeometry().getLocation().getDistance();
				Double factor = Double.valueOf(DISTANCE_COVERED);
				Double distanceDouble = deviceHeight
						- ((distance / factor) * (deviceHeight));
				wrapPrarams.topMargin = (distanceDouble.intValue());

				overlayFrame.addView(txtView, wrapPrarams);
			}
		}

		// Registering for sensor updates as the method call is finished its
		// work for the first time.
		mSensorMgr.registerListener(this, rotaionSensor,
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onClick(View v) {
		// Setting a Toast message to be shown when the Image projected on the
		// screen is clicked.
		Results result = (Results) v.getTag();
		Toast.makeText(
				this,
				result.getName() + " at a distance of "
						+ result.getGeometry().getLocation().getDistance()
						+ " meters.", Toast.LENGTH_LONG).show();
	}

}
