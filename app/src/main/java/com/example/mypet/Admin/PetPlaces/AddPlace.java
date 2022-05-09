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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddPlace extends AppCompatActivity {

    String type;

    EditText editText_name, editText_description, editText_branches, editText_phone;
    TextView tv_store_type;
    Button save_place, cancel_place;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            type = extras.getString("type");


        }

        tv_store_type = findViewById(R.id.tv_store_type);
        tv_store_type.setText(type);

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
                String name = editText_name.getText().toString().trim();
                String description = editText_description.getText().toString().trim();
                String phone = editText_phone.getText().toString().trim();
                String branches = editText_branches.getText().toString().trim();


                boolean flag = true;

                if (name.equals("")) {
                    editText_name.setError("This field is required");
                    flag = false;
                    return;
                }
                if (description.equals("")) {
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
                final String REQUEST_URL = Var.add_place;

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
                                        Toast.makeText(getApplicationContext(), type + " added successfully", Toast.LENGTH_LONG).show();
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

                        MyData.put("type", type);
                        MyData.put("name", name);
                        MyData.put("description", description);
                        MyData.put("phone", phone);
                        MyData.put("branches", branches);


                        return MyData;
                    }
                };
                requestQueue.add(jsonRequest);


            }
        });


    }
}