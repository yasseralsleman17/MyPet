package com.example.mypet.User.Veterinary;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
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

public class VeterinaryDetails extends AppCompatActivity {
    TextView veterinary_title, veterinary_description, veterinary_branches, veterinary_phone_number, veterinary_view_pic, veterinary_view_feedback;

    String veterinary_id,user_id;
    String name = "", description = "", phone = "", branches = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veterinary_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            veterinary_id = extras.getString("veterinary_id");
            user_id = extras.getString("user_id");
        }

        getveterinary();
        veterinary_title = findViewById(R.id.veterinary_title);
        veterinary_description = findViewById(R.id.veterinary_description);
        veterinary_branches = findViewById(R.id.veterinary_branches);
        veterinary_phone_number = findViewById(R.id.veterinary_phone_number);
        veterinary_view_pic = findViewById(R.id.veterinary_view_pic);
        veterinary_view_feedback = findViewById(R.id.veterinary_view_feedback);

        SpannableString content = new SpannableString(veterinary_view_pic.getText());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        veterinary_view_pic.setText(content);
        veterinary_view_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), VeterinaryPictures.class);
                i.putExtra("veterinary_id", veterinary_id);
                i.putExtra("user_id", user_id);
                i.putExtra("feedback_title", name);
                startActivity(i);
            }
        });


        SpannableString content2 = new SpannableString(veterinary_view_feedback.getText());
        content2.setSpan(new UnderlineSpan(), 0, content2.length(), 0);
        veterinary_view_feedback.setText(content2);
        veterinary_view_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), VeterinaryFeedback.class);
                i.putExtra("veterinary_id", veterinary_id);
                i.putExtra("user_id", user_id);
                i.putExtra("feedback_title", name);
                startActivity(i);
            }
        });


    }

    private void getveterinary() {


        final String REQUEST_URL = Var.get_veterinary_info;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject data = new JSONObject(response);

                            name = data.getString("veterinary_name");
                            description = data.getString("veterinary_description");
                            phone = data.getString("phone");
                            JSONArray br = data.getJSONArray("branches");
                            for (int i = 0; i < br.length(); i++) {
                                JSONObject b = (JSONObject) br.get(i);
                                branches = branches + b.getString("branch_name") + "\n";

                            }

                            veterinary_title.setText(name);
                            veterinary_description.setText(description);
                            veterinary_branches.setText(branches);
                            veterinary_phone_number.setText(phone);

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

                MyData.put("veterinary_id", veterinary_id);
                return MyData;
            }
        };
        requestQueue.add(jsonRequest);


    }
}