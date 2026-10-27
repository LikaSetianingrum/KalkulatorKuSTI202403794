package com.swu.kalkulator;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //deklarasi variabel
    EditText editNum1, editNum2;
    Button buttonPlus, buttonMin, buttonMulti, buttonDiv, buttonCalc, buttonClear;
    TextView textResult;

    String operatorTerpilih = "";

    public static ArrayList<Riwayat> dataRiwayat = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        if (androidx.core.content.ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            androidx.core.app.ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);

        }

//inisialisasi variabel
        editNum1 = findViewById(R.id.editNum1);
        editNum2 = findViewById(R.id.editNum2);
        buttonPlus = findViewById(R.id.buttonPlus);
        buttonMin = findViewById(R.id.buttonMin);
        buttonMulti = findViewById(R.id.buttonMulti);
        buttonDiv = findViewById(R.id.buttonDiv);
        buttonCalc = findViewById(R.id.buttonCalc);
        buttonClear = findViewById(R.id.buttonClear);
        textResult = findViewById(R.id.textResult);

//tambah event
        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operatorTerpilih = "+";
                Toast.makeText(MainActivity.this, "Operator + dipilih",Toast.LENGTH_SHORT).show();
            }
        });
        buttonMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operatorTerpilih = "-";
                Toast.makeText(MainActivity.this, "Operator - dipilih",Toast.LENGTH_SHORT).show();
            }
        });
        buttonMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operatorTerpilih = "x";
                Toast.makeText(MainActivity.this, "Operator x dipilih",Toast.LENGTH_SHORT).show();
            }
        });
        buttonDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operatorTerpilih = ":";
                Toast.makeText(MainActivity.this, "Operator : dipilih",Toast.LENGTH_SHORT).show();
            }
        });
        buttonCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitungHasil();
            }
        });
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNum1.setText("");
                editNum2.setText("");
                textResult.setText("");
                operatorTerpilih= "";
                editNum1.requestFocus();
            }
        });
    }

    private void ambilDataGps() {
        android.location.LocationManager locationManager = (android.location.LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = null;

        try {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        if (location != null) {
            String latitude = String.valueOf(location.getLatitude());
            String longitude = String.valueOf(location.getLongitude());

            try {
                // Langsung buka aplikasi Google Maps berdasarkan koordinat GPS HP
                String uriGeo = "geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "(Lokasi Saya)";
                Intent intentMaps = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(uriGeo));

                if (intentMaps.resolveActivity(getPackageManager()) != null) {
                    startActivity(intentMaps);
                } else {
                    // Fallback ke browser jika aplikasi Maps tidak ada
                    String webMaps = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
                    Intent intentWeb = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(webMaps));
                    startActivity(intentWeb);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Gagal membuka Google Maps", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Pastikan GPS HP Menyala dan lokasi terdeteksi", Toast.LENGTH_SHORT).show();
        }

    }
    private void hitungHasil(){
        String input1 = editNum1.getText().toString();
        String input2 = editNum2.getText().toString();

//validasi angka terisi atau tidak

        if(input1.isEmpty() || input2.isEmpty()){
            Toast.makeText(this, "Mohon isi kedua angka terlbih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        if(operatorTerpilih.isEmpty()){
            Toast.makeText(this, "Mohon pilih operator dipilih terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

//konversi tipe data string

        double angka1 = Double.parseDouble(input1);
        double angka2 = Double.parseDouble(input2);
        double hasil = 0;
        String teksPerhitungan = angka1 + " " + operatorTerpilih + " " + angka2;

//proses perhitungan
        switch (operatorTerpilih){
            case "+":
                hasil = angka1 + angka2;
                break;
            case "-":
                hasil = angka1 - angka2;
                break;
            case "x":
                hasil = angka1 * angka2;
                break;
            case ":":
                hasil = angka1 / angka2;
                break;
        }

        textResult.setText(String.valueOf(hasil));
        dataRiwayat.add(new Riwayat(teksPerhitungan, String.valueOf(hasil)));

        //fitur fake calculator
        if (hasil == 0.0){
            android.content.Intent intentKamera = new android.content.Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivity(intentKamera);

        } else if (hasil == 3.0) {
            try {
                Toast.makeText(MainActivity.this, "Mode Audio Rahasia Aktif!", Toast.LENGTH_SHORT).show();
                textResult.setText("🎵 Memutar Audio...");

                final MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, Settings.System.DEFAULT_RINGTONE_URI);

                if (mediaPlayer != null) {
                    mediaPlayer.start();

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                            textResult.setText("3.0");
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Gagal memutar audio", Toast.LENGTH_SHORT).show();
            }

        }else if (hasil == 7.0){
            android.content.Intent intenGambar = new android.content.Intent(MainActivity.this, ImageActivity.class);
            startActivity(intenGambar);

        } else if (hasil == 9.0){
            ambilDataGps();

        } else if (hasil == 4.0){
        Intent intentSensor = new Intent(MainActivity.this, SensorActivity.class);
        startActivity(intentSensor);
    }
    }

}