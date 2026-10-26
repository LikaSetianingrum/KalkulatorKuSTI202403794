package com.swu.kalkulator;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer, gyroscope;
    private TextView textAccel, textGyro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        textAccel = findViewById(R.id.textAccel);
        textGyro = findViewById(R.id.textGyro);

        // Inisialisasi Sensor Manager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Daftarkan listener sensor saat activity aktif
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (gyroscope != null) {
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Matikan listener saat activity ditutup/tidak aktif agar hemat baterai
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Tangkap perubahan nilai sensor secara real-time
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            String val = String.format("Accelerometer (X, Y, Z):\nX: %.2f\nY: %.2f\nZ: %.2f",
                    event.values[0], event.values[1], event.values[2]);
            textAccel.setText(val);
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            String val = String.format("Gyroscope (X, Y, Z):\nX: %.2f\nY: %.2f\nZ: %.2f",
                    event.values[0], event.values[1], event.values[2]);
            textGyro.setText(val);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Tidak perlu diubah, biarkan kosong
    }
}