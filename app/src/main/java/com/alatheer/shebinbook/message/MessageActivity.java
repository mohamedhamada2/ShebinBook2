package com.alatheer.shebinbook.message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.alatheer.shebinbook.products.StoreDetails;
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
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        activityMessageBinding = DataBindingUtil.setContentView(this,R.layout.activity_message);
        messageViewModel = new MessageViewModel(this);
        getSharedPreferenceData();
        getDataIntent();
        messageViewModel.getreplies(datum.getId());
        //get_messages_types();
        init_navigation_menu();
    }


    private void getSharedPreferenceData() {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getData().getUser().getId()+"";
        user_img = loginModel.getData().getUser().getUserImg();
        user_name = loginModel.getData().getUser().getName();
        user_phone = loginModel.getData().getUser().getPhone();
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
        //gender_user = loginModel.getData().getUser().getGender()+"";
    }
    private void validation() {
        message = activityMessageBinding.etPost.getText().toString();
        if(!TextUtils.isEmpty(message)){
            messageViewModel.addreply(datum.getUserIdFk(),message,datum.getStoreIdFk(),datum.getProductIdFk(),trader_id,datum.getId());
        }else {
            activityMessageBinding.etPost.setError("برجاء كتابة التعليق الذي تريد نشره");
        }
    }
    private void getDataIntent() {
        datum = (Datum) getIntent().getSerializableExtra("message");
        activityMessageBinding.txtName.setText(datum.getName());
        activityMessageBinding.messageTxt.setText(datum.getMessage());
        if (datum.getImg() == null){
            activityMessageBinding.messageImg.setVisibility(View.GONE);
        }else{
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
    /*private void get_messages_types() {
        messages_types_list = new ArrayList<>();
        messages_types_list.add(new StoreDetails(1,"الرسائل الواردة"));
        messages_types_list.add(new StoreDetails(2,"الرسائل المرسلة"));
        storeDetailsManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        //storeDetailsManager.setReverseLayout(true);
        messageAdapter = new MessageAdapter(messages_types_list,this);

    }*/
}