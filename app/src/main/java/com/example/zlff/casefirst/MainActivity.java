package com.example.zlff.casefirst;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import static android.provider.BaseColumns._ID;
import static com.example.zlff.casefirst.DbConstants.ADDR;
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


public class MainActivity extends AppCompatActivity{
    private DBHelper dbHelper;
    private EditText editNum,editLic,editCarkind1,editCarkind2,editLaw,editFact,editRemarks;
    private TextView txtdate;
    private Button btn_tocameraAc,btn_toGPS,btn_carkind,btn_rule;
    private AlertDialog dialog,dialog_rule;
    private static final int SET_PHOTOSAVE=1;
    private static final int SET_ADDRESS=2;
    String[] option={"1 汽車","2 拖車","3 重機","4 輕機","5 機械牌(機械)","6 臨時牌(臨)","7 試車牌(試)","1a 軍車牌(軍)","1b 領事牌(領)", "1c 外交牌(外)","1d 外交牌(使)"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //home左上方返回鍵
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //顯示spinner
        Spinner reportspinn=(Spinner)findViewById(R.id.reportnam_spinner);
        ArrayAdapter<CharSequence> nAdapter = ArrayAdapter.createFromResource(
                this, R.array.category_array, android.R.layout.simple_spinner_item );
        nAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
       reportspinn.setAdapter(nAdapter);

        //洗去Db pic資料
        SharedPreferences pref = getSharedPreferences("00", MODE_PRIVATE);
        pref.edit()
                .putString("pic",null)
                .putString("address",null)
                .commit();

        openDatabase();
        findViews();
        displayTime();

        //btn_carkind對話方塊
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("選擇車種");
        builder.setItems(option,listener);
        builder.setNegativeButton("取消",null);
        dialog=builder.create();

        //btn_rule對話方塊
        AlertDialog.Builder builder2=new AlertDialog.Builder(this);
        builder.setTitle("選擇車種");
        builder.setItems(option,listener);
        builder.setNegativeButton("取消",null);
        dialog_rule=builder.create();




        btn_tocameraAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this,CameraActivity.class),SET_PHOTOSAVE);
            }
        });

        btn_toGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this,GPSinf_Activity.class),SET_ADDRESS);
            }
        });


    }
    DialogInterface.OnClickListener listener=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            editCarkind1.setText(option[i].split(" ")[0]);
            editCarkind2.setText(option[i].split(" ")[1]);
        }
    };

    public  void btn_carkind(View view){
        dialog.show();
    }

    public  void btn_rule(View view){
        dialog_rule.show();
    }


    private void findViews(){
        editNum=(EditText)findViewById(R.id.edt_number);
        editLic=(EditText)findViewById(R.id.edt_Lic);
        editCarkind1=(EditText)findViewById(R.id.edt_carkind1);
        editCarkind2=(EditText)findViewById(R.id.edt_carkind2);
        editLaw=(EditText)findViewById(R.id.edt_law);
        editRemarks=(EditText)findViewById(R.id.edt_remarks);
        editFact=(EditText)findViewById(R.id.edt_fact);
        txtdate=(TextView)findViewById(R.id.txv_date);
        btn_tocameraAc=(Button)findViewById(R.id.btn_camera);
        btn_toGPS=(Button)findViewById(R.id.btn_gps);
        btn_carkind=(Button)findViewById(R.id.carkind_btn);
        btn_rule=(Button)findViewById(R.id.rule_btn);
    }



    private void openDatabase(){
        dbHelper=new DBHelper(this);   //取得DBHelper物件

    }
    protected void onDestroy(){
        super.onDestroy();
        closeDatabase();     //關閉資料庫
    }
    private void closeDatabase(){
        dbHelper.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_leave, menu);
        return super.onCreateOptionsMenu(menu);


    }


    //存檔
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                dialog=new AlertDialog.Builder(MainActivity.this).create();
                dialog.setTitle("請選擇");
                dialog.setMessage("是否儲存?");
                dialog.setButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLadd();
                    }
                    });
                dialog.setButton2("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                dialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cleanEditText(){
        editNum.setText("");
        editCarkind2.setText("");
        editLic.setText("");
        editLaw.setText("");
        editRemarks.setText("");
        editFact.setText("");
    }
    private void SQLadd(){
        //從cameraActivity傳來pic name
        String pic = getSharedPreferences("00", MODE_PRIVATE)
                .getString("pic", "");

        String getaddress = getSharedPreferences("00", MODE_PRIVATE)
                .getString("address", "");

        String getusername = getSharedPreferences("00", MODE_PRIVATE)
                .getString("username", "");


        SQLiteDatabase db=dbHelper.getWritableDatabase();  //透過dbHelper取得讀取資料庫的SQLiteDatabase物件，可用在新增、修改與刪除
        ContentValues values=new ContentValues();  //建立 ContentValues 物件並呼叫 put(key,value) 儲存欲新增的資料，key 為欄位名稱  value 為對應值。
        values.put(NUM,editNum.getText().toString());
        values.put(LIC,editLic.getText().toString());
        values.put(CARKIND,editCarkind2.getText().toString());
        values.put(LAW,editLaw.getText().toString());
        values.put(REMARKS,editRemarks.getText().toString());
        values.put(FACT,editFact.getText().toString());
        values.put(DATE,txtdate.getText().toString());
        values.put(PIC,pic);
        values.put(ADDR,getaddress);
        values.put(USERNAME,getusername);

        db.insert(TABLE_NAME,null,values);
        db.close();
        cleanEditText();
    }


    private void displayTime(){
        String date;
        Bundle bundle=this.getIntent().getExtras();
        if(bundle != null){
            date=bundle.getString("Date");

            TextView txv_date=(TextView) findViewById(R.id.txv_date);
            txv_date.setText(date);
        }

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case SET_PHOTOSAVE:
                if(resultCode == RESULT_OK){
                    Button btncamera=(Button)findViewById(R.id.btn_camera);
                    btncamera.setText("照相機(已儲存)");
                }
                break;
            case SET_ADDRESS:
                if(resultCode == RESULT_OK){
                    Button btngps=(Button)findViewById(R.id.btn_gps);
                    btngps.setText("定位資訊(已儲存)");
                }
                break;
        }

    }


}
