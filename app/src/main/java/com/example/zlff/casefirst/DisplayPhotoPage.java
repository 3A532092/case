package com.example.zlff.casefirst;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayPhotoPage extends AppCompatActivity {
    private TextView txtdispic;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_photo_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtdispic=(TextView)findViewById(R.id.txtdisplaypic);
        txtdispic.setText("");

        displayPic();

    }

    private void displayPic(){
        Bundle bundle1 =this.getIntent().getExtras();
        String getpicname = bundle1.getString("pic_1");

        ImageView jpgView = (ImageView)findViewById(R.id.jpgview);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bm = BitmapFactory.decodeFile(getpicname, options);
        if(bm==null){
            txtdispic.setText("照片已被移除");
        }else{
        jpgView.setImageBitmap(bm);}
    }
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
