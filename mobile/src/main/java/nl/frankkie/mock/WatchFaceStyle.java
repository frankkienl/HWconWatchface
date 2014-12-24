package nl.frankkie.mock;

import android.support.v4.app.NotificationCompat;

/**
 * Created by FrankkieNL on 24-12-2014.
 */
public class WatchFaceStyle {

    public static final int PEEK_MODE_VARIABLE = 0;
    public static final int PEEK_MODE_SHORT = 1;
    public static final int PROGRESS_MODE_NONE = 0;
    public static final int PROGRESS_MODE_DISPLAY = 1;
    public static final int PEEK_OPACITY_MODE_OPAQUE = 0;
    public static final int PEEK_OPACITY_MODE_TRANSLUCENT = 1;
    public static final int BACKGROUND_VISIBILITY_INTERRUPTIVE = 0;
    public static final int BACKGROUND_VISIBILITY_PERSISTENT = 1;
    public static final int AMBIENT_PEEK_MODE_VISIBLE = 0;
    public static final int AMBIENT_PEEK_MODE_HIDDEN = 1;
    public static final int PROTECT_STATUS_BAR = 1;
    public static final int PROTECT_HOTWORD_INDICATOR = 2;
    public static final int PROTECT_WHOLE_SCREEN = 4;
    public static final java.lang.String KEY_COMPONENT = "component";
    public static final java.lang.String KEY_CARD_PEEK_MODE = "cardPeekMode";
    public static final java.lang.String KEY_CARD_PROGRESS_MODE = "cardProgressMode";
    public static final java.lang.String KEY_BACKGROUND_VISIBILITY = "backgroundVisibility";
    public static final java.lang.String KEY_SHOW_SYSTEM_UI_TIME = "showSystemUiTime";
    public static final java.lang.String KEY_AMBIENT_PEEK_MODE = "ambientPeekMode";
    public static final java.lang.String KEY_PEEK_CARD_OPACITY = "peekOpacityMode";
    public static final java.lang.String KEY_VIEW_PROTECTION_MODE = "viewProtectionMode";
    public static final java.lang.String KEY_STATUS_BAR_GRAVITY = "statusBarGravity";
    public static final java.lang.String KEY_HOTWORD_INDICATOR_GRAVITY = "hotwordIndicatorGravity";
    public static final java.lang.String KEY_SHOW_UNREAD_INDICATOR = "showUnreadIndicator";


    public static class Builder {
        public Builder(Object o){

        }
        public void setCardPeekMode(int i){}
        public void setBackgroundVisibility(int i){}
        public void setHotwordIndicatorGravity(int i){}
        public void setStatusBarGravity(int i){}
        public Object build(){ return new Object();}

    }
}
