package com.example.zlff.casefirst;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.security.AccessController.getContext;

public class CameraActivity extends AppCompatActivity {
    ImageView imageView;
    private AlertDialog dialog;
    String file_name;
    Button btnupload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btnCame=(Button)findViewById(R.id.btnCamera);
        btnupload=(Button)findViewById(R.id.btn_upload);
        btnupload.setEnabled(false);
        imageView=(ImageView)findViewById(R.id.imageView);
        //取得相機權限
        int permissionCheck = ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            //還沒獲取權限要做什麼呢
            //和使用者要求權限
            ActivityCompat.requestPermissions(CameraActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        }else{
            //以獲取權限要做的事情
            Toast.makeText(CameraActivity.this, "已經拿到權限囉!", Toast.LENGTH_SHORT).show();
        }


        btnCame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);*/
                final Intent takeVideoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

// output file
                File path = new File(Environment.getExternalStorageDirectory(), "/tmp.jpg");
// com.mydomain.fileprovider is authorities (manifest)
// getUri from file
                Uri uri = FileProvider.getUriForFile(CameraActivity.this, "com.mydomain.fileprovider", path);
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(takeVideoIntent, 0);


            }
        });

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog=new AlertDialog.Builder(CameraActivity.this).create();
                dialog.setTitle("請選擇");
                dialog.setMessage("是否要使用此相片?");
                dialog.setButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int permission = ActivityCompat.checkSelfPermission(CameraActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE);

                        if (permission != PackageManager.PERMISSION_GRANTED) {
                            // We don't have permission so prompt the user
                            ActivityCompat.requestPermissions(CameraActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    0);
                        }else{
                        if(imageView.getDrawable() == null){
                            Toast.makeText(CameraActivity.this, "please take a picture", Toast.LENGTH_SHORT).show();
                        }else{
                                //以獲取權限要做的事情
                                //Intent replyIntent=new Intent();
                                setResult(RESULT_OK);

                                starsave();

                                SharedPreferences pref = getSharedPreferences("00", MODE_PRIVATE);
                                pref.edit()
                                    .putString("pic",file_name)
                                    .commit();
                                Toast.makeText(CameraActivity.this, "savesuccess", Toast.LENGTH_SHORT).show();
                                btnupload.setEnabled(false);
                                finish();

                            }
                        }

                    }
                });
                dialog.setButton2("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }
    public static Bitmap viewToBitmap(View view , int width, int height){
        Bitmap bitmap=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


    public void starsave(){
        FileOutputStream fileOutputStream=null;
        File filea = getDisc();
        if(!filea.exists() && !filea.mkdirs())
        {
            Toast.makeText(this, "Cant creat", Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyymmsshhmmss");
        String date =simpleDateFormat.format(new Date());
        String name ="Img"+date+".jpg";

        /*String*/ file_name=filea.getAbsolutePath()+"/"+name;
        File new_file=new File(file_name);


        try {
            fileOutputStream=new FileOutputStream(new_file);
            Bitmap bitmap=viewToBitmap(imageView,imageView.getWidth(),imageView.getHeight());
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);


            //Toast.makeText(this,file_name,Toast.LENGTH_SHORT).show();
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                Toast.makeText(this,"error",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        refreshGallery(new_file);
    }

    private File getDisc() {

        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        return new File(file, "ImageDemo");

    }
    public void refreshGallery(File file){
        Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = BitmapFactory.decodeFile(Environment
                .getExternalStorageDirectory() + "/tmp.jpg");

        imageView.setImageBitmap(bitmap);

        btnupload.setEnabled(true);
        //Bitmap mbmp = (Bitmap) data.getExtras().get("data");

        //imageView.setImageBitmap(mbmp);

        //imageView.setImageBitmap(mbmp);

    }

    //旋轉照片
    /*public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotateBitmapByDegree(Bitmap bitmap, int degree) {
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return newBitmap;
    }*/



    //保留回上一頁資料
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch ( id ){
            // 點擊 ActionBar 返回按鈕時 結束目前的 Activity
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
