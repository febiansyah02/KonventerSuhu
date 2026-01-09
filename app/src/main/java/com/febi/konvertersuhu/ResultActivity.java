package com.febi.konvertersuhu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView resultLabel = findViewById(R.id.text_hasil_label);
        EditText resultEditText = findViewById(R.id.edit_text_result);

        Intent intent = getIntent();
        String result = intent.getStringExtra("RESULT");
        String unitLabel = intent.getStringExtra("UNIT_LABEL");

        if (result != null && unitLabel != null) {
            resultLabel.setText(unitLabel);
            resultEditText.setText(result);
        } else {
            resultLabel.setText("Kesalahan Hasil");
            resultEditText.setText("Data hasil konversi tidak ditemukan.");
        }
    }

    public void backToMainActivity(View view) {
        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}