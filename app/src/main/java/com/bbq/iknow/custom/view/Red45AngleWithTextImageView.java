package com.bbq.iknow.custom.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.feinno.androidbase.system.SystemUtils;


/**
 * @Description: 45度角倾斜红色背景可设置文案的自定义IamgeView
 * @author tmy
 * @date  
 * @version 1.0
 */
public class Red45AngleWithTextImageView extends ImageView {
    private Bitmap bitmap;
    Path path;
    Paint paint;
    int defaultWidth;
    int defaultHeight;

    Path textPath;

    private String text;

    public Red45AngleWithTextImageView(Context context) {
        super(context);
        init();
    }

    private void init() {
        defaultWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getContext().getResources().getDisplayMetrics());
        defaultHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getContext().getResources().getDisplayMetrics());


        path = new Path();
        textPath = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public Red45AngleWithTextImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;

        } else {
            width = defaultWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;

        } else {
            height = defaultHeight;
        }
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(Color.parseColor("#ed323c"));
        paint.setColor(Color.parseColor("#ffae01"));

        path.moveTo(0, SystemUtils.dip2px(getContext(),0));
        path.lineTo( SystemUtils.dip2px(getContext(),60),  SystemUtils.dip2px(getContext(),60));
        path.lineTo( SystemUtils.dip2px(getContext(),60),  SystemUtils.dip2px(getContext(),30));
        path.lineTo( SystemUtils.dip2px(getContext(),30), SystemUtils.dip2px(getContext(),0));

        canvas.drawPath(path, paint);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(1f);
        paint.setTextSize(SystemUtils.dip2px(getContext(),10));

        if (!TextUtils.isEmpty(text)) {
            paint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetrics fm = paint.getFontMetrics();
            double textHeight = (Math.ceil(fm.descent - fm.ascent) + 2);
            textPath.moveTo(SystemUtils.dip2px(getContext(),25), SystemUtils.dip2px(getContext(),9));
            textPath.lineTo(SystemUtils.dip2px(getContext(),55), SystemUtils.dip2px(getContext(),41));
            paint.setColor(Color.TRANSPARENT);
            canvas.drawPath(textPath, paint);
            paint.setColor(Color.WHITE);
            canvas.drawTextOnPath(text, textPath, 0, (float) (textHeight / 4), paint);
        }


    }


    public void setText(String text) {

        this.text = text;

        invalidate();
    }

}
