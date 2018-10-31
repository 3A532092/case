package com.example.zlff.casefirst;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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

import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.Calendar;
import java.util.HashMap;

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

public class Nupload extends AppCompatActivity {
    private DBHelper dbHelper;
    private ListView list_nsave;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nupload);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this);
        list_nsave = (ListView) findViewById(R.id.list_nsave);

        showInList();
    }


    private void showInList() {
        Cursor cursor = getCursor();
        final String[] from = {PLTNO ,DATE,PNAME,  CARKIND, RULE, WHITELIST,TRUTH};
        int[] to = {R.id.txtPltno, R.id.txtDate};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.data_item, cursor, from, to); //SimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to)
        list_nsave.setAdapter(adapter);
        list_nsave.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                Intent intent = new Intent();
                intent.setClass(Nupload.this, Record_modify.class);
                TextView txvPltno = (TextView) view.findViewById(R.id.txtPltno);
                TextView txvDate = (TextView) view.findViewById(R.id.txtDate);

                intent.putExtra("TONUMBER", txvPltno.getText());
                intent.putExtra("TODATE", txvDate.getText());

                startActivity(intent);
            }
        });
    }

    public void Click_upload(View view){
        dialog=new AlertDialog.Builder(Nupload.this).create();
        dialog.setTitle("請選擇");
        dialog.setMessage("是否上傳?");
        dialog.setButton("確認", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put(BTNUPLOAD,"y");
                values.put(ALBUM,ran());
                db.update(TABLE_NAME,values,BTNUPLOAD+"='n'",null);


                setResult(RESULT_OK);
                finish();

            }
        });
        dialog.setButton2("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        dialog.show();


        }

     private  String ran(){

        //int[] ran=new int[4];
        String[] ran = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        String[] getran=new String[4];
        String[] getransec=new String[10];
        String unit="",ranstr="";

        for(int i=0;i<getran.length;i++){
            getran[i]=ran[(int)(Math.random()*25)];
            unit=unit+getran[i];
        }

        for(int i=0;i<getransec.length;i++){
            getransec[i]=ran[(int)(Math.random()*25)];
            ranstr=ranstr+getransec[i];
        }

        return getYMD()+unit+ranstr;

    }
    private String getYMD(){
        String newdate="";
        Calendar mCal = Calendar.getInstance();

        int[] time={mCal.get(Calendar.YEAR)-1911,mCal.get(Calendar.MONTH)+1,mCal.get(Calendar.DAY_OF_MONTH)};
        String[] y=new String[time.length];
        for(int i=0;i<time.length;i++){
            if(time[i]<10){
                y[i]=String.valueOf("0"+time[i]);
            }
            else
                y[i]=String.valueOf(time[i]);
        }
        for(int i=0;i<time.length;i++){
            newdate=newdate+y[i];
        }
        return newdate;
    }




    private Cursor getCursor() {
        String getusername = getSharedPreferences("00", MODE_PRIVATE)
                .getString("username", "");

        SQLiteDatabase db = dbHelper.getReadableDatabase();  //透過dbHelper取得讀取資料庫的SQLiteDatabase物件，可用在查詢
        String[] columns = {USERNAME, _ID, PNAME, PLTNO, CARKIND, RULE, WHITELIST, TRUTH, DATE,BTNUPLOAD};
        Cursor cursor = db.query(TABLE_NAME, columns, "btnupload ='n' and USERNAME='" + getusername + "'", null, null, null, null);  //查詢所有欄位的資料
        return cursor;
    }

    @Override       //这里是实现了自动更新
    protected void onResume() {
        super.onResume();
        showInList();
    }



}
