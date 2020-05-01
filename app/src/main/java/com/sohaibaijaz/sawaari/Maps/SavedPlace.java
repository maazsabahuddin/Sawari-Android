package com.sohaibaijaz.sawaari.Maps;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.sohaibaijaz.sawaari.Fragments.HomeFragment;
import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.RealmHelper;

import java.util.ArrayList;

import io.realm.Realm;

public class SavedPlace extends AppCompatActivity {

    // create a FragmentManager
    FragmentManager fm = getSupportFragmentManager();

    private void loadFragment(Fragment fragment) {
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.saved_place_frame, fragment);
        fragmentTransaction.addToBackStack(null).commit(); // save the changes
    }

    Realm realm;
   // RealmHelper realmHelper;
    private ArrayList<String> savedplaceName;
    ListView savedplace_lv;
    private String checkhome;
    private  String checkwork;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_saved_place);
        getSupportActionBar().hide();

//        loadFragment(new HomeFragment());

        TextView add_home = findViewById(R.id.add_home_saved_place);
        TextView add_work = findViewById(R.id.add_work_saved_place);
        TextView add_place = findViewById(R.id.add_saved_place);
        ImageView back_saved_places_button = findViewById(R.id.back_saved_places_button);
        savedplace_lv= findViewById(R.id.saved_place_name_listview);

        realm = Realm.getDefaultInstance();
        final RealmHelper helper = new RealmHelper(realm);


        savedplaceName=helper.getAllRecords();

        checkhome=helper.checkPlace("Home");
        checkwork=helper.checkPlace("Work");

        if(!checkhome.equals("")){
            add_home.setText(checkhome);
        }
        if(!checkwork.equals("")){
            add_work.setText(checkwork);
        }
        savedplace_lv.setEnabled(false);
        ArrayAdapter adapter = new ArrayAdapter(SavedPlace.this, R.layout.listview_savedplaces,R.id.textView_lv,savedplaceName);
        savedplace_lv.setAdapter(adapter);
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

             //   loadFragment(new AddPlaceFragment());

//                AddPlaceFragment fragment = new AddPlaceFragment();
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.spinner_frame, fragment);
//                transaction.addToBackStack(null);
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

        add_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Fragment newFragment = new AddPlaceFragment();
//                FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.saved_place_frame, newFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
//                Fragment mFragment = new AddPlaceFragment();
//                FragmentManager fragmentManager =getSupportFragmentManager();
//                fragmentManager.beginTransaction()
//                        .add(R.id.saved_place_frame, mFragment).commit();

                AddPlaceFragment fragment2=new AddPlaceFragment();
                FragmentManager fragmentManager=getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.saved_place_frame,fragment2,"tag");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

    }

}
