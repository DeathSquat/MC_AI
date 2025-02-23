package com.eyantra.mind_cure_ai;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class BubbleGameThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private BubbleGameView gameView;
    private boolean running;

    public BubbleGameThread(SurfaceHolder surfaceHolder, BubbleGameView gameView) {
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        while (running) {
            Canvas canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    gameView.update();
                    gameView.draw(canvas);
                }
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

            try {
                sleep(6); // Increased frequency of bubbles appearing
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
