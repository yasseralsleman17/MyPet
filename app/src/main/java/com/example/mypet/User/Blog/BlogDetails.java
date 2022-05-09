package com.example.mypet.User.Blog;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BlogDetails extends AppCompatActivity {

    TextView blog_title, created, edited, description;
    RatingBar ratingBar;
    LinearLayout linear_comment;
    ImageView add_comment;
    EditText editText_comment;
    String blog_id, user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            blog_id = extras.getString("blog_id");
            user_id = extras.getString("user_id");
        }

        linear_comment = findViewById(R.id.linear_comment);

        ratingBar = findViewById(R.id.ratingBar);

        blog_title = findViewById(R.id.blog_title);
        created = findViewById(R.id.created);
        edited = findViewById(R.id.edited);
        description = findViewById(R.id.description);

        add_comment = findViewById(R.id.add_comment);
        editText_comment = findViewById(R.id.editText_comment);

        add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment_txt = editText_comment.getText().toString().trim();

                Date c = Calendar.getInstance().getTime();

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                SimpleDateFormat tf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String comment_date = df.format(c);
                String comment_time = tf.format(c);

                if (comment_txt.equals("")) return;

                final String REQUEST_URL = Var.add_comment;

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest jsonRequest = new StringRequest(
                        Request.Method.POST,
                        REQUEST_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                            }
                        },
                        new Response.ErrorListener() {@Override public void onErrorResponse(VolleyError error) { }})
                {
                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        int mStatusCode = response.statusCode;
                        Log.d("mStatusCode", String.valueOf(mStatusCode));


                        return super.parseNetworkResponse(response);

                    }

                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<String, String>();

                        MyData.put("blog_id", blog_id);
                        MyData.put("user_id", user_id);
                        MyData.put("comment", comment_txt);
                        MyData.put("comment_date", comment_date);
                        MyData.put("comment_time", comment_time);


                        return MyData;
                    }
                };
                requestQueue.add(jsonRequest);


            }
        });


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                String rating_txt = String.valueOf(ratingBar.getProgress());
                addrating(rating_txt);


            }
        });
        getBloginfo();

    }

    private void addrating(String rate) {


        final String REQUEST_URL = Var.rate_blog;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {



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

                MyData.put("blog_id", blog_id);
                MyData.put("user_id", user_id);
                MyData.put("rate", rate);



                return MyData;
            }
        };
        requestQueue.add(jsonRequest);



    }


    private void getBloginfo() {
        final String REQUEST_URL = Var.get_blog_comments;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject obj = new JSONObject(response);

                            if (obj.getString("code").equals("1"))
                                showBlog(obj);
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

                MyData.put("blog_id", blog_id);


                return MyData;
            }
        };
        requestQueue.add(jsonRequest);

    }


    private void showBlog(JSONObject data) throws JSONException {


        created.setText(data.getString("create_date"));
        blog_title.setText(data.getString("blog_name"));
        edited.setText(data.getString("update_date"));
        description.setText(data.getString("description"));


        JSONArray comments = data.getJSONArray("comments");
        JSONObject comment;
        for (int i = 0; i < comments.length(); i++) {
            comment = (JSONObject) comments.get(i);
            viewComment(comment);

        }

    }

    private void viewComment(JSONObject comment) throws JSONException {
        View view = getLayoutInflater().inflate(R.layout.blog_comment_card, null);


        TextView tv_name = view.findViewById(R.id.name);
        TextView tv_blog_date = view.findViewById(R.id.blog_date);
        TextView tv_comment = view.findViewById(R.id.comment);


        tv_name.setText(comment.getString("user_name"));
        tv_blog_date.setText(comment.getString("comment_time") + "  " + comment.getString("comment_date"));
        tv_comment.setText(comment.getString("comment"));


        linear_comment.addView(view);

    }


}