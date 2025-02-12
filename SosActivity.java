package com.eyantra.mind_cure_ai;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        // Initializing buttons
        Button callEmergencyButton = findViewById(R.id.callEmergencyButton);
        Button backButton = findViewById(R.id.backButton);

        // Call Emergency Button - Opens Dialer with emergency number
        callEmergencyButton.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:112")); // You can change this to another emergency number
            startActivity(callIntent);
        });

        // Back Button - Moves back to HomeActivity
        backButton.setOnClickListener(v -> {
            Intent homeIntent = new Intent(SosActivity.this, HomeActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
            finish(); // Ensures SosActivity is removed from the back stack
        });
    }
}
