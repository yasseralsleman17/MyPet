package com.example.mypet.User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.example.mypet.User.pet.PetProfile;
import com.example.mypet.User.pet.ViewPetProfile;
import com.example.mypet.Var;
import com.github.siyamed.shapeimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdoptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdoptionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String id;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdoptionFragment(String id_txt) {
        this.id = id_txt;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdoptionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdoptionFragment newInstance(String param1, String param2, String id_txt) {
        AdoptionFragment fragment = new AdoptionFragment(id_txt);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    GridLayout grid_pet;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_adoption, container, false);

        grid_pet = view.findViewById(R.id.grid_pet);

        get_all_pets();


        return view;
    }


    private void get_all_pets() {

        final String REQUEST_URL = Var.get_all_pets;

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

                Intent i = new Intent(getActivity(), ViewPetProfile.class);
                i.putExtra("pet_id", pet_id);
                i.putExtra("user_id", id);


                startActivity(i);

            }
        });
        grid_pet.addView(view);
    }


}