package nl.frankkie.hwconwatchface;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    ClockfaceView mCfv;
    LinearLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initControls();
    }

    public void initUI(){
        setContentView(R.layout.activity_main);
        mCfv = (ClockfaceView) findViewById(R.id.main_clockface);
        mContainer = (LinearLayout) findViewById(R.id.main_container);
    }

    public void initControls(){
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup ambient = (ViewGroup) inflater.inflate(R.layout.control,mContainer);
        ((TextView)ambient.findViewById(R.id.control_text)).setText("Ambient");
        ((CheckBox)ambient.findViewById(R.id.control_checkbox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCfv.setAmbientMode(isChecked);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mCfv.initLater();
    }
}
