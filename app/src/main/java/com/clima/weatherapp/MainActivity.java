package com.clima.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.Locale;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    final String APP_ID = "3f75cb7c237cb1a16bfa88a957c56e6f";
    final String LANG="es";
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;


    String Location_provider = LocationManager.GPS_PROVIDER;

    TextView NameofCity, weatherState, Temperature;
    ImageView mweatherIcon;
    RelativeLayout mCityFinder;

    LocationManager mLocationManager;
    LocationListener mLocationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherState = findViewById(R.id.weatherCondition);
        Temperature = findViewById(R.id.temperature);
        mweatherIcon = findViewById(R.id.weatherIcon);
        mCityFinder = findViewById(R.id.cityFinder);
        NameofCity = findViewById(R.id.cityName);

        mCityFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, cityFinder.class);
                startActivity(intent);
            }
        });

    }

    /*
    @Override
    protected void onResume() {
        super.onResume();
        getweatherForCurrentLocation();
    } */

    @Override
    protected void onResume() {
        super.onResume();
        Intent mIntent=getIntent();
        String city=mIntent.getStringExtra("City");
        if(city!=null){
            getweatherForNewCity(city);
        }else{
            getweatherForCurrentLocation();
        }



    }

    private void getweatherForNewCity(String city) {

        RequestParams params=new RequestParams();
        params.put("q",city);
        params.put("appid",APP_ID);
        params.put("lang",LANG); //PARAMETRO IDIOMA
        letsdoSomeNetworking(params);

    }


    private void getweatherForCurrentLocation() {

        Locale localization= new Locale("es","ES");
        Locale.setDefault(localization);
        Configuration config=new Configuration();
        config.locale=localization;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());


        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());
                RequestParams params = new RequestParams();
                params.put("lat",Latitude);
                params.put("lon",Longitude);
                params.put("appid",APP_ID);
                params.put("lang",LANG); //PARAMETRO IDIOMA
                letsdoSomeNetworking(params);


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                //ta desactivado el yipies

            }

        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(Location_provider, MIN_TIME, MIN_DISTANCE, mLocationListener);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getweatherForCurrentLocation();
            }else{
                //sin permiso
            }
        }

    }




        private void letsdoSomeNetworking(RequestParams params){
            AsyncHttpClient client=new AsyncHttpClient();
            client.get(WEATHER_URL,params, new JsonHttpResponseHandler(){

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                   // super.onSuccess(statusCode, headers, response);
                    weatherData weatherD=weatherData.fromJson(response);
                    updateUI(weatherD);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                   // super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });




        }

        private void updateUI(weatherData weather){
        Temperature.setText(weather.getmTemperture());
        NameofCity.setText(weather.getMcity());
        weatherState.setText(weather.getmWeatherType());
        int resourceID=getResources().getIdentifier(weather.getMicon(), "drawable",getPackageName());
        mweatherIcon.setImageResource(resourceID);
        }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLocationManager!=null){
            mLocationManager.removeUpdates(mLocationListener);
        }
    }
}