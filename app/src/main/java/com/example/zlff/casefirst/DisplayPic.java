package com.example.zlff.casefirst;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;

import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class DisplayPic extends AppCompatActivity {
    private final static int PHOTO = 99;
    private ImageView imageView, img2, img3, img4, img5, imgset;
    String pic_1, pic_2, pic_3, pic_4, pic_5,uploadBuffer;
    Uri Uri_1, Uri_2, Uri_3, Uri_4, Uri_5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_pic);

        imageView = (ImageView) findViewById(R.id.imageView);
        img2 = (ImageView) findViewById(R.id.imageView2);
        img3 = (ImageView) findViewById(R.id.imageView3);
        img4 = (ImageView) findViewById(R.id.imageView4);
        img5 = (ImageView) findViewById(R.id.imageView5);
        imgset = (ImageView) findViewById(R.id.imgset);

        Bundle bundle1 = this.getIntent().getExtras();
        pic_1 = bundle1.getString("pic_1");
        pic_2 = bundle1.getString("pic_2");
        pic_3 = bundle1.getString("pic_3");
        pic_4 = bundle1.getString("pic_4");
        pic_5 = bundle1.getString("pic_5");

        Uri_1 = Uri.parse(pic_1);
        Uri_2 = Uri.parse(pic_2);
        Uri_3 = Uri.parse(pic_3);
        Uri_4 = Uri.parse(pic_4);
        Uri_5 = Uri.parse(pic_5);

        imgset.setOnClickListener(imgshow);

        try {
            imageView.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(getPath(this,Uri_1)), 300, 300));
            img2.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(getPath(this,Uri_2)), 300, 300));
            img3.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(getPath(this,Uri_3)), 300, 300));
            img4.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(getPath(this,Uri_4)), 300, 300));
            img5.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(getPath(this,Uri_5)), 300, 300));


        ImageView.OnClickListener imgsee = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgset.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(getPath(DisplayPic.this,Uri_1)), 1000, 1000));
                imgset.setVisibility(View.VISIBLE);
            }
        };
        ImageView.OnClickListener imgsee2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgset.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(getPath(DisplayPic.this,Uri_2)), 1000, 1000));
                imgset.setVisibility(View.VISIBLE);
            }
        };
        ImageView.OnClickListener imgsee3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgset.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(getPath(DisplayPic.this,Uri_3)), 1000, 1000));
                imgset.setVisibility(View.VISIBLE);
            }
        };
        ImageView.OnClickListener imgsee4 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgset.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(getPath(DisplayPic.this,Uri_4)), 1000, 1000));
                imgset.setVisibility(View.VISIBLE);
            }
        };
        ImageView.OnClickListener imgsee5 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgset.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(getPath(DisplayPic.this,Uri_5)), 1000, 1000));
                imgset.setVisibility(View.VISIBLE);
            }
        };
        imageView.setOnClickListener(imgsee);
        img2.setOnClickListener(imgsee2);
        img3.setOnClickListener(imgsee3);
        img4.setOnClickListener(imgsee4);
        img5.setOnClickListener(imgsee5);
        } catch (Exception e) {

        }
    }


    ImageView.OnClickListener imgshow = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            imgset.setVisibility(View.INVISIBLE);
        }
    };



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



}
