package com.sohaibaijaz.sawaari.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.sohaibaijaz.sawaari.R;

public class HelpActivity extends AppCompatActivity {

    ListView helpListView;
    private View fragmentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helpfragment);
        getSupportActionBar().setTitle("Help & Support");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        helpListView = findViewById(R.id.help_array);
        helpListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Thank you! We will contact you soon.", Toast.LENGTH_SHORT).show();
            }
        });

    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        fragmentView = inflater.inflate(R.layout.helpfragment, container, false);
//
//        helpListView = fragmentView.findViewById(R.id.help_array);
//        helpListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(), "Thank you! We will contact you soon.", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        return fragmentView;
//    }
}
