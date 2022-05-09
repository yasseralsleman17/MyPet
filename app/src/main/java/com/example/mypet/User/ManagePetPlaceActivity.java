package com.example.mypet.User;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mypet.R;

public class ManagePetPlaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_pet_place);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

    }
}