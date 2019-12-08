package com.sohaibaijaz.sawaari;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetDirectionsData extends AsyncTask<Object, String, String> {
    GoogleMap mMap;
    String url;
    LatLng startLatLng, endLatLng;

    HttpURLConnection httpURLConnection = null;
    String data = "";
    InputStream inputStream = null;

    Context c;


    public GetDirectionsData (Context c)
    {
        this.c = c;
    }
    @Override
    protected String doInBackground(Object... objects) {

        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];
        startLatLng = (LatLng)objects[2];
        endLatLng = (LatLng)objects[3];

        try{
            URL myurl = new URL(url);
            httpURLConnection  = (HttpURLConnection)myurl.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while((line=bufferedReader.readLine())!=null){
                sb.append(line);
            }
            data = sb.toString();
            bufferedReader.close();

        }

        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(String s){
        try{
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("routes")
                    .getJSONObject(0)
                    .getJSONArray("legs")
                    .getJSONObject(0)
                    .getJSONArray("steps");

            int count = jsonArray.length();
            String[] polyline_array = new String[count];

            JSONObject jsonObject2;

            for(int i =0; i< count; i++){
                jsonObject2 = jsonArray.getJSONObject(i);

                String polygone = jsonObject2.getJSONObject("polyline").getString("points");

                polyline_array[i] = polygone;
            }

            int count2 = polyline_array.length;

            for(int i =0; i<count2; i++){
                PolylineOptions options2 = new PolylineOptions();
                options2.color(Color.BLUE);
                options2.width(10);
                options2.addAll(PolyUtil.decode(polyline_array[i]));
                mMap.addPolyline(options2);
            }


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
