package com.sohaibaijaz.sawaari;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sohaibaijaz.sawaari.Settings.SettingsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.realm.Realm;

import static com.sohaibaijaz.sawaari.MainActivity.AppPreferences;

public class SavedPlaceAdapter extends RecyclerView.Adapter<SavedPlaceAdapter.MyViewHolder> {

private ArrayList<HashMap<String,String>> Placedetails;
private  Context context1;
Realm realm;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textViewplace;
         public ImageView imageView;
         public TextView textViewAddress;
        public MyViewHolder(View v) {

            super(v);
            textViewplace = v.findViewById(R.id.textView_rv);
             imageView = v.findViewById(R.id.imagehome);
             textViewAddress=v.findViewById(R.id.textview_address);

        }
    }


    public SavedPlaceAdapter(ArrayList<HashMap<String, String>> placedetails, Context context) {
        Placedetails = placedetails;
        context1=context;
    }
    @NonNull
    @Override
    public SavedPlaceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.listview_savedplaces, parent, false);

        // Return a new holder instance
        SavedPlaceAdapter.MyViewHolder viewHolder = new SavedPlaceAdapter.MyViewHolder(contactView);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull SavedPlaceAdapter.MyViewHolder holder, final int position) {

      // TextView textView= holder.textViewplace;
        holder.textViewplace.setText(Placedetails.get(position).get("name"));
        holder.textViewAddress.setText(Placedetails.get(position).get("address"));
        holder.imageView.setImageResource(R.drawable.deleteicon);



        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the clicked item label
                String placetype = Placedetails.get(position).get("place_type");
                String placeid = Placedetails.get(position).get("place_id");

                deleteUserPlace(context1,placeid,placetype);
                realm = Realm.getDefaultInstance();
                final RealmHelper helper = new RealmHelper(realm);
                // Remove the item on remove/button click
                Placedetails.remove(position);
                helper.DeletePlace(context1, placeid, placetype);

                /*
                    public final void notifyItemRemoved (int position)
                        Notify any registered observers that the item previously located at position
                        has been removed from the data set. The items previously located at and
                        after position may now be found at oldPosition - 1.

                        This is a structural change event. Representations of other existing items
                        in the data set are still considered up to date and will not be rebound,
                        though their positions may be altered.

                    Parameters
                        position : Position of the item that has now been removed
                */
                notifyItemRemoved(position);

                /*
                    public final void notifyItemRangeChanged (int positionStart, int itemCount)
                        Notify any registered observers that the itemCount items starting at
                        position positionStart have changed. Equivalent to calling
                        notifyItemRangeChanged(position, itemCount, null);.

                        This is an item change event, not a structural change event. It indicates
                        that any reflection of the data in the given position range is out of date
                        and should be updated. The items in the given range retain the same identity.

                    Parameters
                        positionStart : Position of the first item that has changed
                        itemCount : Number of items that have changed
                */
                notifyItemRangeChanged(position,Placedetails.size());

                // Show the removed item label
              //  Toast.makeText(context1,"Removed : " + itemLabel,Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return Placedetails.size();
    }


    public static void deleteUserPlace(final Context context, final String placeid, final  String placetype){
        final SharedPreferences sharedPreferences = Objects.requireNonNull(context).getSharedPreferences(AppPreferences, Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("Token", "");
        final RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(context));
        try {
            String URL = MainActivity.baseurl + "/delete/user/place/";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.i("VOLLEY", response);
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getString("status").equals("200")) {
                            Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                            // SettingsFragment.forcedLogout(context);
                        }
                        else if (json.getString("status").equals("404")) {
                            Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                            //SettingsFragment.forcedLogout(context);
                        }
                        else if (json.getString("status").equals("400")) {
                            Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                          //  SettingsFragment.forcedLogout(context);
                        }
                    } catch (JSONException e) {
                        Log.e("VOLLEY", e.toString());

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Server is temporarily down, sorry for your inconvenience", Toast.LENGTH_SHORT).show();
                    Log.e("VOLLEY", error.toString());
                }
            }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();

                    params.put("place_id", placeid);
                    params.put("place_type", placetype);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("Authorization", token);
                    return params;
                }
            };

            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            requestQueue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
