package jenkinsapp.server.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BookmarkDataUtils {
	
	//Database fields
		private SQLiteDatabase database;
		private BookmarkSqlDatabase dbHelper;
		private String[] allColumns = { BookmarkSqlDatabase.ID,
				BookmarkSqlDatabase.HOSTNAME,
				BookmarkSqlDatabase.BOOKMARK_URL,
				BookmarkSqlDatabase.USERNAME,
				BookmarkSqlDatabase.TOKEN,
				BookmarkSqlDatabase.IS_HTTPS
		};
		
		
		public BookmarkDataUtils(Context context)
		{
			dbHelper = new BookmarkSqlDatabase(context);
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
		
		public BookmarkData createBookmark(String hostname, String bookmarkUrl, String userName, String token, String isHttps)
		{
			ContentValues values = new ContentValues();
			values.put(BookmarkSqlDatabase.HOSTNAME, hostname);
			values.put(BookmarkSqlDatabase.BOOKMARK_URL, bookmarkUrl);
			values.put(BookmarkSqlDatabase.USERNAME, userName);
			values.put(BookmarkSqlDatabase.TOKEN, token);
			values.put(BookmarkSqlDatabase.IS_HTTPS, isHttps);			
			long insertId = database.insert(BookmarkSqlDatabase.TABLE_BOOKMARK, null, values);
			Cursor cursor  = database.query(BookmarkSqlDatabase.TABLE_BOOKMARK, allColumns, BookmarkSqlDatabase.ID + " = " + insertId,
					null, null, null, null);
			cursor.moveToFirst();
			BookmarkData newBookmark = cursorToBookmark(cursor);
			cursor.close();
			return newBookmark;
		}
		
		public void deleteBookmark(BookmarkData bookmark)
		{
			long id =  bookmark.getId();
			database.delete(BookmarkSqlDatabase.TABLE_BOOKMARK, BookmarkSqlDatabase.ID + " = " + id, null);
		}
		
		public List<BookmarkData> getAllBookmark(){
			List<BookmarkData> bookmarkData = new ArrayList<BookmarkData>();
			Cursor cursor = database.query(BookmarkSqlDatabase.TABLE_BOOKMARK, allColumns, null, null, null, null, null);
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
				BookmarkData bookmark = cursorToBookmark(cursor);
				bookmarkData.add(bookmark);
				cursor.moveToNext();
			}
			cursor.close();
			return bookmarkData;
		}
		
		private BookmarkData cursorToBookmark(Cursor cursor)
		{
			BookmarkData bookmark = new BookmarkData();
			bookmark.setId(cursor.getLong(0));
			bookmark.setServerName(cursor.getString(1));
			bookmark.setBookmarkUrl(cursor.getString(2));
			bookmark.setUserName(cursor.getString(3));
			bookmark.setToken(cursor.getString(4));
			bookmark.setIsHttps(cursor.getString(5));	
			return bookmark;
		}
}
