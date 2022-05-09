package com.example.mypet.Admin.PetPlaces;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

public class EditPlace extends AppCompatActivity {

    String place_id;

    EditText editText_name, editText_description, editText_branches, editText_phone;
    TextView tv_store_type;
    Button save_place, cancel_place;

    String place_type = "", place_name = "", place_description = "", phone = "", branches = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_place);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            place_id = extras.getString("place_id");

        }

        get_place_info();

        tv_store_type = findViewById(R.id.tv_store_type);

        editText_name = findViewById(R.id.editText_name);
        editText_description = findViewById(R.id.editText_description);
        editText_branches = findViewById(R.id.editText_branches);
        editText_phone = findViewById(R.id.editText_phone);


        save_place = findViewById(R.id.save_place);
        cancel_place = findViewById(R.id.cancel_place);


        cancel_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        save_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                place_name = editText_name.getText().toString().trim();
                place_description = editText_description.getText().toString().trim();
                phone = editText_phone.getText().toString().trim();
                branches = editText_branches.getText().toString().trim();


                boolean flag = true;

                if (place_name.equals("")) {
                    editText_name.setError("This field is required");
                    flag = false;
                    return;
                }
                if (place_description.equals("")) {
                    editText_description.setError("This field is required");
                    flag = false;

                    return;
                }

                if (phone.equals("")) {
                    editText_phone.setError("This field is required");
                    flag = false;
                    return;
                }
                if (branches.equals("")) {
                    editText_branches.setError("This field is required");
                    flag = false;

                    return;
                }

                if (!flag)
                    return;


                final String REQUEST_URL = Var.update_place;

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
                                        Toast.makeText(getApplicationContext(), place_name + " updated added successfully", Toast.LENGTH_LONG).show();
                                        finish();
                                    } } catch (JSONException e) { } }},

                        new Response.ErrorListener() {@Override public void onErrorResponse(VolleyError error) { }
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
                        MyData.put("name", place_name);
                        MyData.put("description", place_description);
                        MyData.put("phone", phone);
                        MyData.put("branches", branches);


                        return MyData;
                    }
                };
                requestQueue.add(jsonRequest);


            }
        });


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
                                if (i == 0) branches = b.getString("branch_name");
                                else
                                    branches = branches + "," + b.getString("branch_name");

                            }

                            tv_store_type.setText(place_type);
                            editText_name.setText(place_name);
                            editText_description.setText(place_description);
                            editText_branches.setText(branches);
                            editText_phone.setText(phone);

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
