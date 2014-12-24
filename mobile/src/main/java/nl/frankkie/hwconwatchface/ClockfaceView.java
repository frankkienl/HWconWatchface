package nl.frankkie.hwconwatchface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;

import java.util.concurrent.TimeUnit;

/**
 * Created by fbouwens on 24-12-14.
 */
public class ClockfaceView extends SurfaceView {

    CanvasWatchFaceService.Engine engine;
    Rect bounds;
    boolean isVisible = true;
    boolean isAmbientMode = false;
    //for the minute tick
    final int MSG_UPDATE_TIME = 0;
    final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.MINUTES.toMillis(1);
    final Handler mUpdateTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_UPDATE_TIME:
                    //invalidate();
                    engine.onTimeTick(); //once a minute tick
                    if (isVisible) {
                        long timeMs = System.currentTimeMillis();
                        long delayMs = INTERACTIVE_UPDATE_RATE_MS
                                - (timeMs % INTERACTIVE_UPDATE_RATE_MS);
                        mUpdateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
                    }
                    break;
            }
        }
    };

    public void setAmbientMode(boolean isAmbientMode) {
        this.isAmbientMode = isAmbientMode;
        if (engine != null)
        engine.onAmbientModeChanged(isAmbientMode);
    }

    public ClockfaceView(Context context) {
        super(context);
        init();
    }

    public ClockfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClockfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void initLater(){
        CanvasWatchFaceService service = new HWconWatchFaceService();
        engine = service.onCreateEngine();
        engine.onCreate(getHolder());
        bounds = new Rect(0,0,280,280);
    }

    public void init(){}

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        isVisible = (visibility == View.VISIBLE);
        //
        if (engine != null)
        engine.onVisibilityChanged(isVisible);
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
            return;
        }
        if (engine != null)
        engine.onDraw(canvas,bounds);
    }
}
