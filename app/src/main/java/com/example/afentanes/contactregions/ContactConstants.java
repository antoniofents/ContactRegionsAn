package com.example.afentanes.contactregions;

import android.annotation.SuppressLint;
import android.os.Build;
import android.provider.ContactsContract;

/**
 * Created by afentanes on 10/23/17.
 */

public interface ContactConstants {

    @SuppressLint("InlinedApi")
    public  static final String[] PROJECTION =
            {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY

            };

    @SuppressLint("InlinedApi")
    public final static String[] FROM_COLUMNS = {
            Build.VERSION.SDK_INT
                    >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                    ContactsContract.Contacts.DISPLAY_NAME
    };

    public static final int CONTACT_ID_INDEX = 0;

    String CONTACT_BUNDLE_ID = "mContactId";
}
