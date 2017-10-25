package com.example.afentanes.contactregions.tasks;

import android.os.AsyncTask;

import com.example.afentanes.contactregions.ContactConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by afentanes on 10/24/17.
 */

public class CountryCodesTask extends AsyncTask <InputStream, Void, Void> {


    @Override
    protected Void doInBackground(InputStream... streams) {

        try (InputStream stream= streams[0]){
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
            Iterator<?> keyset = jsonObject.keys();
            while (keyset.hasNext()) {
                String key = (String) keyset.next();
                ContactConstants.COUNTRY_CODE_MAP.put(key,(String) jsonObject.get(key));
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
