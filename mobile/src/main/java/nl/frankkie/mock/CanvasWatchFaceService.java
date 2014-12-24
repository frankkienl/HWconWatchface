package nl.frankkie.mock;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.SurfaceHolder;

/**
 * Created by FrankkieNL on 24-12-2014.
 */
public abstract class CanvasWatchFaceService {
    public abstract Engine onCreateEngine();

    //Took from WatchFaceService
    public static final java.lang.String COMMAND_AMBIENT_UPDATE = "com.google.android.wearable.action.AMBIENT_UPDATE";
    public static final java.lang.String COMMAND_BACKGROUND_ACTION = "com.google.android.wearable.action.BACKGROUND_ACTION";
    public static final java.lang.String COMMAND_SET_PROPERTIES = "com.google.android.wearable.action.SET_PROPERTIES";
    public static final java.lang.String COMMAND_SET_BINDER = "com.google.android.wearable.action.SET_BINDER";
    public static final java.lang.String COMMAND_REQUEST_STYLE = "com.google.android.wearable.action.REQUEST_STYLE";
    public static final java.lang.String ACTION_REQUEST_STATE = "com.google.android.wearable.watchfaces.action.REQUEST_STATE";
    public static final java.lang.String EXTRA_CARD_LOCATION = "card_location";
    public static final java.lang.String EXTRA_AMBIENT_MODE = "ambient_mode";
    public static final java.lang.String EXTRA_INTERRUPTION_FILTER = "interruption_filter";
    public static final java.lang.String EXTRA_UNREAD_COUNT = "unread_count";
    public static final java.lang.String EXTRA_BINDER = "binder";
    public static final java.lang.String PROPERTY_BURN_IN_PROTECTION = "burn_in_protection";
    public static final java.lang.String PROPERTY_LOW_BIT_AMBIENT = "low_bit_ambient";
    public static final int INTERRUPTION_FILTER_ALL = 1;
    public static final int INTERRUPTION_FILTER_PRIORITY = 2;
    public static final int INTERRUPTION_FILTER_NONE = 3;


    public void registerReceiver(Object o, Object o2) {
    }

    public void unregisterReceiver(Object o) {
    }

    public static class Engine {
        boolean visible = true;
        boolean ambientMode = false;

        public void invalidate() {
        }

        public void onCreate(SurfaceHolder holder) {
        }

        public void setWatchFaceStyle(Object o) {
        }

        public void onPropertiesChanged(Bundle b) {
        }

        public void onTimeTick() {
        }

        public boolean isVisible() {
            return visible;
        }

        public boolean isInAmbientMode() {
            return ambientMode;
        }

        public void onAmbientModeChanged(boolean b) {
        }

        public void onVisibilityChanged(boolean b) {
        }

        public void onDraw(Canvas c, Rect b){}
    }
}
