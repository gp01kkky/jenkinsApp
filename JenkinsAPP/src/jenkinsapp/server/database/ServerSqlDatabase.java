package jenkinsapp.server.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ServerSqlDatabase extends SQLiteOpenHelper{
	
	public static final String TABLE_SERVER = "servers";
	public static final String ID = "_id";
	public static final String HOSTNAME = "hostname";
	public static final String HOST_URL = "hostUrl";
	public static final String USERNAME = "username";
	public static final String IS_HTTPS = "isHttps";
	public static final String PORT = "port";
	public static final String TOKEN = "token";
	
	private static final String DATABASE_NAME = "server.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_SERVER + "(" + ID
			+ " integer primary key autoincrement, " + HOSTNAME
			+ " text not null, " + HOST_URL
			+ " text not null, " + USERNAME
			+ " text not null, " + TOKEN
			+ " text not null," + IS_HTTPS
			+ " text not null," + PORT
			+ " text not null);";
	
	public ServerSqlDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTsS " + TABLE_SERVER);
		onCreate(db);
	}
	

}
