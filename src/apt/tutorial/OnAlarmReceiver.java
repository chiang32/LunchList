package apt.tutorial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
    	Log.e("OnAlarmReceiver-onReceive", "p");

		Intent i = new Intent(context, AlarmActivity.class);
//		We need to add FLAG_ACTIVITY_NEW_TASK to the Intent, because if we do not,
//		our startActivity() call will fail with an error telling us to add
//		FLAG_ACTIVITY_NEW_TASK. Calling startActivity() from someplace other than
//		an activity typically requires this flag, though sometimes it is automatically
//		added for you.

		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		context.startActivity(i);
	}

}
