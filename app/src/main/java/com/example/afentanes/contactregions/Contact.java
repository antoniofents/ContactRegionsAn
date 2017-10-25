package com.example.afentanes.contactregions;

/**
 * Created by afentanes on 10/24/17.
 */

public class Contact {

    public Contact(long id,String name ,String phone){
        this.id=id;
        this.name=name;
        this.phone=phone;
    }
    public long id;
    public String name;
    public String phone;


    public String getCountryCode(){
        return ContactConstants.COUNTRY_CODE_MAP.get(phone.split("\\s")[0]);
    }
}

