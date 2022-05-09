package com.example.mypet.User;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class EditProfile extends AppCompatActivity {


    EditText editText_name, editText_email, editText_password,  editText_sex,  editText_phone;

    Button button_cancel, button_save;

    String user_id;
    String editText_name_txt, editText_email_txt, editText_password_txt,  editText_sex_txt,  editText_phone_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        editText_name = findViewById(R.id.editText_name);
        editText_email = findViewById(R.id.editText_email);
        editText_password = findViewById(R.id.editText_password);
        editText_sex = findViewById(R.id.editText_sex);
        editText_phone = findViewById(R.id.editText_phone);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_id = extras.getString("user_id");


            getuserinfo();


        }

        button_save = findViewById(R.id.button_save);
        button_cancel = findViewById(R.id.button_cancel);


        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editText_name_txt = editText_name.getText().toString().trim();
                editText_email_txt = editText_email.getText().toString().trim();
                editText_password_txt = editText_password.getText().toString().trim();


                final String REQUEST_URL = Var.update_user_info;

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
                                        Toast.makeText(getApplicationContext(), editText_name_txt+" profile edited successfully", Toast.LENGTH_LONG).show();

                                        finish();

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

                        MyData.put("user_id", user_id);
                        MyData.put("name", editText_name_txt);
                        MyData.put("email", editText_email_txt);
                        MyData.put("password", editText_password_txt);


                        return MyData;
                    }
                };
                requestQueue.add(jsonRequest);


            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getuserinfo() {


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
                                editText_email_txt = data.getString("email");
                                editText_password_txt = data.getString("password");

                                editText_sex_txt = data.getString("sex");

                                editText_phone_txt = data.getString("phone");

                                editText_name.setText(editText_name_txt);
                                editText_email.setText(editText_email_txt);
                                editText_password.setText(editText_password_txt);


                                editText_sex.setText(editText_sex_txt);
                                editText_phone.setText(editText_phone_txt);

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

                MyData.put("user_id", user_id);


                return MyData;
            }
        };
        requestQueue.add(jsonRequest);


    }
}
