package com.example.ambulancedispatcher;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    ImageView tickIcon;
    TextView successMessage;
    TextView dataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Handle system bars padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tickIcon = findViewById(R.id.tickIcon);
        successMessage = findViewById(R.id.successMessage);
        dataView = findViewById(R.id.dataView);

        // Show success message
        successMessage.setText("Data Sent ✅");

        // Get JSON data passed from inputLayout
        String submittedData = getIntent().getStringExtra("submittedData");
        if (submittedData != null && !submittedData.isEmpty()) {
            dataView.setText(submittedData);
        } else {
            dataView.setText("No data received.");
        }
    }
}
