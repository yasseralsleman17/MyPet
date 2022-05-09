package com.example.mypet.User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mypet.R;
import com.example.mypet.User.Veterinary.VeterinaryDetails;
import com.example.mypet.Var;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VeterinaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VeterinaryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String id;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VeterinaryFragment(String id_txt) {
        this.id = id_txt;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VeterinaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VeterinaryFragment newInstance(String param1, String param2, String id_txt) {
        VeterinaryFragment fragment = new VeterinaryFragment(id_txt);
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

    LinearLayout veterinary_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_veterinary, container, false);

        veterinary_list = view.findViewById(R.id.veterinary_list);

        getVeterinaryList();
        return view;
    }


    private void getVeterinaryList() {
        final String REQUEST_URL = Var.get_veterinaries;

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject obj = new JSONObject(response);

                            JSONArray data = obj.getJSONArray("veterinaries");
                            for (int i = 0; i < data.length(); i++) {
                                show_Veterinary_list(data.getJSONObject(i));


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


    private void show_Veterinary_list(JSONObject jsonObject) throws JSONException {

        String veterinary_id = jsonObject.getString("veterinary_id");
        View view = getLayoutInflater().inflate(R.layout.blog_list_item_card, null);


        TextView title = view.findViewById(R.id.title);

        ImageView imageView2 = view.findViewById(R.id.imageView2);

        imageView2.setImageLevel(R.drawable.im8);
        title.setText(jsonObject.getString("veterinary_name"));


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), VeterinaryDetails.class);
                i.putExtra("veterinary_id", veterinary_id);
                i.putExtra("user_id", id);
                startActivity(i);
            }
        });

        veterinary_list.addView(view);


    }

}