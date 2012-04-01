package apt.tutorial;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

public class TimePreference extends DialogPreference {
  private int lastHour=0;
  private int lastMinute=0;
  private TimePicker picker=null;
  /*and getMinute() are static helper methods, to extract the
hour and minute, as integers, from a string encoded in HH:MM format.
We have to store our time collected by TimePreference as a single
piece of data in the SharedPreferences, so storing it as an HH:MM
formatted string seems like a reasonable choice.
getHour()
*/
  public static int getHour(String time) {
    String[] pieces=time.split(":");
    
    return(Integer.parseInt(pieces[0]));
  }

  public static int getMinute(String time) {
    String[] pieces=time.split(":");
    
    return(Integer.parseInt(pieces[1]));
  }

  public TimePreference(Context ctxt, AttributeSet attrs) {
    super(ctxt, attrs);
    
    setPositiveButtonText("Set");
    setNegativeButtonText("Cancel");
  }
  /*onCreateDialogView()
will be called as part of the dialog box being
displayed. We need to return a View that represents the content of
the dialog. We could inflate a layout here, if we wanted. However,
for simplicity, we are simply using a TimePicker widget constructed
directly in Java.
*/
  @Override
  protected View onCreateDialogView() {
    picker=new TimePicker(getContext());
    
    return(picker);
  }
/*
*onBindDialogView()
will be called after onCreateDialogView(), and our
job is to fill in whatever preference data should go into that dialog.
Some other methods described later in this list will have been called
first, populating a lastHour and lastMinute pair of data members
with the hour and minute from the SharedPreferences. We just turn
around and pop those into the TimePicker.
**/
  @Override
  protected void onBindDialogView(View v) {
    super.onBindDialogView(v);
    
    picker.setCurrentHour(lastHour);
    picker.setCurrentMinute(lastMinute);
  }
//  onDialogClosed()
//  will be called when the user clicks either the
//  positive or negative button, or clicks the BACK button (same as
//  clicking the negative button). If they clicked the positive button, we
//  assemble a new HH:MM string from the values in the TimePicker, then
//  tell DialogPreference to persist that value to the SharedPreferences.

  @Override
  protected void onDialogClosed(boolean positiveResult) {
    super.onDialogClosed(positiveResult);

    if (positiveResult) {
      lastHour=picker.getCurrentHour();
      lastMinute=picker.getCurrentMinute();
      
      String time=String.valueOf(lastHour)+":"+String.valueOf(lastMinute);
      
      if (callChangeListener(time)) {
        persistString(time);
      }
    }
  }
//  onGetDefaultValue() will be called when
//  convert an android:defaultValue attribute
//  Android needs us to  into an object of the
//  appropriate data type. For example, an integer preference would
//  need to convert the android:defaultValue String to an Integer. In
//  our case, our preference is being stored as a String, so we can
//  extract the String from the TypedArray that represents all of the
//  attributes on this preference in the preference XML resource.

  @Override
  protected Object onGetDefaultValue(TypedArray a, int index) {
    return(a.getString(index));
  }
//  Finally, onSetInitialValue() will be called before onBindDialogView(),
//  where we are told the actual preference value to start with. That
//  could be an actual saved preference value from before, or the
//  android:defaultValue value, or nothing at all (in which case, we start
//  with "00:00"). Wherever the string comes from, we parse it into the
//  lastHour and lastMinute integer data members for use by
//  onBindDialogView().

  @Override
  protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
    String time=null;
    
    if (restoreValue) {
      if (defaultValue==null) {
        time=getPersistedString("00:00");
      }
      else {
        time=getPersistedString(defaultValue.toString());
      }
    }
    else {
      time=defaultValue.toString();
    }
    
    lastHour=getHour(time);
    lastMinute=getMinute(time);
  }
}