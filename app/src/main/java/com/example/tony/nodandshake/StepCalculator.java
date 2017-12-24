package com.example.tony.nodandshake;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class StepCalculator extends AppCompatActivity {

    private SensorManager sensorManager;
    private TextView stepnumber;
    private double stepcount = 0;

    private long TStart;
    private float[] mGravity = new float[3];
    private float[] mAccelerometer = new float[3];
    private HistoryValue mHistory_ACC_Z,mHistory_ACC_Y;
    private int count = 0;
    private long PreTime;
    private int Type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.step);
        stepnumber = (TextView)findViewById(R.id.stepNumber);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        Sensor AccelerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, AccelerSensor, SensorManager.SENSOR_DELAY_NORMAL);

        TStart = SystemClock.elapsedRealtime();
        mHistory_ACC_Z = new HistoryValue(20);
        mHistory_ACC_Y = new HistoryValue(20);
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

            final float alpha = 0.95f;

            mGravity[0] = alpha * mGravity[0] + (1 - alpha) * x;
            mGravity[1] = alpha * mGravity[1] + (1 - alpha) * y;
            mGravity[2] = alpha * mGravity[2] + (1 - alpha) * z;

            mAccelerometer[0] = x - mGravity[0];
            mAccelerometer[1] = y - mGravity[1];
            mAccelerometer[2] = z - mGravity[2];

            mHistory_ACC_Z.AddValue(SystemClock.elapsedRealtime()-TStart,mAccelerometer[2]);
            mHistory_ACC_Y.AddValue(SystemClock.elapsedRealtime()-TStart,mAccelerometer[1]);

            if (mHistory_ACC_Y.GetIndex(-1)[1] > 3 || mHistory_ACC_Y.GetIndex(-1)[1] < -3)
                count = 0;

            if (mHistory_ACC_Y.GetIndex(-1)[1] > mHistory_ACC_Y.GetIndex(-2)[1] && mHistory_ACC_Y.GetIndex(-1)[1] > mHistory_ACC_Y.GetIndex(0)[1] && mHistory_ACC_Y.GetIndex(-1)[1] > 0.5 && mHistory_ACC_Y.GetIndex(-1)[1] < 3) {
                if (SystemClock.elapsedRealtime() - TStart - PreTime < 750) {
                    if (Type == -1 && SystemClock.elapsedRealtime() - TStart - PreTime > 100) {
                        count++;
                        if (count >= 1)//edited
                            stepcount = stepcount + 0.5;
                    }
                } else
                    count = 0;
                PreTime = SystemClock.elapsedRealtime() - TStart;
                Type = 1;
            }

            if (mHistory_ACC_Y.GetIndex(-1)[1] < mHistory_ACC_Y.GetIndex(-2)[1] && mHistory_ACC_Y.GetIndex(-1)[1] < mHistory_ACC_Y.GetIndex(0)[1] && mHistory_ACC_Y.GetIndex(-1)[1] < -0.5 && mHistory_ACC_Y.GetIndex(-1)[1] > -3) {
                if (SystemClock.elapsedRealtime() - TStart - PreTime < 750) {
                    if (Type == 1 && SystemClock.elapsedRealtime() - TStart - PreTime > 100) {
                        count++;
                        if (count >= 1)//edited
                            stepcount = stepcount + 0.5;
                    }
                } else
                    count = 0;
                PreTime = SystemClock.elapsedRealtime() - TStart;
                Type = -1;
            }

            stepnumber.setText("步数为"+String.valueOf(stepcount));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}