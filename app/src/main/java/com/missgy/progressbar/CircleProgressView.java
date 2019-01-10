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

public class CircleProgressView extends View {


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
    private float barWidth=dipToPx(8);//圆弧进度条宽度
    private RectF mRectF;//绘制圆弧的矩形区域

    //自定义属性
    private int startColor;
    private int endColor;


    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.ProgressBar);
        startColor= typedArray.getColor(R.styleable.ProgressBar_startColor,Color.YELLOW);
        endColor=typedArray.getColor(R.styleable.ProgressBar_endColor,Color.BLACK);
        barWidth= typedArray.getDimension(R.styleable.ProgressBar_barWidth,8);
        mRectF=new RectF();
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


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = measureSize(defaultSize, heightMeasureSpec);
        int width = measureSize(defaultSize, widthMeasureSpec);
        int min =Math.min(height,width);
        setMeasuredDimension(min,min);
        mRectF.set(barWidth/2,barWidth/2,min-barWidth/2,min-barWidth/2);
        //canvas.drawCircle(width/2,width/2,width/2- mCircleLineStrokeWidth / 2,mBgPaint);画圆半径要减去一半线宽

    }

    private int measureSize(int defaultSize, int measureSize) {
        int result=defaultSize;
        int specMode=MeasureSpec.getMode(measureSize);
        int specSize=MeasureSpec.getSize(measureSize);
        if(specMode== MeasureSpec.EXACTLY) {
            result=specSize;
        }else if(specMode==MeasureSpec.AT_MOST) {
            result=Math.min(result,specSize);
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
        canvas.drawArc(mRectF, startAngle, endAngle, false, bgPaint);//背景圆环
        canvas.drawArc(mRectF, startAngle, currentAngle, false, progressPaint);//绘制进度条
        //绘制文字
        canvas.drawText(currentText, mRectF.centerX(), baseline+80, textPaint);
        canvas.drawText("当前进度", mRectF.centerX(), baseline-130, textPaint2);

    }

    public class CircleAnim extends Animation {

        public CircleAnim() {
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            // interpolatedTime表示插值器的时间因子，补间时间，取值范围0-1
            currentAngle = (float) ((360 - 90) * progressNum * interpolatedTime);
            DecimalFormat decimalFormat = new DecimalFormat("#%");//数字格式化
            currentText = decimalFormat.format(interpolatedTime * progressNum);

            LinearGradientUtil linearGradientUtil = new LinearGradientUtil(startColor,endColor);
            progressPaint.setColor(linearGradientUtil.getColor(interpolatedTime));

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
