package com.alatheer.shebinbook.trader.editalboum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.databinding.ActivityUpdateAlboumBinding;
import com.alatheer.shebinbook.home.AskAdapter;
import com.alatheer.shebinbook.home.MenuAdapter;
import com.alatheer.shebinbook.setting.ProfileData;
import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UpdateAlboumActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityUpdateAlboumBinding activityUpdateAlboumBinding;
    UpdateAlboumViewModel updateAlboumViewModel;
    Integer store_id,trader_id,alboum_id;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String alboum_name,user_img,user_name,user_phone,user_id;;
    List<com.alatheer.shebinbook.home.slider.MenuItem> menuItemList;
    AskAdapter askAdapter;
    RecyclerView menu_recycler;
    MenuAdapter menuAdapter;
    RecyclerView.LayoutManager menulayoutmanager;
    Integer user_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_alboum);
        activityUpdateAlboumBinding = DataBindingUtil.setContentView(this,R.layout.activity_update_alboum);
        updateAlboumViewModel = new UpdateAlboumViewModel(this);
        activityUpdateAlboumBinding.setUpdatealboumviewmodel(updateAlboumViewModel);
        getDataFromIntent();
        getSharedPreferanceData();
        updateAlboumViewModel.getData(user_id);
        activityUpdateAlboumBinding.etAlboumName.setText(alboum_name);
        activityUpdateAlboumBinding.btnAddAlboum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });

    }

    private void getDataFromIntent() {
        store_id = getIntent().getIntExtra("store_id",0);
        alboum_id = getIntent().getIntExtra("alboum_id",0);
        alboum_name = getIntent().getStringExtra("alboum_name");
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
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityUpdateAlboumBinding.userImg);
        }
        activityUpdateAlboumBinding.userName.setText(user_name);
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
        View headerLayout = activityUpdateAlboumBinding.navView.getHeaderView(0);
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
    private void validation() {
        alboum_name = activityUpdateAlboumBinding.etAlboumName.getText().toString();
        if (!TextUtils.isEmpty(alboum_name)){
            updateAlboumViewModel.update_alboum(store_id,alboum_name,alboum_id,trader_id);
        }else {
            activityUpdateAlboumBinding.etAlboumName.setError("أدخل إسم الألبوم");
        }
    }

    public void showmenu(View view) {
        activityUpdateAlboumBinding.navView.setNavigationItemSelectedListener(this);
        openDrawer();
    }

    private boolean openDrawer() {
        activityUpdateAlboumBinding.drawerlayout.openDrawer(GravityCompat.END);
        return true;

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
        return false;
    }

    public void setData(ProfileData body) {
        user_img = body.getData().getUserImg();
        user_name = body.getData().getName();
        user_phone = body.getData().getPhone();

        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityUpdateAlboumBinding.userImg);
        }
        activityUpdateAlboumBinding.userName.setText(user_name);
        init_navigation_menu();
    }
}