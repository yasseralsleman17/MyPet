package com.example.mypet.Admin.PetPlaces;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mypet.Admin.Veterinary.ManageVeterinary;
import com.example.mypet.R;

public class ManagePetPlaces extends AppCompatActivity {

    ImageView pet_resorts,  accessories,manage_veterinary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_pet_places);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        pet_resorts = findViewById(R.id.pet_resorts);
        accessories = findViewById(R.id.accessories);
        manage_veterinary = findViewById(R.id.manage_veterinary);


        accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManageAccessoriesShop.class);
                intent.putExtra("type", "accessories shops");
                startActivity(intent);
            }
        });





        pet_resorts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManagePetRestoresShop.class);
                intent.putExtra("type", "pet restores shops");
                startActivity(intent);
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