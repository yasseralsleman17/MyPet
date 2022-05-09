package com.example.mypet.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.mypet.R;
import com.example.mypet.User.Shops.AccessoriesShop;
import com.example.mypet.User.Shops.FoodShop;
import com.example.mypet.User.Shops.PetResortsShop;
import com.example.mypet.User.Veterinary.VeterinaryMain;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PetPlacesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PetPlacesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String id;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PetPlacesFragment(String id_txt) {
        this.id = id_txt;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PetPlacesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PetPlacesFragment newInstance(String param1, String param2, String id_txt) {
        PetPlacesFragment fragment = new PetPlacesFragment(id_txt);
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

    ImageView pet_resorts,  accessories,manage_veterinary;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_pet_places, container, false);

        pet_resorts=view.findViewById(R.id.pet_resorts);
        accessories=view.findViewById(R.id.accessories);
        manage_veterinary=view.findViewById(R.id.manage_veterinary);


        accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), AccessoriesShop.class);
                i.putExtra("id_txt", id);
                startActivity(i);

            }
        });



        pet_resorts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PetResortsShop.class);
                i.putExtra("id_txt", id);
                startActivity(i);

            }
        });

        manage_veterinary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), VeterinaryMain.class);
                i.putExtra("id_txt", id);
                startActivity(i);

            }
        });


        return view;
    }
}