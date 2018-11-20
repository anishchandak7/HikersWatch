package com.example.anish.hikerswatch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    LocationManager locationManager;
    LocationListener locationListener;

    TextView longtitudetextview, latitudetextview,altitudetextview,acurracytextview,addresstextview;

    public void Update(Location location)
    {
        latitudetextview.setText("Latitude : "+ location.getLatitude());
        longtitudetextview.setText("Longitude : "+location.getLongitude());
        altitudetextview.setText("Altitude : "+location.getAltitude());
        acurracytextview.setText("Accuracy : "+location.getAccuracy());

        Geocoder geocoder=new Geocoder(getApplicationContext(),Locale.getDefault());
        try {
            List<Address> addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1 );

            String address="";

            if(addressList!=null&&addressList.size()>0)
            {
                Log.i("address",addressList.get(0).toString());

                if(addressList.get(0).getSubThoroughfare()!=null)
                {
                    address=addressList.get(0).getSubThoroughfare()+"\n";
                }
                if(addressList.get(0).getThoroughfare()!=null)
                {
                    address=addressList.get(0).getThoroughfare()+"\n";
                }
                if(addressList.get(0).getLocality()!=null)
                {
                    address=addressList.get(0).getLocality()+"\n";
                }
                if(addressList.get(0).getCountryName()!=null)
                {
                    address=addressList.get(0).getCountryName()+"\n";
                }
                if(addressList.get(0).getCountryCode()!=null)
                {
                    address=addressList.get(0).getCountryCode()+"\n";
                }

                Log.i("Addresss",address);
                addresstextview.setText("Address : \n"+address);


            }
        } catch (IOException e) {
            e.printStackTrace(); }


    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        startlistening();
        }
    }


    public void startlistening(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            locationManager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }

    }
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        longtitudetextview=(TextView) findViewById(R.id.longitudeTextView);
        latitudetextview=(TextView) findViewById(R.id.LatitudetextView);

        acurracytextview=(TextView) findViewById(R.id.AcurracytextView);
        altitudetextview=(TextView) findViewById(R.id.altitudetextView);

        addresstextview=(TextView) findViewById(R.id.addresstextView);

        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Update(location);
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

        if(Build.VERSION.SDK_INT<23)
        {
        //    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        startlistening();
        }
        else
        {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else
        {
            startlistening();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location!=null)
            {
                Update(location);
            }

        }
        }
    }
}
