package com.example.mypet.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.mypet.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserHomePage extends AppCompatActivity {


    String  id_txt;


    BottomNavigationView bottom_navigation;
    BottomAppBar bottomAppBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_txt = extras.getString("id_txt");
        }

        bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setBackground(null);
        bottom_navigation.setItemIconTintList(null);

        bottom_navigation.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AdoptionFragment(id_txt)).commit();


    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.profile:
                    selectedFragment = new ProfileFragment(id_txt);
                    break;
                case R.id.pet_places:
                    selectedFragment = new PetPlacesFragment(id_txt);
                    break;
                case R.id.adoption:
                    selectedFragment = new AdoptionFragment(id_txt);
                    break;
                case R.id.blogs:
                    selectedFragment = new BlogsFragment(id_txt);
                    break;
                case R.id.direct_messages:
                    selectedFragment = new DirectMessagesFragment(id_txt);
                    break;

            }
            // It will help to replace the
            // one fragment to other.
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
            return true;
        }
    };


}