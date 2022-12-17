package com.link.myanalogclock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;


public class MyAnalogClock extends View {

    private Paint paint;
    private int hour, minute, second;

    private Bitmap backgroundBitmapClock;

    public MyAnalogClock(Context context) {
        super(context);
        init(context, null);
    }

    public MyAnalogClock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        paint = new Paint();
        backgroundBitmapClock = BitmapFactory.decodeResource(context.getResources(), R.drawable.clock);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyAnalogClock);

            hour = typedArray.getInt(R.styleable.MyAnalogClock_hour, 0);
            minute = typedArray.getInt(R.styleable.MyAnalogClock_minute, 0 );
            second = typedArray.getInt(R.styleable.MyAnalogClock_second, 0);

            rectifyTime();

            typedArray.recycle();
        }

    }
    private void rectifyTime(){
        if (hour > 12 && hour < 25) {
            hour = hour - 12;
        } else if (hour < 0 || hour > 24) {
            hour = 0;
        }
        if (minute > 59 || minute < 0) minute = 0;
        if (second > 59 || second < 0) second = 0;
    }

    public void setTime (int h, int min, int sec){
        hour = h;
        minute = min;
        second = sec;

        rectifyTime();
    }

    private static final int BASE_WIDTH = 300;
    private static final int BASE_HEIGHT = 300;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int w;
        int h;

        if (widthMode == MeasureSpec.EXACTLY) {
            w = widthSize;
        }
        else if (widthMode == MeasureSpec.AT_MOST) {
            w = Math.min(BASE_WIDTH, widthSize);
        }
        else {
            w = BASE_WIDTH;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            h = heightSize;
        }
        else if (heightMode == MeasureSpec.AT_MOST) {
            h = Math.min(BASE_HEIGHT, heightSize);
        }
        else {
            h = BASE_HEIGHT;
        }

        int smaller = Math.min(w, h);
        w = smaller;
        h = smaller;

        if(!areAllEqual(getPaddingTop(), getPaddingBottom(), getPaddingLeft(), getPaddingRight())){
            w = smaller - (getPaddingTop() + getPaddingBottom());
            h = smaller - (getPaddingLeft() + getPaddingRight());
        }

        setMeasuredDimension(w, h);
    }

    public boolean areAllEqual(int... values) {
        if (values.length == 0) {
            return true;
        }
        int checkValue = values[0];
        for(int i = 1; i< values.length; i++) {
            if (values[i] != checkValue) {
                return false;
            }
        }
        return true;
    }

    Rect backgroundClock = new Rect();
    private float centerYClock;
    private float centerXClock;
    private float hourHandSize, secMinHandsSize;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        backgroundClock.left = getPaddingLeft();
        backgroundClock.top = getPaddingTop();
        backgroundClock.right = w - getPaddingRight();
        backgroundClock.bottom = h - getPaddingBottom();

        float clockWidth = w - getPaddingLeft() - getPaddingRight();
        float clockHeight = h - getPaddingTop() - getPaddingBottom();

        centerXClock = getPaddingLeft() + clockWidth/2;
        centerYClock = getPaddingTop() + clockHeight/2;

        float radius = clockHeight / 2;
        hourHandSize = radius - radius /2;
        secMinHandsSize = radius - radius /4;

    }

    private void setPaintAttributes(int colour, int strokeWidth) {
        paint.reset();
        paint.setColor(colour);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);
    }

    int invisibleHand = 59;
    private void drawHands(Canvas canvas) {

        drawHourHand(canvas, (hour + minute / 60.0) * 5f);
        drawMinuteHand(canvas,minute);
        drawSecondsHand(canvas,second);

        if (invisibleHand == 59){
            invisibleHand = 0;

            if (second == 59){
                second = 0;
                minute = minute + 1;
                if (minute == 59){
                    minute = 0;
                    hour = hour + 1;
                    if (hour > 12){
                        hour = 1;
                    }
                }
            } else {
                second = second + 1;}
        } else {
            invisibleHand = invisibleHand +1;
        }

    }

    private void drawSecondsHand(Canvas canvas, float location) {
        setPaintAttributes(Color.RED, 8);
        double angle =  Math.PI * location / 30 - Math.PI / 2;
        canvas.drawLine(centerXClock,centerYClock,(float)   (centerXClock+Math.cos(angle)*secMinHandsSize)
                , (float) (centerYClock+Math.sin(angle)*hourHandSize),paint);

    }

    private void drawMinuteHand(Canvas canvas, float location) {
        setPaintAttributes(Color.GRAY, 9);
        double angle =  Math.PI * location / 30 - Math.PI / 2;
        canvas.drawLine(centerXClock,centerYClock, (float) (centerXClock+Math.cos(angle)*secMinHandsSize)
                , (float) (centerYClock+Math.sin(angle)*hourHandSize),paint);
    }

    private void drawHourHand(Canvas canvas, double location) {
        setPaintAttributes(Color.BLACK, 10);
        double angle = Math.PI * location / 30 - Math.PI / 2;
        canvas.drawLine(centerXClock,centerYClock,(float) (centerXClock+Math.cos(angle)*hourHandSize)
                , (float) (centerYClock+Math.sin(angle)*hourHandSize),paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
       // super.onDraw(canvas);

        canvas.drawBitmap(backgroundBitmapClock,null, backgroundClock, null);
        drawHands(canvas);
        invalidate();


    }
}
