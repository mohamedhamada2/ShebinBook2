package com.alatheer.shebinbook.contactus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.databinding.ActivityContactUsBinding;
import com.alatheer.shebinbook.home.MenuAdapter;
import com.alatheer.shebinbook.home.slider.MenuItem;
import com.alatheer.shebinbook.message.Datum;
import com.alatheer.shebinbook.message.MessageAdapter2;
import com.alatheer.shebinbook.message.MessageModel;
import com.alatheer.shebinbook.search.SearchStoresAdapter;
import com.alatheer.shebinbook.stores.Store;
import com.alatheer.shebinbook.stores.StoreModel;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ContactUsActivity extends AppCompatActivity {
    ActivityContactUsBinding activityContactUsBinding;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String user_img,user_phone,user_name;
    MenuAdapter menuAdapter;
    List<MenuItem> menuItemList;
    RecyclerView menu_recycler;
    LinearLayoutManager menulayoutmanager;
    Integer user_type;
    RecyclerView message_recycler;
    Dialog dialog;
    RecyclerView search_recycler;
    String user_id;
    Integer trader_id;
    SearchStoresAdapter storesAdapter;
    MessageAdapter2 messageAdapter2;
    RecyclerView.LayoutManager layoutManager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        activityContactUsBinding = DataBindingUtil.setContentView(this,R.layout.activity_contact_us);
        getSharedPreferanceData();
        init_navigation_menu();
        activityContactUsBinding.facebookTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/100167085818033"));
                    startActivity(intent);
                } catch(Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Shebinbook-100167085818033/")));
                }
            }
        });
        activityContactUsBinding.whatsappTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://api.whatsapp.com/send?phone="+"+201033557898";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(Intent.createChooser(i, ""));
            }
        });
        activityContactUsBinding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create_Alert_Dialog();
            }
        });
        activityContactUsBinding.imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create_message_Dialog();
            }
        });
    }
    private void Create_message_Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.message_dialog_item, null);
        RecyclerView message_type_recycler = view.findViewById(R.id.message_type_recycler);
        ImageView cancel_img = view.findViewById(R.id.cancel_img);
        message_recycler = view.findViewById(R.id.message_recycler);
        if (user_type == 4){
            getMessages(trader_id);
        }else {
            getUserMessages(user_id);
        }
        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        //message_type_recycler.setAdapter(messageAdapter);
        //message_type_recycler.setLayoutManager(layoutManager);
        //message_type_recycler.setHasFixedSize(true);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void Create_Alert_Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.search_item, null);
        EditText et_search = view.findViewById(R.id.et_search);
        search_recycler = view.findViewById(R.id.search_recycler);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getSearch_stores(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void getSharedPreferanceData() {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getData().getUser().getId()+"";
        trader_id = loginModel.getData().getUser().getTraderId();
        user_img = loginModel.getData().getUser().getUserImg();
        user_name = loginModel.getData().getUser().getName();
        user_phone = loginModel.getData().getUser().getPhone();
        user_type = loginModel.getData().getUser().getRoleIdFk();
        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityContactUsBinding.userImg);
        }
        activityContactUsBinding.userName.setText(user_name);
    }
    public void showmenu(View view) {
        //activityFavoriteBinding.navView.setNavigationItemSelectedListener(this);
        openDrawer();
    }
    private void init_navigation_menu() {
        menuItemList = new ArrayList<>();
        menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("الرئيسية",R.drawable.home2));
        menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("المفضلة",R.drawable.fav2));
        menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("الإعدادات",R.drawable.setting));
        menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("تواصل معنا",R.drawable.contactus));
        menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("تسجيل خروج",R.drawable.logout2));
        if (user_type == 4){
            menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("متجري",R.drawable.store));
        }
        menuAdapter = new MenuAdapter(menuItemList,this);
        menulayoutmanager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        View headerLayout = activityContactUsBinding.navView.getHeaderView(0);
        RoundedImageView img = headerLayout.findViewById(R.id.user_img);
        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(img);
        }
        TextView txt_name = headerLayout.findViewById(R.id.txt_user_name);
        TextView txt_phone = headerLayout.findViewById(R.id.txt_phone);
        txt_name.setText(user_name);
        txt_phone.setText(user_phone);
        menu_recycler = headerLayout.findViewById(R.id.recycler_view);
        menu_recycler.setHasFixedSize(true);
        menu_recycler.setAdapter(menuAdapter);
        menu_recycler.setLayoutManager(menulayoutmanager);
    }

    private boolean openDrawer() {
        activityContactUsBinding.drawerlayout.openDrawer(GravityCompat.END);
        return true;
    }
    public void getSearch_stores(String toString) {
        if (Utilities.isNetworkAvailable(this)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<StoreModel> call = getDataService.search_store(toString);
            call.enqueue(new Callback<StoreModel>() {
                @Override
                public void onResponse(Call<StoreModel> call, Response<StoreModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            init_search_recycler(response.body().getData());
                        }
                    }
                }

                @Override
                public void onFailure(Call<StoreModel> call, Throwable t) {
                    Log.e("searcherror",t.getMessage());
                }
            });
        }
    }


    public void getMessages(Integer trader_id) {
        if (Utilities.isNetworkAvailable(this)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<MessageModel> call = getDataService.get_messages(trader_id+"");
            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                            init_messages_recycler(response.body().getData().getData());
                        }
                    }
                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {

                }
            });
        }
    }

    public void getUserMessages(String user_id) {
        //Toast.makeText(context, user_id, Toast.LENGTH_SHORT).show();
        if (Utilities.isNetworkAvailable(this)) {
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<MessageModel> call = getDataService.get_user_messages(user_id);
            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                            init_messages_recycler(response.body().getData().getData());
                        }
                    }
                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {
                    Log.e("error36", t.getMessage());
                }
            });
        }
    }

    public void init_search_recycler(List<Store> data) {
        storesAdapter = new SearchStoresAdapter(this,data);
        layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true);
        search_recycler.setHasFixedSize(true);
        search_recycler.setLayoutManager(layoutManager2);
        search_recycler.setAdapter(storesAdapter);
    }

    public void init_messages_recycler(List<Datum> data) {
        messageAdapter2 = new MessageAdapter2(data,this);
        layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        message_recycler.setHasFixedSize(true);
        message_recycler.setLayoutManager(layoutManager2);
        message_recycler.setAdapter(messageAdapter2);
    }
}