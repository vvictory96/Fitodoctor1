package com.example.asus;

import android.graphics.Canvas;

/**
 * Created by gleb on 6/15/17.
 */

public class CircleRadiusHelper {
    public static float getRadius(Canvas canvas) {
        float width = canvas.getWidth();
        float height = canvas.getHeight();
        float minSize = width > height ? height : width;
        float radius = minSize / 2;
        return radius;
    }
}
