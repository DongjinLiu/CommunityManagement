package com.example.jin.communitymanagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDBHelper extends SQLiteOpenHelper {

    public static final String CREATE_USERDATA = "create table userData(" +
            "id integer primary key autoincrement,"
            + "name text,"
            + "password text)";
    public static final  String CREATE_AC_TABLE="create table ActivityTable("+
            "time_start text,"
            +"time_end text,"
            +"association text,"
            +"introduction text,"
            +"inNeedMoney integer)";



    private Context mContext;

    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version) {
        super(context, name, cursorFactory, version);
        mContext = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERDATA);
        db.execSQL(CREATE_AC_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists userData");
        db.execSQL("drop table if exists ActivityTable");
        onCreate(db);
        Toast.makeText(mContext, "升级成功", Toast.LENGTH_SHORT).show();
    }

}
