package com.alatheer.shebinbook.trader.updateproduct;

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
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.allproducts.AllProductsActivity;
import com.alatheer.shebinbook.allproducts.Product;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.databinding.ActivityUpdateProductBinding;
import com.alatheer.shebinbook.home.MenuAdapter;
import com.alatheer.shebinbook.home.slider.MenuItem;
import com.alatheer.shebinbook.trader.addproduct.AddProductActivity;
import com.alatheer.shebinbook.trader.profile.ProfileActivity;
import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class UpdateProductActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityUpdateProductBinding activityUpdateProductBinding;
    UpdateProductViewModel updateProductViewModel;
    Product product;
    com.alatheer.shebinbook.products.Product product2;
    String product_name,product_details,product_price,store_id,alboum_id,trader_id,product_id,user_phone,user_img,user_name;
    Integer IMG = 1,REQUESTCAMERA=2;
    Uri filepath;
    MySharedPreference mySharedPreference;
    LoginModel loginModel ;
    Dialog dialog3;
    Integer flag;
    RecyclerView menu_recycler;
    MenuAdapter menuAdapter;
    RecyclerView.LayoutManager menulayoutmanager;
    Integer user_type;
    List<MenuItem> menuItemList;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        activityUpdateProductBinding = DataBindingUtil.setContentView(this,R.layout.activity_update_product);
        updateProductViewModel = new UpdateProductViewModel(this);
        activityUpdateProductBinding.setUpdateproductviewmodel(updateProductViewModel);
        getSharedPreferenceData();
        init_navigation_menu();
        getDataFromIntent();
        activityUpdateProductBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
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
        View headerLayout = activityUpdateProductBinding.navView.getHeaderView(0);
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

    private void getSharedPreferenceData() {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        trader_id = loginModel.getData().getUser().getTraderId()+"";
        user_img = loginModel.getData().getUser().getUserImg();
        user_name = loginModel.getData().getUser().getName();
        user_phone = loginModel.getData().getUser().getPhone();
        user_type = loginModel.getData().getUser().getRoleIdFk();
        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityUpdateProductBinding.userImg);
        }
        activityUpdateProductBinding.userName.setText(user_name);
    }

    private void validation() {
        product_name = activityUpdateProductBinding.etProductName.getText().toString();
        product_price = activityUpdateProductBinding.etProductPrice.getText().toString();
        product_details = activityUpdateProductBinding.etDetails.getText().toString();
        if (!TextUtils.isEmpty(product_name)){
            if (filepath != null) {
                updateProductViewModel.update_product_with_img(product_id,trader_id,store_id,alboum_id,product_name, product_price, product_details, filepath);
            }else {
                updateProductViewModel.update_product_without_img(product_id,trader_id,store_id,alboum_id,product_name,product_price,product_details);
            }
        }else {
           if (TextUtils.isEmpty(product_name)){
               activityUpdateProductBinding.etProductName.setError("برجاء إدخال إسم المنتج");
           }else {
               activityUpdateProductBinding.etProductName.setError(null);
           }
        }
    }

    private void getDataFromIntent() {
        flag = getIntent().getIntExtra("flag",0);
        if (flag != 2){
            product = (Product) getIntent().getSerializableExtra("product");
            activityUpdateProductBinding.etProductName.setText(product.getTitle());
            activityUpdateProductBinding.etProductPrice.setText(product.getPrice()+"");
            activityUpdateProductBinding.etDetails.setText(product.getDetails());
            alboum_id = product.getAlboumIdFk()+"";
            store_id = product.getStoreIdFk()+"";
            product_id = product.getId()+"";
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+product.getImg()).into(activityUpdateProductBinding.productImg);
        }else {
            product2 = (com.alatheer.shebinbook.products.Product) getIntent().getSerializableExtra("product");
            activityUpdateProductBinding.etProductName.setText(product2.getTitle());
            activityUpdateProductBinding.etProductPrice.setText(product2.getPrice()+"");
            activityUpdateProductBinding.etDetails.setText(product2.getDetails());
            alboum_id = product2.getAlboumIdFk()+"";
            store_id = product2.getStoreIdFk()+"";
            product_id = product2.getId()+"";
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+product2.getImg()).into(activityUpdateProductBinding.productImg);
        }



    }
    private boolean openDrawer() {
        activityUpdateProductBinding.drawerlayout.openDrawer(GravityCompat.END);
        return true;
    }

    public void showmenu(View view) {
        activityUpdateProductBinding.navView.setNavigationItemSelectedListener(this);
        openDrawer();
    }

    public void chooseimage(View view) {
        Check_ReadPermission(IMG);
    }

    private void Check_ReadPermission(Integer img) {
        if (ContextCompat.checkSelfPermission(UpdateProductActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(UpdateProductActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(UpdateProductActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Apply for multiple permissions together
            ActivityCompat.requestPermissions(UpdateProductActivity.this, new String[]{
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
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProductActivity.this);
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
            activityUpdateProductBinding.linearAdd.setVisibility(View.GONE);
            activityUpdateProductBinding.productImg.setVisibility(View.VISIBLE);
            Picasso.get().load(filepath).into(activityUpdateProductBinding.productImg);
        } else if (requestCode == REQUESTCAMERA && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            final Bitmap bitmap = (Bitmap) bundle.get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
            filepath = Utilities.compressImage(UpdateProductActivity.this, path);
            activityUpdateProductBinding.linearAdd.setVisibility(View.GONE);
            activityUpdateProductBinding.productImg.setVisibility(View.VISIBLE);
            Picasso.get().load(filepath).into(activityUpdateProductBinding.productImg);
        }
    }

    public void createsuccessDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.success_dialog, null);
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
                    if (flag != 2){
                        Intent intent = new Intent(UpdateProductActivity.this,AllProductsActivity.class);
                        intent.putExtra("gallery_id",product.getAlboumIdFk());
                        startActivity(intent);
                        ProfileActivity.fa.finish();
                        finish();
                    }else {
                        Intent intent = new Intent(UpdateProductActivity.this, ProfileActivity.class);
                        intent.putExtra("gallery_id",product2.getAlboumIdFk());
                        startActivity(intent);
                        ProfileActivity.fa.finish();
                        finish();
                    }
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
    public boolean onNavigationItemSelected(@NonNull  android.view.MenuItem item) {
        return false;
    }
}