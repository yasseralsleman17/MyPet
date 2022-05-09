package com.example.mypet.User;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mypet.R;
import com.example.mypet.User.Blog.BlogDetails;
import com.example.mypet.Var;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlogsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlogsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String id;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BlogsFragment(String id_txt) {
        this.id = id_txt;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlogsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlogsFragment newInstance(String param1, String param2, String id_txt) {
        BlogsFragment fragment = new BlogsFragment(id_txt);
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

    LinearLayout blogs_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_blocs, container, false);

        blogs_list=view.findViewById(R.id.blogs_list);

        getBlogslist();
        return view;
    }


    private void getBlogslist() {
        final String REQUEST_URL = Var.get_all_blogs;

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest jsonRequest = new StringRequest(
                Request.Method.POST,
                REQUEST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject obj = new JSONObject(response);

                            JSONArray data = obj.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                showtBlog_list(data.getJSONObject(i));


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


    private void showtBlog_list(JSONObject data) throws JSONException {



        View view = getLayoutInflater().inflate(R.layout.blog_list_item_card, null);


        String blog_id = data.getString("id");

        String title_txt = data.getString("name");

        TextView title = view.findViewById(R.id.title);


        title.setText(title_txt);




        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), BlogDetails.class);
                i.putExtra("blog_id", blog_id);
                i.putExtra("user_id", id);

                startActivity(i);
            }
        });

        blogs_list.addView(view);


    }

}