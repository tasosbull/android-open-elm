package com.android.openelm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

//*********************************************************************************
//***** BEGIN GPL LICENSE BLOCK *****
//
//This program is free software; you can redistribute it and/or
//modify it under the terms of the GNU General Public License
//as published by the Free Software Foundation; either version 2
//of the License, or (at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program; if not, write to the Free Software  Foundation,
//Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
//
//The Original Code is Copyright (C) 2012 by Tasos Boulasikis tasosbull@gmail.com 
//All rights reserved.
//
//The Original Code is: all of this file.
//
//Contributor(s): none yet.
//
//***** END GPL LICENSE BLOCK *****
//
//Short description of this file
//************************************************************************************

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
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}

	}

}
