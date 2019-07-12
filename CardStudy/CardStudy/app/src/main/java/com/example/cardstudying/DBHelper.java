package com.example.cardstudying;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    DBHelper(Context context) {
        super(context, "dbHelper", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table cardsTable("
                + "id integer primary key autoincrement,"
                + "question text,"
                + "answer text,"
                + "level integer" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
