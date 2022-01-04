package com.alatheer.shebinbook.trader.images.addimages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.databinding.ActivityAddImagesBinding;
import com.alatheer.shebinbook.home.MenuAdapter;
import com.alatheer.shebinbook.setting.ProfileData;
import com.alatheer.shebinbook.trader.addoffer.AddOfferActivity;
import com.alatheer.shebinbook.trader.addproduct.AddProductActivity;
import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AddImagesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    List<Uri> all_images;
    List<MultipartBody.Part> images;
    Uri filepath,filepath2,filepath3,filepath4;
    Integer IMG =1 ,IMG2 =3,IMG3=5,IMG4=7;
    Integer REQUESTCAMERA=2,REQUESTCAMERA2=4,REQUESTCAMERA3=6,REQUESTCAMERA4=8;
    ActivityAddImagesBinding activityAddImagesBinding;
    AddImageViewModel addImageViewModel;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Integer trader_id;
    String title,store_id,user_name,user_phone,user_img,user_id;
    Integer user_type;
    RecyclerView menu_recycler;
    MenuAdapter menuAdapter;
    RecyclerView.LayoutManager menulayoutmanager;
    List<com.alatheer.shebinbook.home.slider.MenuItem> menuItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_images);
        activityAddImagesBinding = DataBindingUtil.setContentView(this,R.layout.activity_add_images);
        addImageViewModel = new AddImageViewModel(this);
        activityAddImagesBinding.setAddimageviewmodel(addImageViewModel);
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getData().getUser().getId()+"";
        trader_id = loginModel.getData().getUser().getTraderId();
        store_id = getIntent().getStringExtra("store_id");
        user_img = loginModel.getData().getUser().getUserImg();
        user_name = loginModel.getData().getUser().getName();
        user_phone = loginModel.getData().getUser().getPhone();
        user_type = loginModel.getData().getUser().getRoleIdFk();
        addImageViewModel.getData(user_id);

        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityAddImagesBinding.userImg);
        }
        activityAddImagesBinding.userName.setText(user_name);
        all_images = new ArrayList<>();
        activityAddImagesBinding.btnAddAlboum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validation();

            }
        });
        /*for(Uri filePath21 :all_images){
            MultipartBody.Part vimg = Utilities.getMultiPart(this,filePath21,"images[]");
            images.add(vimg);

        }*/
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
        View headerLayout = activityAddImagesBinding.navView.getHeaderView(0);
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

    private void Validation() {
        title = activityAddImagesBinding.etAlboumName.getText().toString();
        if (filepath != null ){
            if (all_images.contains(filepath)){
            }else {
                all_images.add(filepath);
            }
            //Toast.makeText(this, all_images.size()+"", Toast.LENGTH_SHORT).show();
        }
        if (filepath2 != null){
            if (!all_images.contains(filepath2)){
                all_images.add(filepath2);
            }
        }
        if (filepath3 != null){
            if (!all_images.contains(filepath3)){
                all_images.add(filepath3);
            }
        }
        if (filepath4 != null){
            if (!all_images.contains(filepath4)){
                all_images.add(filepath4);
            }
        }
        if (!all_images.isEmpty()&& !TextUtils.isEmpty(title)){
            addImageViewModel.add_gallery(title,trader_id,all_images,store_id);
        }else {
            if(TextUtils.isEmpty(title)){
                activityAddImagesBinding.etAlboumName.setError("ادخل اسم الألبوم");
            }else {
                activityAddImagesBinding.etAlboumName.setError(null);
            }
            if(all_images.isEmpty()){
                Toast.makeText(this, "برجاء إدخال صورة واحدة علي الأقل", Toast.LENGTH_SHORT).show();
            }else {
            }
        }
    }

    public void showmenu(View view) {
        activityAddImagesBinding.navView.setNavigationItemSelectedListener(this);
        openDrawer();
    }

    private Boolean openDrawer() {
        activityAddImagesBinding.drawerlayout.openDrawer(GravityCompat.END);
        return true;
    }

    private void Check_ReadPermission(Integer img) {
        if (ContextCompat.checkSelfPermission(AddImagesActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(AddImagesActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(AddImagesActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Apply for multiple permissions together
            ActivityCompat.requestPermissions(AddImagesActivity.this, new String[]{
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
        AlertDialog.Builder builder = new AlertDialog.Builder(AddImagesActivity.this);
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

    public void chooseimage(View view) {
        Check_ReadPermission(IMG);
    }

    public void chooseimage4(View view) {
        Check_ReadPermission(IMG4);
    }

    public void chooseimage3(View view) {
        Check_ReadPermission(IMG3);
    }

    public void chooseimage2(View view) {
        Check_ReadPermission(IMG2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                String file_name= String.format("%d.jpg",System.currentTimeMillis());
                File finalfile = new File(path,file_name);
                FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG,50,fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                filepath = Uri.fromFile(finalfile);
                activityAddImagesBinding.linearAdd.setVisibility(View.GONE);
                activityAddImagesBinding.productImg.setVisibility(View.VISIBLE);
                activityAddImagesBinding.productImg.setImageURI(filepath);
            }catch (Exception e){

            }
            //Picasso.get().load(filepath).into(activityAddImagesBinding.productImg);
        } else if (requestCode == REQUESTCAMERA && resultCode == Activity.RESULT_OK) {
            try {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                String file_name= String.format("%d.jpg",System.currentTimeMillis());
                File finalfile = new File(path,file_name);
                FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG,50,fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                filepath = Uri.fromFile(finalfile);
                activityAddImagesBinding.linearAdd.setVisibility(View.GONE);
                activityAddImagesBinding.productImg.setVisibility(View.VISIBLE);
                activityAddImagesBinding.productImg.setImageURI(filepath);
            }catch (Exception e){
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                filepath = Utilities.compressImage(AddImagesActivity.this, path);
                activityAddImagesBinding.linearAdd.setVisibility(View.GONE);
                activityAddImagesBinding.productImg.setVisibility(View.VISIBLE);
                Picasso.get().load(filepath).into(activityAddImagesBinding.productImg);
            }
        }
        if (requestCode == IMG2 && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                String file_name= String.format("%d.jpg",System.currentTimeMillis());
                File finalfile = new File(path,file_name);
                FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG,50,fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                filepath2 = Uri.fromFile(finalfile);
                activityAddImagesBinding.linearAdd2.setVisibility(View.GONE);
                activityAddImagesBinding.productImg2.setVisibility(View.VISIBLE);
                activityAddImagesBinding.productImg2.setImageURI(filepath2);
            }catch (Exception e){

            }
        } else if (requestCode == REQUESTCAMERA2 && resultCode == Activity.RESULT_OK) {
            try {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                String file_name= String.format("%d.jpg",System.currentTimeMillis());
                File finalfile = new File(path,file_name);
                FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG,50,fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                filepath2 = Uri.fromFile(finalfile);
                activityAddImagesBinding.linearAdd2.setVisibility(View.GONE);
                activityAddImagesBinding.productImg2.setVisibility(View.VISIBLE);
                activityAddImagesBinding.productImg2.setImageURI(filepath2);
            }catch (Exception e){
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                filepath2 = Utilities.compressImage(AddImagesActivity.this, path);
                activityAddImagesBinding.linearAdd2.setVisibility(View.GONE);
                activityAddImagesBinding.productImg2.setVisibility(View.VISIBLE);
                Picasso.get().load(filepath2).into(activityAddImagesBinding.productImg2);
            }
        }
        if (requestCode == IMG3 && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                String file_name= String.format("%d.jpg",System.currentTimeMillis());
                File finalfile = new File(path,file_name);
                FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG,50,fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                filepath3 = Uri.fromFile(finalfile);
                activityAddImagesBinding.linearAdd3.setVisibility(View.GONE);
                activityAddImagesBinding.productImg3.setVisibility(View.VISIBLE);
                activityAddImagesBinding.productImg3.setImageURI(filepath3);
            }catch (Exception e){

            }
        } else if (requestCode == REQUESTCAMERA3 && resultCode == Activity.RESULT_OK) {
            try {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                String file_name= String.format("%d.jpg",System.currentTimeMillis());
                File finalfile = new File(path,file_name);
                FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG,50,fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                filepath3 = Uri.fromFile(finalfile);
                activityAddImagesBinding.linearAdd3.setVisibility(View.GONE);
                activityAddImagesBinding.productImg3.setVisibility(View.VISIBLE);
                activityAddImagesBinding.productImg3.setImageURI(filepath3);
            }catch (Exception e){
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                filepath3 = Utilities.compressImage(AddImagesActivity.this, path);
                activityAddImagesBinding.linearAdd3.setVisibility(View.GONE);
                activityAddImagesBinding.productImg3.setVisibility(View.VISIBLE);
                Picasso.get().load(filepath3).into(activityAddImagesBinding.productImg3);
            }
        }
        if (requestCode == IMG4 && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                String file_name= String.format("%d.jpg",System.currentTimeMillis());
                File finalfile = new File(path,file_name);
                FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG,50,fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                filepath4 = Uri.fromFile(finalfile);
                activityAddImagesBinding.linearAdd4.setVisibility(View.GONE);
                activityAddImagesBinding.productImg4.setVisibility(View.VISIBLE);
                activityAddImagesBinding.productImg4.setImageURI(filepath4);
            }catch (Exception e){

            }
        } else if (requestCode == REQUESTCAMERA4 && resultCode == Activity.RESULT_OK) {
            try {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                String file_name= String.format("%d.jpg",System.currentTimeMillis());
                File finalfile = new File(path,file_name);
                FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG,50,fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                filepath4 = Uri.fromFile(finalfile);
                activityAddImagesBinding.linearAdd4.setVisibility(View.GONE);
                activityAddImagesBinding.productImg4.setVisibility(View.VISIBLE);
                activityAddImagesBinding.productImg4.setImageURI(filepath4);
            }catch (Exception e){
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                filepath4 = Utilities.compressImage(AddImagesActivity.this, path);
                activityAddImagesBinding.linearAdd4.setVisibility(View.GONE);
                activityAddImagesBinding.productImg4.setVisibility(View.VISIBLE);
                Picasso.get().load(filepath4).into(activityAddImagesBinding.productImg4);
            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
        return false;
    }

    public void setData(ProfileData body) {
        user_img = body.getData().getUserImg();
        user_name = body.getData().getName();
        user_phone = body.getData().getPhone();

        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityAddImagesBinding.userImg);
        }
        activityAddImagesBinding.userName.setText(user_name);
        init_navigation_menu();
    }
}