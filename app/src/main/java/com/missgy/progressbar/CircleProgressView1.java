package com.missgy.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2018/12/20 0020.
 */

public class CircleProgressView1 extends View {


    private Paint progressPaint;
    private Paint textPaint;
    private Paint textPaint2;
    private Paint bgPaint;
    private CircleAnim anim;

    private float startAngle = 135;
    private float endAngle = 270;
    private float currentAngle = 0;
    private String currentText;
    private double progressNum = 0.5;
    private int defaultSize = dipToPx(100);//自定义View默认的宽高

    private RectF mRectF;//绘制圆弧的矩形区域

    //自定义属性
    private int startColor;
    private int endColor;
    private float barWidth = dipToPx(8);//圆弧进度条宽度
    private String text = "当前进度";//数字上面的文字

    private Paint mProgressBackgroundPaint;
    private Paint mProgressBackgroundPaint1;
    private int min;

    public CircleProgressView1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressBar);
        startColor = typedArray.getColor(R.styleable.ProgressBar_startColor, Color.YELLOW);
        endColor = typedArray.getColor(R.styleable.ProgressBar_endColor, Color.BLACK);
        barWidth = typedArray.getDimension(R.styleable.ProgressBar_barWidth, 8);
        text = typedArray.getString(R.styleable.ProgressBar_text);
        mRectF = new RectF();
        anim = new CircleAnim();
        //正方形画笔
//        rPaint = new Paint();
//        rPaint.setStyle(Paint.Style.STROKE);//只描边，不填充
//        rPaint.setColor(Color.RED);
        //进度条画笔
        progressPaint = new Paint();
        progressPaint.setStyle(Paint.Style.STROKE);//只描边，不填充
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setColor(Color.GREEN);
        progressPaint.setAntiAlias(true);//设置抗锯齿
        //setStrokeWidth并不是往圆内侧增加圆环宽度的，而是往外侧增加一半，往内侧增加一半。
        progressPaint.setStrokeWidth(barWidth);
        //绘制灰色进度条背景
        bgPaint = new Paint();
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeCap(Paint.Cap.ROUND);
        bgPaint.setColor(Color.GRAY);
        bgPaint.setAntiAlias(true);
        bgPaint.setStrokeWidth(barWidth);

        textPaint = new Paint();
        textPaint.setTextSize(130);
        textPaint.setColor(Color.BLACK);
        // 实现水平居中，drawText对应改为传入targetRect.centerX()
        textPaint.setTextAlign(Paint.Align.CENTER);

        textPaint2 = new Paint();
        textPaint2.setTextSize(110);
        textPaint2.setFakeBoldText(true);
        textPaint2.setColor(Color.parseColor("#FFA07A"));
        // 实现水平居中，drawText对应改为传入targetRect.centerX()
        textPaint2.setTextAlign(Paint.Align.CENTER);

        mProgressBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressBackgroundPaint.setStyle(Paint.Style.STROKE);
        mProgressBackgroundPaint.setStrokeWidth(2);
        mProgressBackgroundPaint.setColor(Color.parseColor("#C0C0C0"));
        mProgressBackgroundPaint.setStrokeCap(Paint.Cap.BUTT);

        mProgressBackgroundPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressBackgroundPaint1.setStyle(Paint.Style.STROKE);
        mProgressBackgroundPaint1.setStrokeWidth(2);
        mProgressBackgroundPaint1.setStrokeCap(Paint.Cap.BUTT);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = measureSize(defaultSize, heightMeasureSpec);
        int width = measureSize(defaultSize, widthMeasureSpec);
        min = Math.min(height, width);
        setMeasuredDimension(min, min);
        mRectF.set(barWidth / 2, barWidth / 2, min - barWidth / 2, min - barWidth / 2);
        //canvas.drawCircle(width/2,width/2,width/2- mCircleLineStrokeWidth / 2,mBgPaint);画圆半径要减去一半线宽

    }

    private int measureSize(int defaultSize, int measureSize) {
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSize);
        int specSize = MeasureSpec.getSize(measureSize);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        float x = 50;
//        float y = 50;
//        RectF rectF = new RectF(x, y, x + 300, y + 300);//建一个大小为300 * 300的正方形区域

//        Rect targetRect = new Rect(50, 50, 350, 350);
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (int) ((mRectF.bottom + mRectF.top - fontMetrics.bottom - fontMetrics.top) / 2);
//        canvas.drawArc(mRectF, startAngle, endAngle, false, bgPaint);//背景圆环
//        canvas.drawArc(mRectF, startAngle, currentAngle, false, progressPaint);//绘制进度条
//        //绘制文字
//        canvas.drawText(currentText, mRectF.centerX(), baseline+80, textPaint);
//        canvas.drawText(text, mRectF.centerX(), baseline-130, textPaint2);
        //unit=360/10
//        float unit = 36;
//        float hudu= (float) (unit*);

        float x1 = 0, y1 = 0, x2 = 0, y2 = 0;

        for (int i = 0; i < 30; i++) {
            x1 = (float) (mCenterX + Math.sin(12 * i * (Math.PI / 180)) * 200);
            y1 = (float) (mCenterY - Math.cos(12 * i * (Math.PI / 180)) * 200);
            x2 = (float) (mCenterX + Math.sin(12 * i * (Math.PI / 180)) * 240);
            y2 = (float) (mCenterY - Math.cos(12 * i * (Math.PI / 180)) * 240);
            canvas.drawLine(x1, y1, x2, y2, mProgressBackgroundPaint);
        }


        int current = (int) (currentAngle / 12);
        for (int i = 0; i < current; i++) {
            float x3 = (float) (mCenterX + Math.sin(12 * i * (Math.PI / 180)) * 200);
            float y3 = (float) (mCenterY - Math.cos(12 * i * (Math.PI / 180)) * 200);
            float x4 = (float) (mCenterX + Math.sin(12 * i * (Math.PI / 180)) * 240);
            float y4 = (float) (mCenterX - Math.cos(12 * i * (Math.PI / 180)) * 240);
            canvas.drawLine(x3, y3, x4, y4, mProgressBackgroundPaint1);
        }

//        canvas.drawArc(mRectF, 0, currentAngle, false, progressPaint);//绘制进度条

    }
int mCenterX;
int mCenterY;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;

    }

    public class CircleAnim extends Animation {

        public CircleAnim() {
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            // interpolatedTime表示插值器的时间因子，补间时间，取值范围0-1
            currentAngle = (float) (360 * progressNum * interpolatedTime);
            DecimalFormat decimalFormat = new DecimalFormat("#%");//数字格式化
            currentText = decimalFormat.format(interpolatedTime * progressNum);

            LinearGradientUtil linearGradientUtil = new LinearGradientUtil(startColor, endColor);
            mProgressBackgroundPaint1.setColor(linearGradientUtil.getColor(interpolatedTime));

            postInvalidate();
        }
    }

    /*
    progressNum 进度
     */
    public void setProgressNum(double progressNum, int time) {
        this.progressNum = progressNum;
        anim.setDuration(time);
        this.startAnimation(anim);
    }

    /**
     * dip 转换成px
     *
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }
}
