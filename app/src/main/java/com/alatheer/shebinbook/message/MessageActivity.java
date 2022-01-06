package com.alatheer.shebinbook.message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.databinding.ActivityMessageBinding;
import com.alatheer.shebinbook.home.MenuAdapter;
import com.alatheer.shebinbook.home.slider.MenuItem;
import com.alatheer.shebinbook.message.reply.ReplayAdapter;
import com.alatheer.shebinbook.message.reply.Reply;
import com.alatheer.shebinbook.posts.Post;
import com.alatheer.shebinbook.products.ProductsActivity;
import com.alatheer.shebinbook.products.StoreDetails;
import com.alatheer.shebinbook.search.SearchStoresAdapter;
import com.alatheer.shebinbook.setting.ProfileData;
import com.alatheer.shebinbook.stores.Store;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String user_id,user_img,user_name,user_phone,message;
    Integer user_type;
    MenuAdapter menuAdapter;
    RecyclerView.LayoutManager menulayoutmanager;
    List<MenuItem> menuItemList;
    RecyclerView menu_recycler;
    ActivityMessageBinding activityMessageBinding;
    MessageViewModel messageViewModel;
    Datum datum;
    Integer trader_id;
    ReplayAdapter replayAdapter;
    LinearLayoutManager layoutManager,layoutManager2;
    Dialog dialog;
    RecyclerView search_recycler,message_recycler;
    SearchStoresAdapter storesAdapter;
    MessageAdapter2 messageAdapter2;
    private boolean isloading;
    private int pastvisibleitem,visibleitemcount,totalitemcount,previous_total=0;
    int view_threshold = 10;
    Integer page =1;
    Integer type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        activityMessageBinding = DataBindingUtil.setContentView(this,R.layout.activity_message);
        messageViewModel = new MessageViewModel(this);
        getSharedPreferenceData();
        messageViewModel.getData(user_id);
        getDataIntent();
        messageViewModel.getreplies(datum.getId());
        //get_messages_types();
        activityMessageBinding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create_Alert_Dialog();
            }
        });
        activityMessageBinding.imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create_message_Dialog();
            }
        });
        activityMessageBinding.messageImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateImageDialog(datum);
            }
        });
    }

    private void CreateImageDialog(Datum datum) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MessageActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.image_item2, null);
        ImageView img = view.findViewById(R.id.img);
        if (datum.getType().equals("2")){
            //Log.e("offer_img","https://mymissing.online/shebin_book/public/uploads/advertisement/"+datum.getOfferImg());
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/advertisement/"+datum.getOfferImg()).into(img);
        }else if (datum.getType().equals("1")){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+datum.getImg()).into(img);
        }
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
                messageViewModel.getSearch_stores(charSequence.toString());
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
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }
    private void Create_message_Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.message_dialog_item, null);
        RecyclerView message_type_recycler = view.findViewById(R.id.message_type_recycler);
        ImageView cancel_img = view.findViewById(R.id.cancel_img);
        message_recycler = view.findViewById(R.id.message_recycler);
        page = 1;
        if (user_type == 4){
            messageViewModel.getMessages(trader_id+"",page);
        }else {
            messageViewModel.getUserMessages(user_id,page);
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
                    if(isloading){
                        if(totalitemcount>previous_total){
                            isloading = false;
                            previous_total = totalitemcount;

                        }
                    }
                    if(!isloading &&(totalitemcount-visibleitemcount)<= pastvisibleitem+view_threshold){
                        page++;
                        if (user_type == 4){
                            messageViewModel.TraderPagination(trader_id+"",page);
                        }else {
                            messageViewModel.UserPagination(user_id,page);
                        }
                        isloading = true;
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

    private void getSharedPreferenceData() {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getData().getUser().getId()+"";
        user_type = loginModel.getData().getUser().getRoleIdFk();
        trader_id = loginModel.getData().getUser().getTraderId();
        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityMessageBinding.userImg2);
        }
        activityMessageBinding.userName.setText(user_name);
        activityMessageBinding.btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
        if (user_type == 4){
            type = 1;
        }else {
            type = 0;
        }
        //gender_user = loginModel.getData().getUser().getGender()+"";
    }
    private void validation() {
        message = activityMessageBinding.etPost.getText().toString();
        if(!TextUtils.isEmpty(message)){
            messageViewModel.addreply(datum.getUserIdFk(),message,datum.getStoreIdFk(),datum.getProductIdFk(),trader_id,datum.getId(),type);
        }else {
            activityMessageBinding.etPost.setError("برجاء كتابة التعليق الذي تريد نشره");
        }
    }
    private void getDataIntent() {
        datum = (Datum) getIntent().getSerializableExtra("message");
        activityMessageBinding.txtName.setText(datum.getName()+datum.getLastName());
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+datum.getUserImg()).into(activityMessageBinding.userimg);
        activityMessageBinding.messageTxt.setText(datum.getMessage());
        if (datum.getType().equals("2")){
            //Log.e("offer_img","https://mymissing.online/shebin_book/public/uploads/advertisement/"+datum.getOfferImg());
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/advertisement/"+datum.getOfferImg()).into(activityMessageBinding.messageImg);
        }else if (datum.getType().equals("1")){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+datum.getImg()).into(activityMessageBinding.messageImg);
        }
    }
    public void showmenu(View view) {
        //activityPostsBinding.navView.setNavigationItemSelectedListener(this);
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
        View headerLayout = activityMessageBinding.navView.getHeaderView(0);
        menu_recycler = headerLayout.findViewById(R.id.recycler_view);
        menu_recycler.setHasFixedSize(true);
        menu_recycler.setAdapter(menuAdapter);
        menu_recycler.setLayoutManager(menulayoutmanager);
        RoundedImageView img = headerLayout.findViewById(R.id.user_img);
        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(img);
        }
        TextView txt_name = headerLayout.findViewById(R.id.txt_user_name);
        TextView txt_phone = headerLayout.findViewById(R.id.txt_phone);
        txt_name.setText(user_name);
        txt_phone.setText(user_phone);
    }

    private boolean openDrawer() {
        activityMessageBinding.drawerlayout.openDrawer(GravityCompat.END);
        return true;
    }

    public void refresh() {
        finish();
        startActivity(getIntent());
    }

    public void init_replies(List<Reply> data) {
        replayAdapter = new ReplayAdapter(data,this);
        activityMessageBinding.repliesRecycler.setAdapter(replayAdapter);
        activityMessageBinding.repliesRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true);
        activityMessageBinding.repliesRecycler.setLayoutManager(layoutManager);
    }

    public void init_search_recycler(List<Store> data) {
        storesAdapter = new SearchStoresAdapter(MessageActivity.this,data);
        layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
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

    public void setData(ProfileData body) {
        user_img = body.getData().getUserImg();
        user_name = body.getData().getName();
        user_phone = body.getData().getPhone();

        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityMessageBinding.userImg2);
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityMessageBinding.userImg);
        }
        activityMessageBinding.userName.setText(user_name);
        init_navigation_menu();
    }

    /*private void get_messages_types() {
        messages_types_list = new ArrayList<>();
        messages_types_list.add(new StoreDetails(1,"الرسائل الواردة"));
        messages_types_list.add(new StoreDetails(2,"الرسائل المرسلة"));
        storeDetailsManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        //storeDetailsManager.setReverseLayout(true);
        messageAdapter = new MessageAdapter(messages_types_list,this);

    }*/
}