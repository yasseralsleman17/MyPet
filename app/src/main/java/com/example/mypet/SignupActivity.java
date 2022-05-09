package com.example.mypet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {


    private EditText editText_name, editText_email, editText_password, editText_confirm_password;
    String name_txt, email_txt, password_txt, confirm_password_txt;

    private Button button_signup, button_to_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        editText_name = (EditText) findViewById(R.id.editText_name);
        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_password = (EditText) findViewById(R.id.editText_password);
        editText_confirm_password = (EditText) findViewById(R.id.editText_confirm_password);

        button_to_signup = (Button) findViewById(R.id.button_to_signup);

        button_to_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SigninActivity.class));
            }
        });

        button_signup = (Button) findViewById(R.id.button_signup);
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                name_txt = editText_name.getText().toString();
                email_txt = editText_email.getText().toString();
                password_txt = editText_password.getText().toString();
                confirm_password_txt = editText_confirm_password.getText().toString();


                boolean flag = true;

                if (name_txt.isEmpty()) {
                    editText_name.setError("This field is required");
                    flag = false;
                }


                if (password_txt.isEmpty()) {
                    editText_password.setError("This field is required");
                    flag = false;
                }

                if (!isValidPassword(password_txt)) {
                    editText_password.setError("invalid password");
                    flag = false;
                }
              if (!password_txt.equals(confirm_password_txt)) {
                    editText_confirm_password.setError("passwords must match");
                    editText_password.setError("passwords must match");
                    flag = false;
                }

                if (email_txt.isEmpty()) {
                    editText_email.setError("This field is required");
                    flag = false;
                }
                if (!email_txt.matches(emailPattern)) {
                    editText_email.setError("Invalid email address");
                    flag = false;
                }


                if (!flag) {
                    return;
                }

                final String REQUEST_URL = Var.Signup_url;

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
                                        JSONObject data = obj.getJSONObject("data");
                                        String id_txt = data.getString("id");
                                        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                                        i.putExtra("id_txt", id_txt);
                                        startActivity(i);


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

                        MyData.put("name", name_txt);
                        MyData.put("email", email_txt);
                        MyData.put("password", password_txt);
                        MyData.put("user_type", "1");


                        return MyData;
                    }
                };
                requestQueue.add(jsonRequest);


            }
        });


    }


    private boolean isValidPassword(String password_txt) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile(
                "[a-zA-Z0-9]{2,24}");

        return !TextUtils.isEmpty(password_txt) && PASSWORD_PATTERN.matcher(password_txt).matches();
    }

}