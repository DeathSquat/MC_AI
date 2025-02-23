package com.eyantra.mind_cure_ai;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.Random;

public class BreathingExerciseActivity extends AppCompatActivity {

    private ImageView breathCircle;
    private TextView instructionText, timerText, quoteText;
    private Button startButton, stopButton;
    private MediaPlayer mediaPlayer;
    private CountDownTimer countDownTimer;
    private boolean isBreathing = false;

    private final String[] quotes = {
            "You are loved 💙",
            "Stay motivated 💪",
            "Breathe in peace, breathe out stress 😌",
            "Believe in yourself ✨",
            "Every breath is a new beginning 🌿",
            "You are enough 💖",
            "One step at a time 🏞️",
            "Inner peace starts with a deep breath 🧘",
            "Let go of worries, embrace the moment 🌊",
            "Your mind is your superpower ⚡"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathing_exercise);

        // **Toolbar with Back Button**
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            getSupportActionBar().setTitle("Breathing Exercise");
        }

        // **Initialize UI Elements**
        breathCircle = findViewById(R.id.breathCircle);
        instructionText = findViewById(R.id.instructionText);
        timerText = findViewById(R.id.timerText);
        quoteText = findViewById(R.id.quoteText);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);

        // **Background Music**
        mediaPlayer = MediaPlayer.create(this, R.raw.relaxing_music);
        mediaPlayer.setLooping(true);

        startButton.setOnClickListener(v -> startBreathingExercise());
        stopButton.setOnClickListener(v -> stopBreathingExercise());
    }

    private void startBreathingExercise() {
        if (isBreathing) return;
        isBreathing = true;
        mediaPlayer.start();

        // Update quote randomly
        String[] quotes = {
                "You are Loved 💙",
                "Stay motivated 💪",
                "Breathe in Peace, Breathe out Stress 🌿",
                "You are Strong 💪",
                "Relax, You got this! 💜",
                "Believe in yourself ✨",
                "Every breath is a new beginning 🌿",
                "You are enough 💖",
                "One step at a time 🏞️",
                "Inner peace starts with a deep breath 🧘",
                "Let go of worries, embrace the moment 🌊",
                "Your mind is your superpower ⚡"
        };
        int randomIndex = new Random().nextInt(quotes.length);
        TextView quoteText = findViewById(R.id.quoteText);
        quoteText.setText(quotes[randomIndex]);

        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                timerText.setText("Time Left: " + seconds + "s");
            }

            @Override
            public void onFinish() {
                timerText.setText("Session Complete!");
                stopBreathingExercise();
            }
        }.start();

        startBreathCycle();
    }

    private void startBreathCycle() {
        if (!isBreathing) return;

        // **Inhale - Expand (Zoom In)**
        ScaleAnimation inhale = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        inhale.setDuration(4000);
        inhale.setFillAfter(true);

        // **Hold After Inhale (Pause)**
        ScaleAnimation holdAfterInhale = new ScaleAnimation(1.5f, 1.5f, 1.5f, 1.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        holdAfterInhale.setDuration(2000);
        holdAfterInhale.setFillAfter(true);

        // **Exhale - Shrink (Zoom Out)**
        ScaleAnimation exhale = new ScaleAnimation(1.5f, 1.0f, 1.5f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        exhale.setDuration(4000);
        exhale.setFillAfter(true);

        // **Hold After Exhale (Pause)**
        ScaleAnimation holdAfterExhale = new ScaleAnimation(1.0f, 1.0f, 1.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        holdAfterExhale.setDuration(2000);
        holdAfterExhale.setFillAfter(true);

        // **Update Instructions**
        instructionText.setText("Inhale...");
        breathCircle.startAnimation(inhale);

        breathCircle.postDelayed(() -> {
            instructionText.setText("Hold...");
            breathCircle.startAnimation(holdAfterInhale);
        }, 4000);

        breathCircle.postDelayed(() -> {
            instructionText.setText("Exhale...");
            breathCircle.startAnimation(exhale);
        }, 6000);

        breathCircle.postDelayed(() -> {
            instructionText.setText("Hold...");
            breathCircle.startAnimation(holdAfterExhale);
        }, 10000);

        // **Show a random quote after each cycle**
        breathCircle.postDelayed(() -> quoteText.setText(getRandomQuote()), 10000);

        // **Repeat the cycle after 12 seconds**
        breathCircle.postDelayed(this::startBreathCycle, 12000);
    }

    private String getRandomQuote() {
        Random random = new Random();
        return quotes[random.nextInt(quotes.length)];
    }

    private void stopBreathingExercise() {
        isBreathing = false;
        if (countDownTimer != null) countDownTimer.cancel();
        if (mediaPlayer.isPlaying()) mediaPlayer.pause();
        instructionText.setText("Tap Start to Begin");
        timerText.setText("");
        quoteText.setText("");
    }

    // **Handle Back Button Press**
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(BreathingExerciseActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
