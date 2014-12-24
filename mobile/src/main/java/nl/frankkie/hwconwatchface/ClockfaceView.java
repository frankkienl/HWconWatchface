package nl.frankkie.hwconwatchface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by fbouwens on 24-12-14.
 */
public class ClockfaceView extends View {

    public ClockfaceView(Context context) {
        super(context);
    }

    public ClockfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClockfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ClockfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        if (isInEditMode()){
            Paint p = new Paint();
            p.setColor(Color.BLACK);
            canvas.drawRect(new Rect(0,0,280,280),p);
            p.setTextSize(40);
            p.setColor(Color.WHITE);
            canvas.drawText("ClockFace!",30,50, p);
        }
    }
}
