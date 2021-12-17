package com.alatheer.shebinbook.trader.profile;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.Utilities.Constants;
import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.comments.CommentModel;
import com.squareup.picasso.Picasso;


public class DetailsFragment extends Fragment {
    String store_phone,store_address,store_attendance,store_desc,store_facebook,store_what_app,store_instagram,store_id,store_name,store_mini_des,offer,products;
    EditText txt_phone,txt_address,txt_store_attendance,txt_store_describtion,edit_whats,edit_instagram,edit_facebook;
    ImageView facebook_img,instagram_img,whats_app_img,edit_img1,edit_img2,edit_img3,edit_img4,edit_img5;
    Button btn_edit;
    Dialog dialog3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_details2, container, false);
        store_phone =  getArguments().getString("store_phone");
        store_address =  getArguments().getString("store_address");
        store_attendance = getArguments().getString("store_attendance");
        store_desc = getArguments().getString("store_desc");
        store_facebook = getArguments().getString("store_facebook");
        store_what_app = getArguments().getString("store_whats");
        store_instagram = getArguments().getString("store_instagram");
        store_id = getArguments().getString("store_id");
        store_name = getArguments().getString("store_name");
        store_mini_des = getArguments().getString("store_mini");
        offer = getArguments().getString("offer");
        products = getArguments().getString("products");
        txt_phone = view.findViewById(R.id.txt_phone);
        txt_address = view.findViewById(R.id.txt_address);
        btn_edit = view.findViewById(R.id.btn_edit);
        txt_store_attendance = view.findViewById(R.id.store_attendance);
        txt_store_describtion = view.findViewById(R.id.store_description1);
        facebook_img = view.findViewById(R.id.facebook_img);
        whats_app_img = view.findViewById(R.id.whats_app_img);
        instagram_img = view.findViewById(R.id.instagram_img);
        edit_img1 = view.findViewById(R.id.edit_img);
        edit_img2 = view.findViewById(R.id.edit_img2);
        edit_img3 = view.findViewById(R.id.edit_img3);
        edit_img4 = view.findViewById(R.id.edit_img4);
        edit_img5 = view.findViewById(R.id.edit_img5);
        txt_phone.setText(store_phone);
        txt_address.setText(store_address);
        txt_store_attendance.setText(store_attendance);
        txt_store_describtion.setText(store_desc);
        /*txt_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+txt_phone.getText().toString()));
                startActivity(intent);
            }
        });*/
        facebook_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!store_facebook.equals("")){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(store_facebook));
                        startActivity(intent);
                    }else {
                        Toast.makeText(getActivity(), "عفوا لا يوجد صفحة فيس لدينا", Toast.LENGTH_SHORT).show();
                    }
                } catch(Exception e) {
                    Toast.makeText(getActivity(), "عفوا لا يوجد صفحة فيس لدينا", Toast.LENGTH_SHORT).show();
                }
            }
        });
        whats_app_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (store_what_app != null || store_what_app.equals("")){
                    String url = "https://api.whatsapp.com/send?phone="+"+2"+store_what_app;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(Intent.createChooser(i, ""));
                }else {
                    Toast.makeText(getActivity(), "عفوا لا يوجد رقم واتس أب لدينا", Toast.LENGTH_SHORT).show();
                }
            }
        });
        instagram_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!store_instagram.equals("")){
                    Uri uri = Uri.parse("http://instagram.com/_u/"+store_instagram+"/");
                    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                    likeIng.setPackage("com.instagram.android");

                    try {
                        getActivity().startActivity(likeIng);
                    } catch (ActivityNotFoundException e) {
                        getActivity().startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://instagram.com/noamany_fitness_center_")));
                    }
                }else {
                    Toast.makeText(getActivity(), "عفوا لا يوجد صفحة اتستجرام لدينا", Toast.LENGTH_SHORT).show();
                }
            }
        });

        edit_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_store_describtion.setFocusable(true);
                txt_store_describtion.setEnabled(true);
                txt_store_describtion.setClickable(true);
                txt_store_describtion.setFocusableInTouchMode(true);
                txt_store_describtion.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
        edit_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateEditDialog();
            }
        });
        edit_img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_phone.setFocusable(true);
                txt_phone.setEnabled(true);
                txt_phone.setClickable(false);
                txt_phone.setFocusableInTouchMode(true);
                txt_phone.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
        edit_img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_store_attendance.setFocusable(true);
                txt_store_attendance.setEnabled(true);
                txt_store_attendance.setClickable(true);
                txt_store_attendance.setFocusableInTouchMode(true);
                txt_store_attendance.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
        edit_img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_address.setFocusable(true);
                txt_address.setEnabled(true);
                txt_address.setClickable(true);
                txt_address.setFocusableInTouchMode(true);
                txt_address.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                store_desc = txt_store_describtion.getText().toString();
                store_attendance = txt_store_attendance.getText().toString();
                store_address = txt_address.getText().toString() ;
                store_phone = txt_phone.getText().toString();
                update_store(store_desc,store_what_app,store_facebook,store_attendance,store_address,store_phone,store_mini_des,store_name,offer,products,store_instagram);
            }
        });
        return view;
    }

    private void update_store(String store_desc, String store_what_app, String store_facebook, String store_attendance, String store_address, String store_phone, String store_mini_des, String store_name, String offer, String products,String store_instagram) {
        if (Utilities.isNetworkAvailable(getActivity())){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.update_store_without_img(store_id,store_desc,store_attendance,store_facebook,store_what_app,store_mini_des,store_address,store_phone,"","","","",offer,products,store_instagram,store_name);
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getData().getSuccess()==1){
                            Toast.makeText(getActivity(), "تم التعديل بنجاح", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommentModel> call, Throwable t) {

                }
            });
        }
    }

    private void CreateEditDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.edit_dialog2, null);
        edit_whats = view.findViewById(R.id.et_whats_app);
        edit_facebook = view.findViewById(R.id.et_facebook);
        edit_instagram = view.findViewById(R.id.et_instagram);
        Button edit_btn = view.findViewById(R.id.btn_edit);
        edit_facebook.setText(store_facebook);
        edit_instagram.setText(store_instagram);
        edit_whats.setText(store_what_app);
        builder.setView(view);
        dialog3 = builder.create();
        dialog3.show();
        Window window = dialog3.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                store_facebook = edit_facebook.getText().toString();
                store_instagram = edit_instagram.getText().toString();
                store_what_app = edit_whats.getText().toString();
                dialog3.dismiss();
            }
        });
    }
}