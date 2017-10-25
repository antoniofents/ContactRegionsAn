package com.example.afentanes.contactregions;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.afentanes.contactregions.tasks.CountryCodesTask;
import com.example.afentanes.contactregions.tasks.HolidaysTask;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    long currentContactId;
    HashMap<String, String> countryHolidays = new HashMap();
    private final static int[] TO_IDS = {
            R.id.contac_name_id
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState != null) {
            currentContactId = savedInstanceState.getLong("currentContactId");

        }
        setContentView(R.layout.activity_main);
        init();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentContactId > 0)
            contactSelected(currentContactId);

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        initializeContactList();
    }


    private void contactSelected(long id) {

        currentContactId = id;
        if (getCurrentFragment() != null) {
            ContactInfoFragment fragment = (ContactInfoFragment) getCurrentFragment();
            fragment.updateFragmentView(id);
        } else {
            try {
                ContactInfoFragment contactInfoFragment = new ContactInfoFragment();
                if (getIntent().getExtras() == null) {
                    Bundle bundle = new Bundle();
                    bundle.putLong(ContactConstants.CONTACT_BUNDLE_ID, id);
                    contactInfoFragment.setArguments(bundle);
                }
                contactInfoFragment.getArguments().putLong(ContactConstants.CONTACT_BUNDLE_ID, id);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, contactInfoFragment, getResources().getString(R.string.current_fragment_tag)).commit();
            } catch (Exception e) {
                Log.i("a", "a");
            }
        }

    }


    private ContactAdapter getContactsAdapter(Cursor cursor) {

        Contact[] contacts = new Contact[cursor.getCount()];
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            long id = cursor.getLong(ContactConstants.CONTACT_ID_INDEX);
            Contact contact = new Contact(id, cursor.getString(1), getPhoneInfo(id));
            contacts[cursor.getPosition()] = contact;
        }
        return new ContactAdapter(findViewById(R.id.contact_list).getContext(), contacts, countryHolidays, getResources().getString(R.string.holidays_url), getResources().getString(R.string.test_key));
    }



    public String getPhoneInfo(long id) {

        String number = "";
        Cursor phones;

        // its also possible to get the contacts phones from ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        //but in some cases the phones are not available in that resource
        //here is how to get the cursor from that URI
        // getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,new String [] {ContactsContract.Contacts._ID,ContactsContract.CommonDataKinds.Phone._ID,  ContactsContract.CommonDataKinds.Phone.NUMBER}, ContactsContract.CommonDataKinds.Phone._ID + " =? " , new String[] {String.valueOf(id)}, null);

        String selectionFromData =
                ContactsContract.Data.RAW_CONTACT_ID + " =? "
                        + " AND " + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'";
        phones = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, selectionFromData, new String[]{String.valueOf(id)}, null);


        if (phones.getCount() > 0) {
            phones.moveToFirst();
            number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

        }

        phones.close();
        return number;
    }


    private void init() {
        checkForPermissions();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }

        });
        if (ContactConstants.COUNTRY_CODE_MAP.isEmpty()) {
            new CountryCodesTask().execute(getResources().openRawResource(R.raw.country_codes_map));
        }
        initializeContactList();



    }

    private void initializeContactList() {

        Cursor cursor = this.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, ContactConstants.PROJECTION, null, null, null);
        ListView mContactsList =
                (ListView) this.findViewById(R.id.contact_list);

        mContactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long rowId) {

                ContactAdapter adapter = (ContactAdapter) adapterView.getAdapter();

                contactSelected(adapter.getItem(position).id);
            }
        });

        if (cursor.getCount() > 0) {
            mContactsList.setAdapter(getContactsAdapter(cursor));

        }

    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.current_fragment_tag));
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentContactId > 0)
            outState.putLong(ContactConstants.CURRENT_CONTACT_ID, currentContactId);
        outState.putSerializable(ContactConstants.COUNTRIES_IN_HOLIDAYS, countryHolidays);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void checkForPermissions(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    1);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    1);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_CONTACTS},
                    1);
        }
    }
}
