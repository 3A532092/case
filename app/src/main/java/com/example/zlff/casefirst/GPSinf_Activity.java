package com.example.zlff.casefirst;

import android.Manifest;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class GPSinf_Activity extends AppCompatActivity {
    private static final int PERMISS_REQUEST_FINE_LOCATION=101;
    private LocationManager manager;
    private Location currentLocation;
    private String best;
    private Geocoder geocoder;
    private AlertDialog dialog;
    private EditText edt_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsinf_);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edt_address=(EditText)findViewById(R.id.edt_addressoutput);
        geocoder = new Geocoder(this, Locale.TAIWAN);
        manager=(LocationManager)getSystemService(LOCATION_SERVICE);
        if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("定位管理")
                    .setMessage("GPS尚未啟用.\n"+"請問你是否現在就設定啟用GPS?")
                    .setPositiveButton("啟用", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("不啟用",null).create().show();
        }
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISS_REQUEST_FINE_LOCATION
            );
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        Criteria criteria=new Criteria();
        best=manager.getBestProvider(criteria,true);
        int minTime=5000;
        float minDistance=5;

        try{
            if(best != null){
                currentLocation=manager.getLastKnownLocation(best);
                manager.requestLocationUpdates(best,minTime,minDistance,listener);
            }
            else{
                currentLocation=manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,minTime,minDistance,listener);
            }
        }
        catch(SecurityException sex){
            Log.e("Ch14_1_2","GPS權限失敗..."+sex.getMessage());
        }
        updatePosition();
    }

    @Override
    protected void onPause(){
        super.onPause();
        try{
            manager.removeUpdates(listener);
        }
        catch (SecurityException sex){
            Log.e("Ch14_1_2","GPS權限失敗..."+sex.getMessage());
        }
    }

    private void updatePosition(){
        EditText output;
        output =(EditText) findViewById(R.id.edt_addressoutput);

        output.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        //文本显示的位置在EditText的最上方
        //output.setGravity(Gravity.TOP);
        output.setSingleLine(false);
        //水平滚动设置为False
        output.setHorizontallyScrolling(false);
        if(currentLocation == null){
            output.setText("取得定位資訊中");
        }
        else
        {
            output.setText(getLocationInfo(currentLocation));
        }

    }

    private LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            currentLocation=location;
            updatePosition();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public String getLocationInfo(Location location){
        StringBuffer str =new StringBuffer();
       //str.append("定位提供者(Provider):"+location.getProvider());
         /*str.append("\n 緯度(Latitude):"+Double.toString(location.getLatitude()));
        str.append("\n 經度(Longitude):"+Double.toString(location.getLongitude()));
        str.append("\n 高度(Latitude):"+Double.toString(location.getAltitude()));*/

        float latitude=Float.parseFloat(Double.toString(location.getLatitude()));
        float longtitude = Float.parseFloat(Double.toString(location.getLongitude()));
        try {
            List<Address> listAddress = geocoder.getFromLocation(latitude, longtitude, 1);
            String returnAddress = listAddress.get(0).getAddressLine(0);
            //output.setText(returnAddress);
            str.append(returnAddress);
        }catch (Exception ex) {
            //output.setText("錯誤:" + ex.toString());
            str.append("抓取失敗");
        }

        return str.toString();

    }

    public void btn_Click(View view){
        float latitude=(float)currentLocation.getLatitude();
        float longtitude=(float)currentLocation.getLongitude();
        String uri=String.format("geo:%f, %f?z=18",latitude,longtitude);
        Intent geomap=new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(geomap);
    }

    public void btn_Usegpsaddr(View view){

        dialog=new AlertDialog.Builder(GPSinf_Activity.this).create();
        dialog.setTitle("請選擇");
        dialog.setMessage("是否要使用此位置?");
        dialog.setButton("Yes", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SharedPreferences pref = getSharedPreferences("00", MODE_PRIVATE);
                pref.edit()
                        .putString("address",edt_address.getText().toString())
                        .commit();
                setResult(RESULT_OK);
                finish();
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
