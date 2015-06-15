package com.lyndoo.mypassword.db;

import java.util.ArrayList;
import java.util.List;

import com.lyndoo.mypassword.model.Item;
import com.lyndoo.mypassword.model.Record;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.inputmethodservice.Keyboard.Row;
import android.util.Log;

public class MyPasswordDB {
	/**
	 * ���ݿ���
	 */
	public static final String DB_NAME = "mypassword";

	/**
	 * ���ݿ�汾
	 */
	public static final int VERSION = 1;

	private static MyPasswordDB mypasswordDB;
	private SQLiteDatabase db;

	/**
	 * ˽�л����캯��
	 * 
	 * @param context
	 */
	private MyPasswordDB(Context context) {
		PasswordOpenHelper dbhelper = new PasswordOpenHelper(context, DB_NAME,
				null, VERSION);
		db = dbhelper.getWritableDatabase();
	}

	/**
	 * ��ȡMyPasswordDB��ʵ��
	 * 
	 * @param context
	 * @return
	 */
	public synchronized static MyPasswordDB getInstance(Context context) {
		if (mypasswordDB == null)
			mypasswordDB = new MyPasswordDB(context);
		return mypasswordDB;
	}

	/**
	 * ��recordʵ���洢�����ݿ�
	 * @param record
	 * @return
	 */
	public int saveRecord(Record record) {
		if (record != null) {
			ContentValues values = new ContentValues();
			values.put("title", record.getTitle());
			values.put("firstLetter", String.valueOf(record.getFirstLetter()));
			values.put("category", record.getCategory());
			long rows = db.insert("record", null, values);
			if (rows > 0) {
				Cursor cursor = db.rawQuery("select max(id) from record", null);
				if(cursor.moveToFirst())
					return cursor.getInt(cursor.getColumnIndex("id"));
			}
		}
		return -1;
	}
	
	/**
	 * ɾ��1��recordʵ����ɾ��������itemʵ��
	 * @param record
	 */
	public void delRecord(Record record){
		String recordID = String.valueOf(record.getId());
		db.beginTransaction();
		try {
			db.delete("record", "id = ?", new String[]{recordID});
			db.delete("item", "recordID = ?", new String[]{recordID});
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.endTransaction();
		}
	}
	
	
	/**
	 * δ���
	 * @param record
	 * @param items
	 * @return
	 */
	public int updateRecord(Record record,List<Item> items){
		int rows=0;
		String recordID=String.valueOf(record.getId());
		ContentValues recordValues = new ContentValues();
		recordValues.put("title", record.getTitle());
		recordValues.put("firstLetter", record.getFirstLetter());
		db.beginTransaction();
		int temp = db.update("Reord", recordValues, "id = ?", new String[]{recordID});
		for(Item item : items){
			ContentValues values = new ContentValues();
			values.put("title", item.getTitle());
			values.put("content", item.getContent());
		}		
		rows = temp;
		db.setTransactionSuccessful();
		db.endTransaction();
		
		return rows;
	}
	
	/**
	 * �����ݿ��ȡ������Ŀ
	 * @param category
	 * @return
	 */
	public List<Record> loadRecord(int category){
		List<Record> records = new ArrayList<Record>();
		Cursor cursor = db.query("record", null, null, null, null, null, "firstLetter");
		if(cursor.moveToFirst()){
			do {
				Record record = new Record();
				record.setId(cursor.getInt(cursor.getColumnIndex("id")));
				record.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				record.setCategory(cursor.getInt(cursor.getColumnIndex("category")));
				record.setFirstLetter(cursor.getString(cursor.getColumnIndex("firstLetter")));
				records.add(record);
			} while (cursor.moveToNext());
		}
		return records;
	}
	
	/**
	 * ��itemʵ���洢�����ݿ�
	 * @param item
	 * @return
	 */
	public void saveItem(Item item){
		if(item !=null){
			ContentValues values = new ContentValues();
			values.put("recordID", item.getRecordID());
			values.put("title", item.getTitle());
			values.put("content", item.getContent());
			values.put("type", item.getType());
			values.put("order", item.getOrder());
			db.insert("item", null, values);
		}
	}
	
	/**
	 * ɾ��һ��itemʵ��
	 * @param item
	 * @return
	 */
	public int delItem(Item item){
		return db.delete("item", "id = ?", new String[]{String.valueOf(item.getId())});
	}

	/**
	 * ɾ�����itemʵ��
	 * @param item
	 * @return
	 */
	public int delItem(String itemIDs){
		return db.delete("item", "id in (?)", new String[]{itemIDs});
	}
	
	/**
	 * ��ȡ���ݿ���item
	 * @param recordID
	 * @return
	 */
	public List<Item> loadItem(int recordID){
		List<Item> items = new ArrayList<Item>();
		Cursor cursor = db.query("item", null, null, null, null, null, "order");
		if(cursor.moveToFirst()){
			do {
				Item item = new Item();
				item.setId(cursor.getInt(cursor.getColumnIndex("id")));
				item.setRecordID(cursor.getInt(cursor.getColumnIndex("recordID")));
				item.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				item.setContent(cursor.getString(cursor.getColumnIndex("content")));
				item.setType(cursor.getInt(cursor.getColumnIndex("type")));
				item.setOrder(cursor.getInt(cursor.getColumnIndex("order")));
				items.add(item);
			} while (cursor.moveToNext());
		}
		return items;
	}
}
