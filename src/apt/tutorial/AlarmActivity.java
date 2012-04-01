package apt.tutorial;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class AlarmActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	Log.e("AlarmActivity-onCreate", "p"+new Date().toString());

    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.alarm);
    }
}
