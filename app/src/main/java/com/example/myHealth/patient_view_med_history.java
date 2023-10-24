package com.example.myHealth;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;

public class patient_view_med_history extends AppCompatActivity {


    TextView top_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_med_history);
        // Set the top message to bold and underline
        top_message = findViewById(R.id.text_view_med_history_text);
        top_message.setPaintFlags(top_message.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
    }
}