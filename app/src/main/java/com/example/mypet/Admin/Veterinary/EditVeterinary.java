package com.example.mypet.Admin.Veterinary;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditVeterinary extends AppCompatActivity {

    EditText editText_name, editText_description, editText_branches, editText_phone;
    TextView add_another_branch;
    Button save_veterinary, cancel_veterinary;

    LinearLayout linearLayout;

    Uri imuri;

    String encodedImage = "";
    Bitmap bitmap;
    RequestQueue rQueue;
    JSONObject jsonObject;
    String veterinary_name = "", veterinary_description = "", phone = "", branches = "";

    String veterinary_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_veterinary);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            veterinary_id = extras.getString("veterinary_id");

        }

        get_veterinary_info();


        add_another_branch = findViewById(R.id.add_another_branch);

        add_another_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final AlertDialog.Builder popDialog = new AlertDialog.Builder(EditVeterinary.this);

                LinearLayout linearLayout = new LinearLayout(EditVeterinary.this);
                EditText comment = new EditText(EditVeterinary.this);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                lp.setMargins(10, 10, 10, 10);
                comment.setLayoutParams(lp);

                linearLayout.addView(comment);
                popDialog.setTitle("Add another branch ");
                popDialog.setView(linearLayout);
                popDialog.setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String comment_txt = comment.getText().toString();
                                if (!comment_txt.equals(""))

                                    branches = branches + "," + comment_txt;

                                editText_branches.setText(branches);

                                dialog.dismiss();
                            }
                        }).setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                popDialog.create();
                popDialog.show();

            }
        });

        editText_name = findViewById(R.id.editText_name);
        editText_description = findViewById(R.id.editText_description);
        editText_branches = findViewById(R.id.editText_branches);
        editText_phone = findViewById(R.id.editText_phone);


        save_veterinary = findViewById(R.id.save_veterinary);
        cancel_veterinary = findViewById(R.id.cancel_veterinary);


        cancel_veterinary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        save_veterinary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText_name.getText().toString().trim();
                String description = editText_description.getText().toString().trim();
                String phone = editText_phone.getText().toString().trim();
                String branches = editText_branches.getText().toString().trim();


                boolean flag = true;

                if (name.equals("")) {
                    editText_name.setError("This field is required");
                    flag = false;
                }
                if (description.equals("")) {
                    editText_description.setError("This field is required");
                    flag = false;
                }
                if (phone.equals("")) {
                    editText_phone.setError("This field is required");
                    flag = false;
                }
                if (branches.equals("")) {
                    editText_branches.setError("This field is required");
                    flag = false;
                }
                if (!flag)
                    return;

                final String REQUEST_URL = Var.update_veterinary;
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
                                        Toast.makeText(getApplicationContext(), veterinary_name + " updated added successfully", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                } catch (JSONException e) { } }},
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

                        MyData.put("veterinary_id", veterinary_id);
                        MyData.put("name", veterinary_name);
                        MyData.put("description", veterinary_description);
                        MyData.put("phone", phone);
                        MyData.put("branches", branches);


                        return MyData;
                    }
                };
                requestQueue.add(jsonRequest);


            }
        });


    }

    private void openfilechooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imuri = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imuri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                String coded = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

                encodedImage = coded;


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private void get_veterinary_info() {
        final String REQUEST_URL = Var.get_veterinary_info;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject data = new JSONObject(response);

                            veterinary_name = data.getString("veterinary_name");
                            veterinary_description = data.getString("veterinary_description");
                            phone = data.getString("phone");
                            JSONArray br = data.getJSONArray("branches");
                            for (int i = 0; i < br.length(); i++) {
                                JSONObject b = (JSONObject) br.get(i);
                                if (i == 0)
                                    branches = b.getString("branch_name");
                                else
                                    branches = branches + "," + b.getString("branch_name");

                            }

                            editText_name.setText(veterinary_name);
                            editText_description.setText(veterinary_description);
                            editText_branches.setText(branches);
                            editText_phone.setText(phone);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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


}

