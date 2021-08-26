package com.vulpes.iotwatermqtt;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;

public class Splash extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 4500;


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        if (Build.VERSION.SDK_INT >= 28) {
            getWindow().setFlags(512, 512);
            getWindow().getAttributes().layoutInDisplayCutoutMode = 1;
        }

        setContentView(R.layout.splashscreen);

        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(Splash.this, Connect.class);
            Splash.this.startActivity(mainIntent);
            Splash.this.finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}
