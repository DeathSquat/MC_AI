package com.eyantra.mind_cure_ai;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private ImageView breathingImage;
    private TextView deepBreathText;
    private Button chatButton, sosButton, bookSessionButton, gameButton; // Added gameButton

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initializing Views
        deepBreathText = findViewById(R.id.deepBreathText); // FIXED: Missing initialization
        breathingImage = findViewById(R.id.breathingImage);
        chatButton = findViewById(R.id.chatButton);
        sosButton = findViewById(R.id.sosButton);
        bookSessionButton = findViewById(R.id.bookSessionButton);
        gameButton = findViewById(R.id.gameButton); // Added initialization

        // Fade-in animation for text
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(2000);
        deepBreathText.startAnimation(fadeIn);

        // Load and start breathing animation
        try {
            Animation breathingAnimation = AnimationUtils.loadAnimation(this, R.anim.breathing_animation);
            breathingImage.startAnimation(breathingAnimation);
        } catch (Exception e) {
            breathingImage.setImageResource(R.drawable.breathing_image); // Set fallback image
            Toast.makeText(this, "Animation not found. Showing static image.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        // Chat button action (Navigate to ChatActivity)
        chatButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        // SOS Button Action (Navigate to Emergency Help Page)
        sosButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SosActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
        });

        // Book Session Button Action (Navigate to Booking Page)
        bookSessionButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, BookingActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
        });

        // Game Button Action (Navigate to GameActivity)
        gameButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, GamesActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left); // Smooth transition effect
        });
    }
}
