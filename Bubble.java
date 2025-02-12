package com.eyantra.mind_cure_ai;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Bubble {
    private int x, y;
    private Bitmap bitmap;
    private static final int SPEED = 5;

    public Bubble(int x, int y, Bitmap bitmap) {
        this.x = x;
        this.y = y;
        this.bitmap = bitmap;
    }

    public void update() {
        y -= SPEED;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public boolean isTouched(float touchX, float touchY) {
        return touchX >= x && touchX <= x + bitmap.getWidth() &&
                touchY >= y && touchY <= y + bitmap.getHeight();
    }

    public int getY() {
        return y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
