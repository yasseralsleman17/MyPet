package com.example.mypet.User.pet;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.mypet.R;
import com.example.mypet.User.OwnerProfile;


import com.example.mypet.Var;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewPetProfile extends AppCompatActivity {


    ImageView imageView;
    TextView pet_name,pet_birth,pet_sex,pet_weight,pet_diseases,pet_MEDICATION,ownerProfile;
    String pet_name_txt,pet_birth_txt,pet_sex_txt,pet_weight_txt,pet_diseases_txt,pet_MEDICATION_txt;

    String pet_id,user_id,owner_pet_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pet_profile);



        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pet_id = extras.getString("pet_id");
            user_id = extras.getString("user_id");


        }


        imageView=findViewById(R.id.imageView);

        pet_name=findViewById(R.id.pet_name);
        pet_birth=findViewById(R.id.pet_birth);
        pet_sex=findViewById(R.id.pet_sex);
        pet_weight=findViewById(R.id.pet_weight);
        pet_diseases=findViewById(R.id.pet_diseases);
        pet_MEDICATION=findViewById(R.id.pet_MEDICATION);
        ownerProfile=findViewById(R.id.ownerProfile);


        ownerProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), OwnerProfile.class).putExtra("user_id",user_id).putExtra("owner_pet_id",owner_pet_id));
            }
        });

        get_pet_info();

    }

    private void get_pet_info() {
        final String REQUEST_URL = Var.get_pet_info;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d("response", obj.getString("code"));
                            if (obj.getString("code").equals("-1")) {
                            } else {

                                JSONObject data = obj.getJSONObject("data");
                                owner_pet_id=data.getString("user_id");
                                pet_name_txt = data.getString("pet_name");
                                pet_birth_txt = data.getString("birthday");
                                pet_sex_txt = data.getString("sex");
                                pet_weight_txt = data.getString("weight");
                                pet_diseases_txt = data.getString("diseases");
                                pet_MEDICATION_txt = data.getString("medication");


                                String image_name= data.getString("image");



                                Glide.with(getApplicationContext())
                                        .load(Var.images_url+image_name)
                                        .into(imageView);

                                pet_name.setText(pet_name_txt);
                                pet_birth.setText(pet_birth_txt);
                                pet_sex.setText(pet_sex_txt);
                                pet_weight.setText(pet_weight_txt);
                                pet_diseases.setText(pet_diseases_txt);
                                pet_MEDICATION.setText(pet_MEDICATION_txt);



                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }

                }

        ) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                Log.d("mStatusCode", String.valueOf(mStatusCode));


                return super.parseNetworkResponse(response);

            }

            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();

                MyData.put("pet_id", pet_id);


                return MyData;
            }
        };
        requestQueue.add(jsonRequest);




    }


}