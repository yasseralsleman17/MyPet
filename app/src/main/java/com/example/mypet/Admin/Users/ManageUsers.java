package com.example.mypet.Admin.Users;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.mypet.Admin.Blog.ManageBlogs;
import com.example.mypet.R;
import com.example.mypet.User.EditProfile;
import com.example.mypet.User.pet.AddPet;
import com.example.mypet.Var;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ManageUsers extends AppCompatActivity {


    GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        gridLayout = findViewById(R.id.gridLayout);
        getusers();

    }

    private void getusers() {

        final String REQUEST_URL = Var.get_all_users;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray data = obj.getJSONArray("users");
                            for (int i = 0; i < data.length(); i++) {
                                showtuser_list(data.getJSONObject(i));
                            }
                        } catch (JSONException e) { e.printStackTrace(); } }},
                new Response.ErrorListener() {@Override public void onErrorResponse(VolleyError error) { }}

        ) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                Log.d("mStatusCode", String.valueOf(mStatusCode));


                return super.parseNetworkResponse(response);

            }

            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();


                return MyData;
            }
        };
        requestQueue.add(jsonRequest);


    }

    private void showtuser_list(JSONObject data) throws JSONException {
        String user_id = data.getString("user_id");
        View view = getLayoutInflater().inflate(R.layout.user_card, null);
        String name_txt = data.getString("user_name");
        TextView name = view.findViewById(R.id.name);
        name.setText(name_txt);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), EditProfile.class);
                i.putExtra("user_id", user_id);

                startActivity(i);
            }
        });
        gridLayout.addView(view);

    }
}