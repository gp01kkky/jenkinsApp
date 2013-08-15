package jenkinsapp.server.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseUtils {
	
	//Database fields
	private SQLiteDatabase database;
	private ServerSqlDatabase dbHelper;
	private String[] allColumns = { ServerSqlDatabase.ID,
			ServerSqlDatabase.HOSTNAME,
			ServerSqlDatabase.HOST_URL,
			ServerSqlDatabase.USERNAME,
			ServerSqlDatabase.TOKEN,
			ServerSqlDatabase.IS_HTTPS
	};
	
	public DatabaseUtils(Context context)
	{
		dbHelper = new ServerSqlDatabase(context);
	}
	
	/**
	 * To open the database for writing input
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	/**
	 * To close the database
	 */
	public void close() {
		dbHelper.close();
	}
	
	public ServerData createServer(String hostname, String hostUrl, String userName, String token, String isHttps)
	{
		ContentValues values = new ContentValues();
		values.put(ServerSqlDatabase.HOSTNAME, hostname);
		values.put(ServerSqlDatabase.HOST_URL, hostUrl);
		values.put(ServerSqlDatabase.USERNAME, userName);
		values.put(ServerSqlDatabase.TOKEN, token);
		values.put(ServerSqlDatabase.IS_HTTPS, isHttps);
		long insertId = database.insert(ServerSqlDatabase.TABLE_SERVER, null, values);
		Cursor cursor  = database.query(ServerSqlDatabase.TABLE_SERVER, allColumns, ServerSqlDatabase.ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		ServerData newServer = cursorToServer(cursor);
		cursor.close();
		return newServer;
	}
	
	public void deleteServer(ServerData server)
	{
		long id =  server.getId();
		database.delete(ServerSqlDatabase.TABLE_SERVER, ServerSqlDatabase.ID + " = " + id, null);
	}
	
	public List<ServerData> getAllServer(){
		List<ServerData> serverData = new ArrayList<ServerData>();
		Cursor cursor = database.query(ServerSqlDatabase.TABLE_SERVER, allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			ServerData server = cursorToServer(cursor);
			serverData.add(server);
			cursor.moveToNext();
		}
		cursor.close();
		return serverData;
	}
	
	private ServerData cursorToServer(Cursor cursor)
	{
		ServerData server = new ServerData();
		server.setId(cursor.getLong(0));
		server.setDescription(cursor.getString(1));
		server.setUrl(cursor.getString(2));
		server.setUserName(cursor.getString(3));
		server.setToken(cursor.getString(4));
		server.setIsHttps(cursor.getString(5));
		return server;
	}
}
