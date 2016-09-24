package com.project.bluetoothtictactoe;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Handler h = new Handler();
        h.postDelayed(new SplashHandler(), 5000);
    }

    class SplashHandler implements Runnable {
        public void run() {
            startActivity(new Intent(getApplication(), StartupActivity.class));
            Splash.this.finish();
        }
    }
}