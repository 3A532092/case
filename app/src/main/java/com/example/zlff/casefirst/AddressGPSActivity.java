package com.example.zlff.casefirst;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

public class AddressGPSActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    Button btnGetLastLocation,btn_ok;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    String returnAddress;
    EditText edt_addr;
    private static final int REQUEST_FINE_LOCATION_PERMISSION = 102;
    private boolean getService = false;
    private LocationManager status;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_gps);

        btnGetLastLocation = (Button) findViewById(R.id.getlastlocation);
        btnGetLastLocation.setOnClickListener(btnGetLastLocationOnClickListener);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(btnok_onClick);
        edt_addr=(EditText)findViewById(R.id.edt_addr);

        status = (LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        requestLocationPermission();


    }

    private void requestLocationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasPermission = checkSelfPermission(
                    android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_FINE_LOCATION_PERMISSION);
            }

            else {

            }
        }
    }
    View.OnClickListener btnGetLastLocationOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(status.isProviderEnabled(LocationManager.GPS_PROVIDER)&& status.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            {
                //如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
                if(mGoogleApiClient != null){
                    if(mGoogleApiClient.isConnected()){
                        getMyLocation();
                    }else{
                        Toast.makeText(AddressGPSActivity.this,
                                "!mGoogleApiClient.isConnected()", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(AddressGPSActivity.this,
                            "mGoogleApiClient == null", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(AddressGPSActivity.this,"請開啟定位",Toast.LENGTH_LONG).show();
                getService = true; //確認開啟定位服務
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)); //開啟設定頁面
            }


        }
    };

    View.OnClickListener btnok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(edt_addr.getText().toString()==""){
                Toast.makeText(AddressGPSActivity.this, "請輸入地址", Toast.LENGTH_LONG).show();
            }else {
            dialog=new AlertDialog.Builder(AddressGPSActivity.this).create();
            dialog.setTitle("請選擇");
            dialog.setMessage("確認地址?");
            dialog.setButton("Yes", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences pref = getSharedPreferences("00", MODE_PRIVATE);
                        pref.edit()
                                .putString("address",edt_addr.getText().toString())
                                .commit();
                    setResult(RESULT_OK);

                        finish();

                    }});
            dialog.setButton2("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });
            dialog.show();


        }
    }};


    private void getMyLocation(){
        try{
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                edt_addr.setText(getAddressByLocation(mLastLocation));
            }else{
                edt_addr.setText("無法確認當前位置");
            }
        } catch (SecurityException e){
            Toast.makeText(AddressGPSActivity.this,
                    "SecurityException:\n" + e.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //getMyLocation();      //mGoogleApiClient連接時,自動抓取定位
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(AddressGPSActivity.this,
                "onConnectionSuspended: " + String.valueOf(i),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(AddressGPSActivity.this,
                "onConnectionFailed: \n" + connectionResult.toString(),
                Toast.LENGTH_LONG).show();
    }

    public String getAddressByLocation(Location location){
        returnAddress="";
        try {
            if (location != null) {
                Double longitude = location.getLongitude();
                Double latitude = location.getLatitude();
                Geocoder gc = new Geocoder(this, Locale.TRADITIONAL_CHINESE);
                List<Address> lstAddress = gc.getFromLocation(latitude, longitude, 1);
                if (!Geocoder.isPresent()) {
                    returnAddress = "Sorry!Geocoder service not present";
                }
                returnAddress = lstAddress.get(0).getAddressLine(0);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return returnAddress;
    }


}
