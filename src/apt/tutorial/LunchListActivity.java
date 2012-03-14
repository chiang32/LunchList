package apt.tutorial;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

public class LunchListActivity extends Activity {
	List<Restaurant> model = new ArrayList<Restaurant>();
	RestaurantAdapter adapter = null;

//	ArrayAdapter<Restaurant> adapter = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(onSave);

		ListView list = (ListView) findViewById(R.id.restaurants);
		adapter = new RestaurantAdapter();

		list.setAdapter(adapter);

		Restaurant r3 = new Restaurant();
		r3.setAddress("aaaa3");
		r3.setName("name3");
		r3.setType("sit_down");
		adapter.add(r3);

	}

	private View.OnClickListener onSave = new View.OnClickListener() {
		public void onClick(View v) {
			Restaurant r = new Restaurant();

			EditText name = (EditText) findViewById(R.id.name);
			EditText address = (EditText) findViewById(R.id.addr);

			r.setName(name.getText().toString());
			r.setAddress(address.getText().toString());

			RadioGroup types = (RadioGroup) findViewById(R.id.types);

			switch (types.getCheckedRadioButtonId()) {
			case R.id.sit_down:
				r.setType("sit_down");
				break;
			case R.id.take_out:
				r.setType("take_out");
				break;
			case R.id.delivery:
				r.setType("delivery");
				break;
			}
			adapter.add(r);
			appendLog("counts are " + adapter.getCount());
		}

	};

	/**
	 * @description write logs
	 * @param text
	 */
	public void appendLog(String text) {
		String path = "/mnt/sdcard/jarod.log";
		File logFile = new File(path);
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			// BufferedWriter for performance, true to set append to file flag
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
					true));
			buf.append(text);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class RestaurantAdapter extends ArrayAdapter<Restaurant> {
		RestaurantAdapter() {
			super(LunchListActivity.this, android.R.layout.simple_list_item_1, model);
		}
	}

}
