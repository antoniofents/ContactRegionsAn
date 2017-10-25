package com.example.afentanes.contactregions;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ContactInfoFragment  extends Fragment  {




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.contact_description, container, false);

        // Sets the adapter for the ListView

    }

    @Override
    public void onStart() {
        super.onStart();

        Long id= getArguments().getLong(ContactConstants.CONTACT_BUNDLE_ID);

        TextView textView= getView().findViewById(R.id.contact_id);
        textView.setText("holaaaa"+id);

    }


    protected  void updateFragmentView(long contactid){

        TextView text = getActivity().findViewById(R.id.contact_id);
        if(text!=null)text.setText("holaaaa" + contactid);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

  //      Long id= getArguments().getLong(ContactConstants.CONTACT_BUNDLE_ID);


    }
}
