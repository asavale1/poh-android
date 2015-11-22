package co.digitaldavinci.pollsofhumanity;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import co.digitaldavinci.pollsofhumanity.R;

/**
 * Created by ameya on 9/10/15.
 */
public class PieChart extends View {
    private int noCount, yesCount;
    private Paint paint;

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.PieChart, 0, 0);

        try{
            noCount = a.getInteger(R.styleable.PieChart_noCount, 0);
            yesCount = a.getInteger(R.styleable.PieChart_yesCount, 0);
        }finally{
            a.recycle();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {

        System.out.println("No Count: " + noCount);
        int viewWidthHalf = this.getMeasuredWidth() / 2;
        int viewHeightHalf = this.getMeasuredHeight() / 2;

        int radius = 0;
        if(viewHeightHalf > viewWidthHalf){
            radius = viewWidthHalf - 10;
        }else{
            radius = viewHeightHalf - 10;
        }

        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);


        double yesRatio = ((double) yesCount) / ((double) (noCount + yesCount));
        int yesDegrees = (int)(360 * yesRatio);

        System.out.println(yesCount);
        System.out.println(360 - yesDegrees);
        final RectF oval = new RectF();

        oval.set(viewWidthHalf - radius,

                viewHeightHalf - radius,

                viewWidthHalf + radius,

                viewHeightHalf + radius);

        paint.setColor(Color.parseColor("#117909"));
        canvas.drawArc(oval, 90, yesDegrees, true, paint);

        paint.setColor(Color.parseColor("#b11a12"));
        canvas.drawArc(oval, 90 + yesDegrees, 360 - yesDegrees, true, paint);

    }

    public void setNoCount(int noCount){
        invalidate();
        this.noCount = noCount;
    }

    public void setYesCount(int yesCount){
        invalidate();
        this.yesCount = yesCount;
    }
}
