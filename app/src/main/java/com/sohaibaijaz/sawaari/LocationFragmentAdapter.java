package com.sohaibaijaz.sawaari;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;

public class LocationFragmentAdapter extends RecyclerView.Adapter<LocationFragmentAdapter.MyViewHolder>  {


    private ArrayList<HashMap<String,String>> Placedetails;
    private Context context1;
    Realm realm;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textViewplace;
        public TextView textViewAddress;
        public MyViewHolder(View v) {

            super(v);
            textViewplace = v.findViewById(R.id.textView_rv_lf);
            textViewAddress=v.findViewById(R.id.textview_address_lf);

        }
    }


    public LocationFragmentAdapter(ArrayList<HashMap<String, String>> placedetails, Context context) {
        Placedetails = placedetails;
        context1=context;
    }
    @NonNull
    @Override
    public LocationFragmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.recyclerview_locationfragment, parent, false);

        // Return a new holder instance
        LocationFragmentAdapter.MyViewHolder viewHolder = new LocationFragmentAdapter.MyViewHolder(contactView);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull LocationFragmentAdapter.MyViewHolder holder, final int position) {

        // TextView textView= holder.textViewplace;
        holder.textViewplace.setText(Placedetails.get(position).get("name"));
        holder.textViewAddress.setText(Placedetails.get(position).get("address"));
//        holder.imageView.setImageResource(R.drawable.deleteicon);
//
//
//
//        holder.imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Get the clicked item label
//                String placetype = Placedetails.get(position).get("place_type");
//                String placeid = Placedetails.get(position).get("place_id");
//
//               // deleteUserPlace(context1,placeid,placetype);
//                realm = Realm.getDefaultInstance();
//                final RealmHelper helper = new RealmHelper(realm);
//                // Remove the item on remove/button click
//                Placedetails.remove(position);
//                helper.DeletePlace(context1, placeid, placetype);
//
//                /*
//                    public final void notifyItemRemoved (int position)
//                        Notify any registered observers that the item previously located at position
//                        has been removed from the data set. The items previously located at and
//                        after position may now be found at oldPosition - 1.
//
//                        This is a structural change event. Representations of other existing items
//                        in the data set are still considered up to date and will not be rebound,
//                        though their positions may be altered.
//
//                    Parameters
//                        position : Position of the item that has now been removed
//                */
//                notifyItemRemoved(position);
//
//                /*
//                    public final void notifyItemRangeChanged (int positionStart, int itemCount)
//                        Notify any registered observers that the itemCount items starting at
//                        position positionStart have changed. Equivalent to calling
//                        notifyItemRangeChanged(position, itemCount, null);.
//
//                        This is an item change event, not a structural change event. It indicates
//                        that any reflection of the data in the given position range is out of date
//                        and should be updated. The items in the given range retain the same identity.
//
//                    Parameters
//                        positionStart : Position of the first item that has changed
//                        itemCount : Number of items that have changed
//                */
//                notifyItemRangeChanged(position,Placedetails.size());
//
//                // Show the removed item label
//                //  Toast.makeText(context1,"Removed : " + itemLabel,Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return Placedetails.size();
    }

}
