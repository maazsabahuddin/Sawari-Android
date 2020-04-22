package com.sohaibaijaz.sawaari;

import com.sohaibaijaz.sawaari.model.Location;
import com.sohaibaijaz.sawaari.model.User;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {

    Realm realm;
    private String longitudeWDB;
    private String latitudeWDB;
    private String placeNameWDB;
    private String placetype;



    public RealmHelper(Realm realm) {
        this.realm = realm;
    }

    public ArrayList<String> getAllRecords(final String phone){
        ArrayList<String> placeName= new ArrayList<>();
        RealmResults<Location> results = realm.where(com.sohaibaijaz.sawaari.model.Location.class).equalTo("placeType","Extra").equalTo("phoneNumber", phone).findAll();
        for(com.sohaibaijaz.sawaari.model.Location location : results){
                placeName.add(location.getPlaceName());
        }

        return placeName;
    }

    public HashMap<String, String> getPlace(final String placeType){

        final HashMap<String, String> dropoffLocation = new HashMap<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                RealmResults<com.sohaibaijaz.sawaari.model.Location> results = bgRealm.where(com.sohaibaijaz.sawaari.model.Location.class).equalTo("placeType",placeType ).findAll();
                for(com.sohaibaijaz.sawaari.model.Location location : results){
                    dropoffLocation.put("latitude", location.getLatitude());
                    dropoffLocation.put("longitude", location.getLongitude());
                    dropoffLocation.put("name", location.getPlaceName());
                }
                // Toast.makeText(getActivity(), longitude+" "+latitude, Toast.LENGTH_SHORT).show();

            }
        });

        return dropoffLocation;
    }
}
