package com.febi.konvertersuhu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CtoFActivity extends AppCompatActivity {

    private EditText inputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctof);
        inputEditText = findViewById(R.id.edit_text_input);
    }

    public void convertTemperature(View view) {
        String inputStr = inputEditText.getText().toString();

        if (inputStr.isEmpty()) {
            Toast.makeText(this, "Masukkan suhu Celcius terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double celsius = Double.parseDouble(inputStr);
            // Rumus: F = C * 9/5 + 32
            double fahrenheit = (celsius * 9.0 / 5.0) + 32.0;

            Intent intent = new Intent(CtoFActivity.this, ResultActivity.class);
            intent.putExtra("RESULT", String.format("%.2f °F", fahrenheit));
            intent.putExtra("UNIT_LABEL", getString(R.string.hasil_dalam).replace("Farenheit", "Fahrenheit"));
            startActivity(intent);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Input tidak valid. Masukkan angka.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed(View view) {
        super.onBackPressed();
    }
}