package nl.frankkie.hwconwatchface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import nl.frankkie.mock.CanvasWatchFaceService;
import nl.frankkie.mock.WatchFaceStyle;
import android.text.format.Time;
import android.view.Gravity;
import android.view.SurfaceHolder;

import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by FrankkieNL on 22-12-2014.
 */
public class HWconWatchFaceService extends CanvasWatchFaceService {

    //http://www.colorpicker.com/
    public static final int COLOR_BACKGROUND = Color.parseColor("#F2C849"); //Orange
    public static final int COLOR_TEXT = Color.parseColor("#4973F2"); //Blue, complementary

    //https://developer.android.com/training/wearables/watch-faces/service.html
    //https://github.com/googlesamples/android-WatchFace/blob/master/Wearable/src/main/java/com/example/android/wearable/watchface/SweepWatchFaceService.java

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    private class Engine extends CanvasWatchFaceService.Engine {

        Time mTime;
        final BroadcastReceiver mTimeZoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mTime.clear(intent.getStringExtra("time-zone"));
                mTime.setToNow();
            }
        };
        /**
         * https://github.com/googlesamples/android-WatchFace/blob/master/Wearable/src/main/java/com/example/android/wearable/watchface/AnalogWatchFaceService.java#L75
         * Handler to update the time once a second in interactive mode.
         */
        final int MSG_UPDATE_TIME = 0;
        final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.SECONDS.toMillis(1);
        final Handler mUpdateTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case MSG_UPDATE_TIME:
                        invalidate();
                        if (shouldTimerBeRunning()) {
                            long timeMs = System.currentTimeMillis();
                            long delayMs = INTERACTIVE_UPDATE_RATE_MS
                                    - (timeMs % INTERACTIVE_UPDATE_RATE_MS);
                            mUpdateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
                        }
                        break;
                }
            }
        };
        boolean mRegisteredTimeZoneReceiver = false;
        boolean mLowBitAmbient; //black and white only, no anti-aliasing
        boolean mBurnInProtection; //should use outlines only (less white is better)
        boolean mAmbientForce = true; //Force the use of LowBit and BurnIn features in this clockface
        Paint paintText;
        Paint paintBackground;
        Paint paintTextAmbient;
        Paint paintBackgroundAmbient;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            mTime = new Time();

            WatchFaceStyle.Builder builder = new WatchFaceStyle.Builder(HWconWatchFaceService.this);
            builder.setCardPeekMode(WatchFaceStyle.PEEK_OPACITY_MODE_TRANSLUCENT);
            builder.setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE);
            //https://github.com/googlesamples/android-WatchFace/blob/master/Wearable/src/main/java/com/example/android/wearable/watchface/TiltWatchFaceService.java#L132
            builder.setHotwordIndicatorGravity(Gravity.LEFT | Gravity.TOP);
            builder.setStatusBarGravity(Gravity.LEFT | Gravity.TOP);

            setWatchFaceStyle(builder.build());

            //Paint to draw text
            paintText = new Paint();
            paintText.setAntiAlias(true); //default true
            paintText.setTextAlign(Paint.Align.CENTER);
            paintText.setColor(COLOR_TEXT);
            paintText.setTextSize(40);
            paintTextAmbient.setStrokeWidth(1);

            paintTextAmbient = new Paint(paintText);
            paintTextAmbient.setColor(Color.WHITE);

            //Paint to clear background
            paintBackground = new Paint();
            paintBackground.setColor(Color.BLACK);
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            //super.onDraw(canvas, bounds);
            mTime.setToNow();

            canvas.drawRect(bounds, paintBackground);
            //http://linux.die.net/man/3/strftime
            String timeString = "";
            if (isInAmbientMode()) {
                timeString = mTime.format("%H:%M");
            } else {
                timeString = mTime.format("%T");
            }
            String dayString = mTime.format("%A");
            String dateString = mTime.format("%d-%m-%Y");
            String monthString = mTime.format("%B");
            canvas.drawText(timeString, bounds.centerX(), 50, paintText);
            canvas.drawText(dayString, bounds.centerX(), 100, paintText);
            canvas.drawText(dateString, bounds.centerX(), 150, paintText);
            canvas.drawText(monthString, bounds.centerX(), 200, paintText);
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
            mLowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
            mBurnInProtection = properties.getBoolean(PROPERTY_BURN_IN_PROTECTION, false);
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate(); //redraw
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            if (inAmbientMode && (mLowBitAmbient || mBurnInProtection || mAmbientForce)) {
                //it seems my LG G Watch does not have lowBit and burnInProtection
                //So I'm forcing AntiAlias :P
                paintText.setAntiAlias(false);
                paintText.setStyle(Paint.Style.STROKE);
            } else {
                paintText.setAntiAlias(true);
                paintText.setStyle(Paint.Style.FILL_AND_STROKE);
            }
            updateTimer();
            invalidate(); //draw now to make sure he draws on ambiant mode now
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (visible) {
                registerReceiver();

                //update timezone
                mTime.clear(TimeZone.getDefault().getID());
                mTime.setToNow();

                invalidate();
            } else {
                unregisterReceiver();
            }

            updateTimer();
        }

        private void registerReceiver() {
            if (mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            HWconWatchFaceService.this.registerReceiver(mTimeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = false;
            HWconWatchFaceService.this.unregisterReceiver(mTimeZoneReceiver);
        }

        /**
         * Starts the {@link #mUpdateTimeHandler} timer if it should be running and isn't currently
         * or stops it if it shouldn't be running but currently is.
         */
        private void updateTimer() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (shouldTimerBeRunning()) {
                mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }

        /**
         * Returns whether the {@link #mUpdateTimeHandler} timer should be running. The timer should
         * only run when we're visible and in interactive mode.
         */
        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }
    }
}
