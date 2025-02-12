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

public class IntroActivity extends AppCompatActivity {

    private ImageView breathingImage;
    private TextView deepBreathText;
    private Button skipButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Initialize UI elements
        breathingImage = findViewById(R.id.breathingImage);
        deepBreathText = findViewById(R.id.deepBreathText);
        skipButton = findViewById(R.id.skipButton);

        // Apply fade-in & fade-out animation for text
        Animation fadeTextAnimation = new AlphaAnimation(0, 1);
        fadeTextAnimation.setDuration(2000);
        fadeTextAnimation.setRepeatMode(Animation.REVERSE);
        fadeTextAnimation.setRepeatCount(Animation.INFINITE);
        deepBreathText.startAnimation(fadeTextAnimation);

        // Load and start breathing animation with error handling
        try {
            Animation breathingAnimation = AnimationUtils.loadAnimation(this, R.anim.breathing_animation);
            breathingImage.startAnimation(breathingAnimation);
        } catch (Exception e) {
            breathingImage.setImageResource(R.drawable.breathing_image); // Fallback image
            Toast.makeText(this, "Breathing animation not found. Using static image.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        // Apply soft button press animation
        Animation buttonClickAnimation = AnimationUtils.loadAnimation(this, R.anim.button_click_animation);
        skipButton.setOnClickListener(v -> {
            v.startAnimation(buttonClickAnimation);
            navigateToHome();
        });
    }

    // Navigate to HomeActivity with a smooth transition
    private void navigateToHome() {
        try {
            Intent intent = new Intent(IntroActivity.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish(); // Close the intro activity
        } catch (Exception e) {
            Toast.makeText(IntroActivity.this, "Error opening HomeActivity", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
