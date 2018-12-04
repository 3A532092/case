package com.example.zlff.casefirst;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

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

public class Nupload extends AppCompatActivity implements AsyncResponse {
    private DBHelper dbHelper;
    private ListView list_nsave;
    private AlertDialog dialog;
    JSONObject obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nupload);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        TextView tv=(TextView) findViewById(R.id.textView15);

        tv.setMovementMethod(ScrollingMovementMethod.getInstance());



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
        if(list_nsave.getCount()==0) {
            Toast.makeText(this, "無資料可上傳", Toast.LENGTH_LONG).show();


        }
        else {

            dialog=new AlertDialog.Builder(Nupload.this).create();
            dialog.setTitle("請選擇");
            dialog.setMessage("是否上傳?");
            dialog.setButton("確認", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    String album = ran();
                    TextView txv_out = (TextView) findViewById(R.id.textView15);


                    SQLiteDatabase dbb = dbHelper.getReadableDatabase();
                    Cursor cursor = dbb.rawQuery(
                            "select pltno,carkind,j_date,addr,splimit,speed,rule,truth,pname,album,j_time,whitelist,pic,btnupload,piccnt,pic_1,pic_2,pic_3,pic_4,pic_5 from " + TABLE_NAME + " where btnupload='n'",
                            new String[]{});
                    JSONArray array = new JSONArray();

                    try {
                        cursor.moveToFirst();
                        do {// 逐筆讀出資料
                            String pic_1, pic_2, pic_3, pic_4, pic_5;
                            Uri Uri_1, Uri_2, Uri_3, Uri_4, Uri_5;
                            pic_1 = cursor.getString(15);
                            pic_2 = cursor.getString(16);
                            pic_3 = cursor.getString(17);
                            pic_4 = cursor.getString(18);
                            pic_5 = cursor.getString(19);

                            Uri_1 = Uri.parse(pic_1);
                            Uri_2 = Uri.parse(pic_2);
                            Uri_3 = Uri.parse(pic_3);
                            Uri_4 = Uri.parse(pic_4);
                            Uri_5 = Uri.parse(pic_5);

                            String bmp = bitmapToBase64(BitmapFactory.decodeFile(getPath(Nupload.this, Uri_1))),
                                    bmp2 = bitmapToBase64(BitmapFactory.decodeFile(getPath(Nupload.this, Uri_2))),
                                    bmp3 = bitmapToBase64(BitmapFactory.decodeFile(getPath(Nupload.this, Uri_3))),
                                    bmp4 = bitmapToBase64(BitmapFactory.decodeFile(getPath(Nupload.this, Uri_4))),
                                    bmp5 = bitmapToBase64(BitmapFactory.decodeFile(getPath(Nupload.this, Uri_5)));
                            //base64编码中的"+"会被替换成空格，可把+replace("+","%2B")

                            obj = new JSONObject();
                            obj.put("type", "逕舉");
                            obj.put("pltno", cursor.getString(0));
                            obj.put("carkind", cursor.getString(1));
                            obj.put("vil_dt", cursor.getString(2));
                            obj.put("vil_tm", cursor.getString(10));
                            obj.put("vil_addr", cursor.getString(3));
                            obj.put("splimit", cursor.getString(4));
                            obj.put("speed", cursor.getString(5));
                            obj.put("rule1", cursor.getString(6));
                            obj.put("truth1", cursor.getString(7));
                            obj.put("unit", "0000");
                            obj.put("pname", cursor.getString(8));
                            obj.put("report_dt", "");
                            obj.put("report_no", "");
                            obj.put("piccnt", cursor.getString(14));
                            obj.put("picture", "");
                            obj.put("PathLift", "");
                            obj.put("account", "");
                            obj.put("Album", album);
                            obj.put("pic_1", bmp);
                            obj.put("pic_2", bmp2);
                            obj.put("pic_3", bmp3);
                            obj.put("pic_4", bmp4);
                            obj.put("pic_5", bmp5);

                            array.put(obj);
                        } while (cursor.moveToNext());     // 有一下筆就繼續迴圈


                        values.put(BTNUPLOAD, "y");
                        values.put(ALBUM, album);
                        db.update(TABLE_NAME, values, BTNUPLOAD + "='n'", null);
                        dbb.close();
                        dbHelper.close();

                        HashMap postData = new HashMap();
                        postData.put("req", array.toString());

                        startActivity(new Intent(Nupload.this,Home.class));
                        PostResponseAsyncTask task  = new PostResponseAsyncTask(Nupload.this,postData);
                        task.execute("http://10.0.2.2/Myfirstserve/getandroid.php");

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

    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    /*public static boolean base64ToFile(String base64Str,String path){
        byte[] data = Base64.decode(base64Str,Base64.DEFAULT);
        for (int i = 0; i < data.length; i++) {
            if(data[i] < 0){
                //调整异常数据
                data[i] += 256;
            }
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(path);
            os.write(data);
            os.flush();
            os.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }

    }*/

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }





    @Override
    public void processFinish(String result) {
        if(result.equals("success")){
            Toast.makeText(this,"yaaa",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"帳號或密碼錯誤",Toast.LENGTH_LONG).show();
        }
    }



}
