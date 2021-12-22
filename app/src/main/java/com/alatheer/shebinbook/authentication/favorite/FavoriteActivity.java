package com.alatheer.shebinbook.authentication.favorite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.databinding.ActivityFavoriteBinding;
import com.alatheer.shebinbook.home.MenuAdapter;
import com.alatheer.shebinbook.home.slider.MenuItem;
import com.alatheer.shebinbook.message.Datum;
import com.alatheer.shebinbook.message.MessageAdapter2;
import com.alatheer.shebinbook.search.SearchStoresAdapter;
import com.alatheer.shebinbook.stores.Store;
import com.alatheer.shebinbook.stores.StoresAdapter;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    List<Favorite>favoriteList;
    FavoriteAdapter favoriteAdapter;
    RecyclerView fav_rec;
    RecyclerView.LayoutManager layoutManager;
    ActivityFavoriteBinding activityFavoriteBinding;
    FavoriteViewModel favoriteViewModel;
    List<MenuItem> menuItemList;
    MenuAdapter menuAdapter;
    RecyclerView.LayoutManager menulayoutmanager;
    RecyclerView menu_recycler;
    String user_img,user_name,user_phone;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Dialog dialog;
    RecyclerView search_recycler,message_recycler;
    SearchStoresAdapter storesAdapter;
    LinearLayoutManager layoutManager2;
    MessageAdapter2 messageAdapter2;
    Integer user_role,trader_id;
    String user_id;
    private int pastvisibleitem2,visibleitemcount2,totalitemcount2,previous_total2=0;
    int view_threshold2 = 10;
    Integer page2 = 1;
    boolean isloading2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        activityFavoriteBinding = DataBindingUtil.setContentView(this,R.layout.activity_favorite);
        favoriteViewModel = new FavoriteViewModel(this);
        activityFavoriteBinding.setFavoriteviewmodel(favoriteViewModel);
        activityFavoriteBinding.swiperefresh.setOnRefreshListener(this);
        fav_rec = activityFavoriteBinding.favoriteRecycler;
        getSharedPreferanceData();
        favoriteViewModel.getStores();
        init_navigation_menu();
        activityFavoriteBinding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create_Alert_Dialog();
            }
        });
        activityFavoriteBinding.imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create_message_Dialog();
            }
        });
        /*favoriteList = new ArrayList<>();
        favoriteList.add(new Favorite("...لوريم ايبسوم دولار سيت أميت ,كونسيكتيتور أدايبا يسكينج"));
        favoriteList.add(new Favorite("...لوريم ايبسوم دولار سيت أميت ,كونسيكتيتور أدايبا يسكينج"));
        favoriteList.add(new Favorite("...لوريم ايبسوم دولار سيت أميت ,كونسيكتيتور أدايبا يسكينج"));
        favoriteList.add(new Favorite("...لوريم ايبسوم دولار سيت أميت ,كونسيكتيتور أدايبا يسكينج"));*/
    }

    private void Create_message_Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.message_dialog_item, null);
        RecyclerView message_type_recycler = view.findViewById(R.id.message_type_recycler);
        ImageView cancel_img = view.findViewById(R.id.cancel_img);
        message_recycler = view.findViewById(R.id.message_recycler);
        if (user_role == 4){
            favoriteViewModel.getMessages(trader_id,page2);
        }else {
            favoriteViewModel.getUserMessages(user_id,page2);
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
                        if (user_role == 4){
                            favoriteViewModel.TraderPagination(trader_id+"",page2);
                        }else {
                            favoriteViewModel.UserPagination(user_id,page2);
                        }
                        isloading2 = true;
                    }

                }
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
                favoriteViewModel.getSearch_stores(charSequence.toString());
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
        user_role = loginModel.getData().getUser().getRoleIdFk();
        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityFavoriteBinding.userImg);
        }
        activityFavoriteBinding.userName.setText(user_name);
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
        menuAdapter = new MenuAdapter(menuItemList,this);
        menulayoutmanager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        View headerLayout = activityFavoriteBinding.navView.getHeaderView(0);
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
        activityFavoriteBinding.drawerlayout.openDrawer(GravityCompat.END);
        return true;
    }

    public void delete_from_fav(Store favorite) {
        favoriteViewModel.delete_fav(favorite);
    }

    public void init_recycler(List<Store> data) {
        favoriteAdapter = new FavoriteAdapter(data,this);
        layoutManager = new LinearLayoutManager(this);
        fav_rec.setHasFixedSize(true);
        fav_rec.setAdapter(favoriteAdapter);
        fav_rec.setLayoutManager(layoutManager);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                favoriteViewModel.getStores();
                activityFavoriteBinding.swiperefresh.setRefreshing(false);
            }
        }, 2000);
    }

    public void init_search_recycler(List<Store> data) {
        storesAdapter = new SearchStoresAdapter(this,data);
        layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true);
        search_recycler.setHasFixedSize(true);
        search_recycler.setLayoutManager(layoutManager2);
        search_recycler.setAdapter(storesAdapter);
    }

    public void init_messages_recycler(MessageAdapter2 messageAdapter2) {
        layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        message_recycler.setHasFixedSize(true);
        message_recycler.setLayoutManager(layoutManager2);
        message_recycler.setAdapter(messageAdapter2);
    }
}