package nl.frankkie.hwconwatchface;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;


public class MainActivity extends ActionBarActivity {

    ClockfaceView mCfv;
    LinearLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    public void initUI(){
        setContentView(R.layout.activity_main);
        mCfv = (ClockfaceView) findViewById(R.id.main_clockface);
        mContainer = (LinearLayout) findViewById(R.id.main_container);

    }

}
