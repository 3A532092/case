package com.example.zlff.casefirst;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

public class YNuploaded extends AppCompatActivity {
    Button btn_Yup,btn_Nup;
    TextView txt_aa;
    int year, month, day, hour, minute,sec;
    String thisDate;
    private static final int SET_UPLOAD=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ynuploaded);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_aa=(TextView)findViewById(R.id.textView14);

    }


    public void Nup(View view){
        startActivityForResult(new Intent(this,Nupload.class),SET_UPLOAD);
    }
    public void Yup(View view){
        startActivity(new Intent(this,Album.class));
    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case SET_UPLOAD:
                if(resultCode == RESULT_OK){
                    Toast.makeText(this, "上傳成功", Toast.LENGTH_LONG).show();
                }
                break;

        }

    }
}
