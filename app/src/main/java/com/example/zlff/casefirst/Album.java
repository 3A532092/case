package com.example.zlff.casefirst;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import static android.provider.BaseColumns._ID;
import static com.example.zlff.casefirst.DbConstants.ALBUM;
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

public class Album extends AppCompatActivity {
    private DBHelper dbHelper;
    private ListView list_Albumsave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        dbHelper = new DBHelper(this);
        list_Albumsave = (ListView) findViewById(R.id.list_Albumsave);

        showInList();
    }

    private void showInList() {
        Cursor cursor = getCursor();
        final String[] from = {ALBUM,PLTNO ,DATE,PNAME,  CARKIND, RULE, WHITELIST,TRUTH,BTNUPLOAD};
        int[] to = {R.id.txtAlbum};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.album_item, cursor, from, to); //SimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to)
        list_Albumsave.setAdapter(adapter);
        list_Albumsave.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                Intent intent = new Intent();
                intent.setClass(Album.this, Yupload.class);
                TextView txvAlbum = (TextView) view.findViewById(R.id.txtAlbum);

                intent.putExtra("ALBUM", txvAlbum.getText());

                startActivity(intent);
            }
        });
        //cursor.close();
    }

    private Cursor getCursor() {
        String getusername = getSharedPreferences("00", MODE_PRIVATE)
                .getString("username", "");

        SQLiteDatabase db = dbHelper.getReadableDatabase();  //透過dbHelper取得讀取資料庫的SQLiteDatabase物件，可用在查詢
        String[] columns = {USERNAME, _ID, PNAME, PLTNO, CARKIND, RULE, WHITELIST, TRUTH, DATE,ALBUM,BTNUPLOAD};
        Cursor cursor = db.query(TABLE_NAME, columns, "btnupload ='y' and album !='' and USERNAME='" + getusername + "'", null, "album", null, null);  //查詢所有欄位的資料
        return cursor;
    }


}
