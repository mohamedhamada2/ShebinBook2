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
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
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
import com.alatheer.shebinbook.trader.updateproduct.UpdateProductActivity;
import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
    int PICK_IMAGE_MULTIPLE = 6;
    String imageEncoded;
    List<String> imagesEncodedList;
    ArrayList<Uri> mArrayUri;
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
            addProductViewModel.addproduct(trader_id+"",store_id+"",alboum_id+"",product_name,product_price,product_details,filepath,mArrayUri);
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

    public void chooseimage5(View view) {
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
                activityAddProductBinding.linearAdd5.setVisibility(View.GONE);
                activityAddProductBinding.productImg5.setVisibility(View.VISIBLE);
                activityAddProductBinding.productImg5.setImageURI(filepath);
            }catch (Exception e){
                filepath = data.getData();
                activityAddProductBinding.linearAdd5.setVisibility(View.GONE);
                activityAddProductBinding.productImg5.setVisibility(View.VISIBLE);
                Picasso.get().load(filepath).into(activityAddProductBinding.productImg5);
            }
            //filepath = data.getData();
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
                activityAddProductBinding.linearAdd5.setVisibility(View.GONE);
                activityAddProductBinding.productImg5.setVisibility(View.VISIBLE);
                activityAddProductBinding.productImg5.setImageURI(filepath);
            }catch (Exception e){
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                filepath = Utilities.compressImage(AddProductActivity.this, path);
                activityAddProductBinding.linearAdd5.setVisibility(View.GONE);
                activityAddProductBinding.productImg5.setVisibility(View.VISIBLE);
                Picasso.get().load(filepath).into(activityAddProductBinding.productImg5);
            }
        }
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesEncodedList = new ArrayList<String>();
                if(data.getData()!=null){

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());
                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    String file_name= String.format("%d.jpg",System.currentTimeMillis());
                    File finalfile = new File(path,file_name);
                    FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,50,fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    Uri mImageUri = Uri.fromFile(finalfile);
                    mArrayUri = new ArrayList<Uri>();
                    mArrayUri.add(mImageUri);
                    activityAddProductBinding.linearAdd.setVisibility(View.GONE);
                    activityAddProductBinding.productImg.setVisibility(View.VISIBLE);
                    activityAddProductBinding.productImg.setImageURI(mImageUri);
                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded  = cursor.getString(columnIndex);
                    cursor.close();

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            if (mClipData.getItemAt(0).getUri() != null){
                                activityAddProductBinding.linearAdd.setVisibility(View.GONE);
                                activityAddProductBinding.productImg.setVisibility(View.VISIBLE);
                                activityAddProductBinding.productImg.setImageURI(mClipData.getItemAt(0).getUri());
                            }
                            if (mClipData.getItemAt(1).getUri() != null){
                                activityAddProductBinding.linearAdd2.setVisibility(View.GONE);
                                activityAddProductBinding.productImg2.setVisibility(View.VISIBLE);
                                activityAddProductBinding.productImg2.setImageURI(mClipData.getItemAt(1).getUri());
                            }
                            if (mClipData.getItemAt(2).getUri() != null){
                                activityAddProductBinding.linearAdd3.setVisibility(View.GONE);
                                activityAddProductBinding.productImg3.setVisibility(View.VISIBLE);
                                activityAddProductBinding.productImg3.setImageURI(mClipData.getItemAt(2).getUri());
                            }
                            if (mClipData.getItemAt(3).getUri() != null){
                                activityAddProductBinding.linearAdd4.setVisibility(View.GONE);
                                activityAddProductBinding.productImg4.setVisibility(View.VISIBLE);
                                activityAddProductBinding.productImg4.setImageURI(mClipData.getItemAt(3).getUri());
                            }
                            Uri uri = item.getUri();
                            if (mArrayUri.size()<4){
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                                String file_name= String.format("%d.jpg",System.currentTimeMillis());
                                File finalfile = new File(path,file_name);
                                FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                                bitmap.compress(Bitmap.CompressFormat.JPEG,50,fileOutputStream);
                                fileOutputStream.flush();
                                fileOutputStream.close();
                                Uri mImageUri = Uri.fromFile(finalfile);
                                mArrayUri.add(mImageUri);
                                // Get the cursor
                                Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                                // Move to first row
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                imageEncoded  = cursor.getString(columnIndex);
                                imagesEncodedList.add(imageEncoded);
                                cursor.close();
                            }else {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                                String file_name= String.format("%d.jpg",System.currentTimeMillis());
                                File finalfile = new File(path,file_name);
                                FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                                bitmap.compress(Bitmap.CompressFormat.JPEG,50,fileOutputStream);
                                fileOutputStream.flush();
                                fileOutputStream.close();
                                Uri mImageUri = Uri.fromFile(finalfile);
                                mArrayUri.remove(mImageUri);
                            }

                        }
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
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

    public void chooseimage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
    }

    public void chooseimage2(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
    }

    public void chooseimage3(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
    }

    public void chooseimage4(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
    }
}