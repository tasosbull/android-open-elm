package com.android.openelm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class ExpressionEvaluator {
	private static final String DATABASE_NAME = "ELM";
	private static final int DATABASE_VERSION = 1;
	private Context context;
	private SQLiteHelper sqLiteHelper;
	private SQLiteDatabase sqLiteDatabase;
	public static double DOUBLE_ERROR = Double.NaN;

	public ExpressionEvaluator(Context aContext) {
		context = aContext;
		sqLiteHelper = new SQLiteHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
		sqLiteDatabase = sqLiteHelper.getReadableDatabase();
	}

	public double Evaluate(String expression) {
		Cursor cursor = sqLiteDatabase.rawQuery("Select (" + expression + ");",
				null);
		double result = DOUBLE_ERROR;
		if (cursor.moveToFirst()) {
			result = cursor.getDouble(0);
		}
		cursor.close();
		return result;
	}

	private class SQLiteHelper extends SQLiteOpenHelper {

		public SQLiteHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}

	}

}
