package com.example.zlff.casefirst;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import static com.example.zlff.casefirst.DbConstants.TABLE_NAME;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import java.util.HashMap;

public class LoginAvtivity extends AppCompatActivity implements AsyncResponse, View.OnClickListener{
    EditText edtUsername,edtPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_avtivity);

        edtUsername=(EditText)findViewById(R.id.edt_Username) ;
        edtPassword=(EditText)findViewById(R.id.edt_Password);
        btnLogin=(Button)findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        HashMap postData = new HashMap();
        postData.put("btnLogin", "Login");
        postData.put("mobile", "android");
        postData.put("txtUsername", edtUsername.getText().toString());
        postData.put("txtPassword", edtPassword.getText().toString() );
        //startActivity(new Intent(this,Home.class));

        PostResponseAsyncTask task  = new PostResponseAsyncTask(this,postData);

        task.execute("http://10.0.2.2/Myfirstserve/login.php");

    }

    @Override
    public void processFinish(String result) {
        if(result.equals("success")){
            SharedPreferences pref = getSharedPreferences("00", MODE_PRIVATE);
            pref.edit()
                    .putString("username",edtUsername.getText().toString())
                    .commit();
            startActivity(new Intent(this,Home.class));
        }
        else{
            Toast.makeText(this,"帳號或密碼錯誤",Toast.LENGTH_LONG).show();
        }

    }
}
