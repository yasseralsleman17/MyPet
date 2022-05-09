package com.example.mypet.User.pet;

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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mypet.R;
import com.example.mypet.User.UserHomePage;
import com.example.mypet.Var;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddPet extends AppCompatActivity {

    ImageView imageView;
    EditText editText_name, editText_birth, editText_sex, editText_weight, editText_diseases, editText_MEDICATION;
    Button button_save;

    Uri imuri;


    Bitmap bitmap;
    RequestQueue rQueue;
    JSONObject jsonObject;

    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_id = extras.getString("user_id");

        }

        imageView = findViewById(R.id.imageView);

        editText_name = findViewById(R.id.editText_name);
        editText_birth = findViewById(R.id.editText_birth);
        editText_sex = findViewById(R.id.editText_sex);
        editText_weight = findViewById(R.id.editText_weight);
        editText_diseases = findViewById(R.id.editText_diseases);
        editText_MEDICATION = findViewById(R.id.editText_MEDICATION);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openfilechooser();
            }
        });


        button_save = findViewById(R.id.button_save);

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editText_name_txt = editText_name.getText().toString();
                String editText_birth_txt = editText_birth.getText().toString();
                String editText_sex_txt = editText_sex.getText().toString();
                String editText_weight_txt = editText_weight.getText().toString();
                String editText_diseases_txt = editText_diseases.getText().toString();
                String editText_MEDICATION_txt = editText_MEDICATION.getText().toString();


                final String REQUEST_URL = Var.add_pet;


                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                try {
                    jsonObject = new JSONObject();

                    jsonObject.put("image", encodedImage);

                    jsonObject.put("user_id", user_id);
                    jsonObject.put("name", editText_name_txt);
                    jsonObject.put("birthday", editText_birth_txt);
                    jsonObject.put("weight", editText_weight_txt);
                    jsonObject.put("diseases", editText_diseases_txt);
                    jsonObject.put("medication", editText_MEDICATION_txt);
                    jsonObject.put("sex", editText_sex_txt);


                } catch (JSONException e) {
                    Log.e("JSONObject Here", e.toString());
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, REQUEST_URL, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                rQueue.getCache().clear();
                                Toast.makeText(getApplication(), "pet added Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), UserHomePage.class).putExtra("id_txt",user_id));
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
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

            try {

                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imuri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageView.setImageURI(imuri);
        }
    }


}