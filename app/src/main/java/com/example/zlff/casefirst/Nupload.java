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
import android.widget.Toast;

import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        if(list_nsave.getCount()==0){
            Toast.makeText(this, "無資料可上傳", Toast.LENGTH_LONG).show();

            //轉json
            }

            /*try {
                JSONObject inf = new JSONObject();

                JSONArray array = new JSONArray();
                JSONObject arr_ = new JSONObject();
                arr_.put("name", "張三");
                arr_.put("age","");
                arr_.put("IdCard", "XC");
                arr_.put("married", true);

                arr_.put("name", "李四");
                arr_.put("age","");
                arr_.put("IdCard", "@DC");
                arr_.put("married", true);
                array.put( arr_);

                inf.put("inf", array);
                txv_out.setText(array.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        else {
            dialog=new AlertDialog.Builder(Nupload.this).create();
            dialog.setTitle("請選擇");
            dialog.setMessage("是否上傳?");
            dialog.setButton("確認", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SQLiteDatabase db=dbHelper.getWritableDatabase();
                    ContentValues values=new ContentValues();
                    String album=ran();

            try {
                TextView txv_out=(TextView)findViewById(R.id.textView15);
                SQLiteDatabase dbb = dbHelper.getReadableDatabase();
                Cursor cursor = dbb.rawQuery(
                        "select pltno,carkind,j_date,addr,splimit,speed,rule,truth,pname,album,j_time,whitelist,pic,btnupload from "+TABLE_NAME+" where btnupload='n'",
                        new String[]{});
                JSONArray array = new JSONArray();
                JSONObject obj = new JSONObject();

                cursor.moveToFirst();
                do{        // 逐筆讀出資料

                    obj.put("type", "逕舉");
                    obj.put("pltno", cursor.getString(0));
                    obj.put("carkind",cursor.getString(1));
                    obj.put("vil_dt", cursor.getString(2));
                    obj.put("vil_tm", cursor.getString(10));
                    obj.put("vil_addr",cursor.getString(3));
                    obj.put("splimit", cursor.getString(4));
                    obj.put("speed", cursor.getString(5));
                    obj.put("rule1",cursor.getString(6));
                    obj.put("truth1", cursor.getString(7));
                    obj.put("unit", "");
                    obj.put("pname",cursor.getString(8));
                    obj.put("report_dt", "");
                    obj.put("report_no", "");
                    obj.put("piccnt","");
                    obj.put("picture", "");
                    obj.put("PathLift", "");
                    obj.put("account","");
                    obj.put("Album", album);

                    array.put(obj);
                } while(cursor.moveToNext());    // 有一下筆就繼續迴圈

                txv_out.setText(array.toString());

                values.put(BTNUPLOAD,"y");
                values.put(ALBUM,album);
                db.update(TABLE_NAME,values,BTNUPLOAD+"='n'",null);
                dbb.close();

            } catch (JSONException e) {
                e.printStackTrace();
            }

                setResult(RESULT_OK);
                //finish();

            }
        });
        dialog.setButton2("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        dialog.show();


        }}

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

        SQLiteDatabase db = dbHelper.getReadableDatabase();
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
