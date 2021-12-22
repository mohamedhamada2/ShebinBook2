package com.alatheer.shebinbook.products;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.stores.Store;


public class DetailsFragment extends Fragment {
    String store_phone,store_address,store_attendance,store_desc,store_facebook,store_what_app,store_instagram;
    TextView txt_phone,txt_address,txt_store_attendance,txt_store_describtion;
    ImageView facebook_img,whats_app_img,instagram_img;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        store_phone =  getArguments().getString("store_phone");
        store_address =  getArguments().getString("store_address");
        store_attendance=getArguments().getString("store_attendance");
        store_desc =getArguments().getString("store_description");
        store_facebook =getArguments().getString("store_facebook");
        store_what_app = getArguments().getString("store_whats");
        store_instagram = getArguments().getString("store_instagram");
        txt_phone = view.findViewById(R.id.txt_phone);
        txt_address = view.findViewById(R.id.txt_address);
        txt_store_attendance = view.findViewById(R.id.store_attendance);
        txt_store_describtion = view.findViewById(R.id.store_description);
        facebook_img = view.findViewById(R.id.facebook_img);
        whats_app_img = view.findViewById(R.id.whats_app_img);
        instagram_img = view.findViewById(R.id.instagram_img);
        txt_phone.setText(store_phone);
        txt_address.setText(store_address);
        txt_store_attendance.setText(store_attendance);
        txt_store_describtion.setText(store_desc);
        txt_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+txt_phone.getText().toString()));
                startActivity(intent);
            }
        });
        facebook_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(store_facebook));
                    startActivity(intent);
                } catch(Exception e) {
                    Toast.makeText(getActivity(), "عفوا لا يوجد صفحة فيس لدينا", Toast.LENGTH_SHORT).show();
                }
            }
        });
        whats_app_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://api.whatsapp.com/send?phone="+"+2"+store_what_app;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(Intent.createChooser(i, ""));
            }
        });
        instagram_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!store_instagram.equals("")||store_instagram != null){
                    Uri uri = Uri.parse("http://instagram.com/_u/"+store_instagram+"/");
                    Log.e("store_inst",store_instagram);
                    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                    likeIng.setPackage("com.instagram.android");

                    try {
                        getActivity().startActivity(likeIng);
                    } catch (ActivityNotFoundException e) {
                        /*getActivity().startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://instagram.com/noamany_fitness_center_")));*/
                        Toast.makeText(getActivity(), "عفوا لا يوجد صفحة اتستجرام لدينا", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "عفوا لا يوجد صفحة اتستجرام لدينا", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}