package com.example.mypet.User;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends AppCompatActivity {

    private ImageView send, img;
    TextView name;
    EditText msg;
    LinearLayout chat;
    String receiver_id, sender_id;
    String my_message;
    String receiver_name;

    Timer time = new Timer();

    @Override
    protected void onStop() {

        time.cancel();
        super.onStop();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        name = findViewById(R.id.textView_name);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            receiver_name = extras.getString("receiver_name");
            receiver_id = extras.getString("receiver_id");
            sender_id = extras.getString("sender_id");
            name.setText(receiver_name);

        }


        chat = findViewById(R.id.chat);
        msg = findViewById(R.id.editTextText_msg);
        send = findViewById(R.id.imageView_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_message = msg.getText().toString();

                if (!my_message.isEmpty()) {

                    send_my_message(my_message);

                    msg.setText("");
                }
            }
        });

        img = findViewById(R.id.imageView_img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        getallmessage();


    }


    private void getallmessage() {


        final String REQUEST_URL = Var.get_all_messages;


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject chat = new JSONObject(response);


                            JSONArray obj = chat.getJSONArray("data");
                            Log.d("response", String.valueOf(obj));

                            for (int i = 0; i < obj.length(); i++) {
                                if (obj.getJSONObject(i).getString("code").equals("1"))
                                    show_send(obj.getJSONObject(i));
                                else if (obj.getJSONObject(i).getString("code").equals("2"))
                                    show_receive(obj.getJSONObject(i));
                            }
                            time.scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {
                                    getmessage();
                                }
                            }, 0, 2000);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                Log.d("mStatusCode", String.valueOf(mStatusCode));


                return super.parseNetworkResponse(response);

            }

            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();

                MyData.put("first_user", sender_id);
                MyData.put("second_user", receiver_id);


                return MyData;
            }

        };
        requestQueue.add(jsonRequest);


    }

    private void getmessage() {

        final String REQUEST_URL = Var.get_unseen_messages;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray obj = new JSONArray(response);
                            for (int i = 0; i < obj.length(); i++) {
                                show_receive(obj.getJSONObject(i));
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

                }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                Log.d("mStatusCode", String.valueOf(mStatusCode));


                return super.parseNetworkResponse(response);

            }

            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();

                MyData.put("login_user", sender_id);
                MyData.put("other_user", receiver_id);


                return MyData;
            }

        };
        requestQueue.add(jsonRequest);


    }


    private void send_my_message(String mymessage) {

        View view = getLayoutInflater().inflate(R.layout.send_message_card, null);
        TextView message_body = view.findViewById(R.id.message_body);
        message_body.setText(mymessage);
        final String REQUEST_URL = Var.send_message;
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
                }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                Log.d("mStatusCode", String.valueOf(mStatusCode));

                return super.parseNetworkResponse(response);

            }

            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();

                MyData.put("sender_id", sender_id);
                MyData.put("reciver_id", receiver_id);
                MyData.put("content", mymessage);


                return MyData;
            }
        };
        requestQueue.add(jsonRequest);

        chat.addView(view);


    }


    private void show_send(JSONObject jsonObject) throws JSONException {


        View view = getLayoutInflater().inflate(R.layout.send_message_card, null);


        TextView message_body = view.findViewById(R.id.message_body);


        message_body.setText(jsonObject.getString("message"));


        chat.addView(view);


    }

    private void show_receive(JSONObject jsonObject) throws JSONException {


        View view = getLayoutInflater().inflate(R.layout.receive_message_card, null);


        TextView message_body = view.findViewById(R.id.message_body);


        message_body.setText(jsonObject.getString("message"));


        chat.addView(view);


    }


}