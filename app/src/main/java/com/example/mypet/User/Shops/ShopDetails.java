package com.example.mypet.User.Shops;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mypet.R;
import com.example.mypet.Var;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ShopDetails extends AppCompatActivity {
    TextView store_title,     store_type,      store_description,store_branches,Store_phone_number;
    String   place_type = "", place_name = "", place_description = "", phone = "", branches = "";

    String place_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            place_id = extras.getString("place_id");
        }

        get_place_info();

        store_title=findViewById(R.id.store_title);
        store_type=findViewById(R.id.store_type);
        store_description=findViewById(R.id.store_description);
        store_branches=findViewById(R.id.store_branches);
        Store_phone_number=findViewById(R.id.Store_phone_number);

    }

    private void get_place_info() {
        final String REQUEST_URL = Var.get_place_info;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject data = new JSONObject(response);

                            place_type = data.getString("place_type");
                            place_name = data.getString("place_name");
                            place_description = data.getString("place_description");
                            phone = data.getString("phone");
                            JSONArray br = data.getJSONArray("branches");
                            for (int i = 0; i < br.length(); i++) {
                                JSONObject b = (JSONObject) br.get(i);
                                branches = branches  + b.getString("branch_name")+ "\n";

                            }

                            store_type.setText(place_type);
                            store_title.setText(place_name);
                            store_description.setText(place_description);
                            store_branches.setText(branches);
                            Store_phone_number.setText(phone);

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

                MyData.put("place_id", place_id);
                return MyData;
            }
        };
        requestQueue.add(jsonRequest);


    }



}