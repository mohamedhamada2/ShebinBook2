package com.alatheer.shebinbook.trader.addproduct;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.authentication.signup.SignupActivity;
import com.alatheer.shebinbook.databinding.ActivityAddProductBinding;
import com.alatheer.shebinbook.home.AskAdapter;
import com.alatheer.shebinbook.home.MenuAdapter;
import com.alatheer.shebinbook.home.slider.MenuItem;
import com.alatheer.shebinbook.setting.ProfileData;
import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Integer IMG = 1;
    Integer REQUESTCAMERA = 2;
    ActivityAddProductBinding activityAddProductBinding;
    AddProductViewModel addProductViewModel ;
    Uri filepath;
    String product_name,product_price,product_details,user_img,user_name,user_phone,user_id;
    Integer trader_id,store_id,alboum_id;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    List<MenuItem> menuItemList;
    AskAdapter askAdapter;
    RecyclerView menu_recycler;
    MenuAdapter menuAdapter;
    RecyclerView.LayoutManager menulayoutmanager;
    Integer user_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        activityAddProductBinding = DataBindingUtil.setContentView(this,R.layout.activity_add_product);
        addProductViewModel = new AddProductViewModel(this);
        activityAddProductBinding.setAddproductviewmodel(addProductViewModel);
        getSharedPreferanceData();
        addProductViewModel.getData(user_id);
        getDataFromIntent();
        activityAddProductBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
    }

    private void getSharedPreferanceData() {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getData().getUser().getId()+"";
        user_img = loginModel.getData().getUser().getUserImg();
        user_name = loginModel.getData().getUser().getName();
        user_phone = loginModel.getData().getUser().getPhone();
        user_type = loginModel.getData().getUser().getRoleIdFk();
        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityAddProductBinding.userImg);
        }
        activityAddProductBinding.userName.setText(user_name);
    }

    private void getDataFromIntent() {
        store_id = getIntent().getIntExtra("store_id",0);
        alboum_id = getIntent().getIntExtra("gallery_id",0);
        trader_id = getIntent().getIntExtra("trader_id",0);
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
        View headerLayout = activityAddProductBinding.navView.getHeaderView(0);
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
        activityAddProductBinding.drawerlayout.openDrawer(GravityCompat.END);
        return true;
    }

    private void validation() {
        product_name = activityAddProductBinding.etProductName.getText().toString();
        product_price = activityAddProductBinding.etProductPrice.getText().toString();
        product_details = activityAddProductBinding.etDetails.getText().toString();
        if (!TextUtils.isEmpty(product_name)&&filepath != null){
            addProductViewModel.addproduct(trader_id+"",store_id+"",alboum_id+"",product_name,product_price,product_details,filepath);
        }else {
            if (TextUtils.isEmpty(product_name)){
                activityAddProductBinding.etProductName.setError("أدخل إسم المنتج");
            }else {
                activityAddProductBinding.etProductName.setError(null);
            }
            if (filepath == null){
                Toast.makeText(this, "من فضلك ادخل صورة المنتج", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void chooseimage(View view) {
        Check_ReadPermission(IMG);
    }

    private void Check_ReadPermission(Integer img) {
        if (ContextCompat.checkSelfPermission(AddProductActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(AddProductActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(AddProductActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Apply for multiple permissions together
            ActivityCompat.requestPermissions(AddProductActivity.this, new String[]{
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
        AlertDialog.Builder builder = new AlertDialog.Builder(AddProductActivity.this);
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
            activityAddProductBinding.linearAdd.setVisibility(View.GONE);
            activityAddProductBinding.productImg.setVisibility(View.VISIBLE);
            Picasso.get().load(filepath).into(activityAddProductBinding.productImg);
        } else if (requestCode == REQUESTCAMERA && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            final Bitmap bitmap = (Bitmap) bundle.get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
            filepath = Utilities.compressImage(AddProductActivity.this, path);
            activityAddProductBinding.linearAdd.setVisibility(View.GONE);
            activityAddProductBinding.productImg.setVisibility(View.VISIBLE);
            Picasso.get().load(filepath).into(activityAddProductBinding.productImg);
        }
    }

    public void showmenu(View view) {
        activityAddProductBinding.navView.setNavigationItemSelectedListener(this);
        openDrawer();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull  android.view.MenuItem item) {
        return false;
    }

    public void setData(ProfileData body) {
        user_img = body.getData().getUserImg();
        user_name = body.getData().getName();
        user_phone = body.getData().getPhone();

        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityAddProductBinding.userImg);
        }
        activityAddProductBinding.userName.setText(user_name);
        init_navigation_menu();
    }
}