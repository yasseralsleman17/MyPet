package com.example.mypet.User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.mypet.R;
import com.example.mypet.User.pet.ViewPetProfile;
import com.example.mypet.Var;
import com.github.siyamed.shapeimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OwnerProfile extends AppCompatActivity {


    TextView _user_sex, user_name;
    String editText_name_txt, editText_sex_txt;

    ImageView imageView_send;
    GridLayout grid_pet;

    String user_id, owner_pet_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            owner_pet_id = extras.getString("owner_pet_id");
            user_id = extras.getString("user_id");

        }

        grid_pet = findViewById(R.id.grid_pet);
        imageView_send = findViewById(R.id.imageView_send);
        _user_sex = findViewById(R.id._user_sex);
        user_name = findViewById(R.id.user_name);
        imageView_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ChatActivity.class);
                i.putExtra("receiver_id", owner_pet_id);
                i.putExtra("receiver_name", editText_name_txt);
                i.putExtra("sender_id", user_id);
                startActivity(i);
            }
        });

        getOwnerProfile();
        get_user_animals();

    }


    private void get_user_animals() {

        final String REQUEST_URL = Var.get_user_animals;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d("response", response);

                        try {
                            JSONArray obj = new JSONArray(response);

                            for (int i = 0; i < obj.length(); i++) {
                                JSONObject data = obj.getJSONObject(i).getJSONObject("data");
                                showPet(data);
                            }
                        } catch (JSONException e) {
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
                MyData.put("user_id", owner_pet_id);
                return MyData;
            }
        };
        requestQueue.add(jsonRequest);
    }

    void showPet(JSONObject data) throws JSONException {
        String pet_name_txt = data.getString("pet_name");
        String pet_id = data.getString("id");
        String image = data.getString("image");
        View view = getLayoutInflater().inflate(R.layout.pet_card, null);

        CircularImageView pet_image = view.findViewById(R.id.pet_image);
        TextView pet_name = view.findViewById(R.id.pet_name);
        pet_name.setText(pet_name_txt);

        Glide.with(getApplicationContext())
                .load(Var.images_url + image)
                .into(pet_image);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), ViewPetProfile.class);
                i.putExtra("pet_id", pet_id);
                i.putExtra("user_id", user_id);

                startActivity(i);
            }
        });
        grid_pet.addView(view);
    }


    private void getOwnerProfile() {

        final String REQUEST_URL = Var.getuserinfo;

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

                                editText_name_txt = data.getString("name");


                                editText_sex_txt = data.getString("sex");


                                _user_sex.setText(data.getString("sex"));

                                user_name.setText(data.getString("name"));


                            }
                        } catch (JSONException e) {


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

                MyData.put("user_id", owner_pet_id);


                return MyData;
            }
        };
        requestQueue.add(jsonRequest);


    }

}