package com.example.afentanes.contactregions.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by afentanes on 10/23/17.
 */

public class HolidaysTask extends AsyncTask <String,Void,String> {



    @Override
    protected String doInBackground(String... params) {
        InputStream holidaysStream = getHolidaysStream(params);

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(holidaysStream, "UTF-8"));
            String line;
            if( (line = reader.readLine()) != null){
                JSONObject jsonObject = new JSONObject(line);
                JSONArray holidays = jsonObject.getJSONArray("holidays");
                if(holidays.length()>0)
                return ((JSONObject)holidays.get(0)).getString("name");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    InputStream getHolidaysStream(String... params){
        HttpURLConnection urlConnection =null;
        try {
            //url example
            //https://holidayapi.com/v1/holidays?key=622e2498-6c48-4ea5-80f2-61b58ee9a4dc&country=RU&year=2016&month=10&day=24
            String currentDate = new SimpleDateFormat("'&year='2016'&month='MM'&day='dd").format(new Date());

            URL url = new URL(params[0]+params[1]+"&country="+params[2]+currentDate);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            return inputStream;
        }catch (Exception e) {
            Log.i("Main", "error llamando url");
        } finally {
            if(urlConnection!=null) urlConnection.disconnect();
        }

        return null;

    }
}
