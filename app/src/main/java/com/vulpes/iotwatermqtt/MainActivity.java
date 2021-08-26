package com.vulpes.iotwatermqtt;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    String clientId = MqttClient.generateClientId();
    MqttAndroidClient client =
            new MqttAndroidClient(  MainActivity.this , "tcp://io.adafruit.com:1883",
                    clientId);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        if (Build.VERSION.SDK_INT >= 28) {
            getWindow().setFlags(512, 512);
            getWindow().getAttributes().layoutInDisplayCutoutMode = 1;
        }


        setContentView(R.layout.activity_main);

        ProgressBar progressLeft = findViewById(R.id.progressLeft);
        ProgressBar progressRight = findViewById(R.id.progressRight);

        ProgressAnim animLeft = new ProgressAnim(progressLeft , 0 , 0);
        ProgressAnim animRight = new ProgressAnim(progressRight , 0 , 0);
        animLeft.setDuration(1000);
        animRight.setDuration(1000);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        options.setUserName("WhiteVulpes");
        options.setPassword("aio_RyPQ09rYQgUeAHO3SceSIUyjx0NC".toCharArray());

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    subs();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("ItFailed", "onFailure: ");
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String prog = new String(message.getPayload());
                animLeft.from = animLeft.to;
                animLeft.to = Integer.parseInt(prog);
                animRight.from = animRight.to;
                animRight.to = Integer.parseInt(prog);
                progressLeft.startAnimation(animLeft);
                progressRight.startAnimation(animRight);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });


    }


    public void stopbtn(View view) {
        LottieAnimationView stop = findViewById(R.id.stop);

        stop.playAnimation();
        String topic = "WhiteVulpes/feeds/switch";
        String payload = "OFF";
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    public void startbtn(View view) {
        LottieAnimationView start = findViewById(R.id.start);

        start.playAnimation();
        String topic = "WhiteVulpes/feeds/switch";
        String payload = "ON";
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    public void subs(){
        String topic = "WhiteVulpes/feeds/moisture-sensor";
        int qos = 1;
        try {
            IMqttToken subToken = client.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("Yayy!!!", "onSuccess: ");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    Log.d("Noooo", "onSuccess: ");

                }
            });
        }
        catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public class ProgressAnim extends Animation {
        private ProgressBar progressBar;
        private float from;
        private float  to;

        public ProgressAnim(ProgressBar progressBar, float from, float to) {
            super();
            this.progressBar = progressBar;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            progressBar.setProgress((int) value);
        }

    }


}