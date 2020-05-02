package com.sohaibaijaz.sawaari;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SavedPlaceAdapter extends RecyclerView.Adapter<SavedPlaceAdapter.MyViewHolder> {

private ArrayList<String> Placename;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textViewplace;
         public ImageView imageView;
        public MyViewHolder(View v) {

            super(v);
            textViewplace = v.findViewById(R.id.textView_rv);
             imageView = v.findViewById(R.id.imagehome);

        }
    }


    public SavedPlaceAdapter(ArrayList<String> placename) {
        Placename = placename;
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
    public void onBindViewHolder(@NonNull SavedPlaceAdapter.MyViewHolder holder, int position) {

      // TextView textView= holder.textViewplace;
        holder.textViewplace.setText("   "+Placename.get(position));
       holder.imageView.setImageResource(R.drawable.deleteicon);

    }

    @Override
    public int getItemCount() {
        return Placename.size();
    }


}
