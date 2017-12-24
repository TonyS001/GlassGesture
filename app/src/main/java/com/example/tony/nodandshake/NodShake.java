package com.example.tony.nodandshake;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class NodShake extends AppCompatActivity {

    private SensorManager sensorManager;
    private TextView nod;
    private TextView shake;
    private double nodcount = 0;
    private double shakecount = 0;
    private long PreTime=0;
    private long TStart=0;
    private long charge=0;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.nod);
        nod = (TextView)findViewById(R.id.nod);
        shake = (TextView)findViewById(R.id.shake);
        mContext = getApplicationContext();

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        Sensor AccelerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, AccelerSensor, SensorManager.SENSOR_DELAY_NORMAL);

        //TStart = SystemClock.elapsedRealtime();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(sensorManager != null) {
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            FileHelper fHelper = new FileHelper(mContext);
            String filename = "data.txt";
            String filetext = String.valueOf(y);
            try {
                //保存文件名和内容
                fHelper.save(filename, filetext);
            } catch (Exception e) {
                //写入异常时
                e.printStackTrace();
            }

            //PreTime = SystemClock.elapsedRealtime();

            if(z>2 ){
                //if (TStart==0)
                    //TStart=PreTime;
                //else{
                    //if(PreTime-TStart>)
                //}
                if (charge==0) {
                    charge = 1;
                    TStart = SystemClock.elapsedRealtime();
                }
            }
            if(Math.abs(y)>10.1  ) {
                if (charge==1){
                    PreTime = SystemClock.elapsedRealtime();
                    if ((PreTime-TStart)>170){
                        nodcount++;
                        charge=0;
                    }
                    else{
                        charge=0;
                    }
                }
            }

            nod.setText("点头次数为"+String.valueOf(nodcount));
            shake.setText(String.valueOf(PreTime-TStart));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}
