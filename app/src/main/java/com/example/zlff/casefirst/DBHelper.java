package com.example.zlff.casefirst;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static android.provider.BaseColumns._ID;
import static com.example.zlff.casefirst.DbConstants.ADDR;
import static com.example.zlff.casefirst.DbConstants.BTNUPLOAD;
import static com.example.zlff.casefirst.DbConstants.CARKIND;
import static com.example.zlff.casefirst.DbConstants.FACT;
import static com.example.zlff.casefirst.DbConstants.LAW;
import static com.example.zlff.casefirst.DbConstants.LIC;
import static com.example.zlff.casefirst.DbConstants.DATE;
import static com.example.zlff.casefirst.DbConstants.NUM;
import static com.example.zlff.casefirst.DbConstants.REMARKS;
import static com.example.zlff.casefirst.DbConstants.TABLE_NAME;
import static com.example.zlff.casefirst.DbConstants.PIC;
import static com.example.zlff.casefirst.DbConstants.USERNAME;

public class DBHelper extends SQLiteOpenHelper {
        private final static String DATABASE_NAME="violation.db";  //資料庫檔案名稱
        private final static int DATABASE_VERSION=5;   //資料庫版本
        public DBHelper(Context context){
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            final String INIT_TABLE="create table "+TABLE_NAME+"("+_ID+" integer primary key autoincrement,"+USERNAME+" char,"+NUM+" char,"+LIC+" char,"+CARKIND+" char,"+LAW+" char,"+REMARKS+" char,"+FACT+" char,"+ADDR+" char,"+BTNUPLOAD+" char,"+DATE+" char,"+PIC+" char)";
            db.execSQL(INIT_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            final String DROP_TABLE="drop table if exists "+TABLE_NAME;
            db.execSQL(DROP_TABLE);
            onCreate(db);

        }
}
