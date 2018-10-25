package com.example.zlff.casefirst;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import static android.provider.BaseColumns._ID;
import static com.example.zlff.casefirst.DbConstants.BTNUPLOAD;
import static com.example.zlff.casefirst.DbConstants.CARKIND;
import static com.example.zlff.casefirst.DbConstants.FACT;
import static com.example.zlff.casefirst.DbConstants.LAW;
import static com.example.zlff.casefirst.DbConstants.LIC;
import static com.example.zlff.casefirst.DbConstants.DATE;
import static com.example.zlff.casefirst.DbConstants.NUM;
import static com.example.zlff.casefirst.DbConstants.REMARKS;
import static com.example.zlff.casefirst.DbConstants.TABLE_NAME;
import static com.example.zlff.casefirst.DbConstants.USERNAME;


public class Savelist extends AppCompatActivity {
    private DBHelper dbHelper;
    private ListView listsave;
    private String Btnupload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savelist);

        dbHelper = new DBHelper(this);
        listsave = (ListView) findViewById(R.id.list_save);

        showInList();


    }

    private void showInList() {
        Cursor cursor = getCursor();
        final String[] from = {NUM, DATE, LIC, CARKIND, LAW, REMARKS, FACT};
        int[] to = {R.id.txtNum, R.id.txtDate};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.data_item, cursor, from, to); //SimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to)
        listsave.setAdapter(adapter);
        listsave.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                Intent intent = new Intent();
                intent.setClass(Savelist.this, Record_modify.class);
                TextView txvNum = (TextView) view.findViewById(R.id.txtNum);
                TextView txvDate = (TextView) view.findViewById(R.id.txtDate);

                intent.putExtra("TONUMBER", txvNum.getText());
                intent.putExtra("TODATE", txvDate.getText());

                startActivity(intent);
            }
        });
    }

    private Cursor getCursor() {
        String getusername = getSharedPreferences("00", MODE_PRIVATE)
                .getString("username", "");

        SQLiteDatabase db = dbHelper.getReadableDatabase();  //透過dbHelper取得讀取資料庫的SQLiteDatabase物件，可用在查詢
        String[] columns = {USERNAME, _ID, NUM, LIC, CARKIND, LAW, REMARKS, FACT, DATE};
        Cursor cursor = db.query(TABLE_NAME, columns, "USERNAME='" + getusername + "'", null, null, null, null);  //查詢所有欄位的資料
        return cursor;
    }

}