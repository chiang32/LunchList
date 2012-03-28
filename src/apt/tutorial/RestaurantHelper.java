package apt.tutorial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class RestaurantHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "lunchlist2.db";
	private static final int SCHEMA_VERSION = 3;

	public RestaurantHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE restaurants ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "name TEXT, address TEXT, type TEXT, notes TEXT ,feed TEXT,"
				+ "lat REAL, lon REAL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// no-op, since will not be called until 2nd schema
		// version exists
		if ( oldVersion < 2) {
			db.execSQL("ALTER TABLE restaurants ADD COLUMN feed TEXT");
		}
		if ( oldVersion < 3) {
			db.execSQL("ALTER TABLE restaurants ADD COLUMN lat REAL");
			db.execSQL("ALTER TABLE restaurants ADD COLUMN lon REAL");
		}
	}

	public void insert(String name, String address, String type, String notes,
			String feed) {
		ContentValues cv = new ContentValues();

		cv.put("name", name);
		cv.put("address", address);
		cv.put("type", type);
		cv.put("notes", notes);
		cv.put("feed", feed);

		getWritableDatabase().insert("restaurants", "name", cv);
	}

	public void updateLocation(String id, double lat, double lon) {
		ContentValues cv = new ContentValues();
		String[] args = { id };

		cv.put("lat", lat);
		cv.put("lon", lon);

		getWritableDatabase().update("restaurants", cv, "_ID=?", args);
	}

	public void update(String id, String name, String address, String type,
			String notes, String feed) {
		ContentValues cv = new ContentValues();
		String[] args = { id };

		cv.put("name", name);
		cv.put("address", address);
		cv.put("type", type);
		cv.put("notes", notes);
		cv.put("feed", feed);

		getWritableDatabase().update("restaurants", cv, "_ID=?", args);
	}

	public Cursor getAll(String orderBy) {
		return (getReadableDatabase()
				.rawQuery(
						"SELECT _id, name, address, type, notes, feed, lat, lon FROM restaurants ORDER BY "
								+ orderBy, null));
	}

	public String getName(Cursor c) {
		return (c.getString(1));
	}

	public String getAddress(Cursor c) {
		return (c.getString(2));
	}

	public String getType(Cursor c) {
		return (c.getString(3));
	}

	public String getNotes(Cursor c) {
		return (c.getString(4));
	}


	public String getFeed(Cursor c) {
		return (c.getString(5));
	}

	public double getLatitude(Cursor c) {
		return (c.getDouble(6));
	}
	public double getLongitude(Cursor c) {
		return (c.getDouble(7));
	}

	public Cursor getById(String id) {
		String[] args = { id };
		return (getReadableDatabase()
				.rawQuery(
						"SELECT _id, name, address, type, notes, feed, lat, lon FROM restaurants WHERE _ID=?",
						args));
	}

}
