package apt.tutorial;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DetailForm extends Activity {
	EditText name = null;
	EditText address = null;
	EditText notes = null;
	EditText feed = null;
	RadioGroup types = null;
	TextView location = null;

	RestaurantHelper helper = null;
	String restaurantId = null;
	LocationManager locMgr = null; // the system service that is our gateway to
									// location information
    
	@Override
	protected void onPause() {
		save();
		locMgr.removeUpdates(onLocationChange);

		super.onPause();
	}

	private void save() {
		// TODO Auto-generated method stub
		String type = null;
		switch (types.getCheckedRadioButtonId()) {
		case R.id.sit_down:
			type = "sit_down";
			break;
		case R.id.take_out:
			type = "take_out";
			break;
		case R.id.delivery:
			type = "delivery";

		default:
			break;
		}
		if (restaurantId == null) {
			helper.insert(name.getText().toString(), address.getText()
					.toString(), type, notes.getText().toString(), feed
					.getText().toString());
		} else {
			helper.update(restaurantId, name.getText().toString(), address
					.getText().toString(), type, notes.getText().toString(),
					feed.getText().toString());
		}
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
		helper = new RestaurantHelper(this);

		name = (EditText) findViewById(R.id.name);
		address = (EditText) findViewById(R.id.addr);
		types = (RadioGroup) findViewById(R.id.types);
		notes = (EditText) findViewById(R.id.notes);
		feed = (EditText) findViewById(R.id.feed);

		location = (TextView) findViewById(R.id.location);

		restaurantId = getIntent().getStringExtra(LunchList.ID_EXTRA);
		if (restaurantId != null) {
			load();
		}
		locMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		helper.close();
	}

	LocationListener onLocationChange = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLocationChanged(Location fix) {
Log.e(this.getClass().getName(), "msg");
			// Update the UI with the GPS coordinates
			location.setText(String.valueOf(fix.getLatitude()) + ", "
					+ String.valueOf(fix.getLongitude()));
			// • Save those GPS coordinates in the database for this restaurant
			helper.updateLocation(restaurantId, fix.getLatitude(),
					fix.getLongitude());
			// • Stop requesting updates, since we only need the one
			locMgr.removeUpdates(onLocationChange);
			Toast.makeText(DetailForm.this, "Location saved.",
					Toast.LENGTH_LONG).show();

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.details_option, menu);

		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		if (restaurantId == null) {
			menu.findItem(R.id.location).setEnabled(false);
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.e("eeee", "line");
		if (item.getItemId() == R.id.feed) {
			if (isNetworkAvailable()) {

				Intent i = new Intent(this, FeedActivity.class);
				i.putExtra(FeedActivity.FEED_URL, feed.getText().toString());
				// Toast.makeText(this, "Sorry, "+feed.getText().toString(),
				// Toast.LENGTH_LONG).show();
				startActivity(i);
			} else {
				Toast.makeText(this, "Sorry, the Internet is not available",
						Toast.LENGTH_LONG).show();
			}

			return (true);
		} else if (item.getItemId() == R.id.location) {
			
			Log.e(this.getClass().getName(), "line 169.");
			locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, onLocationChange);

			return (true);

		}

		return (super.onOptionsItemSelected(item));
	}

	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);

		state.putString("name", name.getText().toString());
		state.putString("address", address.getText().toString());
		state.putString("notes", notes.getText().toString());
		state.putString("feed", feed.getText().toString());
		state.putInt("type", types.getCheckedRadioButtonId());
	}

	@Override
	public void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);

		name.setText(state.getString("name"));
		address.setText(state.getString("address"));
		notes.setText(state.getString("notes"));
		feed.setText(state.getString("feed"));
		types.check(state.getInt("type"));
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();

		return (info != null);
	}

	private void load() {
		Cursor c = helper.getById(restaurantId);

		c.moveToFirst();
		name.setText(helper.getName(c));
		address.setText(helper.getAddress(c));
		notes.setText(helper.getNotes(c));
		feed.setText(helper.getFeed(c));
		location.setText(String.valueOf(helper.getLatitude(c)) + ", "
				+ String.valueOf(helper.getLongitude(c)));

		if (helper.getType(c).equals("sit_down")) {
			types.check(R.id.sit_down);
		} else if (helper.getType(c).equals("take_out")) {
			types.check(R.id.take_out);
		} else {
			types.check(R.id.delivery);
		}

		c.close();
	}

}
