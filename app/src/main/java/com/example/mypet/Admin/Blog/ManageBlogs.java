package com.example.mypet.Admin.Blog;

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

public class ManageBlogs extends AppCompatActivity {

    Button add_blog;
    LinearLayout blogs_list;


    @Override
    public void onRestart() {
        super.onRestart();
        blogs_list.removeAllViews();
        getBlogslist();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_blogs);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        add_blog = findViewById(R.id.add_blog);
        blogs_list = findViewById(R.id.blogs_list);

        add_blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddBlog.class));
            }
        });

        getBlogslist();

    }


    private void getBlogslist() {
        final String REQUEST_URL = Var.get_all_blogs;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject obj = new JSONObject(response);

                            JSONArray data = obj.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                showtBlog_list(data.getJSONObject(i));


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


                return MyData;
            }
        };
        requestQueue.add(jsonRequest);

    }


    private void showtBlog_list(JSONObject data) throws JSONException {


        String id = data.getString("id");

        View view = getLayoutInflater().inflate(R.layout.admin_blog_list_card, null);
        String title_txt = data.getString("name");
        String description_txt = data.getString("description");

        TextView title = view.findViewById(R.id.title);


        title.setText(title_txt);


        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog(ManageBlogs.this, id, title_txt, description_txt);
            }
        });

        blogs_list.addView(view);


    }

    public void showDialog(Activity activity, String id, String title_txt, String description_txt) {

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

                Intent intent = new Intent(getApplicationContext(), EditBlog.class);
                intent.putExtra("blog_id", id);
                intent.putExtra("title_txt", title_txt);
                intent.putExtra("description_txt", description_txt);
                startActivity(intent);

            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String REQUEST_URL = Var.delete_blog;

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest jsonRequest = new StringRequest(
                        Request.Method.POST,
                        REQUEST_URL,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                Toast.makeText(ManageBlogs.this, "Blog deleted successfully", Toast.LENGTH_SHORT).show();
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
                        MyData.put("blog_id", id);

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