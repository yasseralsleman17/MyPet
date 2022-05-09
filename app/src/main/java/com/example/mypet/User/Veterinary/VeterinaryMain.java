package com.example.mypet.User.Veterinary;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class VeterinaryMain extends AppCompatActivity {

    String id_txt ;

    LinearLayout veterinary_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veterinary_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_txt = extras.getString("id_txt");

        }
        veterinary_list = findViewById(R.id.veterinary_list);


        getVeterinaryList();


    }
    private void getVeterinaryList() {
        final String REQUEST_URL = Var.get_veterinaries;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject obj = new JSONObject(response);

                            JSONArray data = obj.getJSONArray("veterinaries");
                            for (int i = 0; i < data.length(); i++) {
                                show_Veterinary_list(data.getJSONObject(i));


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


                return MyData;
            }
        };
        requestQueue.add(jsonRequest);

    }


    private void show_Veterinary_list(JSONObject jsonObject) throws JSONException {

        String veterinary_id = jsonObject.getString("veterinary_id");
        View view = getLayoutInflater().inflate(R.layout.blog_list_item_card, null);


        TextView title = view.findViewById(R.id.title);

        ImageView imageView2 = view.findViewById(R.id.imageView2);

        imageView2.setImageLevel(R.drawable.im8);
        title.setText(jsonObject.getString("veterinary_name"));


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), VeterinaryDetails.class);
                i.putExtra("veterinary_id", veterinary_id);
                i.putExtra("user_id", id_txt);
                startActivity(i);
            }
        });

        veterinary_list.addView(view);


    }

}