package com.febi.konvertersuhu;

import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;

public class SplashActivity extends AppCompatActivity implements LocationListener {

    private static final int SPLASH_TIME_OUT = 3000;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private static final String DEFAULT_LOGO = "kota_bekasi";
    private static final String DEFAULT_GREETING = "HALO, SELAMAT DATANG";

    private LocationManager locationManager;
    private ImageView logoPemda;
    private TextView textGreeting;
    private boolean isProceeding = false;

    // Variabel untuk menyimpan hasil, agar bisa digunakan saat proceed
    private String determinedLogo = DEFAULT_LOGO;
    private String determinedGreeting = DEFAULT_GREETING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logoPemda = findViewById(R.id.logo_kota_bekasi);
        textGreeting = findViewById(R.id.text_selamat_datang);

        // Meminta Izin Lokasi saat runtime
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            // Meminta update lokasi (menggunakan Network Provider karena lebih cepat, ideal untuk splash screen)
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0, // update minimal waktu (ms)
                    0, // update minimal jarak (m)
                    this
            );
            // Juga ambil lokasi terakhir jika ada (jika GPS/Network belum aktif)
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (lastLocation != null) {
                // Langsung proses jika ada lokasi terakhir yang valid
                onLocationDetermined(lastLocation);
                return;
            }
        } catch (SecurityException e) {
            Toast.makeText(this, "Akses lokasi ditolak.", Toast.LENGTH_SHORT).show();
            onLocationDetermined(null); // Gunakan default
        }

        // Jika tidak ada lokasi yang langsung didapat, tunggu 3 detik lalu proceed
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isProceeding) {
                    // Jika 3 detik berlalu dan onLocationChanged belum dipanggil, gunakan default
                    onLocationDetermined(null);
                }
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Izin lokasi diperlukan.", Toast.LENGTH_LONG).show();
                onLocationDetermined(null); // Gunakan default jika izin ditolak
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
        onLocationDetermined(location);
    }

    // Method inti setelah lokasi didapat (atau tidak didapat)
    private void onLocationDetermined(Location location) {
        // Hentikan update lokasi jika masih berjalan
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }

        // Tentukan logo dan sambutan berdasarkan lokasi
        String[] result = determineRegion(location);
        determinedLogo = result[0];
        determinedGreeting = result[1];

        // Tampilkan ke UI
        displayLogoAndGreeting(determinedLogo, determinedGreeting);

        // Lanjutkan ke Main Activity
        proceedToMainActivity();
    }

    // LOGIKA UTAMA: DITAMBAHKAN PADANG DAN DENPASAR
    private String[] determineRegion(Location location) {
        String logoName = DEFAULT_LOGO;
        String greeting = DEFAULT_GREETING;

        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            // --- LOGIKA PENENTUAN LOKASI OTOMATIS BERDASARKAN KOORDINAT ---

            // 1. Kota Padang (Minang) - LUAR JAWA
            if (latitude > -0.94 && latitude < -0.89 && longitude > 100.3 && longitude < 100.4) {
                logoName = "logo_padang";
                greeting = "SALAMAIK DATANG";
            }
            // 2. Kota Denpasar (Bali) - LUAR JAWA
            else if (latitude > -8.67 && latitude < -8.62 && longitude > 115.20 && longitude < 115.25) {
                logoName = "logo_denpasar";
                greeting = "RAHAJENG RAWUH";
            }
            // 3. Kota Bandung (Sunda) - JAWA
            else if (latitude > -7.0 && latitude < -6.8 && longitude > 107.5 && longitude < 107.7) {
                logoName = "kota_bandung";
                greeting = "WILUJENG SUMPING";
            }
            // 4. Provinsi Jawa Tengah (Jawa) - JAWA
            else if (latitude > -7.5 && latitude < -6.5 && longitude > 109.5 && longitude < 110.5) {
                logoName = "provinsi_jateng";
                greeting = "SUGENG RAWUH";
            }
            // 5. Default / Kota Bekasi (Jika koordinat tidak masuk wilayah lain)
            else {
                logoName = DEFAULT_LOGO;
                greeting = DEFAULT_GREETING;
            }
        }
        return new String[]{logoName, greeting};
    }

    // Method untuk menampilkan logo dan sambutan
    private void displayLogoAndGreeting(String logoName, String greetingText) {
        // Ganti Logo
        int imageResourceId = getResources().getIdentifier(
                logoName,
                "drawable",
                getPackageName()
        );

        if (imageResourceId != 0) {
            logoPemda.setImageResource(imageResourceId);
        } else {
            logoPemda.setImageResource(R.drawable.kota_bekasi); // Fallback
        }

        // Ganti Teks Sambutan
        textGreeting.setText(greetingText);
    }

    private void proceedToMainActivity() {
        // Mencegah navigasi ganda
        if (isProceeding) return;
        isProceeding = true;

        // Delay 1500ms
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 1500);
    }

    // Metode LocationListener lainnya yang harus di-override
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onProviderDisabled(String provider) {
        // Jika lokasi dimatikan, gunakan logo/sambutan default dan proceed
        onLocationDetermined(null);
    }
}