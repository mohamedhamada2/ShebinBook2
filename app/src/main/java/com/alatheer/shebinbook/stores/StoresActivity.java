package com.alatheer.shebinbook.stores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;
import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.databinding.ActivityStoresBinding;
import com.alatheer.shebinbook.home.MenuAdapter;
import com.alatheer.shebinbook.home.slider.MenuItem;
import com.alatheer.shebinbook.home.slider.Slider;
import com.alatheer.shebinbook.home.slider.SliderAdapter;
import com.alatheer.shebinbook.message.MessageAdapter;
import com.alatheer.shebinbook.message.MessageAdapter2;
import com.alatheer.shebinbook.products.ProductsActivity;
import com.alatheer.shebinbook.products.StoreDetails;
import com.alatheer.shebinbook.search.SearchStoresAdapter;
import com.alatheer.shebinbook.setting.ProfileData;
import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StoresActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    ActivityStoresBinding activityStoresBinding;
    StoresViewModel storesViewModel;
    StoresAdapter storesAdapter;
    SearchStoresAdapter searchstoresAdapter;
    LinearLayoutManager layoutManager,layoutManager2;
    MenuAdapter menuAdapter;
    List<MenuItem> menuItemList;
    RecyclerView.LayoutManager menulayoutmanager;
    RecyclerView menu_recycler;
    AutoScrollViewPager viewPager2;
    Integer subcategory_id;
    Dialog dialog;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Integer user_type,page = 1,page2=1;
    String user_id,store_name,store_id,store_img,address,user_img,user_name,user_phone,trader_id,trader_id2;
    List<StoreDetails> messages_types_list;
    RecyclerView.LayoutManager storeDetailsManager;
    MessageAdapter messageAdapter;
    RecyclerView message_recycler;
    MessageAdapter2 messageAdapter2;
    RecyclerView search_recycler;
    private boolean isloading;
    private int pastvisibleitem,visibleitemcount,totalitemcount,previous_total=0;
    int view_threshold = 10;
    private boolean isloading2;
    private int pastvisibleitem2,visibleitemcount2,totalitemcount2,previous_total2=0;
    int view_threshold2 = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);
        activityStoresBinding = DataBindingUtil.setContentView(this,R.layout.activity_stores);
        getDataIntent();
        getSharedPreferenceData();
        //get_messages_types();
        storesViewModel = new StoresViewModel(this,subcategory_id+"");
        storesViewModel.getData(user_id);
        activityStoresBinding.setStoresviewmodel(storesViewModel);
        viewPager2 = activityStoresBinding.viewpager2;
        activityStoresBinding.swiperefresh.setOnRefreshListener(this);
        activityStoresBinding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create_Alert_Dialog();
            }
        });
        activityStoresBinding.imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create_message_Dialog();
            }
        });
        activityStoresBinding.storesRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleitemcount = layoutManager2.getChildCount();
                totalitemcount = layoutManager2.getItemCount();
                pastvisibleitem = layoutManager2.findFirstVisibleItemPosition();
                if(dy>0){
                    if(isloading){
                        if(totalitemcount>previous_total){
                            isloading = false;
                            previous_total = totalitemcount;

                        }
                    }
                    if(!isloading &&(totalitemcount-visibleitemcount)<= pastvisibleitem+view_threshold){
                        page++;
                        storesViewModel.PerformPagination(user_id,page);
                        isloading = true;
                    }

                }else {
                    /*if(isloading){
                        if(totalitemcount>previous_total){
                            isloading = false;
                            previous_total = totalitemcount;
                            Log.e("store1","eeeeee");
                        }
                    }
                    if(!isloading &&(totalitemcount-visibleitemcount)<= pastvisibleitem+view_threshold){
                        page++;
                        Log.e("store2","eeeeee");
                        offerViewModel.PerformPagination(page);
                        isloading = true;
                    }*/
                }
            }
        });

        storesViewModel.getAds();
        storesViewModel.getStores(user_id,page);
    }

    private void Create_message_Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.message_dialog_item, null);
        RecyclerView message_type_recycler = view.findViewById(R.id.message_type_recycler);
        ImageView cancel_img = view.findViewById(R.id.cancel_img);
         message_recycler = view.findViewById(R.id.message_recycler);
        if (user_type == 4){
            storesViewModel.getMessages(trader_id2,page2);
        }else {
            storesViewModel.getuserMessages(user_id,page2);
        }
        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        message_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull  RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleitemcount2 = layoutManager2.getChildCount();
                totalitemcount2 = layoutManager2.getItemCount();
                pastvisibleitem2 = layoutManager2.findFirstVisibleItemPosition();
                if(dy>0){
                    if(isloading2){
                        if(totalitemcount2>previous_total2){
                            isloading2 = false;
                            previous_total2 = totalitemcount2;

                        }
                    }
                    if(!isloading2 &&(totalitemcount2-visibleitemcount2)<= pastvisibleitem2+view_threshold2){
                        page2++;
                        if (user_type == 4){
                            storesViewModel.TraderPagination(trader_id+"",page);
                        }else {
                            storesViewModel.UserPagination(user_id,page);
                        }
                        isloading2 = true;
                    }

                }
            }
        });
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        message_type_recycler.setAdapter(messageAdapter);
        message_type_recycler.setLayoutManager(layoutManager);
        message_type_recycler.setHasFixedSize(true);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

    }
    private void get_messages_types() {
        messages_types_list = new ArrayList<>();
        messages_types_list.add(new StoreDetails(1,"الرسائل الواردة"));
        messages_types_list.add(new StoreDetails(2,"الرسائل المرسلة"));
        storeDetailsManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        //storeDetailsManager.setReverseLayout(true);
        messageAdapter = new MessageAdapter(messages_types_list,this);

    }

    private void getSharedPreferenceData() {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getData().getUser().getId()+"";
        user_img = loginModel.getData().getUser().getUserImg();
        user_name = loginModel.getData().getUser().getName();
        user_phone = loginModel.getData().getUser().getPhone();
        user_type = loginModel.getData().getUser().getRoleIdFk();
        if (user_type == 4){
            trader_id2 = loginModel.getData().getUser().getTraderId()+"";
        }
        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityStoresBinding.userImg);
        }
        activityStoresBinding.userName.setText(user_name);

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
                storesViewModel.getSearch_stores(charSequence.toString());
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

    private void getDataIntent() {
        subcategory_id = getIntent().getIntExtra("category_id",0);
    }

    /*private void getStores() {
        askModelList = new ArrayList<>();
        askModelList.add(new Store("Mohamed Hamada",R.drawable.user2));
        askModelList.add(new Store("Mohamed Hamada",R.drawable.user2));
        askModelList.add(new Store("Mohamed Hamada",R.drawable.user2));
        askModelList.add(new Store("Mohamed Hamada",R.drawable.user2));
        storesAdapter = new StoresAdapter(this,askModelList);
        layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityStoresBinding.storesRecycler.setHasFixedSize(true);
        activityStoresBinding.storesRecycler.setLayoutManager(layoutManager2);
        activityStoresBinding.storesRecycler.setAdapter(storesAdapter);
    }*/
    public void showmenu(View view) {
        activityStoresBinding.navView.setNavigationItemSelectedListener(this);
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
        View headerLayout = activityStoresBinding.navView.getHeaderView(0);
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
        activityStoresBinding.drawerlayout.openDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull  android.view.MenuItem item) {
        return false;
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
            activityStoresBinding.viewpager2.setVisibility(View.GONE);
        }
        //viewPager2.setPageTransformer(compositePageTransformer);
    }

    public void init_recycler(StoresAdapter storesAdapter) {
        layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityStoresBinding.storesRecycler.setHasFixedSize(true);
        activityStoresBinding.storesRecycler.setLayoutManager(layoutManager2);
        activityStoresBinding.storesRecycler.setAdapter(storesAdapter);
    }

    public void add_to_fav(Store store) {
        storesViewModel.add_fav(user_id, store);
    }

    public void delete_from_fav(Store store) {
        storesViewModel.delete_fav(user_id, store);
    }

    public void go_to_store(Store store) {
        Intent intent = new Intent(StoresActivity.this, ProductsActivity.class);
        intent.putExtra("store", store);
        startActivity(intent);
    }

    public void init_messages_recycler(MessageAdapter2 messageAdapter2) {
        layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        message_recycler.setHasFixedSize(true);
        message_recycler.setLayoutManager(layoutManager2);
        message_recycler.setAdapter(messageAdapter2);
    }

    public void init_search_recycler(List<Store> data) {
        //Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        searchstoresAdapter = new SearchStoresAdapter(this,data);
        layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        search_recycler.setHasFixedSize(true);
        search_recycler.setLayoutManager(layoutManager2);
        search_recycler.setAdapter(searchstoresAdapter);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page = 1;
                pastvisibleitem = 0;
                visibleitemcount = 0;
                totalitemcount = 0;
                previous_total=0;
                view_threshold = 10;
                storesViewModel.getAds();
                storesViewModel.getStores(user_id,page);
                activityStoresBinding.storesRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        visibleitemcount = layoutManager2.getChildCount();
                        totalitemcount = layoutManager2.getItemCount();
                        pastvisibleitem = layoutManager2.findFirstVisibleItemPosition();
                        if(dy>0){
                            if(isloading){
                                if(totalitemcount>previous_total){
                                    isloading = false;
                                    previous_total = totalitemcount;

                                }
                            }
                            if(!isloading &&(totalitemcount-visibleitemcount)<= pastvisibleitem+view_threshold){
                                page++;
                                storesViewModel.PerformPagination(user_id,page);
                                isloading = true;
                            }

                        }else {
                    /*if(isloading){
                        if(totalitemcount>previous_total){
                            isloading = false;
                            previous_total = totalitemcount;
                            Log.e("store1","eeeeee");
                        }
                    }
                    if(!isloading &&(totalitemcount-visibleitemcount)<= pastvisibleitem+view_threshold){
                        page++;
                        Log.e("store2","eeeeee");
                        offerViewModel.PerformPagination(page);
                        isloading = true;
                    }*/
                        }
                    }
                });
               get_messages_types();
               activityStoresBinding.swiperefresh.setRefreshing(false);
               storesViewModel.getData(user_id);
            }
        }, 2000);
    }

    public void setData(ProfileData body) {
        user_img = body.getData().getUserImg();
        user_name = body.getData().getName();
        user_phone = body.getData().getPhone();

        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityStoresBinding.userImg);
        }
        activityStoresBinding.userName.setText(user_name);
        init_navigation_menu();
    }
}