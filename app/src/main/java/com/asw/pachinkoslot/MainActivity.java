package com.asw.pachinkoslot;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    private Button roll, stop;
    private ImageView imgview1, imgview2, imgview3;
    private Thread t1, t2, t3;
    private boolean status1, status2, status3;
    private int index;
    private int []imagearray = {R.drawable.symbol_crown,R.drawable.symbol_heart,R.drawable.symbol_keyblade,
            R.drawable.symbol_keyhole,R.drawable.symbol_lucky_charm,R.drawable.symbol_mickey};
    int i, j, k;

    SensorManager sm;
    Sensor gyroSensor, proxySensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //roll = (Button)findViewById(R.id.button);
        //stop = (Button)findViewById(R.id.button2);

        imgview1 = (ImageView)findViewById(R.id.imageView);
        imgview2 = (ImageView)findViewById(R.id.imageView2);
        imgview3 = (ImageView)findViewById(R.id.imageView3);

        //roll.setOnClickListener(startRoll);
        //stop.setOnClickListener(endRoll);

        status1 = status2 = status3 = false;

        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        gyroSensor = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        proxySensor = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        Toast.makeText(this, "Selamat Datang!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume(){
        super.onResume();
        sm.registerListener(proximityEL, proxySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(gyroEL, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause(){
        super.onPause();
        sm.unregisterListener(proximityEL);
        sm.unregisterListener(gyroEL);

    }

    private View.OnClickListener startRoll = new View.OnClickListener(){
        public void onClick(View v) {
            if(t1 == null && t2 == null && t3 == null){
                status1 = status2 = status3 = true;
                index = 1;
                i = (int)(Math.random() * 6);
                j = (int)(Math.random() * 6);
                k = (int)(Math.random() * 6);

                t1 = new Thread(runHandler(1));
                t1.start();
                t2 = new Thread(runHandler(2));
                t2.start();
                t3 = new Thread(runHandler(3));
                t3.start();
            }
        }
    };

    private View.OnClickListener endRoll = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(t1 != null && index == 1){
                status1 = false;
                t1.interrupt();
                t1 = null;
            }else if(t2 != null && index == 2){
                status2 = false;
                t2.interrupt();
                t2 = null;
            }else if(t3 != null && index == 3){
                status3 = false;
                t3.interrupt();
                t3 = null;

                if(i == j && i == k && j == k)
                    Toast.makeText(MainActivity.this, "Selamat Anda Menang!!", Toast.LENGTH_LONG).show();

            }
            index++;
        }
    };

    private void startRoll(){
        if(t1 == null && t2 == null && t3 == null){
            status1 = status2 = status3 = true;
            index = 1;
            i = (int)(Math.random() * 6);
            j = (int)(Math.random() * 6);
            k = (int)(Math.random() * 6);

            t1 = new Thread(runHandler(1));
            t1.start();
            t2 = new Thread(runHandler(2));
            t2.start();
            t3 = new Thread(runHandler(3));
            t3.start();
        }
    }

    private void endRoll(){
        if(t1 != null && index == 1){
            status1 = false;
            t1.interrupt();
            t1 = null;
        }else if(t2 != null && index == 2){
            status2 = false;
            t2.interrupt();
            t2 = null;
        }else if(t3 != null && index == 3){
            status3 = false;
            t3.interrupt();
            t3 = null;

            if(i == j && i == k && j == k)
                Toast.makeText(MainActivity.this, "Selamat Anda Menang!!", Toast.LENGTH_LONG).show();

        }
        index++;

    }

    private Runnable runHandler(final int no){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if(status1 && no == 1){
                    try {
                        imgview1.post(new Runnable() {
                            @Override
                            public void run() {
                                imgview1.setImageResource(imagearray[i]);
                            }
                        });
                        i++;
                        if (i >= imagearray.length)
                            i = 0;
                        imgview1.postDelayed(this, 500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if(status2 && no == 2){
                    try {
                        imgview2.post(new Runnable() {
                            @Override
                            public void run() {
                                imgview2.setImageResource(imagearray[j]);
                            }
                        });
                        j--;
                        if (j < 0)
                            j = imagearray.length - 1;
                        imgview2.postDelayed(this, 500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if(status3 && no == 3){
                    try {
                        imgview3.post(new Runnable() {
                            @Override
                            public void run() {
                                imgview3.setImageResource(imagearray[k]);
                            }
                        });
                        k++;
                        if (k >= imagearray.length)
                            k = 0;
                        imgview3.postDelayed(this, 500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        return r;
    }

    private SensorEventListener proximityEL = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.values[0] < proxySensor.getMaximumRange()) {
                // Detected something nearby
                startRoll();
            } else {
                // Nothing is nearby
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    private SensorEventListener gyroEL = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.values[2] > 2.5f || event.values[2] < -2.5f) {
                endRoll();

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

}
