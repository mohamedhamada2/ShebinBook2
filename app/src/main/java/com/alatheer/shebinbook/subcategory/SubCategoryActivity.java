package com.alatheer.shebinbook.subcategory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;
import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.databinding.ActivitySubCategoryBinding;
import com.alatheer.shebinbook.home.MenuAdapter;
import com.alatheer.shebinbook.home.category.Category;
import com.alatheer.shebinbook.home.category.SubCate;
import com.alatheer.shebinbook.home.slider.MenuItem;
import com.alatheer.shebinbook.home.slider.Slider;
import com.alatheer.shebinbook.home.slider.SliderAdapter;
import com.alatheer.shebinbook.message.Datum;
import com.alatheer.shebinbook.message.MessageAdapter;
import com.alatheer.shebinbook.message.MessageAdapter2;
import com.alatheer.shebinbook.products.StoreDetails;
import com.alatheer.shebinbook.search.SearchStoresAdapter;
import com.alatheer.shebinbook.stores.Store;
import com.alatheer.shebinbook.stores.StoresAdapter;
import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    Category category;
    ActivitySubCategoryBinding activitySubCategoryBinding;
    SubCategoryViewModel subCategoryViewModel;
    GridLayoutManager layoutManager;
    RecyclerView.LayoutManager layoutManager2;
    SubCategoryAdapter subCategoryAdapter;
    AutoScrollViewPager viewPager2;
    MenuAdapter menuAdapter;
    List<MenuItem> menuItemList;
    RecyclerView.LayoutManager menulayoutmanager;
    RecyclerView menu_recycler;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String user_img,user_name,user_phone,user_id;
    Integer user_type ;
    RecyclerView message_recycler;
    Dialog dialog;
    String trader_id;
    List<StoreDetails> messages_types_list;
    RecyclerView.LayoutManager storeDetailsManager;
    MessageAdapter messageAdapter;
    MessageAdapter2 messageAdapter2;
    RecyclerView search_recycler;
    SearchStoresAdapter storesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        activitySubCategoryBinding = DataBindingUtil.setContentView(this,R.layout.activity_sub_category);
        subCategoryViewModel = new SubCategoryViewModel(this);
        activitySubCategoryBinding.setSubcategoryviewmodel(subCategoryViewModel);
        try {
            getData();
            subCategoryViewModel.getAds();
            getSharedPreferanceData();
            activitySubCategoryBinding.categoryName.setText(category.getName());
            initSubCategories(category.getSubCates());
        }catch (Exception e){

        }
        activitySubCategoryBinding.swiperefresh.setOnRefreshListener(this);

        init_navigation_menu();

        activitySubCategoryBinding.imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create_message_Dialog();
            }
        });
        activitySubCategoryBinding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create_Alert_Dialog();
            }
        });

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
                subCategoryViewModel.getSearch_stores(charSequence.toString());
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

    private void Create_message_Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.message_dialog_item, null);
        RecyclerView message_type_recycler = view.findViewById(R.id.message_type_recycler);
        ImageView cancel_img = view.findViewById(R.id.cancel_img);
        message_recycler = view.findViewById(R.id.message_recycler);

        if (user_type == 4){
            subCategoryViewModel.getMessages(trader_id);
        }else {
            subCategoryViewModel.getUserMessages(user_id);
        }
        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        message_type_recycler.setAdapter(messageAdapter);
        message_type_recycler.setLayoutManager(layoutManager2);
        message_type_recycler.setHasFixedSize(true);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void initSubCategories(List<SubCate> subCates) {
        subCategoryAdapter = new SubCategoryAdapter(this,subCates);
        layoutManager = new GridLayoutManager(this,2);
        activitySubCategoryBinding.subcategoryRecycler.setHasFixedSize(true);
        activitySubCategoryBinding.subcategoryRecycler.setAdapter(subCategoryAdapter);
        activitySubCategoryBinding.subcategoryRecycler.setLayoutManager(layoutManager);
    }
    private void getSharedPreferanceData() {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        //user_id = loginModel.getData().getUser().getId()+"";
        user_img = loginModel.getData().getUser().getUserImg();
        user_name = loginModel.getData().getUser().getName();
        user_phone = loginModel.getData().getUser().getPhone();
        user_type = loginModel.getData().getUser().getRoleIdFk();
        user_id = loginModel.getData().getUser().getId()+"";
        if (user_type == 4){
            trader_id =loginModel.getData().getUser().getTraderId()+"";
        }
        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activitySubCategoryBinding.userImg2);
        }
        activitySubCategoryBinding.userName.setText(user_name);
    }

    private void getData() {
        category = (Category) getIntent().getSerializableExtra("category");
    }

    public void init_sliders(List<Slider> data) {
        if (!data.isEmpty()){
            viewPager2.setAdapter(new SliderAdapter(this,data));
            viewPager2.setPadding(60,0,60,0);
            viewPager2.setOffscreenPageLimit(3);
            viewPager2.startAutoScroll();
            viewPager2.setInterval(3000);
            viewPager2.setCycle(true);
            viewPager2.setStopScrollWhenTouch(true);
            //viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
            compositePageTransformer.addTransformer(new MarginPageTransformer(40));
            compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                @Override
                public void transformPage(@NonNull View page, float position) {
                    float r = 1 - Math.abs(position);
                    page.setScaleY(0.85f + r * 0.15f);
                }
            });
        }else {
            activitySubCategoryBinding.viewpager2.setVisibility(View.GONE);
        }
        //viewPager2.setPageTransformer(compositePageTransformer);
    }
    private boolean closeDrawer() {
        activitySubCategoryBinding.drawerlayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showmenu(View view) {
        activitySubCategoryBinding.navView.setNavigationItemSelectedListener(this);
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
        View headerLayout = activitySubCategoryBinding.navView.getHeaderView(0);
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
        activitySubCategoryBinding.drawerlayout.openDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull  android.view.MenuItem item) {
        return false;
    }

    public void init_messages_recycler(List<Datum> data) {
        messageAdapter2 = new MessageAdapter2(data,this);
        layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true);
        message_recycler.setHasFixedSize(true);
        message_recycler.setLayoutManager(layoutManager2);
        message_recycler.setAdapter(messageAdapter2);
    }

    public void init_search_recycler(List<Store> data) {
        storesAdapter = new SearchStoresAdapter(this,data);
        layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true);
        search_recycler.setHasFixedSize(true);
        search_recycler.setLayoutManager(layoutManager2);
        search_recycler.setAdapter(storesAdapter);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                subCategoryViewModel.getAds();
                getData();
                initSubCategories(category.getSubCates());
                activitySubCategoryBinding.swiperefresh.setRefreshing(false);
            }
        }, 2000);
    }
}