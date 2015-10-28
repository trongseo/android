package edu.cs4730.qrDemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	String TAG = "DBAdapter";

	public static final String STU_TABLE = "stu_table";
	public static final String STU_ID = "_id";// 0 integer
	public static final String STU_NAME = "name";// 1 text(String)
	public static final String STU_AGE = "age";// 2 integer
	public static final String STU_BIRTH = "birthday";// 3 date(String)
	public static final String STU_SCORE = "score";// 3 date(String)

	private SQLiteDatabase db;
	private DBHelper dbHelper;

	public DBAdapter(Context context) {
		dbHelper = new DBHelper(context);
	}

	/**
	 * 打开数�?�库
	 */
	public void open() {
		if (null == db || !db.isOpen()) {
			try {
				db = dbHelper.getWritableDatabase();
			} catch (SQLiteException sqLiteException) {
				Log.e(TAG, "数�?�库打开失败");
			}
		}
	}

	/**
	 * 关闭数�?�库
	 */
	public void close() {
		if (db != null) {
			db.close();
		}
	}

	/**
	 * 
	 * @param table
	 *            在哪个表中�?�入数�?�
	 * @param values
	 *            使用ContentValues�?��?装�?�入的数�?�
	 * @return 返回�?�入�?�的行�?�
	 */
	public int insert(String table, ContentValues values) {
		Log.e(TAG, "�?�入数�?� " + values);
		return (int) db.insert(table, null, values);
	}
	public void deleteTable(){
		
		db.execSQL("delete from "+STU_TABLE+";");
	}
	/**
	 * 查询数�?�，获得所有记录
	 * 
	 * @param table
	 *            在哪个表中查找
	 * @return 返回查询得到的所有记录
	 */
	public Cursor getAllRow(String table) {
		return db.query(table, null, null, null, null, null, STU_ID);
	}
 public String getOneRow(String barcode){
	 String returnV = "";
	 String selectQuery = "SELECT name FROM "+STU_TABLE+" WHERE age =?";
	 Cursor c = db.rawQuery(selectQuery, new String[] { barcode });
	 if (c.moveToFirst()) {
		 returnV = c.getString(c.getColumnIndex("name"));
	 }
	 c.close();
	 return returnV;
 }
	/**
	 * SQLite 辅助类：创建数�?�库和表
	 * 
	 * @author tangyuchun
	 * 
	 */

	private class DBHelper extends SQLiteOpenHelper {
		private static final int VERSION = 11;
		private static final String DB_NAME = "stu_dbas.db";

		public DBHelper(Context context) {
			super(context, DB_NAME, null, VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			
			String create_sql = "CREATE TABLE IF NOT EXISTS " + STU_TABLE + "("
					+ STU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + STU_NAME
					+ " TEXT NOT NULL," + STU_SCORE + " TEXT NOT NULL,"
					+ STU_AGE + " TEXT NOT NULL," + STU_BIRTH
					+ " DATE NOT NULL" + ")";

			db.execSQL(create_sql);
			Log.d(TAG, "创建表" + STU_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + STU_TABLE);
		}
	}
}
