package com.example.zlff.casefirst;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import static android.provider.BaseColumns._ID;
import static com.example.zlff.casefirst.DbConstants.BTNUPLOAD;
import static com.example.zlff.casefirst.DbConstants.CARKIND;
import static com.example.zlff.casefirst.DbConstants.DATE;
import static com.example.zlff.casefirst.DbConstants.PLTNO;
import static com.example.zlff.casefirst.DbConstants.PNAME;
import static com.example.zlff.casefirst.DbConstants.RULE;
import static com.example.zlff.casefirst.DbConstants.TABLE_NAME;
import static com.example.zlff.casefirst.DbConstants.TRUTH;
import static com.example.zlff.casefirst.DbConstants.USERNAME;
import static com.example.zlff.casefirst.DbConstants.WHITELIST;

public class Yupload extends AppCompatActivity {
    private DBHelper dbHelper;
    private ListView list_ysave;
    String getalbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yupload);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this);
        list_ysave = (ListView) findViewById(R.id.list_ysave);
        Bundle bundle=this.getIntent().getExtras();
        getalbum=bundle.getString("ALBUM");

        showInList();


    }

    private void showInList() {
        Cursor cursor = getCursor();
        final String[] from = {PLTNO ,DATE,PNAME,  CARKIND, RULE, WHITELIST,TRUTH};
        int[] to = {R.id.txtPltno, R.id.txtDate};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.data_item, cursor, from, to); //SimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to)
        list_ysave.setAdapter(adapter);
        list_ysave.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                Intent intent = new Intent();
                intent.setClass(Yupload.this, Record_modify.class);
                TextView txvPltno = (TextView) view.findViewById(R.id.txtPltno);
                TextView txvDate = (TextView) view.findViewById(R.id.txtDate);

                intent.putExtra("TONUMBER", txvPltno.getText());
                intent.putExtra("TODATE", txvDate.getText());

                startActivity(intent);
            }
        });
    }

    private Cursor getCursor() {
        String getusername = getSharedPreferences("00", MODE_PRIVATE)
                .getString("username", "");

        SQLiteDatabase db = dbHelper.getReadableDatabase();  //透過dbHelper取得讀取資料庫的SQLiteDatabase物件，可用在查詢
        String[] columns = {USERNAME, _ID, PNAME, PLTNO, CARKIND, RULE, WHITELIST, TRUTH, DATE,BTNUPLOAD};
        Cursor cursor = db.query(TABLE_NAME, columns, "btnupload ='y' "+" and album='"+getalbum+"' and USERNAME='"+ getusername + "'", null, null, null, null);  //查詢所有欄位的資料
        return cursor;
    }

    @Override       //这里是实现了自动更新
    protected void onResume() {
        super.onResume();
        showInList();
    }
}
