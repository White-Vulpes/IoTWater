package com.vulpes.iotwatermqtt;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;


import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Connect extends Activity {


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);


        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        if (Build.VERSION.SDK_INT >= 28) {
            getWindow().setFlags(512, 512);
            getWindow().getAttributes().layoutInDisplayCutoutMode = 1;
        }

        setContentView(R.layout.connect);

        LottieAnimationView lottie = findViewById(R.id.animationView);

        lottie.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.e("Animation:","start");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                change();
                Log.e("Animation:","end");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.e("Animation:","repeat");

               MqttConnectOptions options = new MqttConnectOptions();
                options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
                options.setUserName("WhiteVulpes");
                options.setPassword("aio_RyPQ09rYQgUeAHO3SceSIUyjx0NC".toCharArray());

                String clientId = MqttClient.generateClientId();
                MqttAndroidClient client =
                        new MqttAndroidClient(  getApplicationContext() , "tcp://io.adafruit.com:1883",
                                clientId);

                try {
                    IMqttToken token = client.connect(options);
                    token.setActionCallback(new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.d("ItWorked", "onSuccess: ");
                            lottie.setAnimation("connected.json");
                            lottie.setRepeatCount(0);
                            lottie.playAnimation();
                            //MediaPlayer song = MediaPlayer.create( Connect.this, R.raw.connectiondone);
                            //song.start();
                            try {
                                client.disconnect();
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.d("ItFailed", "onFailure: ");
                        }
                    });
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void change(){
            Intent mainIntent = new Intent(Connect.this, MainActivity.class);
            Connect.this.startActivity(mainIntent);
            Connect.this.finish();
        }
    }
