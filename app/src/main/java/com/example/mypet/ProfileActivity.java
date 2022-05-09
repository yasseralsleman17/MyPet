package com.example.mypet;

import android.content.Intent;
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
import com.example.mypet.User.UserHomePage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {


    private EditText editText_sex, editText_phone;
    String sex_txt, phone_txt, id_txt;

    private Button button_save, button_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_txt = extras.getString("id_txt");
        }


        editText_sex = (EditText) findViewById(R.id.editText_sex);
        editText_phone = (EditText) findViewById(R.id.editText_phone);

        button_cancel = (Button) findViewById(R.id.button_cancel);

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
            }
        });

        button_save = (Button) findViewById(R.id.button_save2);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                sex_txt = editText_sex.getText().toString();

                phone_txt = editText_phone.getText().toString();


                if (sex_txt.isEmpty()) {
                    sex_txt = " ";
                }

                if (phone_txt.isEmpty()) {
                    phone_txt = " ";
                }

                final String REQUEST_URL = Var.Moreinfo;

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest jsonRequest = new StringRequest(
                        Request.Method.POST,
                        REQUEST_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("response", response);
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    Log.d("response", obj.getString("code"));
                                    if (obj.getString("code").equals("-1")) {
                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                    } else {


                                        Intent i = new Intent(getApplicationContext(), UserHomePage.class);
                                        i.putExtra("id_txt", id_txt);
                                        startActivity(i);

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
                        MyData.put("sex", sex_txt);
                        MyData.put("phone", phone_txt);
                        MyData.put("id", id_txt);


                        return MyData;
                    }
                };
                requestQueue.add(jsonRequest);


            }
        });


    }


}