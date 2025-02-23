package com.eyantra.mind_cure_ai;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class BubbleGameView extends SurfaceView implements SurfaceHolder.Callback {
    private BubbleGameThread thread;
    private CopyOnWriteArrayList<Bubble> bubbles;
    private Bitmap bubbleBitmap;
    private Random random;
    private Paint backgroundPaint;
    private int score = 0;
    private TextView scoreTextView;
    private MediaPlayer popSound;
    private CountDownTimer gameTimer;
    private boolean gameRunning = true;

    public BubbleGameView(Context context, TextView scoreTextView) {
        super(context);
        getHolder().addCallback(this);

        this.scoreTextView = scoreTextView;
        bubbles = new CopyOnWriteArrayList<>();
        random = new Random();

        // Load bubble image (small size)
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bubble);
        bubbleBitmap = Bitmap.createScaledBitmap(originalBitmap, 100, 100, false);

        // Set background color
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.parseColor("#B3E5FC"));

        // Load pop sound effect
        popSound = MediaPlayer.create(context, R.raw.pop_sound);

        thread = new BubbleGameThread(getHolder(), this);
        setFocusable(true);

        startGameTimer();
    }

    private void startGameTimer() {
        if (gameTimer != null) {
            gameTimer.cancel();
        }
        gameTimer = new CountDownTimer(60000, 1000) { // 60 seconds timer
            @Override
            public void onTick(long millisUntilFinished) {
                scoreTextView.setText("Time: " + millisUntilFinished / 1000 + "s | Score: " + score);
            }

            @Override
            public void onFinish() {
                gameRunning = false;
                scoreTextView.setText("Game Over! Score: " + score);
            }
        }.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (thread.getState() == Thread.State.TERMINATED) {
            thread = new BubbleGameThread(getHolder(), this);
        }
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (popSound != null) {
            popSound.release();
            popSound = null;
        }

        if (gameTimer != null) {
            gameTimer.cancel();
        }
    }

    public void update() {
        if (!gameRunning) return;

        Iterator<Bubble> iterator = bubbles.iterator();
        while (iterator.hasNext()) {
            Bubble bubble = iterator.next();
            bubble.update();
            if (bubble.getY() + bubble.getBitmap().getHeight() < 0) {
                bubbles.remove(bubble);
            }
        }

        if (random.nextInt(15) == 0) {
            spawnBubble();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundPaint);
            synchronized (bubbles) {
                for (Bubble bubble : bubbles) {
                    bubble.draw(canvas);
                }
            }
        }
    }

    private void spawnBubble() {
        if (!gameRunning) return;
        int width = getWidth();
        if (width > 0) {
            int x = random.nextInt(Math.max(1, width - bubbleBitmap.getWidth()));
            int y = getHeight();
            bubbles.add(new Bubble(x, y, bubbleBitmap));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && gameRunning) {
            float touchX = event.getX();
            float touchY = event.getY();

            Iterator<Bubble> iterator = bubbles.iterator();
            while (iterator.hasNext()) {
                Bubble bubble = iterator.next();
                if (bubble.isTouched(touchX, touchY)) {
                    bubbles.remove(bubble);
                    score++;
                    scoreTextView.setText("Score: " + score);

                    if (popSound != null) {
                        popSound.start();
                    }
                }
            }
        }
        return true;
    }

    public void resetGame() {
        gameRunning = true;
        score = 0;
        bubbles.clear();
        scoreTextView.setText("Score: " + score);
        startGameTimer();
    }
}
