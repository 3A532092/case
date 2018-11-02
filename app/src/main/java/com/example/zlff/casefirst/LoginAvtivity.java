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

public class LoginAvtivity extends AppCompatActivity implements View.OnClickListener{
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
        startActivity(new Intent(this,Home.class));


    }


}
