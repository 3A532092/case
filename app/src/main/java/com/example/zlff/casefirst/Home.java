package com.example.zlff.casefirst;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static com.example.zlff.casefirst.DbConstants.CARKIND;
import static com.example.zlff.casefirst.DbConstants.DATE;
import static com.example.zlff.casefirst.DbConstants.NUM;
import static com.example.zlff.casefirst.DbConstants.PLTNO;
import static com.example.zlff.casefirst.DbConstants.REMARKS;
import static com.example.zlff.casefirst.DbConstants.RULE;
import static com.example.zlff.casefirst.DbConstants.TABLE_NAME;

import java.io.IOException;
import java.util.Calendar;

import static android.provider.BaseColumns._ID;

public class Home extends AppCompatActivity {
    int year, month, day, hour;
    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbHelper=new DBHelper(this);

        //刪除超過三個月的資料
        try {
            delet();

        }catch (Exception e){
        }
    }


    public void btn_record_Click(View view) {
        Intent recordintent=new Intent();
        recordintent.setClass(this,YNuploaded.class);
        startActivity(recordintent);

    }

    public void btn_signout_Click(View view){
        SharedPreferences pref = getSharedPreferences("00", MODE_PRIVATE);
        pref.edit()
                .putString("username","")
                .commit();
        startActivity(new Intent(this,LoginAvtivity.class));

    }

    public void btn_writein_Click (View view){

            Intent timeintent = new Intent();
            timeintent.setClass(this, MainActivity.class);

            startActivity(timeintent);
    }

    private void delet(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Calendar mCal = Calendar.getInstance();
        int now_day=mCal.get(Calendar.DAY_OF_MONTH);
        int now_year=mCal.get(Calendar.YEAR) - 1911;


        Cursor cursor = db.rawQuery(
                "select pltno,date,_ID from "+TABLE_NAME, new String[]{});
        while (cursor.moveToNext()) {
            String getdate = cursor.getString(1);
            String getid = cursor.getString(2);
            String getdatemon=getdate.substring(getdate.indexOf("年")+1,getdate.indexOf("月"));
            String getdateday=getdate.substring(getdate.indexOf("月")+1,getdate.indexOf("日"));
            String getyear=getdate.substring(0,getdate.indexOf("年"));
            int now_mon=mCal.get(Calendar.MONTH) + 1;

            now_mon=now_mon+12*(now_year-Integer.parseInt(getyear));

            if(now_mon-Integer.parseInt(getdatemon)>=3 && now_day>Integer.parseInt(getdateday)){
                db.delete(TABLE_NAME, _ID + "=" +getid+" and "+DATE+"='"+getdate+"'", null);
            }

        }
        db.close();
        cursor.close();
    }


}
