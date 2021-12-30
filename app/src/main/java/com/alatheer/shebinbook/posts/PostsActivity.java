package com.alatheer.shebinbook.posts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.authentication.signup.SignupActivity;
import com.alatheer.shebinbook.databinding.ActivityPostsBinding;
import com.alatheer.shebinbook.home.AskModel;
import com.alatheer.shebinbook.home.MenuAdapter;
import com.alatheer.shebinbook.message.Datum;
import com.alatheer.shebinbook.message.MessageAdapter;
import com.alatheer.shebinbook.message.MessageAdapter2;
import com.alatheer.shebinbook.search.SearchStoresAdapter;
import com.alatheer.shebinbook.setting.ProfileData;
import com.alatheer.shebinbook.stores.Store;
import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PostsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,GenderClick, SwipeRefreshLayout.OnRefreshListener {
    List<AskModel> askModelList;
    AskAdapter askAdapter;
    LinearLayoutManager layoutManager2;
    ActivityPostsBinding activityPostsBinding;
    PostViewModel postViewModel;
    List<com.alatheer.shebinbook.home.slider.MenuItem> menuItemList;
    MenuAdapter menuAdapter;
    RecyclerView.LayoutManager menulayoutmanager;
    RecyclerView menu_recycler;
    List<Gender> genderList;
    AlertDialog dialog;
    String gender_id,user_id,post,gender_user,user_name,user_phone,user_img;
    Integer IMG =1;
    Uri filepath;
    Integer REQUESTCAMERA = 2;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Integer user_type;
    private boolean isloading;
    private int pastvisibleitem,visibleitemcount,totalitemcount,previous_total=0;
    int view_threshold = 10;
    int page =1 ;
    Integer trader_id2;
    RecyclerView search_recycler,message_recycler;
    SearchStoresAdapter storesAdapter;
    MessageAdapter2 messageAdapter2;
    private boolean isloading2;
    private int pastvisibleitem2,visibleitemcount2,totalitemcount2,previous_total2=0;
    int view_threshold2 = 10;
    Integer page2 = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        activityPostsBinding = DataBindingUtil.setContentView(this,R.layout.activity_posts);
        postViewModel = new PostViewModel(this);
        activityPostsBinding.setPostviewmodel(postViewModel);
        activityPostsBinding.swiperefresh.setOnRefreshListener(this);
        gender_id = "3";
        getSharedPreferenceData();
        postViewModel.getData(user_id);
        getGenders();
        postViewModel.getPosts(gender_user,page,user_id);
        activityPostsBinding.groupImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAlertDialog(genderList);
            }
        });
        activityPostsBinding.btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
        activityPostsBinding.addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check_ReadPermission(IMG);
            }
        });
        activityPostsBinding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Create_Alert_Dialog();
            }
        });
        activityPostsBinding.imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create_message_Dialog();
            }
        });
        activityPostsBinding.askRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        postViewModel.PerformPagination(gender_user,page,user_id);
                        isloading = true;
                    }

                }
            }
        });
    }

    private void getSharedPreferenceData() {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getData().getUser().getId()+"";
        trader_id2 = loginModel.getData().getUser().getTraderId();
        gender_user = loginModel.getData().getUser().getGender()+"";
        user_img = loginModel.getData().getUser().getUserImg();
        user_name = loginModel.getData().getUser().getName();
        user_phone = loginModel.getData().getUser().getPhone();
        user_type = loginModel.getData().getUser().getRoleIdFk();
        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityPostsBinding.userImg2);
        }
        activityPostsBinding.userName.setText(user_name);
    }

    private void validation() {
        post = activityPostsBinding.etPost.getText().toString();
        if(!TextUtils.isEmpty(post)){
            postViewModel.addpost(user_id,gender_id,filepath,post);
        }else {
            activityPostsBinding.etPost.setError("برجاء كتابة البوست الذي تريد نشره");
        }
    }

    private void Check_ReadPermission(Integer img) {
        if (ContextCompat.checkSelfPermission(PostsActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(PostsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(PostsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Apply for multiple permissions together
            ActivityCompat.requestPermissions(PostsActivity.this, new String[]{
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
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PostsActivity.this);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            filepath = data.getData();
            activityPostsBinding.relativeAddImg.setVisibility(View.GONE);
            activityPostsBinding.postImg.setVisibility(View.VISIBLE);
            Picasso.get().load(filepath).into(activityPostsBinding.postImg);
        }else if (requestCode == REQUESTCAMERA && resultCode == Activity.RESULT_OK){
            Bundle bundle = data.getExtras();
            final Bitmap bitmap = (Bitmap) bundle.get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
            filepath = Utilities.compressImage(PostsActivity.this,path);
            activityPostsBinding.relativeAddImg.setVisibility(View.GONE);
            activityPostsBinding.postImg.setVisibility(View.VISIBLE);
            Picasso.get().load(filepath).into(activityPostsBinding.postImg);
        }
    }

    private void getGenders() {
        genderList = new ArrayList<>();
        genderList.add(new Gender("1","رجال فقط",R.drawable.man));
        genderList.add(new Gender("2","نساء فقط",R.drawable.woman));
        genderList.add(new Gender("3","الجميع",R.drawable.groups1));
    }

    private void CreateAlertDialog(List<Gender> genderList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.gender_pop_up, null);
        RecyclerView gender_recycler = view.findViewById(R.id.gender_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        GenderAdapter genderAdapter = new GenderAdapter(PostsActivity.this,genderList,this::onItemClicked);
        gender_recycler.setAdapter(genderAdapter);
        gender_recycler.setHasFixedSize(true);
        gender_recycler.setLayoutManager(linearLayoutManager);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        /*WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(window.getAttributes());
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);*/
        window.setGravity(Gravity.LEFT);
        window.setLayout(430,1000);
    }

    public void showmenu(View view) {
        activityPostsBinding.navView.setNavigationItemSelectedListener(this);
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
        View headerLayout = activityPostsBinding.navView.getHeaderView(0);
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
        activityPostsBinding.drawerlayout.openDrawer(GravityCompat.END);
        return true;
    }
    private void init_ask() {
        askModelList = new ArrayList<>();
        askModelList.add(new AskModel(R.drawable.user2, "ما السياسات المتبعه لشراء منتج من الموقع ؟", "Mohamed Hamada"));
        askModelList.add(new AskModel(R.drawable.user2, "ما السياسات المتبعه لشراء منتج من الموقع ؟", "Mohamed Hamada"));
        askModelList.add(new AskModel(R.drawable.user2, "ما السياسات المتبعه لشراء منتج من الموقع ؟", "Mohamed Hamada"));
        askModelList.add(new AskModel(R.drawable.user2, "ما السياسات المتبعه لشراء منتج من الموقع ؟", "Mohamed Hamada"));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
        return false;
    }

    @Override
    public void onItemClicked(Gender gender) {
        gender_id = gender.getId();
        activityPostsBinding.groupImg.setImageResource(gender.getImage());
        Toast.makeText(this, gender_id, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    public void init_recycler(List<Post> data) {
        askAdapter = new AskAdapter(data, this);
        layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activityPostsBinding.askRecycler.setHasFixedSize(true);
        activityPostsBinding.askRecycler.setLayoutManager(layoutManager2);
        activityPostsBinding.askRecycler.setAdapter(askAdapter);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                postViewModel.getPosts(gender_user,1,user_id);
                activityPostsBinding.swiperefresh.setRefreshing(false);
            }
        }, 2000);
    }

    public void add_post_to_fav(Integer id) {
        postViewModel.add_fav(id,user_id);
    }

    public void delete_post_from_fav(Integer id) {
        postViewModel.delete_fav(id,user_id);
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
                postViewModel.delete_post(id,user_id);
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
                postViewModel.getSearch_stores(charSequence.toString());
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
            postViewModel.getMessages(trader_id2,page2);
        }else {
            postViewModel.getUserMessages(user_id,page2);
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
                            postViewModel.TraderPagination(trader_id2+"",page);
                        }else {
                            postViewModel.UserPagination(user_id,page);
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

    public void setData(ProfileData body) {
        user_img = body.getData().getUserImg();
        user_name = body.getData().getName();
        user_phone = body.getData().getPhone();

        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityPostsBinding.userImg2);
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityPostsBinding.userImg);
        }
        activityPostsBinding.userName.setText(user_name);
        init_navigation_menu();
    }
}