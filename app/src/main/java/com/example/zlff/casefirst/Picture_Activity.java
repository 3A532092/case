package com.example.zlff.casefirst;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Picture_Activity extends AppCompatActivity {
    private final static int PHOTO = 99 ;
    private ImageView imageView,img2,img3,img4,img5,imgset;
    TextView txvuri;
    Uri[] uris;
    Uri urionly;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_);

        imageView=(ImageView)findViewById(R.id.imageView);
        img2=(ImageView)findViewById(R.id.imageView2);
        img3=(ImageView)findViewById(R.id.imageView3);
        img4=(ImageView)findViewById(R.id.imageView4);
        img5=(ImageView)findViewById(R.id.imageView5);
        imgset=(ImageView)findViewById(R.id.imgset);
        txvuri=(TextView)findViewById(R.id.txv_urioutput);




        imgset.setOnClickListener(imgshow);
    }

    public void btnopencamera(View v){
        Intent intent = new Intent(); //調用照相機
        intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
        startActivity(intent);
    }


    public void btnphoto(View v){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PHOTO);

        imageView.setImageDrawable(null);
        img2.setImageDrawable(null);
        img3.setImageDrawable(null);
        img4.setImageDrawable(null);
        img5.setImageDrawable(null);
    }

    public void btcheck(View view){
        dialog=new AlertDialog.Builder(Picture_Activity.this).create();
        dialog.setTitle("請選擇");
        dialog.setMessage("確認相片?");
        dialog.setButton("Yes", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(img2.getDrawable() == null){
                    SharedPreferences pref = getSharedPreferences("00", MODE_PRIVATE);
                    pref.edit()
                            .putString("pic1",urionly.toString())
                            .putString("piccnt","1")
                            .commit();

                    setResult(RESULT_OK);

                    finish();
                }else
                {
                    String[] y = new String[5];
                    for (int i = 0; i < uris.length; i++) {
                        if (uris[i]==null) {
                            y[i] = "";
                        } else
                            y[i] = String.valueOf(uris[i]);
                    }

                    SharedPreferences pref = getSharedPreferences("00", MODE_PRIVATE);
                    pref.edit()
                            .putString("pic1",y[0])
                            .putString("pic2",y[1])
                            .putString("pic3",y[2])
                            .putString("pic4",y[3])
                            .putString("pic5",y[4])
                            .putString("piccnt",Integer.toString(uris.length))
                            .commit();

                    setResult(RESULT_OK);
                finish();
            }
        }});
        dialog.setButton2("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        dialog.show();

        }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if(Build.VERSION.SDK_INT >= 16) {

                ClipData clipdata = data.getClipData();
                ContentResolver resolver = getContentResolver();
                if (null != clipdata) {


                    int count = clipdata.getItemCount();
                    uris = new Uri[count];//获得图片的uri
                    final Bitmap[] bmp = new Bitmap[count];

                    if (count > 0 && count <= 5) {

                        for (int i = 0; i < count; i++) {
                            uris[i] = clipdata.getItemAt(i).getUri();
                            bmp[i] = MediaStore.Images.Media.getBitmap(resolver, uris[i]);
                        }

                        switch (count) {
                        /*case 1:
                            imageView.setImageBitmap(ThumbnailUtils.extractThumbnail(photo, 400, 400));

                            imageView.setVisibility(View.VISIBLE);
                            break;*/
                            case 2:
                                imageView.setImageBitmap(ThumbnailUtils.extractThumbnail(bmp[0], 300, 300));
                                img2.setImageBitmap(ThumbnailUtils.extractThumbnail(bmp[1], 300, 300));

                                imageView.setVisibility(View.VISIBLE);
                                img2.setVisibility(View.VISIBLE);

                                break;
                            case 3:
                                imageView.setImageBitmap(ThumbnailUtils.extractThumbnail(bmp[0], 300, 300));
                                img2.setImageBitmap(ThumbnailUtils.extractThumbnail(bmp[1], 300, 300));
                                img3.setImageBitmap(ThumbnailUtils.extractThumbnail(bmp[2], 300, 300));

                                imageView.setVisibility(View.VISIBLE);
                                img2.setVisibility(View.VISIBLE);
                                img3.setVisibility(View.VISIBLE);
                                break;
                            case 4:
                                imageView.setImageBitmap(ThumbnailUtils.extractThumbnail(bmp[0], 300, 300));
                                img2.setImageBitmap(ThumbnailUtils.extractThumbnail(bmp[1], 300, 300));
                                img3.setImageBitmap(ThumbnailUtils.extractThumbnail(bmp[2], 300, 300));
                                img4.setImageBitmap(ThumbnailUtils.extractThumbnail(bmp[3], 300, 300));

                                imageView.setVisibility(View.VISIBLE);
                                img2.setVisibility(View.VISIBLE);
                                img3.setVisibility(View.VISIBLE);
                                img4.setVisibility(View.VISIBLE);
                                break;
                            case 5:
                                imageView.setImageBitmap(ThumbnailUtils.extractThumbnail(bmp[0], 300, 300));
                                img2.setImageBitmap(ThumbnailUtils.extractThumbnail(bmp[1], 300, 300));
                                img3.setImageBitmap(ThumbnailUtils.extractThumbnail(bmp[2], 300, 300));
                                img4.setImageBitmap(ThumbnailUtils.extractThumbnail(bmp[3], 300, 300));
                                img5.setImageBitmap(ThumbnailUtils.extractThumbnail(bmp[4], 300, 300));

                                imageView.setVisibility(View.VISIBLE);
                                img2.setVisibility(View.VISIBLE);
                                img3.setVisibility(View.VISIBLE);
                                img4.setVisibility(View.VISIBLE);
                                img5.setVisibility(View.VISIBLE);
                                break;
                        }
                        ImageView.OnClickListener imgsee = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imgset.setImageBitmap(ThumbnailUtils.extractThumbnail(bmp[0], 1000, 1000));
                                imgset.setVisibility(View.VISIBLE);
                            }
                        };
                        ImageView.OnClickListener imgsee2 = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imgset.setImageBitmap(ThumbnailUtils.extractThumbnail(bmp[1], 1000, 1000));
                                imgset.setVisibility(View.VISIBLE);
                            }
                        };
                        ImageView.OnClickListener imgsee3 = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imgset.setImageBitmap(ThumbnailUtils.extractThumbnail(bmp[2], 1000, 1000));
                                imgset.setVisibility(View.VISIBLE);
                            }
                        };
                        ImageView.OnClickListener imgsee4 = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imgset.setImageBitmap(ThumbnailUtils.extractThumbnail(bmp[3], 1000, 1000));
                                imgset.setVisibility(View.VISIBLE);
                            }
                        };
                        ImageView.OnClickListener imgsee5 = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imgset.setImageBitmap(ThumbnailUtils.extractThumbnail(bmp[4], 1000, 1000));
                                imgset.setVisibility(View.VISIBLE);
                            }
                        };
                        imageView.setOnClickListener(imgsee);
                        img2.setOnClickListener(imgsee2);
                        img3.setOnClickListener(imgsee3);
                        img4.setOnClickListener(imgsee4);
                        img5.setOnClickListener(imgsee5);
                    } else {
                        Toast.makeText(this, "您選取的相片數超過5張", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    urionly=data.getData();
                    final Bitmap photo=MediaStore.Images.Media.getBitmap(resolver,urionly);
                    imageView.setImageBitmap(ThumbnailUtils.extractThumbnail(photo,400,400));
                    imageView.setVisibility(View.VISIBLE);
                    ImageView.OnClickListener imgsee = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imgset.setImageBitmap(ThumbnailUtils.extractThumbnail(photo, 900, 900));
                            imgset.setVisibility(View.VISIBLE);
                        }
                    };
                    imageView.setOnClickListener(imgsee);
                }
            }
        }
        catch (Exception e)
        {

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    ImageView.OnClickListener imgshow=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            imgset.setVisibility(View.INVISIBLE);
        }
    };




}
