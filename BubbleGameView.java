package com.eyantra.mind_cure_ai;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class BubbleGameView extends SurfaceView implements SurfaceHolder.Callback {
    private BubbleGameThread thread;
    private ArrayList<Bubble> bubbles;
    private Bitmap bubbleBitmap;
    private Random random;
    private Paint backgroundPaint;
    private int score = 0;
    private TextView scoreTextView;

    public BubbleGameView(Context context, TextView scoreTextView) {
        super(context);
        getHolder().addCallback(this);

        this.scoreTextView = scoreTextView;
        bubbles = new ArrayList<>();
        random = new Random();

        // Load bubble image
        bubbleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bubble);

        // Set soft blue background color
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.parseColor("#B3E5FC"));

        thread = new BubbleGameThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
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
    }

    public void update() {
        Iterator<Bubble> iterator = bubbles.iterator();
        while (iterator.hasNext()) {
            Bubble bubble = iterator.next();
            bubble.update();
            if (bubble.getY() + bubble.getBitmap().getHeight() < 0) {
                iterator.remove(); // Remove bubbles that go out of screen
            }
        }

        // Spawn new bubbles at intervals
        if (random.nextInt(50) == 0) {
            spawnBubble();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundPaint);
            for (Bubble bubble : bubbles) {
                bubble.draw(canvas);
            }
        }
    }

    private void spawnBubble() {
        int width = getWidth();
        if (width > 0) {
            int x = random.nextInt(Math.max(1, width - bubbleBitmap.getWidth()));
            int y = getHeight();
            bubbles.add(new Bubble(x, y, bubbleBitmap));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float touchX = event.getX();
            float touchY = event.getY();

            Iterator<Bubble> iterator = bubbles.iterator();
            while (iterator.hasNext()) {
                Bubble bubble = iterator.next();
                if (bubble.isTouched(touchX, touchY)) {
                    iterator.remove();
                    score++;
                    scoreTextView.setText("Score: " + score);
                }
            }
        }
        return true;
    }
}
