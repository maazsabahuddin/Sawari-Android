package com.sohaibaijaz.sawaari.Maps;

import androidx.annotation.NonNull;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sohaibaijaz.sawaari.R;
import com.sohaibaijaz.sawaari.RealmHelper;
import com.sohaibaijaz.sawaari.SavedPlaceAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;

public class SavedPlaceFragment extends Fragment {

    private View fragmentView;
   // FragmentManager fm = getSupportFragmentManager();


    Realm realm;
    // RealmHelper realmHelper;
    private ArrayList<HashMap<String, String>> savedplacedetails= new ArrayList<>();
    ListView savedplace_lv;
    private String checkhome;
    private  String checkwork;
    RecyclerView recyclerView_savedplace;
    SavedPlaceAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    private HashMap<String, String> currentLocation = new HashMap<>();
    private  String activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView= inflater.inflate(R.layout.fragment_saved_place, container, false);

        TextView add_home = fragmentView.findViewById(R.id.add_home_saved_place);
        TextView add_work =fragmentView.findViewById(R.id.add_work_saved_place);
        TextView add_place = fragmentView.findViewById(R.id.add_saved_place);

        LinearLayout add_home_LL = fragmentView.findViewById(R.id.add_home_place);
        LinearLayout add_work_LL =fragmentView.findViewById(R.id.add_work_place);

        recyclerView_savedplace= fragmentView.findViewById(R.id.savedplace_recycler_view);
        // savedplace_lv= findViewById(R.id.saved_place_name_listview);

        realm = Realm.getDefaultInstance();
        final RealmHelper helper = new RealmHelper(realm);


        Bundle b = this.getArguments();
        if (b.getSerializable("currentLocation") != null) {
            currentLocation = (HashMap<String, String>) b.getSerializable("currentLocation");
        }
       // Bundle b = getIntent().getExtras();
        activity = b.getString("activity");
        // value = b.getString("value");
        if (b.getSerializable("currentLocation") != null) {
            currentLocation = (HashMap<String, String>) b.getSerializable("currentLocation");
        }

        savedplacedetails=helper.getAllRecords();

        checkhome=helper.checkPlace("Home");
        checkwork=helper.checkPlace("Work");

        if(!checkhome.equals("")){
            add_home.setText(checkhome);
        }
        if(!checkwork.equals("")){
            add_work.setText(checkwork);
        }

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView_savedplace.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView_savedplace.setLayoutManager(layoutManager);

        mAdapter = new SavedPlaceAdapter(savedplacedetails, getContext());
        recyclerView_savedplace.setAdapter(mAdapter);

        add_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "Working", Toast.LENGTH_LONG).show();
                //  AddPlaceFragment frag = new AddPlaceFragment();
//                 FragmentManager manager = getSupportFragmentManager();
//                Fragment fragment = new AddPlaceFragment();
//                Bundle arguments = new Bundle();
//                arguments.putSerializable("currentLocation" , currentLocation);
//                arguments.putString("value", "Home");
//                arguments.putString("activity", activity);
//                fragment.setArguments(arguments);
//                getSupportFragmentManager().beginTransaction().replace(R.id.saved_place_fragment, fragment).commit();

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
                Toast.makeText(getActivity(), "Working", Toast.LENGTH_LONG).show();
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

                Fragment fragment = new AddPlaceFragment();
                Bundle arguments = new Bundle();
                arguments.putSerializable("currentLocation" , currentLocation);
                arguments.putString("value", "Other");
                arguments.putString("activity", activity);
                fragment.setArguments(arguments);
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.saved_place_fragment, fragment).commit();

              // AddPlaceFragment fragment2=new AddPlaceFragment();
             //  FragmentManager fragmentManager=getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.saved_place_frame,fragment2,"tag");
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
            }
        });

   return fragmentView;
    }




}
