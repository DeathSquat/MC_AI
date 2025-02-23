package com.eyantra.mind_cure_ai;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Button;

public class BubbleGameActivity extends Activity {
    private BubbleGameView gameView;
    private TextView scoreTextView, timerTextView;
    private Button exitButton;
    private MediaPlayer bgMusic;
    private CountDownTimer countDownTimer;
    private int timeLeft = 60; // 60 seconds timer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Full-screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_bubble_game);

        // Initialize views
        scoreTextView = findViewById(R.id.scoreTextView);
        timerTextView = findViewById(R.id.timerTextView);
        exitButton = findViewById(R.id.exit_button);
        FrameLayout gameContainer = findViewById(R.id.game_container);

        // Create and add game view
        gameView = new BubbleGameView(this, scoreTextView);
        gameContainer.addView(gameView);

        // Start background music
        bgMusic = MediaPlayer.create(this, R.raw.relaxing_music);
        bgMusic.setLooping(true);
        bgMusic.start();

        // Start countdown timer
        startGameTimer();

        // Exit button click event
        exitButton.setOnClickListener(v -> finish());
    }

    private void startGameTimer() {
        countDownTimer = new CountDownTimer(timeLeft * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft--;
                timerTextView.setText("Time Left: " + timeLeft + "s");
            }

            @Override
            public void onFinish() {
                endGame();
            }
        }.start();
    }

    private void endGame() {
        if (countDownTimer != null) countDownTimer.cancel();

        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("Your Score: " + scoreTextView.getText().toString().replace("Score: ", ""))
                .setPositiveButton("Play Again", (dialog, which) -> restartGame())
                .setNegativeButton("Exit", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    private void restartGame() {
        timeLeft = 60;
        timerTextView.setText("Time Left: 60s");
        gameView.resetGame(); // Make sure BubbleGameView has a reset function
        startGameTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bgMusic != null) {
            bgMusic.stop();
            bgMusic.release();
            bgMusic = null;
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
