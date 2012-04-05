package apt.tutorial;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class OnBootReceiver extends BroadcastReceiver {

	public static void setAlarm(Context arg0) {
		// TODO Auto-generated method stub
		AlarmManager mgr = (AlarmManager) arg0
				.getSystemService(Context.ALARM_SERVICE);
		Calendar cal = Calendar.getInstance();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(arg0);
		
		String time = prefs.getString("alarm_time", "16:15");
		Log.e("OnBootReceiver-setAlarm", "H:"+TimePreference.getHour(time));
		Log.e("OnBootReceiver-setAlarm", "M:"+TimePreference.getHour(time));
		cal.set(Calendar.HOUR_OF_DAY, TimePreference.getHour(time));
		cal.set(Calendar.MINUTE, TimePreference.getMinute(time));
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		if (cal.getTimeInMillis() < System.currentTimeMillis()) {
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}

		mgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				AlarmManager.INTERVAL_DAY, getPendingIntent(arg0));
	}

	public static void cancelAlarm(Context ctxt) {
		AlarmManager mgr = (AlarmManager) ctxt
				.getSystemService(Context.ALARM_SERVICE);

		mgr.cancel(getPendingIntent(ctxt));
	}
// getPendingIntent() broadcast to another receiver.
    private static PendingIntent getPendingIntent(Context ctxt) {
		Intent i = new Intent(ctxt, OnAlarmReceiver.class);
		return (PendingIntent.getBroadcast(ctxt, 0, i, 0));
	}

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		setAlarm(arg0);
	}
}
