package com.example.mypet.Admin.PetPlaces;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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

public class ManageAccessoriesShop extends AppCompatActivity {

    String type;

    Button add_accessories_shop;
    LinearLayout accessories_shop_list;



    @Override
    public void onRestart() {
        super.onRestart();
        accessories_shop_list.removeAllViews();
        getAccessoriesShoplist();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_accessories_shop);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            type = extras.getString("type");
        }
        add_accessories_shop = findViewById(R.id.add_accessories_shop);
        accessories_shop_list = findViewById(R.id.accessories_shop_list);


        add_accessories_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), AddPlace.class);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });

        getAccessoriesShoplist();

    }


    private void getAccessoriesShoplist() {
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
                                ShowAccessoriesShoplist(data.getJSONObject(i));


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

                MyData.put("type", type);
                return MyData;
            }
        };
        requestQueue.add(jsonRequest);

    }


    private void ShowAccessoriesShoplist(JSONObject data) throws JSONException {


        String id = data.getString("place_id");

        View view = getLayoutInflater().inflate(R.layout.admin_blog_list_card, null);
        String title_txt = data.getString("name");
        // String description_txt = data.getString("description");

        ImageView imageView2 = view.findViewById(R.id.imageView2);

        imageView2.setImageLevel(R.drawable.a17);

        TextView title = view.findViewById(R.id.title);


        title.setText(title_txt);


        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog(ManageAccessoriesShop.this, id);
            }
        });

        accessories_shop_list.addView(view);


    }

    public void showDialog(Activity activity, String id) {

        View view = getLayoutInflater().inflate(R.layout.option, null);


        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(view);

        TextView edit = (TextView) dialog.findViewById(R.id.edit);
        TextView delete = (TextView) dialog.findViewById(R.id.delete);
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                Intent intent = new Intent(getApplicationContext(), EditPlace.class);
                intent.putExtra("place_id",id);
                startActivity(intent);

            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String REQUEST_URL = Var.delete_place;

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest jsonRequest = new StringRequest(
                        Request.Method.POST,
                        REQUEST_URL,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                Toast.makeText(ManageAccessoriesShop.this, "Accessories Shop deleted successfully", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
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
                        MyData.put("place_id", id);

                        return MyData;
                    }
                };
                requestQueue.add(jsonRequest);

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });


        dialog.show();

    }


}