package com.example.ambulancedispatcher;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class inputLayout extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST = 1;

    private TextView latitudeText, longitudeText;
    private Button getLocationButton;
    private Spinner severitySpinner;
    private GridLayout symptomGrid;
    private TextView symptomCounter;
    Button submitButton;
    EditText otherInfoInput;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_input_layout);

        // Handle system bars padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        latitudeText = findViewById(R.id.latitudeText);
        longitudeText = findViewById(R.id.longitudeText);
        getLocationButton = findViewById(R.id.getLocationButton);
        severitySpinner = findViewById(R.id.severitySpinner);
        symptomGrid = findViewById(R.id.symptomGrid);
        symptomCounter = findViewById(R.id.symptomCounter);
        submitButton = findViewById(R.id.submitButton);
        otherInfoInput = findViewById(R.id.otherInfoInput);

        // Setup severity spinner
        ArrayAdapter<CharSequence> severityAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.severity_levels,
                android.R.layout.simple_spinner_item
        );
        severityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        severitySpinner.setAdapter(severityAdapter);

        // Setup location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationButton.setOnClickListener(v -> requestLocationPermissions());

        // Setup symptom selection checkboxes
        setupSymptomSelection();


        submitButton.setOnClickListener(v -> {
            // Collect location
            String latitude = latitudeText.getText().toString();
            String longitude = longitudeText.getText().toString();

            // Collect severity
            String severity = severitySpinner.getSelectedItem().toString();

            // Collect selected symptoms
            List<String> selectedSymptoms = new ArrayList<>();
            for (int i = 0; i < symptomGrid.getChildCount(); i++) {
                if (symptomGrid.getChildAt(i) instanceof CheckBox) {
                    CheckBox cb = (CheckBox) symptomGrid.getChildAt(i);
                    if (cb.isChecked()) {
                        selectedSymptoms.add(cb.getText().toString());
                    }
                }
            }

            // Collect other info
            String otherInfo = otherInfoInput.getText().toString();

            // Build JSON string
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("{");
            jsonBuilder.append("\"latitude\":\"").append(latitude).append("\",");
            jsonBuilder.append("\"longitude\":\"").append(longitude).append("\",");
            jsonBuilder.append("\"severity\":\"").append(severity).append("\",");

            jsonBuilder.append("\"symptoms\":[");
            for (int i = 0; i < selectedSymptoms.size(); i++) {
                jsonBuilder.append("\"").append(selectedSymptoms.get(i)).append("\"");
                if (i < selectedSymptoms.size() - 1) {
                    jsonBuilder.append(",");
                }
            }
            jsonBuilder.append("],");

            jsonBuilder.append("\"otherInfo\":\"").append(otherInfo).append("\"");
            jsonBuilder.append("}");

            String jsonData = jsonBuilder.toString();

            android.util.Log.d("SubmitData", jsonData);
            android.widget.Toast.makeText(this, "Data Submitted:\n" + jsonData, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(inputLayout.this, MainActivity.class);
            intent.putExtra("submittedData", jsonData);
            startActivity(intent);

        });


    }

    // Request both FINE and COARSE location permissions
    private void requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST
            );
        } else {
            fetchLocation();
        }
    }

    // Fetch location once permissions are granted
    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                latitudeText.setText("Latitude: " + location.getLatitude());
                longitudeText.setText("Longitude: " + location.getLongitude());
            } else {
                latitudeText.setText("Latitude: Unknown");
                longitudeText.setText("Longitude: Unknown");
            }
        });
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation(); // retry getting location
            } else {
                latitudeText.setText("Latitude: Permission denied");
                longitudeText.setText("Longitude: Permission denied");
            }
        }
    }

    // Setup symptom selection grid
    private void setupSymptomSelection() {
        String[] symptoms = {
                "Tenderness at injury site",
                "Able to move normally",
                "Moderate pain",
                "Visible swelling",
                "Deeper cuts (controlled bleeding)",
                "Difficulty moving injured part",
                "Limping",
                "Dizziness without loss of consciousness",
                "Severe pain",
                "Heavy bruising",
                "Suspected fracture",
                "Limited or no movement of limb"
        };

        List<CheckBox> symptomBoxes = new ArrayList<>();

        for (String symptom : symptoms) {
            CheckBox box = new CheckBox(this);
            box.setText(symptom);
            box.setTextSize(14);
            box.setPadding(8, 8, 8, 8);
            symptomGrid.addView(box);
            symptomBoxes.add(box);
        }

        // Update counter dynamically
        for (CheckBox box : symptomBoxes) {
            box.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int selectedCount = 0;
                for (CheckBox cb : symptomBoxes) {
                    if (cb.isChecked()) selectedCount++;
                }
                symptomCounter.setText("Selected: " + selectedCount + " / Minimum: 7");
            });
        }
    }
}
