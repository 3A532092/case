package com.example.zlff.casefirst;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.util.HashMap;
import java.util.UUID;

import static android.provider.BaseColumns._ID;
import static com.example.zlff.casefirst.DbConstants.ADDR;
import static com.example.zlff.casefirst.DbConstants.BTNUPLOAD;
import static com.example.zlff.casefirst.DbConstants.CARKIND;
import static com.example.zlff.casefirst.DbConstants.FACT;
import static com.example.zlff.casefirst.DbConstants.LAW;
import static com.example.zlff.casefirst.DbConstants.LIC;
import static com.example.zlff.casefirst.DbConstants.DATE;
import static com.example.zlff.casefirst.DbConstants.NUM;
import static com.example.zlff.casefirst.DbConstants.PIC;
import static com.example.zlff.casefirst.DbConstants.REMARKS;
import static com.example.zlff.casefirst.DbConstants.TABLE_NAME;
import static com.example.zlff.casefirst.DbConstants.USERNAME;


public class Record_modify extends AppCompatActivity implements AsyncResponse,View.OnClickListener{
    private EditText editNum,editLic,editCarkind,editLaw,editFact,editRemarks,edtaddress;
    private TextView txtdate;
    private Button btnselcarkind,btnsellaw,btnselmpolice,btnselfact,btnupload,btnphoto;
    private boolean optionMenuOn = false;  //标示是否要显示optionmenu
    private Menu aMenu;
    private DBHelper dbHelper;
    private  int datafinish=0;
    private String pic;
    private static final String UPLOAD_URL = "http://10.0.2.2/Myfirstserve/insert_image.php";
    private static final int IMAGE_REQUEST_CODE = 3;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private AlertDialog dialog;
    private String Btnupload;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_modify);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbHelper=new DBHelper(this);

        findViews();
        displayNum();
        setviewenable();
        if(Btnupload != null){
            btnupload.setEnabled(false);

        }

        btnupload.setOnClickListener(this);
        requestStoragePermission();

        btnphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Record_modify.this, DisplayPhotoPage.class);
                Bundle bundle = new Bundle();
                bundle.putString("picname",pic);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        /*btnaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Record_modify.this, DisplayPhotoPage.class);
                Bundle bundle = new Bundle();
                bundle.putString("addressname",);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });*/



    }

    private void findViews(){
        editNum=(EditText)findViewById(R.id.edt_number);
        editLic=(EditText)findViewById(R.id.edt_Lic);
        editCarkind=(EditText)findViewById(R.id.edt_carkind2);
        editLaw=(EditText)findViewById(R.id.edt_law);
        editRemarks=(EditText)findViewById(R.id.edt_remarks);
        editFact=(EditText)findViewById(R.id.edt_fact);
        txtdate=(TextView)findViewById(R.id.txv_date);
        btnselcarkind=(Button) findViewById(R.id.btn_selcarkind);
        btnselmpolice=(Button) findViewById(R.id.btn_mpolice);
        btnsellaw=(Button) findViewById(R.id.btn_sellaw);
        btnselfact=(Button) findViewById(R.id.btn_selfact);
        btnupload=(Button) findViewById(R.id.btn_upload);
        btnphoto=(Button)findViewById(R.id.btn_photo);
        edtaddress=(EditText) findViewById(R.id.edt_address);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_modify:
                btnupload.setEnabled(false);
                optionMenuOn = true;
                checkOptionMenu();
                editLic.setEnabled(true);
                editCarkind.setEnabled(true);
                editLaw.setEnabled(true);
                editFact.setEnabled(true);
                editRemarks.setEnabled(true);
                edtaddress.setEnabled(true);
                btnselcarkind.setEnabled(true);
                btnselmpolice.setEnabled(true);
                btnsellaw.setEnabled(true);
                btnselfact.setEnabled(true);

                return true;

            case R.id.action_fin:
                update();
                optionMenuOn = false;
                checkOptionMenu();
                setviewenable();
                btnupload.setEnabled(true);

                return true;

            case R.id.action_del:
                dialog=new AlertDialog.Builder(Record_modify.this).create();
                dialog.setTitle("請選擇");
                dialog.setMessage("是否要刪除此筆紀錄?");
                dialog.setButton("Yes", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delet();
                        startActivity(new Intent(Record_modify.this,Savelist.class));

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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        aMenu = menu;
        checkOptionMenu();
        return super.onPrepareOptionsMenu(menu);
    }


    public void setviewenable(){
        editNum.setEnabled(false);
        editLic.setEnabled(false);
        editCarkind.setEnabled(false);
        editLaw.setEnabled(false);
        editFact.setEnabled(false);
        editRemarks.setEnabled(false);
        edtaddress.setEnabled(false);
        btnselcarkind.setEnabled(false);
        btnselmpolice.setEnabled(false);
        btnsellaw.setEnabled(false);
        btnselfact.setEnabled(false);


    }

    private void checkOptionMenu(){
        if(null != aMenu){
            if(optionMenuOn){

                aMenu.getItem(0).setVisible(false);
                aMenu.getItem(0).setEnabled(false);
                aMenu.getItem(1).setVisible(false);
                aMenu.getItem(1).setEnabled(false);
                aMenu.getItem(2).setVisible(true);
                aMenu.getItem(2).setEnabled(true);
                }
                else if(Btnupload != null){
                aMenu.getItem(0).setVisible(false);
                aMenu.getItem(0).setEnabled(false);
                aMenu.getItem(1).setVisible(true);
                aMenu.getItem(1).setEnabled(true);
                aMenu.getItem(2).setVisible(false);
                aMenu.getItem(2).setEnabled(false);

            }
                else{
                aMenu.getItem(0).setVisible(true);
                aMenu.getItem(0).setEnabled(true);
                aMenu.getItem(1).setVisible(true);
                aMenu.getItem(1).setEnabled(true);
                aMenu.getItem(2).setVisible(false);
                aMenu.getItem(2).setEnabled(false);

            }
            }

        }

    private void delet(){
        SQLiteDatabase dbb = dbHelper.getReadableDatabase();
        String num=editNum.getText().toString();
        Cursor cursor = dbb.rawQuery(
                "select num,_ID from "+TABLE_NAME+" where num=?",
                new String[]{num});
        while (cursor.moveToNext()) {
            String getid = cursor.getString(1);
            dbb.delete(TABLE_NAME, _ID + "=" +getid, null);
        }
    }

    private void update(){
        SQLiteDatabase dbb = dbHelper.getReadableDatabase();
        String num=editNum.getText().toString();
        Cursor cursor = dbb.rawQuery(
                "select num,_ID from "+TABLE_NAME+" where num=?",
                new String[]{num});
        while (cursor.moveToNext()) {
        String getid = cursor.getString(1);

        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(NUM,editNum.getText().toString());
        values.put(LIC,editLic.getText().toString());
        values.put(CARKIND,editCarkind.getText().toString());
        values.put(LAW,editLaw.getText().toString());
        values.put(REMARKS,editRemarks.getText().toString());
        values.put(FACT,editFact.getText().toString());
        values.put(DATE,txtdate.getText().toString());
        values.put(ADDR,edtaddress.getText().toString());
        db.update(TABLE_NAME,values,_ID+"="+getid,null);
    }
    }

    private void displayNum(){
        String todate,num;
        Bundle bundle=this.getIntent().getExtras();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if(bundle != null){
            num=bundle.getString("TONUMBER");
            todate=bundle.getString("TODATE");

            Cursor cursor = db.rawQuery(
                    "select num,lic,carkind,law,remarks,fact,pic,addr,btnupload from "+TABLE_NAME+" where num=?",
                    new String[]{num});
            while (cursor.moveToNext()) {

                String Lic = cursor.getString(1);
                String Carkind = cursor.getString(2);
                String Law = cursor.getString(3);
                String Remarks = cursor.getString(4);
                String Fact = cursor.getString(5);
                pic = cursor.getString(6);
                String Address = cursor.getString(7);
                Btnupload = cursor.getString(8);

                editLic.setText(Lic);
                editCarkind.setText(Carkind);
                editLaw.setText(Law);
                editRemarks.setText(Remarks);
                editFact.setText(Fact);
                edtaddress.setText(Address);
            }

            cursor.close();

            editNum.setText(num);
            txtdate.setText(todate);
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.modify_del, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_upload){
            if(editNum.getText().toString().matches("") || editLic.getText().toString().matches("")||editCarkind.getText().toString().matches("")|| editLaw.getText().toString().matches("")|| editFact.getText().toString().matches("")){
                Toast.makeText(this, "請完成資料", Toast.LENGTH_LONG).show();
                datafinish=1;
            }
            else {
                dialog=new AlertDialog.Builder(Record_modify.this).create();
                dialog.setTitle("請選擇");
                dialog.setMessage("是否要上傳此筆紀錄?");
                dialog.setButton("Yes", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        HashMap postData = new HashMap();
                        postData.put("mobile", "android");
                        postData.put("btnupload", "Login");
                        PostResponseAsyncTask task  = new PostResponseAsyncTask(Record_modify.this,postData);
                        task.execute("http://10.0.2.2/Myfirstserve/ckeckupload.php");



                    }
                });
                dialog.setButton2("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                dialog.show();




                /*舊HashMap postData = new HashMap();
                postData.put("txtnumber", editNum.getText().toString());
                postData.put("txtLic", editLic.getText().toString());
                postData.put("txtcarkind2", editCarkind.getText().toString());
                postData.put("txtlaw", editLaw.getText().toString());
                postData.put("txtremarks", editRemarks.getText().toString());
                postData.put("txtfact", editFact.getText().toString());
                postData.put("txtdate", txtdate.getText().toString());

                PostResponseAsyncTask task = new PostResponseAsyncTask(this, postData);
                task.execute("http://10.0.2.2/Myfirstserve/Android insertMySQL.php");*/
            }

        }

    }

    @Override
    public void processFinish(String result) {
            if(result.equals("yes")){
                //更新BTNUPLOAD上傳按鈕狀態
                SQLiteDatabase dbb = dbHelper.getReadableDatabase();
                String num=editNum.getText().toString();
                Cursor cursor = dbb.rawQuery(
                        "select num,_ID from "+TABLE_NAME+" where num=?",
                        new String[]{num});
                while (cursor.moveToNext()) {
                    String getid = cursor.getString(1);
                    SQLiteDatabase db=dbHelper.getWritableDatabase();
                    ContentValues values=new ContentValues();
                    values.put(BTNUPLOAD,"1");
                    values.put(NUM,num+("(已上傳)"));
                    db.update(TABLE_NAME,values,_ID+"="+getid,null);
                }
                //將編輯按鈕隱藏
                aMenu.getItem(0).setVisible(false);
                aMenu.getItem(0).setEnabled(false);
                aMenu.getItem(1).setVisible(true);
                aMenu.getItem(1).setEnabled(true);
                aMenu.getItem(2).setVisible(false);
                aMenu.getItem(2).setEnabled(false);
                uploadMultipart();
                btnupload.setEnabled(false);
                Toast.makeText(this, "上傳成功???", Toast.LENGTH_LONG).show();

        }
        else {
                Toast.makeText(this, "上傳失敗", Toast.LENGTH_LONG).show();
            }
    }



    public void uploadMultipart() {
        //getting the actual path of the image
        String newdate=txtdate.getText().toString().replaceAll("(?:年|月)","/");
        String newday=newdate.replace('日',' ');
        String newtime=newday.replace('時',':');
        String lasttime=newtime.replace('分',' ');


        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();


            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                    .setUtf8Charset()
                    .addFileToUpload(pic, "image") //Adding file(path為圖片路徑)
                    .addParameter("qq", editNum.getText().toString()) //Adding text parameter to the request
                    .addParameter("aa", editLic.getText().toString())
                    .addParameter("zz", editCarkind.getText().toString())
                    .addParameter("ww", editLaw.getText().toString())
                    .addParameter("ss", editRemarks.getText().toString())
                    .addParameter("xx", editFact.getText().toString())
                    .addParameter("ee", lasttime)
                    .addParameter("ff", edtaddress.getText().toString())
                    .setMaxRetries(11)
                    .startUpload();//Starting the upload
            //Toast.makeText(this,"上傳成功",Toast.LENGTH_SHORT).show();

        }

        catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

}

