package com.sohaibaijaz.sawaari.Maps;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.sohaibaijaz.sawaari.Fragments.HomeFragment;
import com.sohaibaijaz.sawaari.R;

public class SavedPlace extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_saved_place);
        getSupportActionBar().hide();

        TextView add_home = findViewById(R.id.add_home_saved_place);
        TextView add_work = findViewById(R.id.add_work_saved_place);
        ImageView back_saved_places_button = findViewById(R.id.back_saved_places_button);

        back_saved_places_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        add_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(SavedPlace.this, "Working", Toast.LENGTH_LONG).show();
//                AddPlaceFragment frag = new AddPlaceFragment();
//                FragmentManager manager = getSupportFragmentManager();
//                FragmentTransaction transaction = manager.beginTransaction();
//                transaction.add(R.id.fragment_container, frag);
//                transaction.commit();

//                Fragment fragment = new AddPlaceFragment();
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

//                AddPlaceFragment fragment = new AddPlaceFragment();
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, fragment);
//                transaction.commit();
//                onBackPressed();
            }
        });

        add_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SavedPlace.this, "Working", Toast.LENGTH_LONG).show();
            }
        });

    }

}
