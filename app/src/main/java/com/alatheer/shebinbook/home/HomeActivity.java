package com.alatheer.shebinbook.home;

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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.shebinbook.MainActivity;
import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.cities.Datum;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.categories.CategoryActivity;
import com.alatheer.shebinbook.databinding.ActivityHomeBinding;
import com.alatheer.shebinbook.home.category.Category;
import com.alatheer.shebinbook.home.category.CategoryAdapter;
import com.alatheer.shebinbook.home.slider.Slider;
import com.alatheer.shebinbook.home.slider.SliderAdapter;
import com.alatheer.shebinbook.message.MessageAdapter;
import com.alatheer.shebinbook.message.MessageAdapter2;
import com.alatheer.shebinbook.posts.Post;
import com.alatheer.shebinbook.posts.PostsActivity;
import com.alatheer.shebinbook.products.StoreDetails;
import com.alatheer.shebinbook.search.SearchStoresAdapter;
import com.alatheer.shebinbook.setting.ProfileData;
import com.alatheer.shebinbook.stores.Store;
import com.alatheer.shebinbook.stores.StoresAdapter;
import com.alatheer.shebinbook.trader.addoffer.AddOfferActivity;
import com.alatheer.shebinbook.trader.profile.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    AutoScrollViewPager viewPager2;
    ActivityHomeBinding activityHomeBinding;
    HomeViewModel homeViewModel;
    CategoryAdapter categoryAdapter;
    LinearLayoutManager layoutManager, layoutManager2,menulayoutmanager;
    List<AskModel> askModelList;
    List<com.alatheer.shebinbook.home.slider.MenuItem> menuItemList;
    AskAdapter askAdapter;
    RecyclerView menu_recycler;
    MenuAdapter menuAdapter;
    MySharedPreference mySharedPreference ;
    LoginModel loginModel;
    String user_id,user_img,user_name,user_phone,trader_id,firebase_token,topic;
    Integer user_type,gender,city;
    Dialog dialog;
    RecyclerView message_recycler;
    MessageAdapter2 messageAdapter2;
    List<StoreDetails> messages_types_list;
    RecyclerView.LayoutManager storeDetailsManager;
    MessageAdapter messageAdapter;
    RecyclerView search_recycler;
    SearchStoresAdapter storesAdapter;
    Integer page = 1,flag;
    PostAdapter postAdapter;
    boolean isloading;
    int pastvisibleitem,visibleitemcount,totalitemcount,previous_total,view_threshold;
    int page2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        homeViewModel = new HomeViewModel(this);
        activityHomeBinding.setHomeviewmodel(homeViewModel);
        activityHomeBinding.swiperefresh.setOnRefreshListener(this);
        viewPager2 = activityHomeBinding.viewpager2;
        menu_recycler = findViewById(R.id.recycler_view);
        getSharedPreferanceData();
        homeViewModel.getAdvertisment();
        getDataIntent();
        init_ask();

        activityHomeBinding.linearShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(HomeActivity.this, CategoryActivity.class);
                startActivity(intent);
            }
        });
        activityHomeBinding.btnShebin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CategoryActivity.class));
            }
        });
        activityHomeBinding.btnAskManifa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, PostsActivity.class));
            }
        });
        activityHomeBinding.imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create_message_Dialog();
            }
        });
        activityHomeBinding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create_Alert_Dialog();
            }
        });

    }

    private void getDataIntent() {
        flag = getIntent().getIntExtra("flag",0);
        if (flag == 1){
            getToken();
        }else {
            firebase_token = getIntent().getStringExtra("token");
            homeViewModel.send_welcome_notification(firebase_token);
            FirebaseMessaging.getInstance().subscribeToTopic("android");
            getTopic();
        }
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
                homeViewModel.getSearch_stores(charSequence.toString());
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
        page2 = 1;
        isloading = false;
        pastvisibleitem =0;
        visibleitemcount =0;
        totalitemcount =0;
        previous_total=0;
        view_threshold = 10;
        message_recycler = view.findViewById(R.id.message_recycler);
        if (user_type == 4){
            homeViewModel.getMessages(trader_id,page2);
        }else {
            homeViewModel.getUserMessages(user_id,page2);
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
                visibleitemcount = layoutManager2.getChildCount();
                totalitemcount = layoutManager2.getItemCount();
                pastvisibleitem = layoutManager2.findFirstVisibleItemPosition();
                if(dy>0){
                    //Toast.makeText(HomeActivity.this, "hello", Toast.LENGTH_SHORT).show();
                    if(isloading){
                        if(totalitemcount>previous_total){
                            isloading = false;
                            previous_total = totalitemcount;
                        }
                    }
                    if(!isloading &&(totalitemcount-visibleitemcount)<= pastvisibleitem+view_threshold){
                        page++;
                        if (user_type == 4){
                            homeViewModel.TraderPagination(trader_id+"",page);

                        }else {
                            homeViewModel.UserPagination(user_id,page);

                        }
                        isloading = true;
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

    private void getSharedPreferanceData() {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getData().getUser().getId()+"";
        user_type = loginModel.getData().getUser().getRoleIdFk();
        gender = loginModel.getData().getUser().getGender();
        city = loginModel.getData().getUser().getCityId();
        if (user_type == 4){
            trader_id = loginModel.getData().getUser().getTraderId()+"";
        }

        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityHomeBinding.userImg);
        }
        activityHomeBinding.userName.setText(user_name);

    }

    private void init_ask() {
        askModelList = new ArrayList<>();
        askModelList.add(new AskModel(R.drawable.user2, "ما السياسات المتبعه لشراء منتج من الموقع ؟", "Mohamed Hamada"));
        askModelList.add(new AskModel(R.drawable.user2, "ما السياسات المتبعه لشراء منتج من الموقع ؟", "Mohamed Hamada"));
        askModelList.add(new AskModel(R.drawable.user2, "ما السياسات المتبعه لشراء منتج من الموقع ؟", "Mohamed Hamada"));
        askModelList.add(new AskModel(R.drawable.user2, "ما السياسات المتبعه لشراء منتج من الموقع ؟", "Mohamed Hamada"));

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
        View headerLayout = activityHomeBinding.navView.getHeaderView(0);
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
        activityHomeBinding.drawerlayout.openDrawer(GravityCompat.END);
        return true;
    }


    public void init_sliders(List<Slider> sliderList) {

        ///Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        viewPager2.setAdapter(new SliderAdapter(this,sliderList));
        viewPager2.setPadding(30,0,30,0);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.startAutoScroll();
        viewPager2.setInterval(4000);
        viewPager2.setCycle(true);
        viewPager2.setStopScrollWhenTouch(true);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                   homeViewModel.getCategories();
            }
        };
        handler.postDelayed(runnable, 200);
        //viewPager2.setPageTransformer(compositePageTransformer);
    }

    public void init_categories(List<com.alatheer.shebinbook.home.category.Category> category_list) {
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                homeViewModel.getData(user_id);
            }
        };
        handler.postDelayed(runnable, 300);
        categoryAdapter = new CategoryAdapter(category_list, this);
        activityHomeBinding.categoryRecycler.setAdapter(categoryAdapter);
        layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        activityHomeBinding.categoryRecycler.setHasFixedSize(true);
        activityHomeBinding.categoryRecycler.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String itemName = (String) item.getTitle();
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_logout:

                break;
            case R.id.nav_contact_us:

                break;
            case R.id.nav_setting:

                break;

            case R.id.nav_fav:
                break;
            case R.id.nav_home:

                break;
        }
        closeDrawer();
        return true;
    }
    private boolean closeDrawer() {
        activityHomeBinding.drawerlayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showmenu(View view) {
        activityHomeBinding.navView.setNavigationItemSelectedListener(this);
        openDrawer();
    }

    public void init_recycler(List<Post> data) {
        activityHomeBinding.recyclerAskMenofia.setVisibility(View.VISIBLE);
        askAdapter = new AskAdapter(data, this);
        layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityHomeBinding.recyclerAskMenofia.setHasFixedSize(true);
        activityHomeBinding.recyclerAskMenofia.setLayoutManager(layoutManager2);
        activityHomeBinding.recyclerAskMenofia.setAdapter(askAdapter);
    }
    private void get_messages_types() {
        messages_types_list = new ArrayList<>();
        messages_types_list.add(new StoreDetails(1,"الرسائل الواردة"));
        messages_types_list.add(new StoreDetails(2,"الرسائل المرسلة"));
        storeDetailsManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        //storeDetailsManager.setReverseLayout(true);
        messageAdapter = new MessageAdapter(messages_types_list,this);

    }

    public void init_messages_recycler(MessageAdapter2 messageAdapter2) {

        layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
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
                homeViewModel.getData(user_id);
                homeViewModel.getAdvertisment();
                homeViewModel.getposts(page,user_id);
                homeViewModel.getCategories();
                activityHomeBinding.swiperefresh.setRefreshing(false);
            }
        }, 2000);
    }

    public void setViewPagerGone() {
        homeViewModel.getCategories();
        activityHomeBinding.viewpager2.setVisibility(View.GONE);
    }

    public void dismiss() {
        activityHomeBinding.recyclerAskMenofia.setVisibility(View.GONE);
    }

    public void dismisscategorycart() {
        activityHomeBinding.categoryCard.setVisibility(View.GONE);
    }

    public void init_recycler2(List<Post> data) {
        activityHomeBinding.recyclerAskMenofia.setVisibility(View.VISIBLE);
        postAdapter = new PostAdapter(data, this);
        layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        activityHomeBinding.recyclerAskMenofia.setHasFixedSize(true);
        activityHomeBinding.recyclerAskMenofia.setLayoutManager(layoutManager2);
        activityHomeBinding.recyclerAskMenofia.setAdapter(postAdapter);
    }

    public void add_post_to_fav(Integer id) {
        homeViewModel.add_fav(id,user_id);
    }

    public void delete_post_from_fav(Integer id) {
        homeViewModel.delete_fav(id,user_id);
    }

    public void init_categories2(List<Category> data) {
    }

    public void delete_post(Post post) {
        CreateDeleteDialog(post.getId());
    }

    private void CreateDeleteDialog(Integer id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.delete_dialog, null);
        Button btn_delete = view.findViewById(R.id.btn_delete);
        Button btn_skip = view.findViewById(R.id.btn_skip);
        TextView txt = view.findViewById(R.id.txt);
        txt.setText("هل تريد حذف المنشور؟");
        builder.setView(view);
        Dialog dialog4 = builder.create();
        dialog4.show();
        Window window = dialog4.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeViewModel.delete_post(id,user_id);
                dialog4.dismiss();
                //CreateBasketDialog(product);
            }
        });
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog4.dismiss();
                //CreateDeleteDialog(product);
            }
        });
    }

    public void setData(ProfileData body) {
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                homeViewModel.getposts(page,user_id);
            }
        };
        handler.postDelayed(runnable, 200);
        user_img = body.getData().getUserImg();
        user_name = body.getData().getName();
        user_phone = body.getData().getPhone();
        user_type = loginModel.getData().getUser().getRoleIdFk();
        if (user_type == 4){
            trader_id = loginModel.getData().getUser().getTraderId()+"";
        }
        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityHomeBinding.userImg);
        }
        activityHomeBinding.userName.setText(user_name);
        init_navigation_menu();
    }

    public void getTopic() {
        if (gender == 1){
            //Toast.makeText(this, gender+"", Toast.LENGTH_SHORT).show();
            FirebaseMessaging.getInstance().subscribeToTopic("male")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "male";
                            if (!task.isSuccessful()) {
                                msg = "male";
                            }
                            Log.d("TAG", msg);
                            homeViewModel.update_token(user_id,firebase_token,"male");
                            //Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
            try {
                Thread.sleep(2000);
                FirebaseMessaging.getInstance().subscribeToTopic("all")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String msg = "all";
                                if (!task.isSuccessful()) {
                                    msg = "all";
                                }
                                Log.d("TAG", msg);
                                homeViewModel.update_token(user_id,firebase_token,"all");
                                //Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else if (gender == 2){
            FirebaseMessaging.getInstance().subscribeToTopic("all")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "all";
                            if (!task.isSuccessful()) {
                                msg = "all";
                            }
                            Log.d("TAG", msg);
                            homeViewModel.update_token(user_id,firebase_token,"all");
                            //Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
            try {
                Thread.sleep(2000);
                FirebaseMessaging.getInstance().subscribeToTopic("female")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String msg = "female";
                                if (!task.isSuccessful()) {
                                    msg = "female";
                                }
                                Log.d("TAG", msg);
                                homeViewModel.update_token(user_id,firebase_token,"female");
                                //Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(2000);
            FirebaseMessaging.getInstance().subscribeToTopic(city+"")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "city";
                            if (!task.isSuccessful()) {
                                msg = "city";
                            }
                            Log.d("TAG", msg);
                            homeViewModel.update_token(user_id,firebase_token,city+"");
                            //Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getToken() {
        try {
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if (task.isSuccessful()) {
                        firebase_token = task.getResult().getToken();
                        Log.e("firebase_token", firebase_token);
                        //homeViewModel.send_welcome_notification(firebase_token);
                        getTopic();

                        //mainViewModel.update_token(firebase_token);
                    }
                }
            });
        } catch (Exception e) {
            Log.e("exception_e", e.toString());
            e.printStackTrace();
        }
        FirebaseMessaging.getInstance().subscribeToTopic("android");

    }
}