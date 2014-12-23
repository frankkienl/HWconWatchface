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
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;

import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by FrankkieNL on 22-12-2014.
 */
public class HWconWatchFaceService extends CanvasWatchFaceService {
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
        Paint paint;
        Paint paintClear;

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
            paint = new Paint();
            paint.setAntiAlias(true); //default true
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setColor(Color.WHITE);
            paint.setTextSize(40);

            //Paint to clear background
            paintClear = new Paint();
            paintClear.setColor(Color.BLACK);
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            //super.onDraw(canvas, bounds);
            mTime.setToNow();

            canvas.drawRect(bounds, paintClear);
            //http://linux.die.net/man/3/strftime
            String timeString = "";
            if (isInAmbientMode()) {
                timeString = mTime.format("%H:%M");
            } else {
                timeString = mTime.format("%T");
            }
            String dayString = mTime.format("%A");
            String dateString = mTime.format("%d %m %Y");
            String monthString = mTime.format("%B");
            canvas.drawText(timeString, bounds.centerX(), 50, paint);
            canvas.drawText(dayString, bounds.centerX(), 100, paint);
            canvas.drawText(dateString, bounds.centerX(), 150, paint);
            canvas.drawText(monthString, bounds.centerX(), 200, paint);
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
            boolean antiAlias = true;
            if (inAmbientMode && mLowBitAmbient) {
                //how about burn-in? Should that also not use antiAlias?
                antiAlias = false;
            }
            paint.setAntiAlias(antiAlias);

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
