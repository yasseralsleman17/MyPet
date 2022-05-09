package com.example.mypet.User.Shops;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.mypet.R;
import com.example.mypet.Var;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FoodShop extends AppCompatActivity {

    LinearLayout food_shop;

    String id_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_shop);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        food_shop = findViewById(R.id.food_shop);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_txt = extras.getString("id_txt");
        }

        getFoodShop();
    }


    private void getFoodShop() {
        final String REQUEST_URL = Var.get_places;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject obj = new JSONObject(response);

                            JSONArray data = obj.getJSONArray("places");
                            for (int i = 0; i < data.length(); i++) {
                                showShopList(data.getJSONObject(i));


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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

                MyData.put("type", "food shops");


                return MyData;
            }
        };
        requestQueue.add(jsonRequest);

    }


    private void showShopList(JSONObject data) throws JSONException {

        String id = data.getString("place_id");
        String title_txt = data.getString("name");

        View view = getLayoutInflater().inflate(R.layout.shop_item_list_card, null);


        TextView title = view.findViewById(R.id.title);

        ImageView imageView2 = view.findViewById(R.id.imageView2);

        imageView2.setImageLevel(R.drawable.a16);
        title.setText(title_txt);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ShopDetails.class);
                i.putExtra("place_id", id);
                startActivity(i);
            }
        });

        food_shop.addView(view);


    }


}