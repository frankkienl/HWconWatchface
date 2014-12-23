package nl.frankkie.hwconwatchface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.view.Gravity;
import android.view.SurfaceHolder;

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
        boolean mRegisteredTimeZoneReceiver = false;
        boolean mLowBitAmbient;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            WatchFaceStyle.Builder builder = new WatchFaceStyle.Builder(HWconWatchFaceService.this);
            builder.setCardPeekMode(WatchFaceStyle.PEEK_OPACITY_MODE_TRANSLUCENT);
            builder.setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE);
            //https://github.com/googlesamples/android-WatchFace/blob/master/Wearable/src/main/java/com/example/android/wearable/watchface/TiltWatchFaceService.java#L132
            builder.setHotwordIndicatorGravity(Gravity.LEFT | Gravity.TOP);
            builder.setStatusBarGravity(Gravity.LEFT | Gravity.TOP);

            setWatchFaceStyle(builder.build());
        }


    }
}
