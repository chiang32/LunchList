package apt.tutorial;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.R;
import android.app.TabActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * @author Jarod Chiang
 * 
 */
public class LunchListActivity extends TabActivity {
	List<Restaurant> model = new ArrayList<Restaurant>();
	RestaurantAdapter adapter = null;
	EditText name = null;
	EditText address = null;
	RadioGroup types = null;
	  private static final String LOG_KEY = "TEST";

	// ArrayAdapter<Restaurant> adapter = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		name = (EditText) findViewById(R.id.name);
		address = (EditText) findViewById(R.id.addr);
		types = (RadioGroup) findViewById(R.id.types);

		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(onSave);

		ListView list = (ListView) findViewById(R.id.restaurants);
		adapter = new RestaurantAdapter();

		list.setAdapter(adapter);

		TabHost.TabSpec spec = getTabHost().newTabSpec("tag1");

		spec.setContent(R.id.restaurants);
		spec.setIndicator("List", getResources().getDrawable(R.drawable.list));

		getTabHost().addTab(spec);

		spec = getTabHost().newTabSpec("tag2");
		spec.setContent(R.id.details);
		spec.setIndicator("Details",
				getResources().getDrawable(R.drawable.restaurant));

		getTabHost().addTab(spec);
		getTabHost().setCurrentTab(0);

		list.setOnItemClickListener(onListClick);
		getTabHost().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				appendLog("OnClick");
				  Log.d(LOG_KEY, "OnClick");
				//Toast.makeText(this, "message", Toast.LENGTH_LONG).show();

//				if (getTabHost().getCurrentTabTag().equals( myTabTag )) {
					//getTabHost().setCurrentTab(myTabIndex);
//				}
			}
		});

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
	private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
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

			buf.append(getDateTime() +"\n"+text);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class RestaurantAdapter extends ArrayAdapter<Restaurant> {
		RestaurantAdapter() {
			super(LunchListActivity.this, android.R.layout.simple_list_item_1,
					model);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			RestaurantHolder holder = null;

			if (row == null) {

				// Context mContext = LunchListActivity.this;
				appendLog(LAYOUT_INFLATER_SERVICE);
				// 下面俩种方法都可以
				// //LayoutInflater inflater = getLayoutInflater();
				// LayoutInflater inflater = (LayoutInflater)
				// mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

				LayoutInflater inflater = getLayoutInflater();

				row = inflater.inflate(R.layout.row, parent, false);
				holder = new RestaurantHolder(row);
				row.setTag(holder);
			} else {
				holder = (RestaurantHolder) row.getTag();
			}
			holder.populateFrom(model.get(position));
			return (row);

		}

	}

	static class RestaurantHolder {

		private TextView name = null;
		private TextView address = null;
		private ImageView icon = null;

		RestaurantHolder(View row) {
			name = (TextView) row.findViewById(R.id.title);
			address = (TextView) row.findViewById(R.id.address);
			icon = (ImageView) row.findViewById(R.id.icon);
		}

		void populateFrom(Restaurant r) {
			name.setText(r.getName());
			address.setText(r.getAddress());
			if (r.getType().equals("sit_down")) {
				icon.setImageResource(R.drawable.ball_red);
			} else if (r.getType().equals("take_out")) {
				icon.setImageResource(R.drawable.ball_yellow);
			} else {
				icon.setImageResource(R.drawable.ball_green);
			}
		}
	}

	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Restaurant r = model.get(position);
			name.setText(r.getName());
			address.setText(r.getAddress());

			if (r.getType().equals("sit_down")) {
				types.check(R.id.sit_down);
			} else if (r.getType().equals("take_out")) {
				types.check(R.id.take_out);
			} else {
				types.check(R.id.delivery);
			}
			getTabHost().setCurrentTab(1);
		}

	};
}
