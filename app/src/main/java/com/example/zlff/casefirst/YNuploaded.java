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

   /* public  void ran(View view){

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

        txt_aa.setText(getYMD()+unit+ranstr);

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
    }*/


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
