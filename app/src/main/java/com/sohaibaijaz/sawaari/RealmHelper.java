package com.sohaibaijaz.sawaari;

import com.sohaibaijaz.sawaari.model.Location;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {

    Realm realm;


    public RealmHelper(Realm realm) {
        this.realm = realm;
    }

    public ArrayList<String> getAllRecords(){
        ArrayList<String> placeName= new ArrayList<>();
        RealmResults<Location> results = realm.where(com.sohaibaijaz.sawaari.model.Location.class).equalTo("placeType","Extra").findAll();
        for(com.sohaibaijaz.sawaari.model.Location location : results){
                placeName.add(location.getPlaceName());
        }

        return placeName;
    }
}
