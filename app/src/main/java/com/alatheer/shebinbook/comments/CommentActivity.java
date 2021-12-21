package com.alatheer.shebinbook.comments;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
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

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.databinding.ActivityCommentBinding;
import com.alatheer.shebinbook.home.MenuAdapter;
import com.alatheer.shebinbook.home.slider.MenuItem;
import com.alatheer.shebinbook.message.Datum;
import com.alatheer.shebinbook.message.MessageAdapter2;
import com.alatheer.shebinbook.posts.GenderAdapter;
import com.alatheer.shebinbook.posts.Post;
import com.alatheer.shebinbook.posts.PostsActivity;
import com.alatheer.shebinbook.search.SearchStoresAdapter;
import com.alatheer.shebinbook.stores.Store;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    ActivityCommentBinding activityCommentBinding;
    CommentViewModel commentViewModel;
    Post post;
    Integer IMG = 1;  Integer REQUESTCAMERA = 2;
    Uri filepath;
    List<MenuItem> menuItemList;
    MenuAdapter menuAdapter;
    RecyclerView.LayoutManager menulayoutmanager;
    RecyclerView menu_recycler;
    String comment;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String user_id,user_img,user_name,user_phone;
    RecyclerView.LayoutManager layoutManager;
    CommentAdapter commentAdapter;
    Dialog dialog;
    String reply;
    Integer user_type,comments_num;
    RecyclerView search_recycler,message_recycler;
    Integer trader_id2;
    SearchStoresAdapter storesAdapter;
    MessageAdapter2 messageAdapter2;
    RecyclerView.LayoutManager layoutManager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        activityCommentBinding = DataBindingUtil.setContentView(this,R.layout.activity_comment);
        commentViewModel = new CommentViewModel(this);
        activityCommentBinding.setCommentviewmodel(commentViewModel);
        activityCommentBinding.swiperefresh.setOnRefreshListener(this);
        getSharedPreferenceData();
        getDataIntent();
        init_navigation_menu();
        commentViewModel.getComments(post.getId()+"");
        activityCommentBinding.btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
        activityCommentBinding.addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check_ReadPermission(IMG);
            }
        });
        activityCommentBinding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create_Alert_Dialog();
            }
        });
        activityCommentBinding.imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create_message_Dialog();
            }
        });
    }
    private void getSharedPreferenceData() {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getData().getUser().getId()+"";
        trader_id2 = loginModel.getData().getUser().getTraderId();
        user_img = loginModel.getData().getUser().getUserImg();
        user_name = loginModel.getData().getUser().getName();
        user_phone = loginModel.getData().getUser().getPhone();
        user_type = loginModel.getData().getUser().getRoleIdFk();
        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityCommentBinding.userImg2);
        }
        activityCommentBinding.userName.setText(user_name);
        //gender_user = loginModel.getData().getUser().getGender()+"";
    }
    private void validation() {
        comment = activityCommentBinding.etPost.getText().toString();
        if(!TextUtils.isEmpty(comment)){
            commentViewModel.addcomment(user_id,filepath,comment,post.getUserIdFk()+"",post.getId()+"");
        }else {
            activityCommentBinding.etPost.setError("برجاء كتابة التعليق الذي تريد نشره");
        }
    }

    private void Check_ReadPermission(Integer img) {
        if (ContextCompat.checkSelfPermission(CommentActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(CommentActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(CommentActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Apply for multiple permissions together
            ActivityCompat.requestPermissions(CommentActivity.this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, img);
        }else {
            select_photo(img);
        }
    }

    private void select_photo(Integer img) {
        final  CharSequence[] items = {"كاميرا","ملفات الصور","الغاء"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CommentActivity.this);
        builder.setTitle("اضافة صورة للملف الشخصي");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("كاميرا")){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,REQUESTCAMERA);
                }else if (items[which].equals("ملفات الصور")){
                    Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    //startActivityForResult(intent.createChooser(intent,"Select File"),img);
                    startActivityForResult(intent,img);

                }else if (items[which].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            filepath = data.getData();
            activityCommentBinding.relativeAddImg.setVisibility(View.GONE);
            activityCommentBinding.commentImg2.setVisibility(View.VISIBLE);
            Picasso.get().load(filepath).into(activityCommentBinding.commentImg2);
        } else if (requestCode == REQUESTCAMERA && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            final Bitmap bitmap = (Bitmap) bundle.get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
            filepath = Utilities.compressImage(CommentActivity.this, path);
            activityCommentBinding.relativeAddImg.setVisibility(View.GONE);
            activityCommentBinding.commentImg2.setVisibility(View.VISIBLE);
            Picasso.get().load(filepath).into(activityCommentBinding.commentImg2);
        }

    }
    private void getDataIntent() {
        post = (Post) getIntent().getSerializableExtra("post");
        comments_num = post.getCountComments();
        activityCommentBinding.txtName.setText(post.getName());
        activityCommentBinding.txtComment.setText(post.getPost());
        activityCommentBinding.msgNum.setText(comments_num+"");
        if (post.getImg().equals("noimage")){
            activityCommentBinding.postImg.setVisibility(View.GONE);
        }else{
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+post.getImg()).into(activityCommentBinding.postImg);
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
        View headerLayout = activityCommentBinding.navView.getHeaderView(0);
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
        activityCommentBinding.drawerlayout.openDrawer(GravityCompat.END);
        return true;
    }

    public void refresh() {
        finish();
        startActivity(getIntent());
    }

    public void init_comments(List<Comment> body) {
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        activityCommentBinding.commentRecycler.setHasFixedSize(true);
        activityCommentBinding.commentRecycler.setLayoutManager(layoutManager);
        commentAdapter = new CommentAdapter(this,body);
        activityCommentBinding.commentRecycler.setAdapter(commentAdapter);
    }

    public void addreplay(Comment comment) {
        if (user_id.equals(comment.getPostUserIdFk()+"")||user_id.equals(comment.getCommentUserIdFk()+"")){
            createAlertDialog(comment);
        }
    }

    private void createAlertDialog(Comment comment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.add_reply_item, null);
        EditText et_reply = view.findViewById(R.id.et_post);
        Button btn_add = view.findViewById(R.id.btn_add_post);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,1200);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reply = et_reply.getText().toString();
                if(!TextUtils.isEmpty(reply)){
                    commentViewModel.addreplay(post.getId()+"",post.getUserIdFk()+"",comment.getId()+"",comment.getCommentUserIdFk()+"",user_id,reply);
                }else {
                    activityCommentBinding.etPost.setError("برجاء كتابة الرد الذي تريد نشره");
                }
            }
        });
    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getDataIntent();
                commentViewModel.getComments(post.getId()+"");
                activityCommentBinding.swiperefresh.setRefreshing(false);
            }
        }, 2000);
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
                commentViewModel.getSearch_stores(charSequence.toString());
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
            commentViewModel.getMessages(trader_id2);
        }else {
            commentViewModel.getUserMessages(user_id);
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
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void init_search_recycler(List<Store> data) {
        storesAdapter = new SearchStoresAdapter(this,data);
        layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true);
        search_recycler.setHasFixedSize(true);
        search_recycler.setLayoutManager(layoutManager2);
        search_recycler.setAdapter(storesAdapter);
    }

    public void init_messages_recycler(List<Datum> data) {
        messageAdapter2 = new MessageAdapter2(data,this);
        layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        message_recycler.setHasFixedSize(true);
        message_recycler.setLayoutManager(layoutManager2);
        message_recycler.setAdapter(messageAdapter2);
    }
}