package com.sohaibaijaz.sawaari;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.sohaibaijaz.sawaari.model.Location;
import com.sohaibaijaz.sawaari.model.UserDetailsTable;

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

    public ArrayList<HashMap<String, String>> getAllRecords(){

        ArrayList<HashMap<String, String>> place= new ArrayList<>();
        try{
            RealmResults<Location> results = realm.where(com.sohaibaijaz.sawaari.model.Location.class).equalTo("placeType","Other").findAll();
            for(com.sohaibaijaz.sawaari.model.Location location : results){
                final HashMap<String, String> places = new HashMap<>();

                places.put("place_id", location.getPlaceID());
                places.put("place_type", location.getPlaceType());
                places.put("name", location.getPlaceName());

                place.add(places);
                //place.add(location.getPlaceName());
            }
            return place;
        }catch (Exception e){}
        return place;
    }

    public HashMap<String, String> getPlace(final String placeType){

        final HashMap<String, String> dropoffLocation = new HashMap<>();
        try{
            RealmResults<com.sohaibaijaz.sawaari.model.Location> results = realm.where(com.sohaibaijaz.sawaari.model.Location.class).equalTo("placeType",placeType ).findAll();
            for(com.sohaibaijaz.sawaari.model.Location location : results){
                dropoffLocation.put("latitude", location.getLatitude());
                dropoffLocation.put("longitude", location.getLongitude());
                dropoffLocation.put("name", location.getPlaceName());
            }
            // Toast.makeText(getActivity(), longitude+" "+latitude, Toast.LENGTH_SHORT).show();

            return dropoffLocation;
        }
        catch (Exception e){}
        return dropoffLocation;
    }

    public String checkPlace(final String placeType){
        String place="";
        try{


            RealmResults<com.sohaibaijaz.sawaari.model.Location> results = realm.where(com.sohaibaijaz.sawaari.model.Location.class).equalTo("placeType",placeType ).findAll();
            for(com.sohaibaijaz.sawaari.model.Location location : results){
            place = location.getPlaceName();
            }
            // Toast.makeText(getActivity(), longitude+" "+latitude, Toast.LENGTH_SHORT).show();

           // return place;
        }
        catch (Exception e){}
        return place;
    }


//    public HashMap<String, String> getPlace(final String placeType, final String phone){
//
//        final HashMap<String, String> dropoffLocation = new HashMap<>();
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm bgRealm) {
//
//                RealmResults<com.sohaibaijaz.sawaari.model.Location> results = bgRealm.where(com.sohaibaijaz.sawaari.model.Location.class).equalTo("placeType",placeType ).equalTo("phoneNumber", phone).findAll();
//                for(com.sohaibaijaz.sawaari.model.Location location : results){
//                    dropoffLocation.put("latitude", location.getLatitude());
//                    dropoffLocation.put("longitude", location.getLongitude());
//                    dropoffLocation.put("name", location.getPlaceName());
//                }
//                // Toast.makeText(getActivity(), longitude+" "+latitude, Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//        return dropoffLocation;
//    }


    public HashMap<String, String> getUserDetailsDB(){

        final HashMap<String, String> userDetails = new HashMap<>();
        try{
            RealmResults<com.sohaibaijaz.sawaari.model.UserDetailsTable> results = realm.where(com.sohaibaijaz.sawaari.model.UserDetailsTable.class).findAll();
            for(com.sohaibaijaz.sawaari.model.UserDetailsTable details : results){
                userDetails.put("firstname", details.getFirstName());
                userDetails.put("lastname", details.getLastName());
                userDetails.put("email", details.getEmail());
                userDetails.put("phonenumber", details.getPhoneNumber());
            }
            // Toast.makeText(getActivity(), longitude+" "+latitude, Toast.LENGTH_SHORT).show();

            return userDetails;
        }
        catch (Exception e){}
        return userDetails;
    }

    public void InsertUserDetails(final Context context,final String phonenumber, final String firstname, final String lastname, final String email) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                UserDetailsTable userdetails = bgRealm.createObject(UserDetailsTable.class, phonenumber);
                // user.setPlaceID(placeID);
               // userdetails.setPhoneNumber(phonenumber);
                userdetails.setFirstName(firstname);
                userdetails.setLastName(lastname);
                userdetails.setEmail(email);

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
              //   Toast.makeText(context, "Details Saved", Toast.LENGTH_LONG).show();

                // Log.v("Database","Data inserted");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

                Log.e("Database", error.getMessage());
            }
        });
    }

    public void insertUserPlaces( final Context context,final String placeID, final String placeName, final String latitude, final String longitude, final String placeType) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Location user = bgRealm.createObject(Location.class);
                user.setPlaceID(placeID);
                user.setPlaceName(placeName);
                user.setLatitude(latitude);
                user.setLongitude(longitude);
                user.setPlaceType(placeType);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
                //Toast.makeText(context, "Place Saved", Toast.LENGTH_LONG).show();

                // Log.v("Database","Data inserted");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
               // Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

                Log.e("Database", error.getMessage());
            }
        });
    }



    public void UpdateUserPlaces(final String placeID, final String placeName, final String latitude, final String longitude, final String placeType) {



        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                RealmResults<Location> place = bgRealm.where(Location.class).equalTo("placeType", placeType).findAll();

                place.setString("placeID",placeID);
                place.setString("placeName",placeName);
                place.setString("longitude",longitude);
                place.setString("latitude",latitude);
                place.setString("placeType",placeType);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
                //Toast.makeText(context, "Place Saved", Toast.LENGTH_LONG).show();

                // Log.v("Database","Data inserted");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                // Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

                Log.e("Database", error.getMessage());
            }
        });
    }

//    public void insertUserPlaces( final Context context,final String placeID, final String placeName, final String latitude, final String longitude, final String placeType) {
//        realm.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm bgRealm) {
//                Location user = bgRealm.createObject(Location.class, placeID.toString());
//                // user.setPlaceID(placeID);
//                user.setPlaceName(placeName);
//                user.setLatitude(latitude);
//                user.setLongitude(longitude);
//                user.setPlaceType(placeType);
//            }
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//                // Transaction was a success.
//                //Toast.makeText(context, "Place Saved", Toast.LENGTH_LONG).show();
//
//                // Log.v("Database","Data inserted");
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                // Transaction failed and was automatically canceled.
//                // Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
//
//                Log.e("Database", error.getMessage());
//            }
//        });
//    }


public void DeleteUserDetails( final Context context) {

       try {
           final RealmResults<UserDetailsTable> results = realm.where(UserDetailsTable.class).findAll();

           // All changes to data must happen in a transaction
           realm.executeTransaction(new Realm.Transaction() {
               @Override
               public void execute(Realm realm) {
                   results.deleteAllFromRealm();
                  // Toast.makeText(context, "Details Deleted", Toast.LENGTH_LONG).show();

               }
           });
       }
         catch (Exception e){}
   }

    public void DeleteUserPlaces( final Context context) {

        try {
            final RealmResults<Location> results = realm.where(Location.class).findAll();

            // All changes to data must happen in a transaction
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    results.deleteAllFromRealm();
                    // Toast.makeText(context, "Details Deleted", Toast.LENGTH_LONG).show();

                }
            });
        }
        catch (Exception e){}
    }

    public void check(final HashMap<String,String> abcd, final String phonenumber, final  Context context) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Location userdetails = bgRealm.createObject(Location.class, phonenumber);
                // user.setPlaceID(placeID);
                // userdetails.setPhoneNumber(phonenumber);
                userdetails.setPlaceID(abcd.get("place_id"));
                userdetails.setPlaceType(abcd.get("place_type"));
                userdetails.setPlaceName(abcd.get("place_name"));
                userdetails.setLongitude(abcd.get("longitude"));
                userdetails.setLatitude(abcd.get("latitude"));

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
                //   Toast.makeText(context, "Details Saved", Toast.LENGTH_LONG).show();

                // Log.v("Database","Data inserted");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

                Log.e("Database", error.getMessage());
            }
        });
    }
}
