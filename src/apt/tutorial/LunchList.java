package apt.tutorial;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * @author Jarod Chiang
 * 
 */
public class LunchList extends ListActivity {
	Cursor model = null;
	RestaurantAdapter adapter = null;
	EditText name = null;
	EditText address = null;
	EditText notes = null;
	RadioGroup types = null;

	RestaurantHelper helper = null;
	public final static String ID_EXTRA = "apt.tutorial._ID";

	private static final String LOG_KEY = "TEST";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		helper = new RestaurantHelper(this);

//		name = (EditText) findViewById(R.id.name);
/*		address = (EditText) findViewById(R.id.addr);
		types = (RadioGroup) findViewById(R.id.types);
		notes = (EditText) findViewById(R.id.notes);

		Button save = (Button) findViewById(R.id.save);
		

		ListView list = (ListView) findViewById(R.id.list);
*/
		model = helper.getAll();
		startManagingCursor(model);
		adapter = new RestaurantAdapter(model);
		setListAdapter(adapter);
	//	list.setAdapter(adapter);
		//
		// TabHost.TabSpec spec = getTabHost().newTabSpec("tag1");
		//
		// spec.setContent(R.id.restaurants);
		// spec.setIndicator("List",
		// getResources().getDrawable(R.drawable.list));
		//
		// getTabHost().addTab(spec);
		//
		// spec = getTabHost().newTabSpec("tag2");
		// spec.setContent(R.id.details);
		// spec.setIndicator("Details",
		// getResources().getDrawable(R.drawable.restaurant));
		//
		// getTabHost().addTab(spec);
		// getTabHost().setCurrentTab(0);
		//
		// list.setOnItemClickListener(onListClick);
		// getTabHost().setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// appendLog("OnClick");
		// Log.d(LOG_KEY, "OnClick");
		// }
		// });

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		helper.close();
	}

    @Override
	public void onListItemClick(ListView list, View view, int position, long id) {
			Intent i = new Intent(LunchList.this, DetailForm.class);

			i.putExtra(ID_EXTRA, String.valueOf(id));
			startActivity(i);

			// model.moveToPosition(position);
			// name.setText(helper.getName(model));
			// address.setText(helper.getAddress(model));
			// notes.setText(helper.getNotes(model));
			//
			// if (helper.getType(model).equals("sit_down")) {
			// types.check(R.id.sit_down);
			// } else if (helper.getType(model).equals("take_out")) {
			// types.check(R.id.take_out);
			// } else {
			// types.check(R.id.delivery);
			// }
			//
			// getTabHost().setCurrentTab(1);
	};

	class RestaurantAdapter extends CursorAdapter {
		RestaurantAdapter(Cursor c) {
			super(LunchList.this, c);
		}

		@Override
		public void bindView(View row, Context ctxt, Cursor c) {
			RestaurantHolder holder = (RestaurantHolder) row.getTag();

			holder.populateFrom(c, helper);
		}

		@Override
		public View newView(Context ctxt, Cursor c, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.row, parent, false);
			RestaurantHolder holder = new RestaurantHolder(row);

			row.setTag(holder);

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

		void populateFrom(Cursor c, RestaurantHelper helper) {
			name.setText(helper.getName(c));
			address.setText(helper.getAddress(c));

			if (helper.getType(c).equals("sit_down")) {
				icon.setImageResource(R.drawable.ball_red);
			} else if (helper.getType(c).equals("take_out")) {
				icon.setImageResource(R.drawable.ball_yellow);
			} else {
				icon.setImageResource(R.drawable.ball_green);
			}
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

			buf.append(getDateTime() + "\n" + text);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.option, menu);
		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.add) {
			startActivity(new Intent(LunchList.this, DetailForm.class));
		
		return (true);
		}
		return (super.onOptionsItemSelected(item));
	}
}
