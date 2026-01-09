package com.febi.konvertersuhu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class FtoCActivity extends AppCompatActivity {

    private EditText inputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftoc);
        inputEditText = findViewById(R.id.edit_text_input);
    }

    public void convertTemperature(View view) {
        String inputStr = inputEditText.getText().toString();

        if (inputStr.isEmpty()) {
            Toast.makeText(this, "Masukkan suhu Fahrenheit terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double fahrenheit = Double.parseDouble(inputStr);
            // Rumus: C = (F - 32) * 5/9
            double celsius = (fahrenheit - 32.0) * 5.0 / 9.0;

            Intent intent = new Intent(FtoCActivity.this, ResultActivity.class);
            intent.putExtra("RESULT", String.format("%.2f °C", celsius));
            // Mengubah label hasil ke Celcius
            intent.putExtra("UNIT_LABEL", "Hasil dalam Celcius:");
            startActivity(intent);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Input tidak valid. Masukkan angka.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed(View view) {
        super.onBackPressed();
    }
}