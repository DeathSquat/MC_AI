package com.eyantra.mind_cure_ai;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Button;

public class BubbleGameActivity extends Activity {
    private BubbleGameView gameView;
    private TextView scoreTextView;
    private Button exitButton;

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
        exitButton = findViewById(R.id.exit_button);
        FrameLayout gameContainer = findViewById(R.id.game_container);

        // Create and add game view
        gameView = new BubbleGameView(this, scoreTextView);
        gameContainer.addView(gameView);

        // Exit button click event
        exitButton.setOnClickListener(v -> finish());
    }
}
