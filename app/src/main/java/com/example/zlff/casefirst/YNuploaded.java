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

        txt_aa=(TextView)findViewById(R.id.textView14);
    }

    public  void ran(View view){





        //int[] ran=new int[4];
        String[] ran = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        String[] getran=new String[6];
        String aa="";
        for(int i=0;i<getran.length;i++){
            getran[i]=ran[(int)(Math.random()*25)];

        }
        for(int i=0;i<getran.length;i++){
            aa=aa+getran[i];
        }

        txt_aa.setText(aa);

    }

    public void Nup(View view){
        startActivityForResult(new Intent(this,Nupload.class),SET_UPLOAD);
    }
    public void Yup(View view){
        startActivity(new Intent(this,Yupload.class));
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
