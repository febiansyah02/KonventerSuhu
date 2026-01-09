package com.febi.konvertersuhu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openCtoFActivity(View view) {
        Intent intent = new Intent(MainActivity.this, CtoFActivity.class);
        startActivity(intent);
    }

    public void openFtoCActivity(View view) {
        Intent intent = new Intent(MainActivity.this, FtoCActivity.class);
        startActivity(intent);
    }

    public void openAboutActivity(View view) {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    // CATATAN: Method openSettingsActivity/openChangeRegionDialog dihapus
    // karena fitur logo dan sambutan sekarang otomatis di SplashActivity.
}