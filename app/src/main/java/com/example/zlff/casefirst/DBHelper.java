package com.example.zlff.casefirst;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static android.provider.BaseColumns._ID;
import static com.example.zlff.casefirst.DbConstants.ADDR;
import static com.example.zlff.casefirst.DbConstants.ALBUM;
import static com.example.zlff.casefirst.DbConstants.BTNUPLOAD;
import static com.example.zlff.casefirst.DbConstants.CARKIND;
import static com.example.zlff.casefirst.DbConstants.DATE;
import static com.example.zlff.casefirst.DbConstants.J_DATE;
import static com.example.zlff.casefirst.DbConstants.J_TIME;
import static com.example.zlff.casefirst.DbConstants.NUM;
import static com.example.zlff.casefirst.DbConstants.PICCNT;
import static com.example.zlff.casefirst.DbConstants.PIC_1;
import static com.example.zlff.casefirst.DbConstants.PIC_2;
import static com.example.zlff.casefirst.DbConstants.PIC_3;
import static com.example.zlff.casefirst.DbConstants.PIC_4;
import static com.example.zlff.casefirst.DbConstants.PIC_5;
import static com.example.zlff.casefirst.DbConstants.PLTNO;
import static com.example.zlff.casefirst.DbConstants.REMARKS;
import static com.example.zlff.casefirst.DbConstants.RULE;
import static com.example.zlff.casefirst.DbConstants.SPEED;
import static com.example.zlff.casefirst.DbConstants.SPLIMIT;
import static com.example.zlff.casefirst.DbConstants.TABLE_NAME;
import static com.example.zlff.casefirst.DbConstants.PIC;
import static com.example.zlff.casefirst.DbConstants.TRUTH;
import static com.example.zlff.casefirst.DbConstants.USERNAME;
import static com.example.zlff.casefirst.DbConstants.PNAME;
import static com.example.zlff.casefirst.DbConstants.WHITELIST;

public class DBHelper extends SQLiteOpenHelper {
        private final static String DATABASE_NAME="violation.db";  //資料庫檔案名稱
        private final static int DATABASE_VERSION=12;   //資料庫版本
        public DBHelper(Context context){
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            final String INIT_TABLE="create table "+TABLE_NAME+"("+_ID+" integer primary key autoincrement,"+USERNAME+" char,"+PNAME+" char,"+PLTNO+" char,"+ALBUM+" char,"+J_DATE+" char,"+J_TIME+" char,"+CARKIND+" char,"+RULE+" char,"+WHITELIST+" char,"+SPEED+" char,"+SPLIMIT+" char,"+ADDR+" char,"+TRUTH+" char,"+DATE+" char,"+PICCNT+" char,"+PIC_1+" char,"+PIC_2+" char,"+PIC_3+" char,"+PIC_4+" char,"+PIC_5+" char,"+BTNUPLOAD+" char,"+PIC+" char)";
            db.execSQL(INIT_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            final String DROP_TABLE="drop table if exists "+TABLE_NAME;
            db.execSQL(DROP_TABLE);
            onCreate(db);

        }
}
