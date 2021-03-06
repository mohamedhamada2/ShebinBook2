package com.alatheer.shebinbook.trader.addalboum;

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
import com.alatheer.shebinbook.databinding.ActivityAddAlboumBinding;
import com.alatheer.shebinbook.home.AskAdapter;
import com.alatheer.shebinbook.home.MenuAdapter;
import com.alatheer.shebinbook.setting.ProfileData;
import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AddAlboumActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityAddAlboumBinding activityAddAlboumBinding;
    AddAlboumViewModel addAlboumViewModel;
    String alboum_name, store_id, user_img, user_name, user_phone;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Integer trader_id;
    List<com.alatheer.shebinbook.home.slider.MenuItem> menuItemList;
    AskAdapter askAdapter;
    RecyclerView menu_recycler;
    MenuAdapter menuAdapter;
    RecyclerView.LayoutManager menulayoutmanager;
    Integer user_type;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alboum);
        activityAddAlboumBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_alboum);
        addAlboumViewModel = new AddAlboumViewModel(this);
        activityAddAlboumBinding.setAddalboumviewmodel(addAlboumViewModel);
        getDatafromIntent();
        getShardpreferanceData();
        addAlboumViewModel.getData(user_id);
        activityAddAlboumBinding.btnAddAlboum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
    }

    private void getShardpreferanceData() {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getData().getUser().getId()+"";
        trader_id = loginModel.getData().getUser().getTraderId();
        user_img = loginModel.getData().getUser().getUserImg();
        user_name = loginModel.getData().getUser().getName();
        user_phone = loginModel.getData().getUser().getPhone();
        user_type = loginModel.getData().getUser().getRoleIdFk();
        if (user_img != null) {
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/" + user_img).into(activityAddAlboumBinding.userImg);
        }
        activityAddAlboumBinding.userName.setText(user_name);
    }

    private void init_navigation_menu() {
        menuItemList = new ArrayList<>();
        menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("????????????????", R.drawable.home2));
        menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("??????????????", R.drawable.fav2));
        menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("??????????????????", R.drawable.setting));
        menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("?????????? ????????", R.drawable.contactus));
        menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("?????????? ????????", R.drawable.logout2));
        if (user_type == 4) {
            menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("??????????", R.drawable.store));
        }
        menuAdapter = new MenuAdapter(menuItemList, this);
        menulayoutmanager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        View headerLayout = activityAddAlboumBinding.navView.getHeaderView(0);
        RoundedImageView img = headerLayout.findViewById(R.id.user_img);
        if (user_img != null) {
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/" + user_img).into(img);
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

    private void getDatafromIntent() {
        store_id = getIntent().getStringExtra("store_id");
    }

    private void validation() {
        alboum_name = activityAddAlboumBinding.etAlboumName.getText().toString();
        if (!TextUtils.isEmpty(alboum_name)) {
            addAlboumViewModel.add_alboum(trader_id + "", store_id, alboum_name);
        } else {
            activityAddAlboumBinding.etAlboumName.setError("???????? ?????? ??????????????");
        }
    }

    public void showmenu(View view) {
        activityAddAlboumBinding.navView.setNavigationItemSelectedListener(this);
        openDrawer();
    }

    private Boolean openDrawer() {
        activityAddAlboumBinding.drawerlayout.openDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public void setData(ProfileData body) {
        user_img = body.getData().getUserImg();
        user_name = body.getData().getName();
        user_phone = body.getData().getPhone();

        if (user_img != null) {
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/" + user_img).into(activityAddAlboumBinding.userImg);
        }
        activityAddAlboumBinding.userName.setText(user_name);
        init_navigation_menu();
    }
}