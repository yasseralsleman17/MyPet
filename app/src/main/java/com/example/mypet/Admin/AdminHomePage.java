package com.example.mypet.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mypet.Admin.Blog.ManageBlogs;
import com.example.mypet.Admin.PetPlaces.ManagePetPlaces;
import com.example.mypet.Admin.Users.ManageUsers;
import com.example.mypet.Admin.Veterinary.ManageVeterinary;
import com.example.mypet.R;

public class AdminHomePage extends AppCompatActivity {

    LinearLayout manage_user, manage_blogs, manage_pet_places, manage_veterinary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        manage_user = findViewById(R.id.manage_user);
        manage_blogs = findViewById(R.id.manage_blogs);
        manage_pet_places = findViewById(R.id.manage_pet_places);
        manage_veterinary = findViewById(R.id.manage_veterinary);


        manage_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ManageUsers.class));
            }
        });
        manage_blogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ManageBlogs.class));
            }
        });
        manage_pet_places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ManagePetPlaces.class));
            }
        });
        manage_veterinary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ManageVeterinary.class));
            }
        });
    }
}