package com.eyantra.mind_cure_ai;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MemoryMatchActivity extends AppCompatActivity {

    private TextView timerTextView, scoreTextView;
    private GridView memoryGrid;
    private Button newGameButton, exitButton;
    private CountDownTimer countDownTimer;
    private int timeLeft = 30; // 30-second timer
    private int score = 0;
    private boolean isTimerRunning = false;
    private MediaPlayer matchSound;

    private Integer[] cards = {
            R.drawable.card1, R.drawable.card2, R.drawable.card3, R.drawable.card4,
            R.drawable.card5, R.drawable.card6, R.drawable.card7, R.drawable.card8,
            R.drawable.card1, R.drawable.card2, R.drawable.card3, R.drawable.card4,
            R.drawable.card5, R.drawable.card6, R.drawable.card7, R.drawable.card8
    };

    private int firstCardIndex = -1;
    private int secondCardIndex = -1;
    private boolean isProcessing = false;
    private View firstCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_match);

        timerTextView = findViewById(R.id.timerTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        memoryGrid = findViewById(R.id.memoryGrid);
        newGameButton = findViewById(R.id.newGameButton);
        exitButton = findViewById(R.id.exitButton);

        matchSound = MediaPlayer.create(this, R.raw.relaxing_music);

        startNewGame();

        exitButton.setOnClickListener(view -> finish());
        newGameButton.setOnClickListener(view -> startNewGame());
    }

    private void startNewGame() {
        score = 0;
        timeLeft = 60;
        isTimerRunning = false;
        timerTextView.setText("Time Left: 60s");
        scoreTextView.setText("Score: 0");

        List<Integer> cardList = Arrays.asList(cards);
        Collections.shuffle(cardList);
        cardList.toArray(cards);

        MemoryMatchAdapter adapter = new MemoryMatchAdapter(this, cards, this::onCardClicked);
        memoryGrid.setAdapter(adapter);

        newGameButton.setVisibility(View.GONE);

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void onCardClicked(View view, int position) {
        if (isProcessing || position == firstCardIndex) return;

        if (!isTimerRunning) {
            startTimer();
            isTimerRunning = true;
        }

        MemoryMatchAdapter.ViewHolder holder = (MemoryMatchAdapter.ViewHolder) view.getTag();
        ImageView imageView = holder.imageView;
        imageView.setImageResource(cards[position]);

        if (firstCardIndex == -1) {
            firstCardIndex = position;
            firstCardView = view;
        } else {
            secondCardIndex = position;
            isProcessing = true;

            memoryGrid.postDelayed(this::checkMatch, 500);
        }
    }

    private void checkMatch() {
        if (cards[firstCardIndex].equals(cards[secondCardIndex])) {
            score += 10;
            matchSound.start();

            firstCardView.setEnabled(false);
            memoryGrid.getChildAt(secondCardIndex).setEnabled(false);
        } else {
            MemoryMatchAdapter.ViewHolder holder1 = (MemoryMatchAdapter.ViewHolder) firstCardView.getTag();
            MemoryMatchAdapter.ViewHolder holder2 = (MemoryMatchAdapter.ViewHolder) memoryGrid.getChildAt(secondCardIndex).getTag();

            holder1.imageView.setImageResource(R.drawable.card_back);
            holder2.imageView.setImageResource(R.drawable.card_back);
        }

        firstCardIndex = -1;
        secondCardIndex = -1;
        isProcessing = false;
        scoreTextView.setText("Score: " + score);

        if (score == (cards.length / 2) * 10) {
            endGame();
        }
    }

    private void startTimer() {
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
                .setMessage("Your score: " + score)
                .setPositiveButton("Play Again", (dialog, which) -> startNewGame())
                .setNegativeButton("Exit", (dialog, which) -> finish())
                .setCancelable(false)
                .show();

        newGameButton.setVisibility(View.VISIBLE);
    }
}
