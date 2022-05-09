package com.example.mypet.User.Veterinary;

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

public class VeterinaryFeedback extends AppCompatActivity {


    TextView feedback_title;
    RatingBar ratingBar;
    LinearLayout linear_feedback;
    EditText editText_feedback;
    ImageView add_feedback;
    String veterinary_id, user_id;

    String feedback_title_txt="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veterinary_feedback);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            veterinary_id = extras.getString("veterinary_id");
            user_id = extras.getString("user_id");
            feedback_title_txt = extras.getString("feedback_title");
        }
        ratingBar=findViewById(R.id.ratingBar);
        linear_feedback=findViewById(R.id.linear_feedback);
        feedback_title=findViewById(R.id.feedback_title);
        editText_feedback=findViewById(R.id.editText_feedback);
        add_feedback=findViewById(R.id.add_feedback);
        add_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String rating = String.valueOf(ratingBar.getProgress());

                String comment_txt = editText_feedback.getText().toString().trim();

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                SimpleDateFormat tf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String comment_date = df.format(c);
                String comment_time = tf.format(c);

                if (comment_txt.equals("")) return;
                final String REQUEST_URL = Var.rate_veterinary;
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest jsonRequest = new StringRequest(
                        Request.Method.POST, REQUEST_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0); }},
                        new Response.ErrorListener() {@Override                         public void onErrorResponse(VolleyError error) { }}
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
                        MyData.put("user_id", user_id);
                        MyData.put("rate", rating);
                        MyData.put("comment", comment_txt);
                        MyData.put("date", comment_date);
                        MyData.put("time", comment_time);
                        return MyData;
                    }
                };
                requestQueue.add(jsonRequest);



            }
        });
        feedback_title.setText("Feedbacks on "+feedback_title_txt+" veterinary clinic");
        get_veterinary_rate();


    }

    private void get_veterinary_rate() {

        final String REQUEST_URL = Var.get_veterinary_rate;

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
                                showveterinary(obj);
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


        JSONArray comments = data.getJSONArray("rate_info");
        JSONObject comment;
        for (int i = 0; i < comments.length(); i++) {
            comment = (JSONObject) comments.get(i);
            viewComment(comment);

        }

    }

    private void viewComment(JSONObject data) throws JSONException {
        View view = getLayoutInflater().inflate(R.layout.feedback_rate_card, null);


        TextView tv_name = view.findViewById(R.id.name);
        TextView tv_date = view.findViewById(R.id.date);
        TextView tv_comment = view.findViewById(R.id.comment);
        RatingBar ratingBar=view.findViewById(R.id.ratingBar);
        ratingBar.setRating(Float.valueOf(data.getString("rate")));
        tv_name.setText(data.getString("user_name"));
        tv_date.setText(data.getString("time") + "  " + data.getString("date"));
        tv_comment.setText(data.getString("comment"));


        linear_feedback.addView(view);

    }


}