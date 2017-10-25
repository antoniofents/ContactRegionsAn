package com.example.afentanes.contactregions;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Build;
import android.provider.ContactsContract;
import android.renderscript.ScriptGroup;

import com.example.afentanes.contactregions.tasks.CountryCodesTask;

import java.io.InputStream;
import java.util.HashMap;


public   abstract class ContactConstants {

    public static HashMap<String, String> COUNTRY_CODE_MAP = new HashMap<>();

    @SuppressLint("InlinedApi")
    public  static final String[] PROJECTION =
            {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            };

    @SuppressLint("InlinedApi")
    public final static String[] FROM_COLUMNS = {
            Build.VERSION.SDK_INT
                    >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                    ContactsContract.Contacts.DISPLAY_NAME
    };

    public static final int CONTACT_ID_INDEX = 0;

    public static String CONTACT_BUNDLE_ID = "mContactId";
    public static String CURRENT_CONTACT_ID = "currentContactId";
    public static String COUNTRIES_IN_HOLIDAYS = "countrys_holidays";




}
