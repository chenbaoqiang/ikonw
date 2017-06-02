package com.bbq.iknow.custom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.bbq.iknow.R;

/**
 * Created by chenbaoqiang on 2017/3/20.
 */

/**
 * android View 中requstLayout()和invalidate的原理及流程
 * <p/>
 * requestLayout();
 * 当调用 requestLayout()时，会重新走 measure()-->onMesure()-->layout()-->onLayout() -->dipatchDraw()-->draw()-->onDraw();
 * <p/>
 * <p/>
 * invalidate();
 * 当调用invalidate()时 会 重新走dipatchDraw()-->draw()-->onDraw()
 */
public class CustomCircleView extends View {
    private static final String TAG = CustomCircleView.class.getSimpleName();
    private Paint mPaint;
    private Context mContext;// 上下文环境引用
    private int radiu;// 圆环半径
    private int textColor;
    private float textSize;

    public CustomCircleView(Context context) {
        super(context);
        initPaint(context,null);
    }

    public CustomCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint(context,attrs);
    }

    public CustomCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context,attrs);


    }

    private void initPaint(Context context, AttributeSet attrs) {
        mContext = context;
        mPaint = new Paint();
        if (attrs != null){

            TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                    R.styleable.CustomCircleView);
            textColor = mTypedArray.getColor(R.styleable.CustomCircleView_titleTextColor, Color.RED);
            textSize = mTypedArray.getDimension(R.styleable.CustomCircleView_titleTextSize, 15);
            mTypedArray.recycle();
        }else{
            textSize = 30;
            textColor= Color.WHITE;
        }



        mPaint.setAntiAlias(true);
        /** 设置画笔样式为描边，圆环嘛……当然不能填充不然就么意思了
         *
         * 画笔样式分三种：
         * 1.Paint.Style.STROKE：描边
         * 2.Paint.Style.FILL_AND_STROKE：描边并填充
         * 3.Paint.Style.FILL：填充
         */
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        // 设置画笔颜色为浅灰色
        mPaint.setColor(Color.LTGRAY);
        /** 设置描边的粗细，单位：像素px
         * 注意：当setStrokeWidth(0)的时候描边宽度并不为0而是只占一个像素
         */
        mPaint.setStrokeWidth(10);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG, "onLayout");

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw");
        // 绘制圆环
        if (radiu > 300){
            mPaint.setColor(Color.RED);

        }else if (radiu > 100){
            mPaint.setColor(Color.GREEN);

        }else{
            mPaint.setColor(Color.LTGRAY);
        }
        canvas.drawCircle(500, 500,radiu,mPaint);

        int centre = getWidth()/2; //获取圆心的x坐标
        float textWidth = mPaint.measureText(radiu + "%");   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间

        mPaint.setStrokeWidth(0);
        mPaint.setTextSize(textSize);
        mPaint.setColor(textColor);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
        canvas.drawText(radiu+"", 520, 520, mPaint); //画出进度百分比
    }

    public synchronized void setRadiu(int radiu){
        this.radiu = radiu;
        postInvalidate();
    }

}
