package com.alatheer.shebinbook.products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.Utilities.Constants;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.databinding.ActivityProductsBinding;
import com.alatheer.shebinbook.home.MenuAdapter;
import com.alatheer.shebinbook.home.slider.Slider;
import com.alatheer.shebinbook.home.slider.SliderAdapter;
import com.alatheer.shebinbook.message.Datum;
import com.alatheer.shebinbook.message.MessageAdapter;
import com.alatheer.shebinbook.message.MessageAdapter2;
import com.alatheer.shebinbook.products.offers.OffersFragment;
import com.alatheer.shebinbook.products.rating.RatingFragment;
import com.alatheer.shebinbook.search.SearchStoresAdapter;
import com.alatheer.shebinbook.setting.ProfileData;
import com.alatheer.shebinbook.stores.Store;
import com.alatheer.shebinbook.stores.StoresActivity;
import com.alatheer.shebinbook.trader.images.ImageFragment;
import com.alatheer.shebinbook.trader.profile.ProfileActivity;
import com.alatheer.shebinbook.trader.updateoffer.UpdateOfferActivity;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    List<com.alatheer.shebinbook.home.slider.MenuItem> menuItemList;
    RecyclerView menu_recycler;
    MenuAdapter menuAdapter;
    LinearLayoutManager menulayoutmanager,storeDetailsManager;
    ActivityProductsBinding activityProductsBinding;
    ProductViewModel productViewModel;
    AutoScrollViewPager viewPager2;
    List<StoreDetails> storeDetailsList;
    StoreDetailsAdapter storeDetailsAdapter;
    String store_name,store_img,store_id,user_img,user_name,user_phone,user_id,store_address,store_phone,store_attendance,store_description,store_facebook,store_what_app,store_instagram,desc;
    Integer department;
    Fragment selectedfragment;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    int rating,user_type,flag;
    Dialog dialog4;
    Slider slider;
    Store store;
    Integer trader_id,trader_id2;
    Dialog dialog;
    RecyclerView message_recycler;
    MessageAdapter2 messageAdapter2;
    List<StoreDetails> messages_types_list;
    MessageAdapter messageAdapter;
    RecyclerView search_recycler;
    LinearLayoutManager layoutManager,layoutManager2;
    SearchStoresAdapter storesAdapter;
    private int pastvisibleitem2,visibleitemcount2,totalitemcount2,previous_total2=0;
    int view_threshold2 = 10;
    Integer page2 = 1;
    boolean isloading2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        activityProductsBinding = DataBindingUtil.setContentView(this,R.layout.activity_products);
        productViewModel = new ProductViewModel(this);
        activityProductsBinding.setProductsviewmodel(productViewModel);
        viewPager2 = activityProductsBinding.viewpager2;
        activityProductsBinding.swiperefresh.setOnRefreshListener(this);
        department = 1;
        getsharedpreferanceData();
        productViewModel.getData(user_id);
        getDataIntent();

        activityProductsBinding.constraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRatingDialog();
            }
        });
        activityProductsBinding.favImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (store.getFavourite() == 0){
                    productViewModel.add_fav(user_id,store_id);

                }else {
                    productViewModel.delete_fav(user_id,store_id);
                }
            }
        });
        activityProductsBinding.imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create_message_Dialog();
            }
        });
        activityProductsBinding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create_Alert_Dialog();
            }
        });
        activityProductsBinding.storeLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateImageDialog(store_img);
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
                productViewModel.getSearch_stores(charSequence.toString());
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
        page2 = 1;
        if (user_type == 4){
            productViewModel.getMessages(trader_id2,page2);
        }else {
            productViewModel.getUserMessages(user_id,page2);
        }
        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        message_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
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
                            productViewModel.TraderPagination(trader_id+"",page2);
                        }else {
                            productViewModel.UserPagination(user_id,page2);
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

    public void init_search_recycler(List<Store> data) {
        storesAdapter = new SearchStoresAdapter(ProductsActivity.this,data);
        layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        search_recycler.setHasFixedSize(true);
        search_recycler.setLayoutManager(layoutManager2);
        search_recycler.setAdapter(storesAdapter);
    }

    private void createRatingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.rating_dialog, null);
        RatingBar ratingbar = view.findViewById(R.id.rating_bar);
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                 rating = (int) v;
            }
        });
        Button btn_add = view.findViewById(R.id.btn_add);
        EditText et_post1 = view.findViewById(R.id.et_post);
        ImageView store_logo = view.findViewById(R.id.store_logo);
        TextView txt_store_name = view.findViewById(R.id.store_name);
        ImageView userimg = view.findViewById(R.id.user_img);
        txt_store_name.setText(store_name);
        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(userimg);
        }
        Picasso.get().load(Constants.BASE_URL +"public/uploads/images/images/"+store_img).into(store_logo);
        builder.setView(view);
        dialog4 = builder.create();
        dialog4.show();
        Window window = dialog4.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desc = et_post1.getText().toString();
                //Toast.makeText(ProductsActivity.this, desc, Toast.LENGTH_SHORT).show();
                productViewModel.add_rating(store_id,user_id,rating+"",desc);
                dialog4.dismiss();
                //CreateBasketDialog(product);
            }
        });
    }

    private void getsharedpreferanceData() {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        //user_id = loginModel.getData().getUser().getId()+"";
        user_img = loginModel.getData().getUser().getUserImg();
        user_name = loginModel.getData().getUser().getName();
        user_phone = loginModel.getData().getUser().getPhone();
        user_id = loginModel.getData().getUser().getId()+"";
        user_type = loginModel.getData().getUser().getRoleIdFk();
        if (user_type == 4){
            trader_id2 = loginModel.getData().getUser().getTraderId();
        }
        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityProductsBinding.userImg);
        }
        activityProductsBinding.userName.setText(user_name);
    }

    private void getDataIntent() {
        flag = getIntent().getIntExtra("flag",0);
        if (flag != 1){
            store = (Store) getIntent().getSerializableExtra("store");
            //Toast.makeText(this, store.getFavourite()+"", Toast.LENGTH_SHORT).show();
            trader_id = store.getTradeId();
            productViewModel.getStoreFromIntent(trader_id,user_id);
            getStoreDetails();
        }else{
            slider = (Slider) getIntent().getSerializableExtra("slider");
            trader_id = slider.getTraderIdFk();
            //trader_id2 = mySharedPreference.Get_UserData(this).getData().getUser().getTraderId();
            productViewModel.getStore(trader_id+"",user_id);

        }
    }
    private void creatTraderOfferDialog(Slider slider) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.trader_product_dialog, null);
        ImageView product_img = view.findViewById(R.id.product_img);
        TextView product_name = view.findViewById(R.id.product_name);
        TextView product_price = view.findViewById(R.id.product_price);
        TextView product_details = view.findViewById(R.id.product_details);
        TextView txt_store_name = view.findViewById(R.id.store_name);
        ImageView store_logo = view.findViewById(R.id.store_logo);
        TextView product_price_offer = view.findViewById(R.id.product_price_offer);
        ImageView bin_img = view.findViewById(R.id.bin_img);
        ImageView edit_img = view.findViewById(R.id.edit_img);
        builder.setView(view);
        Dialog dialog3 = builder.create();
        dialog3.show();
        product_name.setText(slider.getTitle());
        if (slider.getPriceBeforeOffer() != null){
            product_price.setText(slider.getPriceBeforeOffer()+"LE");
        }else {
            product_price.setText("أدخل السعر");
        }
        if (slider.getPriceAfterOffer()!= null){
            product_price_offer.setText(slider.getPriceAfterOffer()+"LE");
        }else {
            product_price_offer.setText("أدخل العرض");
        }
        if (product_details != null){
            product_details.setText(slider.getDescription());
        }else {
            product_details.setText("------");
        }
        txt_store_name.setText(store_name);
        Picasso.get().load(Constants.BASE_URL +"public/uploads/images/images/"+store_img).into(store_logo);
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/advertisement/"+slider.getImg()).into(product_img);
        Window window = dialog3.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        edit_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductsActivity.this, UpdateOfferActivity.class);
                intent.putExtra("slider", slider);
                startActivity(intent);
                dialog3.dismiss();
                //CreateBasketDialog(product);
            }
        });
        bin_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3.dismiss();
                CreateDeleteDialog(slider);
            }
        });
    }

    private void CreateDeleteDialog(Slider slider) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductsActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.delete_dialog, null);
        Button btn_delete = view.findViewById(R.id.btn_delete);
        Button btn_skip = view.findViewById(R.id.btn_skip);
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
                productViewModel.delete_offer(slider.getId());
                dialog4.dismiss();
                //CreateBasketDialog(product);
            }
        });
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog4.dismiss();
                // CreateDeleteDialog(product);
            }
        });
    }

    private void CreateOfferDialog(Slider slider) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.product_dialog, null);
        ImageView product_img = view.findViewById(R.id.product_img);
        ImageView image_store_img = view.findViewById(R.id.store_logo);
        TextView product_name = view.findViewById(R.id.product_name);
        TextView txt_store_name = view.findViewById(R.id.store_name);
        TextView product_price = view.findViewById(R.id.product_price);
        TextView product_price_offer = view.findViewById(R.id.product_price_offer);
        TextView txt_product_decription = view.findViewById(R.id.product_decription);
        ImageView msg_img = view.findViewById(R.id.msg_img);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        product_name.setText(slider.getTitle());
        if (slider.getPriceBeforeOffer()!= null){
            product_price.setText(slider.getPriceBeforeOffer()+"");
        }else {
            product_price.setText("أطلب السعر");
        }
        if (user_type == 4){
            msg_img.setVisibility(View.GONE);
        }
        if (slider.getPriceAfterOffer() != null){
            product_price_offer.setText(slider.getPriceAfterOffer());
        }else {
            product_price_offer.setText("أطلب العرض");
        }
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/advertisement/"+slider.getImg()).into(product_img);
        Picasso.get().load(Constants.BASE_URL +"public/uploads/images/images/"+store_img).into(image_store_img);
        txt_store_name.setText(store_name);
        txt_product_decription.setText(slider.getDescription());
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        msg_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateBasketDialog(slider);
                dialog.dismiss();
            }
        });
    }

    private void CreateBasketDialog(Slider slider) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.basket_dialog, null);
        ImageView product_img = view.findViewById(R.id.product_img);
        EditText et_post = view.findViewById(R.id.et_post);
        ImageView user_img2 = view.findViewById(R.id.user_img);
        AppCompatButton btn_add = view.findViewById(R.id.btn_add);
        TextView txt_store_name = view.findViewById(R.id.store_name);
        ImageView image_store_img = view.findViewById(R.id.store_logo);
        builder.setView(view);
        Dialog dialog2 = builder.create();
        dialog2.show();
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/advertisement/"+slider.getImg()).into(product_img);
        Picasso.get().load(Constants.BASE_URL +"public/uploads/images/images/"+store_img).into(image_store_img);
        Picasso.get().load(Constants.BASE_URL +"public/uploads/images/images/"+user_img).into(user_img2);
        txt_store_name.setText(store_name);
        Window window = dialog2.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String post = et_post.getText().toString();
                if (!TextUtils.isEmpty(post)){
                    productViewModel.add_message(user_id,slider.getId()+"",slider.getTraderIdFk()+"",store_id,post);
                    dialog2.dismiss();
                }else {
                    et_post.setError("أكتب رسالتك");
                }
            }
        });
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
        storeDetailsManager.setReverseLayout(true);
        storeDetailsAdapter = new StoreDetailsAdapter(storeDetailsList,this);
        activityProductsBinding.storeDetailsRecycler.setAdapter(storeDetailsAdapter);
        activityProductsBinding.storeDetailsRecycler.setLayoutManager(storeDetailsManager);
        activityProductsBinding.storeDetailsRecycler.setHasFixedSize(true);

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
        View headerLayout = activityProductsBinding.navView.getHeaderView(0);
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
        activityProductsBinding.drawerlayout.openDrawer(GravityCompat.END);
        return true;
    }


    public void init_sliders(List<Slider> sliderList) {
        ///Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        if (!sliderList.isEmpty()){
            viewPager2.setAdapter(new SliderAdapter(this,sliderList));
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
            activityProductsBinding.viewpager2.setVisibility(View.GONE);
        }
        //viewPager2.setPageTransformer(compositePageTransformer);
    }

    public void showmenu(View view) {
        //activityHomeBinding.navView.setNavigationItemSelectedListener(this);
        openDrawer();
    }

    public void setData(StoreDetails storeDetails) {
        department = storeDetails.getId();
        if (department == 1) {
            if (mySharedPreference.Get_UserData(ProductsActivity.this).getData().getUser().getTraderId()==  trader_id ){
                selectedfragment = new com.alatheer.shebinbook.trader.profile.DetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("store_id", store_id);
                bundle.putString("store_phone", store_phone);
                bundle.putString("store_address", store_address);
                bundle.putString("store_attendance", store_attendance);
                bundle.putString("store_desc", store_description);
                bundle.putString("store_facebook", store_facebook);
                bundle.putString("store_whats", store_what_app);
                bundle.putString("store_instagram", store_instagram);
                bundle.putString("store_img", store_img);
                bundle.putString("store_name", store_name);
                selectedfragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedfragment).commit();
            }else {
                selectedfragment = new DetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("store_phone", store_phone);
                bundle.putString("store_address", store_address);
                bundle.putString("store_attendance", store_attendance);
                bundle.putString("store_description", store_description);
                bundle.putString("store_facebook", store_facebook);
                bundle.putString("store_whats", store_what_app);
                bundle.putString("store_instagram", store_instagram);
                selectedfragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedfragment).commit();
            }
        }else if (department == 2){
            if(mySharedPreference.Get_UserData(ProductsActivity.this).getData().getUser().getTraderId()==  trader_id){
                selectedfragment = new ImageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("store_id", store_id);
                bundle.putString("store_name",store_name);
                bundle.putString("store_image",store_img);
                bundle.putString("trader_id",trader_id+"");
                selectedfragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedfragment).commit();
            }else {
                selectedfragment = new com.alatheer.shebinbook.products.images.ImageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("store_id", store_id);
                bundle.putString("store_name",store_name);
                bundle.putString("store_image",store_img);
                bundle.putString("trader_id",trader_id+"");
                selectedfragment.setArguments(bundle);
                //Log.e("trader_id",trader_id+"");
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedfragment).commit();
            }
        }else if (department == 3){
            if(mySharedPreference.Get_UserData(ProductsActivity.this).getData().getUser().getTraderId()==  trader_id){
                selectedfragment = new com.alatheer.shebinbook.trader.profile.GalleryFragment();
                Bundle bundle = new Bundle();
                bundle.putString("store_id", store_id);
                bundle.putString("store_name",store_name);
                bundle.putString("store_image",store_img);
                selectedfragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedfragment).commit();
            }else {
                selectedfragment = new GalleryFragment();
                Bundle bundle = new Bundle();
                bundle.putString("store_id", store_id);
                bundle.putString("store_name",store_name);
                bundle.putString("store_image",store_img);
                bundle.putInt("trader_id",trader_id);
                selectedfragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedfragment).commit();
            }
        }else if(department == 4){
            selectedfragment = new OffersFragment();
            Bundle bundle = new Bundle();
            bundle.putString("trader_id", trader_id+"");
            bundle.putString("store_name", store_name);
            bundle.putString("store_logo", store_img);
            bundle.putString("store_id", store_id);
            selectedfragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedfragment).commit();
        }else if(department == 5){
            selectedfragment = new RatingFragment();
            Bundle bundle = new Bundle();
            bundle.putString("store_id", store_id);
            selectedfragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedfragment).commit();
        }
    }

    public void dismiss() {
        dialog4.dismiss();
    }

    public void setStoreData(Store store) {
        this.store = store;
        store_name = this.store.getStoreName();
        store_img =  this.store.getLogo();
        store_id = this.store.getId()+"";
        trader_id = this.store.getTradeId();
        store_address = this.store.getStoreAddress();
        store_phone = this.store.getStoreMobile();
        store_attendance = this.store.getAppointmentsWork();
        store_description = this.store.getDescription();
        store_instagram = this.store.getInstagram();;
        store_what_app = this.store.getStoreWhats();
        store_facebook = this.store.getFacebook();
        activityProductsBinding.storeName.setText(store_name);
        activityProductsBinding.txtDescription.setText(store.getMini_description());
        getStoreDetails();
        Picasso.get().load(Constants.BASE_URL +"public/uploads/images/images/"+store_img).into(activityProductsBinding.storeLogo);
        selectedfragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("store_phone",store_phone);
        bundle.putString("store_address",store_address);
        bundle.putString("store_attendance",store_attendance);
        bundle.putString("store_description",store_description);

        selectedfragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedfragment).commit();
        productViewModel.getAdvertisment(slider.getTraderIdFk()+"");
        if (this.store.getFavourite() == 0){
            activityProductsBinding.favImg.setImageResource(R.drawable.store_fav);
        }else {
            activityProductsBinding.favImg.setImageResource(R.drawable.fav3);
        }
        if (user_type != 4){
            CreateOfferDialog(slider);
        }else {
            Integer slider_trader_id = slider.getTraderIdFk();
            //Log.d("trader_id_",id+"");
            if (trader_id2.equals(slider_trader_id)){
                creatTraderOfferDialog(slider);

            }else {

                CreateOfferDialog(slider);
            }
        }

    }

    public void make_fav() {
        store.setFavourite(1);
        activityProductsBinding.favImg.setImageResource(R.drawable.fav3);
    }

    public void dismake_fav() {
        store.setFavourite(0);
        activityProductsBinding.favImg.setImageResource(R.drawable.store_fav);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                productViewModel.getData(user_id);
                getDataIntent();
                getStoreDetails();
                activityProductsBinding.swiperefresh.setRefreshing(false);
            }
        }, 2000);
    }

    public void setStoreDataIntent(Store store) {
        this.store = store;
        store_name = store.getStoreName();
        store_img =  store.getLogo();
        store_id = store.getId()+"";
        store_phone = store.getStoreMobile();
        store_address = store.getStoreAddress();
        store_attendance = store.getAppointmentsWork();
        store_description = store.getDescription();
        store_facebook = store.getFacebook();
        store_what_app = store.getStoreWhats();
        store_instagram = store.getInstagram();
        activityProductsBinding.txtDescription.setText(store.getMini_description());
        activityProductsBinding.storeName.setText(store_name);
        Picasso.get().load(Constants.BASE_URL +"public/uploads/images/images/"+store_img).into(activityProductsBinding.storeLogo);
        selectedfragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("store_phone",store_phone);
        bundle.putString("store_address",store_address);
        bundle.putString("store_attendance",store_attendance);
        bundle.putString("store_description",store_description);
        bundle.putString("store_facebook",store_facebook);
        bundle.putString("store_whats",store_what_app);
        bundle.putString("store_instagram",store_instagram);
        selectedfragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedfragment).commit();
        productViewModel.getAdvertisment(trader_id+"");
       // Toast.makeText(this, store.getFavourite()+"", Toast.LENGTH_SHORT).show();
        try{
            if (store.getFavourite() == 0){
                activityProductsBinding.favImg.setImageResource(R.drawable.store_fav);
            }else {
                activityProductsBinding.favImg.setImageResource(R.drawable.fav3);
            }
        }catch (Exception e){
            store.setFavourite(1);
            activityProductsBinding.favImg.setImageResource(R.drawable.fav3);
        }
    }

    public void setData(ProfileData body) {
        user_img = body.getData().getUserImg();
        user_name = body.getData().getName();
        user_phone = body.getData().getPhone();

        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityProductsBinding.userImg);
        }
        activityProductsBinding.userName.setText(user_name);
        init_navigation_menu();
    }
}