package in.nrsh.augumentedreality;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

	private static final int SPLASH_TIME_OUT = 5000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		new Handler().postDelayed(new Runnable() {
			 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
 
                Intent mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                // close this activity
                SplashActivity.this.finish();
            }
        }, SPLASH_TIME_OUT);


		/**
		 * Setting the splash screen to the activity.
		 */
		setContentView(R.layout.activity_main);

	}

}
