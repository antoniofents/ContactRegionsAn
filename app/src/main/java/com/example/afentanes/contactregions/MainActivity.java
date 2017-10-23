package com.example.afentanes.contactregions;

import android.Manifest;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    SimpleCursorAdapter mCursorAdapter;

    long currentContactId;
    private final static int[] TO_IDS = {
            R.id.contac_name_id
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(savedInstanceState!=null){
             currentContactId = savedInstanceState.getLong("currentContactId");

        }
        setContentView(R.layout.activity_main);
        init();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentContactId>0)
            contactSelected(currentContactId);
    }

    private void contactSelected(long id){

        currentContactId=id;
        if(getCurrentFragment()!=null){
            ContactInfoFragment fragment = (ContactInfoFragment)  getCurrentFragment();
            fragment.updateFragmentView(id);
        }else{
            try {
                ContactInfoFragment contactInfoFragment = new ContactInfoFragment();
                if(getIntent().getExtras()==null){
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


    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        initializeContactList();
    }






    private void initializeContactList() {

        Cursor cursor = this.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, ContactConstants.PROJECTION, null, null, null);
        ListView mContactsList =
                (ListView) this.findViewById(R.id.contact_list);

        mContactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long rowId) {


                Cursor cursor =((SimpleCursorAdapter)adapterView.getAdapter()).getCursor();
                // Move to the selected contact
                cursor.moveToPosition(position);
                // Get the _ID value
                long mContactId = cursor.getLong(ContactConstants.CONTACT_ID_INDEX);
                contactSelected(mContactId);


            }
        });

        if (cursor.getCount() > 0) {

            mCursorAdapter = new SimpleCursorAdapter(
                    this,
                    R.layout.contact_item,
                    cursor,
                    ContactConstants.FROM_COLUMNS, TO_IDS,
                    0);
            mContactsList.setAdapter(mCursorAdapter);

        }


    }



    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentByTag(getResources().getString(R.string.current_fragment_tag));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentContactId>0)
        outState.putLong("currentContactId", currentContactId);


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
}
