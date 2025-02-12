package com.eyantra.mind_cure_ai;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class BookingActivity extends AppCompatActivity {

    private ListView psychiatristListView;
    private ArrayList<String> psychiatristNames;
    private ArrayList<String> psychiatristContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        psychiatristListView = findViewById(R.id.psychiatristListView);


        // Sample Data - Can be replaced with real API data
        psychiatristNames = new ArrayList<>();
        psychiatristContacts = new ArrayList<>();

        psychiatristNames.add("1. Dr. B.L Dhaka - Psychiatrist");
        psychiatristContacts.add("tel:9887083066");

        psychiatristNames.add("2. Dr. Sumit Kumar Gupta - Addiction Psychiatrist");
        psychiatristContacts.add("tel:1140036735");

        psychiatristNames.add("3. Dr. Rahul Mehta - Clinical Psychologist");
        psychiatristContacts.add("tel:8527676692");

        psychiatristNames.add("4. Dr. Neha Verma - Mental Health Specialist");
        psychiatristContacts.add("tel:8046961837");

        psychiatristNames.add("5. Dr. Rajesh Kumar - Psychiatrist");
        psychiatristContacts.add("tel:1141168734");

        psychiatristNames.add("6. Dr. Ankit Gupta - Consultant Psychiatrist");
        psychiatristContacts.add("tel:7042511847");

        // Setting up ListView with an Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, psychiatristNames);
        psychiatristListView.setAdapter(adapter);

        // Clicking an item will open the dialer to call the selected psychiatrist
        psychiatristListView.setOnItemClickListener((adapterView, view, position, id) -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse(psychiatristContacts.get(position)));
            startActivity(callIntent);
        });
    }
}
