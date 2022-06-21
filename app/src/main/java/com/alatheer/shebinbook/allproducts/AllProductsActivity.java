package com.alatheer.shebinbook.allproducts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.databinding.ActivityAllProductsBinding;
import com.alatheer.shebinbook.home.MenuAdapter;
import com.alatheer.shebinbook.home.slider.MenuItem;
import com.alatheer.shebinbook.message.MessageAdapter2;
import com.alatheer.shebinbook.products.ProductSliderClient;
import com.alatheer.shebinbook.products.ProductsSlider_for_Trader;
import com.alatheer.shebinbook.search.SearchStoresAdapter;
import com.alatheer.shebinbook.setting.ProfileData;
import com.alatheer.shebinbook.stores.Store;
import com.alatheer.shebinbook.trader.addproduct.AddProductActivity;
import com.makeramen.roundedimageview.RoundedImageView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AllProductsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    ActivityAllProductsBinding activityAllProductsBinding;
    ProductViewModel productViewModel;
    Integer gallery_id;
    ProductAdapter2 productAdapter2;
    LinearLayoutManager productlayoutManager,layoutManager2;
    Dialog dialog,dialog2,dialog3,dialog4;
    String post;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String user_id,user_img,user_name,user_phone;
    List<MenuItem> menuItemList;
    MenuAdapter menuAdapter;
    RecyclerView.LayoutManager menulayoutmanager;
    RecyclerView menu_recycler;
    Integer user_role,trader_id,trader_id2,store_id;
    Integer user_type;
    String store_img,store_name;
    RecyclerView message_recycler,search_recycler;
    MessageAdapter2 messageAdapter2;
    SearchStoresAdapter storesAdapter;
    private int pastvisibleitem2,visibleitemcount2,totalitemcount2,previous_total2=0;
    int view_threshold2 = 10;
    Integer page2 = 1;
    boolean isloading2;
    List<Integer> product_id_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);
        activityAllProductsBinding = DataBindingUtil.setContentView(this,R.layout.activity_all_products);
        productViewModel = new ProductViewModel(this);
        activityAllProductsBinding.setProductviewmodel(productViewModel);
        product_id_list = new ArrayList<>();
        activityAllProductsBinding.swiperefresh.setOnRefreshListener(this);
        getDataIntent();
        getSharedPreferenceData();
        productViewModel.getData(user_id);
        productViewModel.getStore(trader_id2+"",user_id);
        productViewModel.getproducts(gallery_id+"");
        activityAllProductsBinding.addToAlboumImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllProductsActivity.this, AddProductActivity.class);
                intent.putExtra("gallery_id",gallery_id);
                intent.putExtra("trader_id",trader_id);
                intent.putExtra("store_id",store_id);
                startActivity(intent);
            }
        });
        activityAllProductsBinding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create_Alert_Dialog();
            }
        });
        activityAllProductsBinding.imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create_message_Dialog();
            }
        });
        activityAllProductsBinding.deleteFromAlboumImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!product_id_list.isEmpty()){
                    productViewModel.delete_products(product_id_list);
                }else {
                    Toast.makeText(AllProductsActivity.this, "قم بتحديد المنتجات التي تريد حذفها", Toast.LENGTH_SHORT).show();
                }
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
        page2 = 1;
        message_recycler = view.findViewById(R.id.message_recycler);
        if (user_role == 4){
            productViewModel.getMessages(trader_id,page2);
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
                            productViewModel.TraderPagination(trader_id+"",page2);
                        }else {
                            productViewModel.UserPagination(user_id,page2);
                        }
                        isloading2 = true;
                    }

                }
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

    private void getSharedPreferenceData() {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getData().getUser().getId()+"";
        user_img = loginModel.getData().getUser().getUserImg();
        user_name = loginModel.getData().getUser().getName();
        user_phone = loginModel.getData().getUser().getPhone();
        user_role = loginModel.getData().getUser().getRoleIdFk();
        if (user_role == 4){
            trader_id = loginModel.getData().getUser().getTraderId();
        }
        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityAllProductsBinding.userImg);
        }
        if(user_role != 4){
            activityAllProductsBinding.addToAlboumImg.setVisibility(View.GONE);
            activityAllProductsBinding.deleteFromAlboumImg.setVisibility(View.GONE);
        }else {
            if (trader_id.equals(trader_id2)){
                activityAllProductsBinding.deleteFromAlboumImg.setVisibility(View.VISIBLE);
                activityAllProductsBinding.addToAlboumImg.setVisibility(View.VISIBLE);
            }else {
                activityAllProductsBinding.addToAlboumImg.setVisibility(View.GONE);
                activityAllProductsBinding.deleteFromAlboumImg.setVisibility(View.GONE);
            }
        }
        activityAllProductsBinding.userName.setText(user_name);
    }

    private void getDataIntent() {
        gallery_id = getIntent().getIntExtra("gallery_id",0);
        trader_id2 = getIntent().getIntExtra("trader_id",0);
        store_id = getIntent().getIntExtra("store_id",0);
        //store_img = getIntent().getStringExtra("store_logo");
        //Toast.makeText(this, trader_id2+"", Toast.LENGTH_SHORT).show();
    }


    public void init_recycler(List<Product> data) {
        productAdapter2 = new ProductAdapter2(data,this,user_role);
        activityAllProductsBinding.productRecycler.setHasFixedSize(true);
        productlayoutManager = new GridLayoutManager(this,2,RecyclerView.VERTICAL,false);
        activityAllProductsBinding.productRecycler.setHasFixedSize(true);
        activityAllProductsBinding.productRecycler.setLayoutManager(productlayoutManager);
        activityAllProductsBinding.productRecycler.setAdapter(productAdapter2);
    }

    public void createAlertDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.trader_product_dialog, null);
        if (Utilities.isNetworkAvailable(this)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ProductModel> call = getDataService.get_products(gallery_id+"");
            call.enqueue(new Callback<ProductModel>() {
                @Override
                public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                    if (response.isSuccessful()){
                        SliderView sliderView = view.findViewById(R.id.imageSlider2);
                        ProductSliderClient productSliderClient = new ProductSliderClient(AllProductsActivity.this,response.body().getData(),store_name,store_img);
                        sliderView.setSliderAdapter(productSliderClient);
                        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                        sliderView.setIndicatorSelectedColor(R.color.purple_500);
                        sliderView.setIndicatorUnselectedColor(Color.GRAY);
                        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
                        sliderView.startAutoCycle();
                        for (int i = 0;i<response.body().getData().size();i++){
                            if (product.getId().equals(response.body().getData().get(i).getId())){
                                sliderView.setCurrentPagePosition(i);
                            }else {
                                //Toast.makeText(AllProductsActivity.this, product.getId()+"", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProductModel> call, Throwable t) {

                }
            });
        }
        /*ImageView product_img = view.findViewById(R.id.product_img);
        TextView product_name = view.findViewById(R.id.product_name);
        TextView product_price = view.findViewById(R.id.product_price);
        TextView product_price_offer = view.findViewById(R.id.product_price_offer);
        TextView product_decription = view.findViewById(R.id.product_decription);
        TextView txt_store_name = view.findViewById(R.id.store_name);
        ImageView msg_img = view.findViewById(R.id.msg_img);
        ImageView store_img2 = view.findViewById(R.id.store_logo);
        if (user_role == 4){
            msg_img.setVisibility(View.GONE);
        }
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        product_name.setText(product.getTitle());
        txt_store_name.setText(store_name);
        if (product.getPrice() != null){
            product_price_offer.setText(product.getPrice()+"");
        }else {
            product_price_offer.setText("أطلب السعر");
        }
        if (product.getDetails() != null){
            product_decription.setText(product.getDetails());
        }else {
            product_decription.setText(null);
        }
        product_price.setVisibility(View.GONE);
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+product.getImg()).into(product_img);
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+store_img).into(store_img2);*/
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    public void CreateBasketDialog(Product product) {
        dialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.basket_dialog, null);
        ImageView product_img = view.findViewById(R.id.product_img);
        EditText et_post = view.findViewById(R.id.et_post);
        ImageView user_img2 = view.findViewById(R.id.user_img);
        ImageView store_img2 = view.findViewById(R.id.store_logo);
        TextView txt_store_name = view.findViewById(R.id.store_name);
        AppCompatButton btn_add = view.findViewById(R.id.btn_add);
        builder.setView(view);
        dialog2 = builder.create();
        dialog2.show();
        txt_store_name.setText(store_name);
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+product.getImg()).into(product_img);
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(user_img2);
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+store_img).into(store_img2);
        Window window = dialog2.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post = et_post.getText().toString();
                if (!TextUtils.isEmpty(post)){
                    productViewModel.add_message(user_id,product.getId()+"",product.getTraderIdFk()+"",product.getStoreIdFk()+"",post);
                }else {
                    et_post.setError("أكتب رسالتك");
                }
            }
        });
    }

    public void dismiss_dialog() {
        dialog2.dismiss();
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
        if (user_role == 4){
            menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("متجري",R.drawable.store));
        }
        menuAdapter = new MenuAdapter(menuItemList,this);
        menulayoutmanager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        View headerLayout = activityAllProductsBinding.navView.getHeaderView(0);
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
        activityAllProductsBinding.drawerlayout.openDrawer(GravityCompat.END);
        return true;
    }

    public void createTraderDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.trader_product_dialog, null);
        if (Utilities.isNetworkAvailable(this)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ProductModel> call = getDataService.get_products(gallery_id+"");
            call.enqueue(new Callback<ProductModel>() {
                @Override
                public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                    if (response.isSuccessful()){
                        SliderView sliderView = view.findViewById(R.id.imageSlider2);
                        sliderView.setSliderAdapter(new ProductsSlider_for_Trader(AllProductsActivity.this,response.body().getData(),store_name,store_img));
                        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                        sliderView.setIndicatorSelectedColor(R.color.purple_500);
                        sliderView.setIndicatorUnselectedColor(Color.GRAY);
                        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
                        sliderView.startAutoCycle();
                        for (int i = 0;i<response.body().getData().size();i++){
                            if (product.getId().equals(response.body().getData().get(i).getId())){
                                sliderView.setCurrentPagePosition(i);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProductModel> call, Throwable t) {

                }
            });
        }
        /*ImageView product_img = view.findViewById(R.id.product_img);
        TextView product_name = view.findViewById(R.id.product_name);
        TextView product_price = view.findViewById(R.id.product_price);
        TextView product_details = view.findViewById(R.id.product_details);
        TextView product_price_offer = view.findViewById(R.id.product_price_offer);
        ImageView bin_img = view.findViewById(R.id.bin_img);
        ImageView edit_img = view.findViewById(R.id.edit_img);
        TextView txt_store_name = view.findViewById(R.id.store_name);
        ImageView store_img2 = view.findViewById(R.id.store_logo);
        product_price.setVisibility(View.GONE);
        builder.setView(view);
        dialog3 = builder.create();
        dialog3.show();
        product_name.setText(product.getTitle());
        txt_store_name.setText(store_name);
        if (product.getPrice() != null){
            product_price_offer.setText(product.getPrice()+"");
        }else {
            product_price_offer.setText("أدخل السعر ان أردت");
        }
        if (product_details != null){
            product_details.setText(product.getDetails());
        }else {
            product_price.setText("--------");
        }
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+product.getImg()).into(product_img);
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+store_img).into(store_img2);
        edit_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllProductsActivity.this, UpdateProductActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
                dialog3.dismiss();
                //CreateBasketDialog(product);
            }
        });
        bin_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3.dismiss();
                CreateDeleteDialog(product);
            }
        });*/
        builder.setView(view);
        dialog3 = builder.create();
        dialog3.show();
        Window window = dialog3.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void CreateDeleteDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.delete_dialog, null);
        Button btn_delete = view.findViewById(R.id.btn_delete);
        Button btn_skip = view.findViewById(R.id.btn_skip);
        builder.setView(view);
        dialog4 = builder.create();
        dialog4.show();
        Window window = dialog4.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               productViewModel.delete_product(product.getId());
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

    public void createsuccessDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.success_dialog, null);
        TextView txt = view.findViewById(R.id.txt);
        txt.setText("تم الحذف");
        builder.setView(view);
        dialog3 = builder.create();
        dialog3.show();
        Window window = dialog3.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(450,LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog3.isShowing()) {
                    dialog3.dismiss();
                    productViewModel.getproducts(gallery_id+"");
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

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getDataIntent();
                productViewModel.getproducts(gallery_id+"");
                activityAllProductsBinding.swiperefresh.setRefreshing(false);
            }
        }, 2000);
    }

    public void addstore(Store store) {
        store_img = store.getLogo();
        store_name = store.getStoreName();
    }

    public void init_messages_recycler(MessageAdapter2 messageAdapter2) {
        layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        message_recycler.setHasFixedSize(true);
        message_recycler.setLayoutManager(layoutManager2);
        message_recycler.setAdapter(messageAdapter2);
    }

    public void init_search_recycler(List<Store> data) {
        storesAdapter = new SearchStoresAdapter(this,data);
        layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        search_recycler.setHasFixedSize(true);
        search_recycler.setLayoutManager(layoutManager2);
        search_recycler.setAdapter(storesAdapter);
    }

    public void setData(ProfileData body) {
        user_img = body.getData().getUserImg();
        user_name = body.getData().getName();
        user_phone = body.getData().getPhone();
        user_type = loginModel.getData().getUser().getRoleIdFk();


        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityAllProductsBinding.userImg);
        }
        activityAllProductsBinding.userName.setText(user_name);
        init_navigation_menu();
    }

    public void get_products_id(List<Integer> product_id_list) {
        this.product_id_list = product_id_list;
    }

    public void dismiss() {
        dialog3.dismiss();
    }
}