package com.example.mypet.User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.mypet.R;
import com.example.mypet.User.pet.AddPet;
import com.example.mypet.User.pet.PetProfile;
import com.example.mypet.Var;
import com.github.siyamed.shapeimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String id;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment(String id_txt) {
        this.id = id_txt;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @param id_txt
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2, String id_txt) {
        ProfileFragment fragment = new ProfileFragment(id_txt);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onStart() {
        super.onStart();

        getUserProfile();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    Button add_pet, edit_profile;
    TextView user_phone_number, _user_sex, user_name;
    String editText_name_txt, editText_email_txt, password, editText_sex_txt, editText_phone_txt;

    GridLayout grid_pet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        add_pet = view.findViewById(R.id.add_pet);
        edit_profile = view.findViewById(R.id.edit_profile);
        user_phone_number = view.findViewById(R.id.user_phone_number);
        _user_sex = view.findViewById(R.id._user_sex);
        user_name = view.findViewById(R.id.user_name);
        grid_pet = view.findViewById(R.id.grid_pet);


        getUserProfile();
        get_user_animals();

        add_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddPet.class);
                i.putExtra("user_id", id);
                startActivity(i);
            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), EditProfile.class);
                i.putExtra("user_id", id);


                startActivity(i);
            }
        });


        return view;
    }

    private void getUserProfile() {

        final String REQUEST_URL = Var.getuserinfo;

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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

                                JSONObject data = obj.getJSONObject("data");

                                editText_name_txt = data.getString("name");
                                editText_email_txt = data.getString("email");
                                password = data.getString("password");
                                editText_sex_txt = data.getString("sex");
                                editText_phone_txt = data.getString("phone");

                                user_phone_number.setText(data.getString("phone"));
                                _user_sex.setText(data.getString("sex"));
                                user_name.setText(data.getString("name"));


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

                MyData.put("user_id", id);


                return MyData;
            }
        };
        requestQueue.add(jsonRequest);


    }

    private void get_user_animals() {

        final String REQUEST_URL = Var.get_user_animals;

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d("response", response);

                        try {
                            JSONArray obj = new JSONArray(response);

                            for (int i = 0; i < obj.length(); i++) {

                                JSONObject data = obj.getJSONObject(i).getJSONObject("data");

                                showPet(data);




                            }


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

                MyData.put("user_id", id);


                return MyData;
            }
        };
        requestQueue.add(jsonRequest);


    }

    void showPet(JSONObject data) throws JSONException {
        String pet_name_txt = data.getString("pet_name");
        String pet_id = data.getString("id");
        String image = data.getString("image");
        View view = getLayoutInflater().inflate(R.layout.pet_card, null);


        CircularImageView pet_image = view.findViewById(R.id.pet_image);
        TextView pet_name = view.findViewById(R.id.pet_name);
        pet_name.setText(pet_name_txt);



        Glide.with(getActivity())
                .load(Var.images_url+image)
                .into(pet_image);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), PetProfile.class);
                i.putExtra("pet_id", pet_id);
                i.putExtra("user_id", id);


                startActivity(i);

            }
        });
        grid_pet.addView(view);
    }

}