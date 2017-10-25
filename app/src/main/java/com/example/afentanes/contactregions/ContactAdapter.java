package com.example.afentanes.contactregions;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.afentanes.contactregions.tasks.HolidaysTask;

import java.util.HashMap;

/**
 * Created by afentanes on 10/24/17.
 */

public class ContactAdapter extends ArrayAdapter <Contact> {

    HashMap <String, String> countryHolidays;
    String url;
    String key;

    public ContactAdapter(@NonNull Context context, Contact[] contacts, HashMap countryHolidays, String url, String key) {
        super(context, R.layout.contact_item, contacts);
        this.countryHolidays=countryHolidays;
        this.url=url;
        this.key=key;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(getContext());
        View customRowView = inflater.inflate(R.layout.contact_item, parent, false);

        TextView text= customRowView.findViewById(R.id.contac_name_id);
        Contact item = getItem(position);
        String countryCode = item.getCountryCode();
        text.setText(item.name +" "+ countryCode);
        checkHolidays(countryCode, text);

        return customRowView;
    }

    public void checkHolidays(final String countryCode, final TextView textView) {

        if(!countryHolidays.containsKey(countryCode)) {
            new HolidaysTask() {
                @Override
                protected void onPostExecute(String result) {
                    countryHolidays.put(countryCode, result);
                    if(result!=null&&!result.isEmpty()){
                        textView.setTextColor(Color.RED);
                    }
                }
            }.execute(url, key, countryCode);
        }else{
            if(!countryHolidays.get(countryCode).isEmpty()){
                textView.setTextColor(Color.RED);
            }
        }

    }




}
