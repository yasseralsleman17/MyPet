package com.example.mypet.User.Veterinary;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.Glide;
import com.example.mypet.R;
import com.example.mypet.Var;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VeterinaryPictures extends AppCompatActivity {

    TextView veterinary_title;
    String veterinary_id, user_id;
    ImageView pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veterinary_pictures);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            veterinary_id = extras.getString("veterinary_id");
            user_id = extras.getString("user_id");
        }

        pic = findViewById(R.id.pic);

        veterinary_title = findViewById(R.id.veterinary_title);
        get_veterinary_images();

    }

    private void get_veterinary_images() {


        final String REQUEST_URL = Var.get_veterinary_images;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("code").equals("1")) {
                                showveterinary(obj);
                                veterinary_title.setText(obj.getString("veterinary_name"));
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

                MyData.put("veterinary_id", veterinary_id);


                return MyData;
            }
        };
        requestQueue.add(jsonRequest);


    }

    private void showveterinary(JSONObject data) throws JSONException {

        JSONArray images = data.getJSONArray("images");

        JSONObject image;
        for (int i = 0; i < images.length(); i++) {
            image = (JSONObject) images.get(i);

            String download_URL = Var.images_url + image.getString("image");

            Glide.with(getApplicationContext())
                    .load(download_URL)
                    .into(pic);


        }


    }
}