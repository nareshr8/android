package in.nrsh.augumentedreality;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback {

	private SurfaceHolder mHolder;
	private Camera mCamera;

	public CameraPreview(Context context, AttributeSet attrs) {
		super(context, attrs);

		// Added to all custom views so that it appears in Custom Previews tab
		// in eclipse IDE. Needed in every possible object initiation
		// (constructor)
		isInEditMode();
	}

	public CameraPreview(Context context, Camera camera) {
		super(context);
		mCamera = camera;
		mHolder = getHolder();
		mHolder.addCallback(this);
		isInEditMode();

		// deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	}

	/**
	 * If your preview can change or rotate, take care of those events here.
	 */
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// Make sure to stop the preview before resizing or reformatting it.
		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}
		// stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {

		}

		// set preview size and make any resize,rotate or reformatting changes
		// start preview with new settings
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
		} catch (Exception e) {
			Log.d("Error starting camera preview: ", e.getMessage());
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.setDisplayOrientation(90);
			mCamera.startPreview();
		} catch (IOException e) {
			Log.d("Error setting camera preview: ", e.getMessage());
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		mCamera.release();
	}

}
