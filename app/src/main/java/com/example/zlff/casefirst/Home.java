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
import static com.example.zlff.casefirst.DbConstants.REMARKS;
import static com.example.zlff.casefirst.DbConstants.TABLE_NAME;

import java.util.Calendar;

import static android.provider.BaseColumns._ID;

public class Home extends AppCompatActivity {
    int year, month, day, hour, minute,sec;
    String thisDate;
    private TextView result;
    private EditText edtdel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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


}
