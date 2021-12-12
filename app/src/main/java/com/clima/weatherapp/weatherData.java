package com.clima.weatherapp;

import org.json.JSONException;
import org.json.JSONObject;

public class weatherData {

    private String mTemperture,micon,mcity,mWeatherType;
    private int mCondition;
    public static weatherData fromJson(JSONObject jsonObject){

        try { //chingaderas que le copie a un indio a las 3 de la mañana
            weatherData weatherD=new weatherData();
            weatherD.mcity=jsonObject.getString("name");
            weatherD.mCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.mWeatherType=jsonObject.getJSONArray("weather").getJSONObject(0).getString("description"); //ladescripciontraducida
            weatherD.micon=updateWeatherIcon(weatherD.mCondition);
            double tempResult=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedValue=(int)Math.rint(tempResult);
            weatherD.mTemperture=Integer.toString(roundedValue);
            return weatherD;

        }catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }



    private static String updateWeatherIcon(int condition){
        if(condition>=0 && condition<=300)
        {
            return "thunder";
        }
        else if(condition>=300 && condition<=500)
        {
            return "rain";
        }
        else if(condition>=500 && condition<=600)
        {
            return "rain";
        }
        else  if(condition>=600 && condition<=700)
        {
            return "snow";
        }
        else if(condition>=701 && condition<=771)
        {
            return "fog";
        }

        else if(condition>=772 && condition<=799)
        {
            return "clouds";
        }
        else if(condition==800)
        {
            return "sun";
        }
        else if(condition>=801 && condition<=804)
        {
            return "clouds";
        }
        else  if(condition>=900 && condition<=902)
        {
            return "thunder";
        }
        if(condition==903)
        {
            return "snow";
        }
        if(condition==904)
        {
            return "sun";
        }
        if(condition>=905 && condition<=1000)
        {
            return "thunder";
        }

        return "error"; //pos algun error aqui si se pasa


    }


    public String getmTemperture() {
        return mTemperture+"°C";
    }

    public String getMicon() {
        return micon;
    }

    public String getMcity() {
        return mcity;
    }

    public String getmWeatherType() {
        return mWeatherType;
    }
}
