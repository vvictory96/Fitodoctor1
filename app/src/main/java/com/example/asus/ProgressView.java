package com.example.asus;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

//Класс элемента прогресса задачи
public class ProgressView extends View {

    private final RectF mRect = new RectF();
    private final RectF mRectInner = new RectF();
    private final Paint mPaintForeground = new Paint();
    private final Paint mPaintStroke = new Paint();
    private final Paint mPaintBackground = new Paint();
    private final Paint mPaintErase = new Paint();

    private static final Xfermode PORTER_DUFF_CLEAR = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    private int mColorForeground = Color.WHITE;
    private int mColorStroke = Color.WHITE;
    private int mColorBackground = Color.BLACK;

    private float mValue = -1f;
    private static final float PADDING = 5;
    private float mPadding;
    private Bitmap mBitmap;
    private Paint emptyData = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int progressMax = 1;

    private int idTask=0;
    private int idDayTask=0;


    private static final float INNER_RADIUS_RATIO = 0.84f;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Resources r = context.getResources();
        float scale = r.getDisplayMetrics().density;
        mPadding = scale * PADDING ;
        mPaintForeground.setColor(mColorForeground);
        mPaintForeground.setAntiAlias(true);
        mPaintBackground.setColor(mColorBackground);
        mPaintBackground.setAntiAlias(true);
        mPaintErase.setXfermode(PORTER_DUFF_CLEAR);
        mPaintErase.setAntiAlias(true);
//        emptyData.setColor(Color.GRAY);
        emptyData.setColor(getResources().getColor(R.color.progressbar_background_color));
        setForegroundColor(getResources().getColor(R.color.progressbar_foreground_color));
        setBackgroundColor(getResources().getColor(R.color.progressbar_background_color));
        idTask=0;
    }

    public void SetTaskIDay(int id, int idd){idTask=id;idDayTask=idd;};

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, getWidth() / 2 - mBitmap.getWidth() / 2, getHeight() / 2 - mBitmap.getHeight() / 2, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        float bitmapWidth = w - 2 * mPadding;
        float bitmapHeight = h - 2 * mPadding;
        float radius = Math.min(bitmapWidth / 2, bitmapHeight / 2);
        mRect.set(0, 0, bitmapWidth, bitmapHeight);
        radius *= INNER_RADIUS_RATIO;
        mRectInner.set(bitmapWidth / 2f - radius, bitmapHeight / 2f - radius, bitmapWidth / 2f + radius, bitmapHeight / 2f + radius);
        updateBitmap();
    }

    private void updateBitmap() {
        if (mRect == null || mRect.width() == 0) return;

        mBitmap = Bitmap.createBitmap((int) mRect.width(), (int) mRect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mBitmap);
        float angle = mValue * (3600 / progressMax);
        if (mValue < 0) {
            //Если прогесса не может быть, нарисовать серый элемент
            emptyData.setColor(Color.GRAY);
            setBackgroundColor(getResources().getColor(R.color.progressbar_empty_color));
            setBackgroundStrokeColor(getResources().getColor(R.color.round_color_center_circle_text));
            setForegroundColor(getResources().getColor(R.color.progressbar_empty_color));
            canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, CircleRadiusHelper.getRadius(canvas) / 2, emptyData);
        } else {
            if(mValue == 0) {
                //Если прогресса ещё нет, нарисовать пустой элемент
                emptyData.setStyle(Paint.Style.STROKE);
                emptyData.setColor(getResources().getColor(R.color.progressbar_background_color));
                canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, CircleRadiusHelper.getRadius(canvas), emptyData);
            } else {
                //Если есть прогресс, наросивать доли выполнения
                canvas.drawArc(mRect, -90, 360, true, mPaintBackground);
                canvas.drawArc(mRect, -90, angle, true, mPaintForeground);
            }
        }
        postInvalidate();
    }

    public void setForegroundColor(int color) {
        this.mColorForeground = color;
        mPaintForeground.setColor(color);
        invalidate();
    }

    public void setBackgroundColor(int color) {
        this.mColorBackground = color;
        mPaintBackground.setColor(color);
        invalidate();
    }

    public void setBackgroundStrokeColor(int color) {
        this.mColorStroke = color;
        mPaintStroke.setColor(color);
        mPaintStroke.setStrokeWidth(3);
        mPaintStroke.setStyle(Paint.Style.STROKE);
        invalidate();
    }

    public synchronized void setValue(float value) {
        mValue = value / 10f;
        if(Float.isInfinite(mValue))
            mValue = 0;
        updateBitmap();
    }

    public void setProgressMaximum(int value) {
        this.progressMax = value;
    }
}