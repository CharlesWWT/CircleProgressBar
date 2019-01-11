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

/**
 * Created by Administrator on 2018/12/20 0020.
 */

public class CircleLineProgressView extends View {

    private CircleAnim anim;
    private float currentAngle = 0;
    private double progressNum;

    private RectF mRectF;//绘制圆弧的矩形区域

    //自定义属性
    private int startColor;
    private int endColor;

    private Paint mProgressBackgroundPaint;//内环画笔
    private Paint mProgressBackgroundPaint1;//外环进度条画笔

    public CircleLineProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressBar);
        startColor = typedArray.getColor(R.styleable.ProgressBar_startColor, Color.YELLOW);
        endColor = typedArray.getColor(R.styleable.ProgressBar_endColor, Color.BLACK);
        anim = new CircleAnim();

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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        float x1 = 0, y1 = 0, x2 = 0, y2 = 0;
        /*
        数据暂时写死 有30根线 弧度与角度计算见 https://blog.csdn.net/chelen_jak/article/details/80451193
        内环圆半径为200 外环圆半径为240
         */

        for (int i = 0; i < 30; i++) {
            x1 = (float) (mCenterX + Math.sin(12 * i * (Math.PI / 180)) * 200);
            y1 = (float) (mCenterY - Math.cos(12 * i * (Math.PI / 180)) * 200);
            x2 = (float) (mCenterX + Math.sin(12 * i * (Math.PI / 180)) * 240);
            y2 = (float) (mCenterY - Math.cos(12 * i * (Math.PI / 180)) * 240);
            canvas.drawLine(x1, y1, x2, y2, mProgressBackgroundPaint);
        }


        int current = (int) (currentAngle / 12);//当前进度有几根线
        for (int i = 0; i < current; i++) {
            float x3 = (float) (mCenterX + Math.sin(12 * i * (Math.PI / 180)) * 200);
            float y3 = (float) (mCenterY - Math.cos(12 * i * (Math.PI / 180)) * 200);
            float x4 = (float) (mCenterX + Math.sin(12 * i * (Math.PI / 180)) * 240);
            float y4 = (float) (mCenterX - Math.cos(12 * i * (Math.PI / 180)) * 240);
            canvas.drawLine(x3, y3, x4, y4, mProgressBackgroundPaint1);
        }


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
