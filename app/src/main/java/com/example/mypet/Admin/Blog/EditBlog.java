package com.example.mypet.Admin.Blog;

import android.os.Bundle;
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
import com.example.mypet.R;
import com.example.mypet.Var;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditBlog extends AppCompatActivity {

    EditText editText_title, editText_description;
    Button save_blog, cancel_blog;

    String blog_id = "", title_txt = "", description_txt = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_blog);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            blog_id = extras.getString("blog_id");
            title_txt = extras.getString("title_txt");
            description_txt = extras.getString("description_txt");

        }
        editText_title = findViewById(R.id.editText_title);
        editText_description = findViewById(R.id.editText_description);
        save_blog = findViewById(R.id.save_blog);
        cancel_blog = findViewById(R.id.cancel_blog);
        editText_title.setText(title_txt);
        editText_description.setText(description_txt);

        cancel_blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        save_blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title_txt = editText_title.getText().toString().trim();
                description_txt = editText_description.getText().toString().trim();


                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String date = df.format(c);
                boolean flag = true;

                if (title_txt.equals("")) {
                    editText_title.setError("This field is required");
                    flag = false;
                    return;
                }
                if (description_txt.equals("")) {
                    editText_description.setError("This field is required");
                    flag = false;

                    return;
                }

                if (!flag)
                    return;

                final String REQUEST_URL = Var.update_blog;
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest jsonRequest = new StringRequest(
                        Request.Method.POST,
                        REQUEST_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    Log.d("response", obj.getString("code"));
                                    if (obj.getString("code").equals("-1")) {
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Blog edited successfully", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show(); } }
                        },
                        new Response.ErrorListener() {@Override public void onErrorResponse(VolleyError error) { }}
                ) {
                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        int mStatusCode = response.statusCode;
                        Log.d("mStatusCode", String.valueOf(mStatusCode));


                        return super.parseNetworkResponse(response);

                    }

                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<String, String>();

                        MyData.put("name", title_txt);
                        MyData.put("description", description_txt);
                        MyData.put("blog_id", blog_id);
                        MyData.put("update_date", date);


                        return MyData;
                    }
                };
                requestQueue.add(jsonRequest);


            }
        });

    }
}
