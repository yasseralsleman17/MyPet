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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mypet.R;
import com.example.mypet.Var;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddVeterinary extends AppCompatActivity {


    EditText editText_name, editText_description, editText_branches, editText_phone;
    TextView add_pic, add_another_branch;
    Button save_veterinary, cancel_veterinary;


    String branches = "";
    LinearLayout linearLayout;

    Uri imuri;

    String encodedImage = "";
    Bitmap bitmap;
    RequestQueue rQueue;
    JSONObject jsonObject;
    int image_index = 0;
    ImageView v_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_veterinary);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();



        v_image = findViewById(R.id.v_image);
        linearLayout = findViewById(R.id.linearLayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openfilechooser();

            }
        });

        add_pic = findViewById(R.id.add_pic);
        add_another_branch = findViewById(R.id.add_another_branch);
        add_another_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder popDialog = new AlertDialog.Builder(AddVeterinary.this);

                LinearLayout linearLayout = new LinearLayout(AddVeterinary.this);
                EditText comment = new EditText(AddVeterinary.this);

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
                                    if (branches.equals(""))

                                        branches = comment_txt;

                              else
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

        add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openfilechooser();
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


                final String REQUEST_URL = Var.add_veterinary;


                try {
                    jsonObject = new JSONObject();

                    jsonObject.put("image", encodedImage);
                    jsonObject.put("name", name);
                    jsonObject.put("description", description);
                    jsonObject.put("phone", phone);
                    jsonObject.put("branches", branches);

                } catch (JSONException e) {
                    Log.e("JSONObject Here", e.toString());
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, REQUEST_URL, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Log.e("test ", jsonObject.toString());

                                rQueue.getCache().clear();
                                finish();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("aaaaaaa", volleyError.toString());
                    }
                });
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        -1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                rQueue = Volley.newRequestQueue(getApplicationContext());

                rQueue.add(jsonObjectRequest);


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
            v_image.setImageURI(imuri);
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


}