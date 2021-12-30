package com.alatheer.shebinbook.trader.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;
import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
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
import com.alatheer.shebinbook.Utilities.Constants;
import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.databinding.ActivityProfileBinding;
import com.alatheer.shebinbook.home.MenuAdapter;
import com.alatheer.shebinbook.home.slider.MenuItem;
import com.alatheer.shebinbook.home.slider.Slider;
import com.alatheer.shebinbook.message.Datum;
import com.alatheer.shebinbook.message.MessageAdapter;
import com.alatheer.shebinbook.message.MessageAdapter2;
import com.alatheer.shebinbook.products.StoreDetails;
import com.alatheer.shebinbook.products.offers.OffersFragment;
import com.alatheer.shebinbook.products.rating.RatingFragment;
import com.alatheer.shebinbook.search.SearchStoresAdapter;
import com.alatheer.shebinbook.setting.ProfileData;
import com.alatheer.shebinbook.stores.Store;
import com.alatheer.shebinbook.stores.StoresActivity;
import com.alatheer.shebinbook.stores.StoresAdapter;
import com.alatheer.shebinbook.trader.addoffer.AddOfferActivity;
import com.alatheer.shebinbook.trader.addproduct.AddProductActivity;
import com.alatheer.shebinbook.trader.images.ImageFragment;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    ActivityProfileBinding activityProfileBinding;
    ProfileViewModel profileViewModel;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String trader_id,user_img,user_name,user_phone,store_id,store_image,store_name,store_phone,store_address,store_desc,store_attendance,store_facebook,store_what_app,store_instagram,store_mini_desc;
    String store_lat,store_lon,store_products,store_offer,store_city,store_gov,user_id;
    String slider_trader_id;
    Integer user_type,department;
    List<MenuItem> menuItemList;
    MenuAdapter menuAdapter;
    RecyclerView.LayoutManager menulayoutmanager,storeDetailsManager;
    AutoScrollViewPager viewPager2;
    RecyclerView menu_recycler;
    List<StoreDetails> storeDetailsList;
    StoreDetailsAdapter storeDetailsAdapter;
    Fragment selectedfragment;
    Store store;
    Dialog dialog,dialog3;
    RecyclerView message_recycler;
    MessageAdapter2 messageAdapter2;
    List<StoreDetails> messages_types_list;
    MessageAdapter messageAdapter;
    RecyclerView search_recycler;
    LinearLayoutManager layoutManager,layoutManager2;
    SearchStoresAdapter storesAdapter;
    Slider slider;
    Integer flag;
    Integer IMG =1;
    Uri filepath;
    Integer REQUESTCAMERA = 2;
    ImageView store_img;
    LinearLayout linearLayout;
    public static Activity fa;
    private boolean isloading2;
    private int pastvisibleitem2,visibleitemcount2,totalitemcount2,previous_total2=0;
    int view_threshold2 = 10;
    Integer page2 = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        activityProfileBinding = DataBindingUtil.setContentView(this,R.layout.activity_profile);
        profileViewModel = new ProfileViewModel(this);
        viewPager2 = activityProfileBinding.viewpager2;
        activityProfileBinding.setProfileviewmodel(profileViewModel);
        activityProfileBinding.swiperefresh.setOnRefreshListener(this);
        fa = this;
        getSharedPreferenceData();
        profileViewModel.getData(user_id);
        getDataIntent();
        activityProfileBinding.editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateEditDialog();
            }
        });

        activityProfileBinding.btnAddOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, AddOfferActivity.class));

            }
        });
        activityProfileBinding.imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create_message_Dialog();
            }
        });
        activityProfileBinding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create_Alert_Dialog();
            }
        });
        activityProfileBinding.storeLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateImageDialog(store_image);
            }
        });

    }

    private void CreateEditDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.edit_dialog, null);
        linearLayout = view.findViewById(R.id.linear_add);
        store_img = view.findViewById(R.id.store_img);
        EditText et_store_name = view.findViewById(R.id.et_store_name);
        EditText et_details = view.findViewById(R.id.et_details);
        Button edit_btn = view.findViewById(R.id.btn_edit);
        et_store_name.setText(store_name);
        et_details.setText(store_mini_desc);
        if (store_image.equals("noImage") || store_image == null) {
            linearLayout.setVisibility(View.VISIBLE);
            store_img.setVisibility(View.GONE);
            //Toast.makeText(this, store_image, Toast.LENGTH_SHORT).show();
        } else {
            linearLayout.setVisibility(View.GONE);
            store_img.setVisibility(View.VISIBLE);
            Log.e("base_url",Constants.BASE_URL +"public/uploads/images/images/"+store_image);
            Picasso.get().load(Constants.BASE_URL +"public/uploads/images/images/"+store_image).into(store_img);
        }
        store_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check_ReadPermission(IMG);
            }
        });
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileViewModel.edit_profile(store_id, store_desc,store_attendance,store_facebook,
                        store_what_app,et_details.getText().toString(),store_address,store_phone,
                        filepath,store_gov,store_city,store_lon,
                        store_lat,store_offer,store_products,
                        store_instagram,et_store_name.getText().toString());
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check_ReadPermission(IMG);
            }
        });
        builder.setView(view);
        dialog3 = builder.create();
        dialog3.show();
        Window window = dialog3.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }



    private void getDataIntent() {
        flag = getIntent().getIntExtra("flag",0);
        if (flag != 1){
            profileViewModel.get_store(trader_id);
        }else{
            slider = (Slider) getIntent().getSerializableExtra("slider");
            slider_trader_id = slider.getTraderIdFk()+"";
            //trader_id2 = mySharedPreference.Get_UserData(this).getData().getUser().getTraderId();
            profileViewModel.get_store(slider_trader_id);

        }
    }

    private void getSharedPreferenceData() {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getData().getUser().getId()+"";
        trader_id = loginModel.getData().getUser().getTraderId()+"";
        user_img = loginModel.getData().getUser().getUserImg();
        user_name = loginModel.getData().getUser().getName();
        user_phone = loginModel.getData().getUser().getPhone();
        user_type = loginModel.getData().getUser().getRoleIdFk();
        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityProfileBinding.userImg);
        }
        activityProfileBinding.userName.setText(user_name);

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
        View headerLayout = activityProfileBinding.navView.getHeaderView(0);
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
        activityProfileBinding.drawerlayout.openDrawer(GravityCompat.END);
        return true;
    }


    public void init_sliders(List<Slider> sliderList) {
        if (!sliderList.isEmpty()){
            viewPager2.setAdapter(new SliderAdapter(this,sliderList,store_name,store_image));
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
            activityProfileBinding.viewpager2.setVisibility(View.GONE);
        }
        ///Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        //viewPager2.setPageTransformer(compositePageTransformer);
    }

    public void showmenu(View view) {
        openDrawer();
    }

    public void setData(Store store) {
        this.store = store;
        getStoreDetails();
        store_id = store.getId()+"";
        store_name = store.getStoreName();
        store_image = store.getLogo();
        if (store.getStoreAddress()!= null){
            store_address = store.getStoreAddress();
        }else {
            store_address = "";
        }
        if (store.getStoreMobile()!=null){
            store_phone = store.getStoreMobile();
        }else {
            store_phone = "";
        }
        if (store.getAppointmentsWork()!= null){
            store_attendance = store.getAppointmentsWork();
        }else {
            store_attendance = "";
        }
        if (store.getDescription()!= null){
            store_desc = store.getDescription();
        }else {
            store_desc = "";
        }
        if (store.getFacebook()!= null){
            store_facebook = store.getFacebook();
        }else {
            store_facebook = "";
        }
        if (store.getStoreWhats()!= null){
            store_what_app = store.getStoreWhats();
        }else {
            store_what_app = "";
        }
        if (store.getMini_description()!=null){
            store_mini_desc =store.getMini_description();
        }else {
            store_mini_desc = "";
        }
        if (store.getInstagram()!= null){
            store_instagram = store.getInstagram();
        }else {
            store_instagram = "";
        }
        if (store.getOffersProducts()!= null){
            store_products = store.getOffersProducts();
        }else {
            store_products = "";
        }
        if (store.getOffersWords()!= null){
            store_offer = store.getOffersWords();
        }else {
            store_offer = "";
        }
        if (store.getLatMap()!= null){
            store_lat = store.getLatMap();
        }else {
            store_lat = "";
        }
        if (store.getLongMap()!= null){
            store_lon = store.getLongMap();
        }else {
            store_lon = "";
        }
        if (store.getCity() != null){
            store_city = store.getCity()+"";
        }else {
            store_city = "";
        }
        if (store.getGovernorate() != null){
            store_gov = store.getGovernorate()+"";
        }else {
            store_gov = "";
        }
        activityProfileBinding.storeName.setText(store.getStoreName());
        activityProfileBinding.txtDescription.setText(store.getMini_description());
        Picasso.get().load(Constants.BASE_URL +"public/uploads/images/images/"+ store.getLogo()).into(activityProfileBinding.storeLogo);
        department = 1;
        selectedfragment = new com.alatheer.shebinbook.trader.profile.DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("store_id", store_id);
        bundle.putString("store_name", store_name);
        bundle.putString("store_phone", store_phone);
        bundle.putString("store_address", store_address);
        bundle.putString("store_attendance", store_attendance);
        bundle.putString("store_desc", store_desc);
        bundle.putString("store_facebook",store_facebook);
        bundle.putString("store_whats",store_what_app);
        bundle.putString("store_instagram",store_instagram);
        bundle.putString("store_mini",store_mini_desc);
        bundle.putString("offer",store.getOffersWords());
        bundle.putString("products",store.getOffersProducts());
        selectedfragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedfragment).commit();
        profileViewModel.getAdvertisment(trader_id);

    }
    private void getStoreDetails() {
        storeDetailsList = new ArrayList<>();
        storeDetailsList.add(new StoreDetails(1,"التفاصيل"));
        storeDetailsList.add(new StoreDetails(2,"المعرض"));
        try {
            if(store.getOffersProducts()!= null){
                storeDetailsList.add(new StoreDetails(3,store.getOffersProducts()));
            }else {
                storeDetailsList.add(new StoreDetails(3,"المنتجات"));
            }
        }catch (Exception e){
            storeDetailsList.add(new StoreDetails(3,"المنتجات"));
        }
        try {
            if (store.getOffersWords() != null){
                storeDetailsList.add(new StoreDetails(4,store.getOffersWords()));
            }else {
                storeDetailsList.add(new StoreDetails(4,"العروض"));
            }
        }catch (Exception e){
            storeDetailsList.add(new StoreDetails(4,"العروض"));
        }
        storeDetailsList.add(new StoreDetails(5,"مراجعة"));
        storeDetailsManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        //storeDetailsManager.setReverseLayout(true);
        storeDetailsAdapter = new StoreDetailsAdapter(storeDetailsList,this);
        activityProfileBinding.storeDetailsRecycler.setAdapter(storeDetailsAdapter);
        activityProfileBinding.storeDetailsRecycler.setLayoutManager(storeDetailsManager);
        activityProfileBinding.storeDetailsRecycler.setHasFixedSize(true);

    }

    public void setDetailsData(StoreDetails storeDetails) {
        department = storeDetails.getId();
        if (department == 1) {
            selectedfragment = new DetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("store_id", store_id);
            bundle.putString("store_phone", store_phone);
            bundle.putString("store_address", store_address);
            bundle.putString("store_attendance", store_attendance);
            bundle.putString("store_desc", store_desc);
            bundle.putString("store_facebook", store_facebook);
            bundle.putString("store_whats", store_what_app);
            bundle.putString("store_instagram", store_instagram);
            bundle.putString("store_img", store_image);
            bundle.putString("store_name",store_name);
            selectedfragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedfragment).commit();
        }else if (department == 2){
            selectedfragment = new ImageFragment();
            Bundle bundle = new Bundle();
            bundle.putString("store_id", store_id);
            bundle.putString("store_name",store_name);
            bundle.putString("store_image",store_image);
            bundle.putString("trader_id",trader_id);
            selectedfragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedfragment).commit();
        }else if (department == 3){
            selectedfragment = new GalleryFragment();
            Bundle bundle = new Bundle();
            bundle.putString("store_id", store_id);
            bundle.putString("store_name",store_name);
            bundle.putString("store_image",store_image);
            selectedfragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedfragment).commit();
        }else if(department == 4){
            selectedfragment = new OffersFragment();
            Bundle bundle = new Bundle();
            bundle.putString("trader_id", trader_id+"");
            bundle.putString("store_name", store_name);
            bundle.putString("store_logo", store_image);
            bundle.putString("store_id", store_id);
            selectedfragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedfragment).commit();
        }else if(department == 5){
            selectedfragment = new RatingFragment();
            Bundle bundle = new Bundle();
            bundle.putString("store_id", store_id);
            bundle.putString("trader_id",trader_id);
            selectedfragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedfragment).commit();
        }
    }


    public void delete_offer(Integer id) {
        profileViewModel.delete_offer(id);
    }

    public void createsuccessdialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.success_dialog, null);
        TextView txt = view.findViewById(R.id.txt);
        txt.setText("تم الحذف");
        builder.setView(view);
        Dialog dialog3 = builder.create();
        dialog3.show();
        Window window = dialog3.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(450, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog3.isShowing()) {
                    dialog3.dismiss();
                    profileViewModel.getAdvertisment(trader_id+"");
                    //Intent intent = new Intent(AllProductsActivity.this,AllProductsActivity.class);
                    //intent.putExtra("gallery_id",product.getAlboumIdFk());
                    //startActivity(intent);
                    //finish();
                }
            }
        };
        dialog3.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 3000);
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
                profileViewModel.getSearch_stores(charSequence.toString());
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
    private void CreateImageDialog(String product_img) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.image_item, null);
        ImageView img = view.findViewById(R.id.img);
        Picasso.get().load(Constants.BASE_URL +"public/uploads/images/images/"+product_img).resize(600,600).into(img);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(600, 600);
    }
    private void Create_message_Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.message_dialog_item, null);
        RecyclerView message_type_recycler = view.findViewById(R.id.message_type_recycler);
        ImageView cancel_img = view.findViewById(R.id.cancel_img);
        message_recycler = view.findViewById(R.id.message_recycler);
        profileViewModel.getMessages(trader_id,page2);
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
                            profileViewModel.TraderPagination(trader_id,page2);
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
    public void init_messages_recycler(MessageAdapter2 messageAdapter2) {
        layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        message_recycler.setHasFixedSize(true);
        message_recycler.setLayoutManager(layoutManager2);
        message_recycler.setAdapter(messageAdapter2);
    }

    public void init_search_recycler(List<Store> data) {
        storesAdapter = new SearchStoresAdapter(ProfileActivity.this,data);
        layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        search_recycler.setHasFixedSize(true);
        search_recycler.setLayoutManager(layoutManager2);
        search_recycler.setAdapter(storesAdapter);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                profileViewModel.getData(user_id);
                profileViewModel.get_store(trader_id);
                activityProfileBinding.swiperefresh.setRefreshing(false);
            }
        }, 2000);
    }
    private void Check_ReadPermission(Integer img) {
        if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Apply for multiple permissions together
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, img);
        } else {
            select_photo(img);
        }
    }

    private void select_photo(Integer img) {
        final CharSequence[] items = {"كاميرا", "ملفات الصور", "الغاء"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("اضافة صورة للملف الشخصي");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("كاميرا")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUESTCAMERA);
                } else if (items[which].equals("ملفات الصور")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    //startActivityForResult(intent.createChooser(intent,"Select File"),img);
                    startActivityForResult(intent, img);

                } else if (items[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            filepath = data.getData();
            linearLayout.setVisibility(View.GONE);
            store_img.setVisibility(View.VISIBLE);
            Picasso.get().load(filepath).into(store_img);
        }else if (requestCode == REQUESTCAMERA && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            final Bitmap bitmap = (Bitmap) bundle.get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
            filepath = Uri.parse(path);
            linearLayout.setVisibility(View.GONE);
            store_img.setVisibility(View.VISIBLE);
            Picasso.get().load(filepath).into(store_img);
        }
    }

    public void dismiss() {
        dialog3.dismiss();
        finish();
        startActivity(getIntent());
    }

    public void setUserData(ProfileData body) {
        user_img = body.getData().getUserImg();
        user_name = body.getData().getName();
        user_phone = body.getData().getName();

        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityProfileBinding.userImg);
        }
        activityProfileBinding.userName.setText(user_name);
        init_navigation_menu();
    }
}