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
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.allproducts.AllProductsActivity;
import com.alatheer.shebinbook.allproducts.Product;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.databinding.ActivityUpdateProductBinding;
import com.alatheer.shebinbook.home.MenuAdapter;
import com.alatheer.shebinbook.home.slider.MenuItem;
import com.alatheer.shebinbook.setting.ProfileData;
import com.alatheer.shebinbook.trader.addproduct.AddProductActivity;
import com.alatheer.shebinbook.trader.profile.ProfileActivity;
import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class UpdateProductActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityUpdateProductBinding activityUpdateProductBinding;
    UpdateProductViewModel updateProductViewModel;
    Product product;
    com.alatheer.shebinbook.products.Product product2;
    String product_name,product_details,product_price,store_id,alboum_id,trader_id,product_id,user_phone,user_img,user_name,user_id,img_id;
    Integer MainIMG = 1,IMG = 1,IMG2 = 2,IMG3 = 3,IMG4 = 4,REQUESTCAMERA=5,REQUESTCAMERA2=6,REQUESTCAMERA3=7,REQUESTCAMERA4=8,REQUESTCAMERA5=9;
    Uri filepath,filepath2,filepath3,filepath4,filepath5;
    MySharedPreference mySharedPreference;
    LoginModel loginModel ;
    Dialog dialog3;
    Integer flag;
    RecyclerView menu_recycler;
    MenuAdapter menuAdapter;
    RecyclerView.LayoutManager menulayoutmanager;
    Integer user_type;
    List<MenuItem> menuItemList;
    String imageEncoded;
    List<String> imagesEncodedList;
    ArrayList<Uri> mArrayUri;
    Data1 product3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        activityUpdateProductBinding = DataBindingUtil.setContentView(this,R.layout.activity_update_product);
        updateProductViewModel = new UpdateProductViewModel(this);
        activityUpdateProductBinding.setUpdateproductviewmodel(updateProductViewModel);
        try {
            getSharedPreferenceData();
            updateProductViewModel.getData(user_id);
            getDataFromIntent();
            updateProductViewModel.get_product_data(product_id);
        }catch (Exception e){
            Log.e("exc",e.getMessage());
        }
        activityUpdateProductBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
        activityUpdateProductBinding.deleteImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*activityUpdateProductBinding.deleteImg1.setVisibility(View.GONE);
                activityUpdateProductBinding.productImg.setVisibility(View.GONE);
                activityUpdateProductBinding.cameraImg.setVisibility(View.VISIBLE);*/
                if (flag != 2){
                    updateProductViewModel.delete_img(product3.getSubImages().get(0),product_id);
                }else {
                    updateProductViewModel.delete_img(product3.getSubImages().get(0),product_id);
                }

            }
        });
        activityUpdateProductBinding.deleteImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*activityUpdateProductBinding.cameraImg2.setVisibility(View.VISIBLE);
                activityUpdateProductBinding.deleteImg2.setVisibility(View.GONE);
                activityUpdateProductBinding.productImg2.setVisibility(View.GONE);*/
                if (flag != 2){
                    updateProductViewModel.delete_img(product3.getSubImages().get(1),product_id);
                }else {
                    updateProductViewModel.delete_img(product3.getSubImages().get(1),product_id);
                }
            }
        });
        activityUpdateProductBinding.deleteImg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*activityUpdateProductBinding.cameraImg3.setVisibility(View.VISIBLE);
                activityUpdateProductBinding.deleteImg3.setVisibility(View.GONE);
                activityUpdateProductBinding.productImg3.setVisibility(View.GONE);*/
                if (flag != 2){
                    updateProductViewModel.delete_img(product3.getSubImages().get(2),product_id);
                }else {
                    updateProductViewModel.delete_img(product3.getSubImages().get(2),product_id);
                }
            }
        });
        activityUpdateProductBinding.deleteImg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*activityUpdateProductBinding.cameraImg4.setVisibility(View.VISIBLE);
                activityUpdateProductBinding.deleteImg4.setVisibility(View.GONE);
                activityUpdateProductBinding.productImg4.setVisibility(View.GONE);*/
                if (flag != 2){
                    updateProductViewModel.delete_img(product3.getSubImages().get(3),product_id);
                }else {
                    updateProductViewModel.delete_img(product3.getSubImages().get(3),product_id);
                }
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
        user_id = loginModel.getData().getUser().getId()+"";
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
            product_id = product.getId()+"";
        }else {
            product2 = (com.alatheer.shebinbook.products.Product) getIntent().getSerializableExtra("product");
            product_id = product.getId()+"";
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

    public void chooseimage5(View view) {
        Check_ReadPermission(MainIMG);
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
    private void Check_ReadPermission2(Integer img) {
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
            select_photo2(img);
        }
    }
    private void Check_ReadPermission3(Integer img) {
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
            select_photo3(img);
        }
    }
    private void Check_ReadPermission4(Integer img) {
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
            select_photo4(img);
        }
    }
    private void Check_ReadPermission5(Integer img) {
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
            select_photo5(img);
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
    private void select_photo2(Integer img) {
        final CharSequence[] items = {"كاميرا", "ملفات الصور", "الغاء"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProductActivity.this);
        builder.setTitle("اضافة صورة للملف الشخصي");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("كاميرا")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUESTCAMERA2);
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
    private void select_photo3(Integer img) {
        final CharSequence[] items = {"كاميرا", "ملفات الصور", "الغاء"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProductActivity.this);
        builder.setTitle("اضافة صورة للملف الشخصي");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("كاميرا")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUESTCAMERA3);
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
    private void select_photo4(Integer img) {
        final CharSequence[] items = {"كاميرا", "ملفات الصور", "الغاء"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProductActivity.this);
        builder.setTitle("اضافة صورة للملف الشخصي");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("كاميرا")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUESTCAMERA4);
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
    private void select_photo5(Integer img) {
        final CharSequence[] items = {"كاميرا", "ملفات الصور", "الغاء"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProductActivity.this);
        builder.setTitle("اضافة صورة للملف الشخصي");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("كاميرا")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUESTCAMERA5);
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
        if (requestCode == MainIMG && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                String file_name = String.format("%d.jpg", System.currentTimeMillis());
                File finalfile = new File(path, file_name);
                FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                filepath = Uri.fromFile(finalfile);
                activityUpdateProductBinding.linearAdd5.setVisibility(View.GONE);
                activityUpdateProductBinding.productImg5.setVisibility(View.VISIBLE);
                activityUpdateProductBinding.productImg5.setImageURI(filepath);
            } catch (Exception e) {
                filepath = data.getData();
                activityUpdateProductBinding.linearAdd5.setVisibility(View.GONE);
                activityUpdateProductBinding.productImg5.setVisibility(View.VISIBLE);
                Picasso.get().load(filepath).into(activityUpdateProductBinding.productImg5);
            }

        } else if (requestCode == REQUESTCAMERA && resultCode == Activity.RESULT_OK) {
            try {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                String file_name = String.format("%d.jpg", System.currentTimeMillis());
                File finalfile = new File(path, file_name);
                FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                filepath = Uri.fromFile(finalfile);
                activityUpdateProductBinding.linearAdd5.setVisibility(View.GONE);
                activityUpdateProductBinding.productImg5.setVisibility(View.VISIBLE);
                activityUpdateProductBinding.productImg5.setImageURI(filepath);
                //Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                filepath = Utilities.compressImage(UpdateProductActivity.this, path);
                activityUpdateProductBinding.linearAdd5.setVisibility(View.GONE);
                activityUpdateProductBinding.productImg5.setVisibility(View.VISIBLE);
                Picasso.get().load(filepath).into(activityUpdateProductBinding.productImg5);
            }

        }
        if (requestCode == IMG && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                String file_name = String.format("%d.jpg", System.currentTimeMillis());
                File finalfile = new File(path, file_name);
                FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                filepath2 = Uri.fromFile(finalfile);
                if (flag != 2){
                    try {
                        if (product3.getSubImages().get(0) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(0).getId(),product_id,filepath2);
                        }
                    }catch (Exception ll){
                        updateProductViewModel.add_img(product_id,filepath2);
                    }
                }else {
                    try {
                        if (product3.getSubImages().get(0) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(0).getId(),product_id,filepath2);
                        }
                    }catch (Exception l2){
                        updateProductViewModel.add_img(product_id,filepath2);
                    }
                }
                //activityUpdateProductBinding.linearAdd.setVisibility(View.GONE);
                activityUpdateProductBinding.productImg.setVisibility(View.VISIBLE);
                activityUpdateProductBinding.productImg.setImageURI(filepath2);
            } catch (Exception e) {
                filepath2 = data.getData();
                if (flag != 2){
                    try {
                        if (product3.getSubImages().get(0) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(0).getId(),product_id,filepath2);
                        }
                    }catch (Exception l){
                        updateProductViewModel.add_img(product_id,filepath2);
                    }
                }else {
                    try {
                        if (product3.getSubImages().get(0) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(0).getId(),product_id,filepath2);
                        }
                    }catch (Exception e9){
                        updateProductViewModel.add_img(product_id,filepath2);
                    }
                }
                //activityUpdateProductBinding.linearAdd.setVisibility(View.GONE);
                activityUpdateProductBinding.productImg.setVisibility(View.VISIBLE);
                Picasso.get().load(filepath2).into(activityUpdateProductBinding.productImg);
            }

        } else if (requestCode == REQUESTCAMERA2 && resultCode == Activity.RESULT_OK) {
            try {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                String file_name = String.format("%d.jpg", System.currentTimeMillis());
                File finalfile = new File(path, file_name);
                FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                filepath2 = Uri.fromFile(finalfile);
                if (flag != 2){
                    try {
                        if (product3.getSubImages().get(0) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(0).getId(),product_id,filepath2);
                        }
                    }catch (Exception m){
                        updateProductViewModel.add_img(product_id,filepath2);
                    }
                }else {
                    try {
                        if (product3.getSubImages().get(0) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(0).getId(),product_id,filepath2);
                        }
                    }catch (Exception ll){
                        updateProductViewModel.add_img(product_id,filepath2);
                    }
                }
                //activityUpdateProductBinding.linearAdd.setVisibility(View.GONE);
                activityUpdateProductBinding.productImg.setVisibility(View.VISIBLE);
                activityUpdateProductBinding.productImg.setImageURI(filepath2);
                //Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                filepath2 = Utilities.compressImage(UpdateProductActivity.this, path);
                if (flag != 2){
                    try {
                        if (product3.getSubImages().get(0) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(0).getId(),product_id,filepath2);
                        }
                    }catch (Exception e6){
                        updateProductViewModel.add_img(product_id,filepath2);
                    }

                }else {
                    try {
                        if (product3.getSubImages().get(0) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(0).getId(),product_id,filepath2);
                        }
                    }catch (Exception e5){
                        updateProductViewModel.add_img(product_id,filepath2);
                    }

                }
                //activityUpdateProductBinding.linearAdd.setVisibility(View.GONE);
                activityUpdateProductBinding.productImg.setVisibility(View.VISIBLE);
                Picasso.get().load(filepath2).into(activityUpdateProductBinding.productImg);
            }

        }
        if (requestCode == IMG2 && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                String file_name = String.format("%d.jpg", System.currentTimeMillis());
                File finalfile = new File(path, file_name);
                FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                filepath3 = Uri.fromFile(finalfile);
                if (flag != 2){
                    try {
                        if (product3.getSubImages().get(1) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(1).getId(),product_id,filepath3);
                        }
                    }catch (Exception j){
                        updateProductViewModel.add_img(product_id,filepath3);
                    }
                }else {
                    try {
                        if (product3.getSubImages().get(1) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(1).getId(),product_id,filepath3);
                        }
                    }catch (Exception e){
                        updateProductViewModel.add_img(product_id,filepath3);
                    }
                }
                //activityUpdateProductBinding.linearAdd2.setVisibility(View.GONE);
                activityUpdateProductBinding.productImg2.setVisibility(View.VISIBLE);
                activityUpdateProductBinding.productImg2.setImageURI(filepath3);
            } catch (Exception e) {
                filepath3= data.getData();
                if (flag != 2){
                    try {
                        if (product3.getSubImages().get(1) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(1).getId(),product_id,filepath3);
                        }
                    }catch (Exception e9){
                        updateProductViewModel.add_img(product_id,filepath3);
                    }
                }else {
                    try {
                        if (product3.getSubImages().get(1) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(1).getId(),product_id,filepath3);
                        }
                    }catch (Exception e6){
                        updateProductViewModel.add_img(product_id,filepath3);
                    }
                }
                //activityUpdateProductBinding.linearAdd2.setVisibility(View.GONE);
                activityUpdateProductBinding.productImg2.setVisibility(View.VISIBLE);
                Picasso.get().load(filepath3).into(activityUpdateProductBinding.productImg2);
            }

        } else if (requestCode == REQUESTCAMERA3 && resultCode == Activity.RESULT_OK) {
            try {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                String file_name = String.format("%d.jpg", System.currentTimeMillis());
                File finalfile = new File(path, file_name);
                FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                filepath3 = Uri.fromFile(finalfile);
                if (flag != 2){
                    try {
                        if (product3.getSubImages().get(1) != null) {
                            updateProductViewModel.update_img(product3.getSubImages().get(1).getId(), product_id, filepath3);
                        }
                    }catch (Exception e){
                        updateProductViewModel.add_img(product_id,filepath3);
                    }
                }else {
                    try {
                        if (product3.getSubImages().get(1) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(1).getId(),product_id,filepath3);
                        }
                    }catch (Exception e){
                        updateProductViewModel.add_img(product_id,filepath3);
                    }

                }
                //activityUpdateProductBinding.linearAdd2.setVisibility(View.GONE);
                activityUpdateProductBinding.productImg2.setVisibility(View.VISIBLE);
                activityUpdateProductBinding.productImg2.setImageURI(filepath3);
                //Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                filepath3 = Utilities.compressImage(UpdateProductActivity.this, path);
                if (flag != 2){
                    try {
                        if (product3.getSubImages().get(1) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(1).getId(),product_id,filepath3);
                        }
                    }catch (Exception n2){
                        updateProductViewModel.add_img(product_id,filepath3);
                    }

                }else {
                    try {
                        if (product3.getSubImages().get(1) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(1).getId(),product_id,filepath3);
                        }
                    }catch (Exception e9){
                        updateProductViewModel.add_img(product_id,filepath3);
                    }
                }
                //activityUpdateProductBinding.linearAdd2.setVisibility(View.GONE);
                activityUpdateProductBinding.productImg2.setVisibility(View.VISIBLE);
                Picasso.get().load(filepath3).into(activityUpdateProductBinding.productImg3);
            }

        }
        if (requestCode == IMG3 && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                String file_name = String.format("%d.jpg", System.currentTimeMillis());
                File finalfile = new File(path, file_name);
                FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                filepath4 = Uri.fromFile(finalfile);
                if (flag != 2){
                    Toast.makeText(this, filepath4.toString(), Toast.LENGTH_SHORT).show();
                    try {
                        if (product3.getSubImages().get(2) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(2).getId(),product_id,filepath4);
                        }
                    }catch (Exception e){
                        Log.e("lllll",e.getMessage());
                        updateProductViewModel.add_img(product_id,filepath4);
                    }
                }else {
                    try {
                        if (product3.getSubImages().get(2) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(2).getId(),product_id,filepath4);
                        }
                    }catch (Exception e){
                        updateProductViewModel.add_img(product_id,filepath4);
                    }
                }
                //activityUpdateProductBinding.linearAdd3.setVisibility(View.GONE);
                activityUpdateProductBinding.productImg3.setVisibility(View.VISIBLE);
                activityUpdateProductBinding.productImg3.setImageURI(filepath4);
            } catch (Exception e) {
                filepath4 = data.getData();
                if (flag != 2){
                    try {
                        if (product3.getSubImages().get(2) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(2).getId(),product_id,filepath4);
                        }
                    }catch (Exception e2){
                        updateProductViewModel.add_img(product_id,filepath4);
                    }
                }else {
                    try {
                        if (product3.getSubImages().get(2) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(2).getId(),product_id,filepath4);
                        }
                    }catch (Exception e3){
                        updateProductViewModel.add_img(product_id,filepath4);
                    }
                }
                //activityUpdateProductBinding.linearAdd3.setVisibility(View.GONE);
                activityUpdateProductBinding.productImg3.setVisibility(View.VISIBLE);
                Picasso.get().load(filepath4).into(activityUpdateProductBinding.productImg3);
            }

        } else if (requestCode == REQUESTCAMERA4 && resultCode == Activity.RESULT_OK) {
            try {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                String file_name = String.format("%d.jpg", System.currentTimeMillis());
                File finalfile = new File(path, file_name);
                FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                filepath4 = Uri.fromFile(finalfile);
                if (flag != 2){
                    try {
                        if (product3.getSubImages().get(2) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(2).getId(),product_id,filepath4);
                        }
                    }catch (Exception e){
                        updateProductViewModel.add_img(product_id,filepath4);
                    }
                }else {
                    try {
                        if (product3.getSubImages().get(2) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(2).getId(),product_id,filepath4);
                        }
                    }catch (Exception e){
                        updateProductViewModel.add_img(product_id,filepath4);
                    }
                }
                //activityUpdateProductBinding.linearAdd3.setVisibility(View.GONE);
                activityUpdateProductBinding.productImg3.setVisibility(View.VISIBLE);
                activityUpdateProductBinding.productImg3.setImageURI(filepath4);
                //Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                filepath4 = Utilities.compressImage(UpdateProductActivity.this, path);
                if (flag != 2){
                    try {
                        if (product3.getSubImages().get(2) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(2).getId(),product_id,filepath4);
                        }
                    }catch (Exception e1){
                        updateProductViewModel.add_img(product_id,filepath4);
                    }
                }else {
                    if (product3.getSubImages().get(2) != null){
                        updateProductViewModel.update_img(product3.getSubImages().get(2).getId(),product_id,filepath4);
                    }else {
                        updateProductViewModel.add_img(product_id,filepath4);
                    }
                }
                //activityUpdateProductBinding.linearAdd3.setVisibility(View.GONE);
                activityUpdateProductBinding.productImg3.setVisibility(View.VISIBLE);
                Picasso.get().load(filepath4).into(activityUpdateProductBinding.productImg3);
            }


        }
        if (requestCode == IMG4 && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                String file_name = String.format("%d.jpg", System.currentTimeMillis());
                File finalfile = new File(path, file_name);
                FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                filepath5 = Uri.fromFile(finalfile);
                if (flag != 2){
                    try {
                        if (product3.getSubImages().get(3) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(3).getId(),product_id,filepath5);
                        }
                    }catch (Exception e){
                        updateProductViewModel.add_img(product_id,filepath5);
                    }
                }else {
                    try {
                        if (product3.getSubImages().get(3) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(3).getId(),product_id,filepath5);
                        }
                    }catch (Exception e){
                        updateProductViewModel.add_img(product_id,filepath5);
                    }
                }
                //activityUpdateProductBinding.linearAdd4.setVisibility(View.GONE);
                activityUpdateProductBinding.productImg4.setVisibility(View.VISIBLE);
                activityUpdateProductBinding.productImg4.setImageURI(filepath5);
            } catch (Exception e) {
                filepath5 = data.getData();
                if (flag != 2){
                    try {
                        if (product3.getSubImages().get(3) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(3).getId(),product_id,filepath5);
                        }
                    }catch (Exception e2){
                        updateProductViewModel.add_img(product_id,filepath5);
                    }
                }else {
                    try {
                        if (product3.getSubImages().get(3) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(3).getId(),product_id,filepath5);
                        }
                    }catch (Exception e6){
                        updateProductViewModel.add_img(product_id,filepath5);
                    }
                }
                activityUpdateProductBinding.linearAdd5.setVisibility(View.GONE);
                activityUpdateProductBinding.productImg5.setVisibility(View.VISIBLE);
                Picasso.get().load(filepath5).into(activityUpdateProductBinding.productImg4);
            }

        } else if (requestCode == REQUESTCAMERA5 && resultCode == Activity.RESULT_OK) {
            try {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                String file_name = String.format("%d.jpg", System.currentTimeMillis());
                File finalfile = new File(path, file_name);
                FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                filepath5 = Uri.fromFile(finalfile);
                if (flag != 2){
                    try {
                        if (product3.getSubImages().get(3) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(3).getId(),product_id,filepath5);
                        }
                    }catch (Exception e6){
                        updateProductViewModel.add_img(product_id,filepath5);
                    }
                }else {
                    try {
                        if (product3.getSubImages().get(3) != null){
                            updateProductViewModel.update_img(product3.getSubImages().get(3).getId(),product_id,filepath5);
                        }
                    }catch (Exception k){
                        updateProductViewModel.add_img(product_id,filepath5);
                    }
                }
                //activityUpdateProductBinding.linearAdd4.setVisibility(View.GONE);
                activityUpdateProductBinding.productImg4.setVisibility(View.VISIBLE);
                activityUpdateProductBinding.productImg4.setImageURI(filepath5);
                //Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                filepath5 = Utilities.compressImage(UpdateProductActivity.this, path);
                //activityUpdateProductBinding.linearAdd4.setVisibility(View.GONE);
                activityUpdateProductBinding.productImg4.setVisibility(View.VISIBLE);
                Picasso.get().load(filepath5).into(activityUpdateProductBinding.productImg4);
                if (flag != 2){
                    if (product3.getSubImages().get(3) != null){
                        updateProductViewModel.update_img(product3.getSubImages().get(3).getId(),product_id,filepath5);
                    }else {
                        updateProductViewModel.add_img(product_id,filepath5);
                    }
                }else {
                    if (product3.getSubImages().get(3) != null){
                        updateProductViewModel.update_img(product3.getSubImages().get(3).getId(),product_id,filepath5);
                    }else {
                        updateProductViewModel.add_img(product_id,filepath5);
                    }
                }
            }

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

    public void setData(ProfileData body) {
        user_img = body.getData().getUserImg();
        user_name = body.getData().getName();
        user_phone = body.getData().getPhone();

        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityUpdateProductBinding.userImg);
        }
        activityUpdateProductBinding.userName.setText(user_name);
        init_navigation_menu();
    }
    public void chooseimage(View view) {
        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);*/
        Check_ReadPermission2(IMG);
    }

    public void chooseimage2(View view) {
        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);*/
        Check_ReadPermission3(IMG2);
    }

    public void chooseimage3(View view) {
        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);*/
        try {
            Check_ReadPermission4(IMG3);
        }catch (Exception e){
            Log.e("llll",e.getMessage());
        }
    }

    public void chooseimage4(View view) {
        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);*/
        Check_ReadPermission5(IMG4);
    }

    public void set_product_data(Data1 data) {
        activityUpdateProductBinding.cameraImg.setVisibility(View.VISIBLE);
        activityUpdateProductBinding.cameraImg2.setVisibility(View.VISIBLE);
        activityUpdateProductBinding.cameraImg3.setVisibility(View.VISIBLE);
        activityUpdateProductBinding.cameraImg4.setVisibility(View.VISIBLE);
        activityUpdateProductBinding.productImg.setVisibility(View.GONE);
        activityUpdateProductBinding.deleteImg1.setVisibility(View.GONE);
        activityUpdateProductBinding.productImg2.setVisibility(View.GONE);
        activityUpdateProductBinding.deleteImg2.setVisibility(View.GONE);
        activityUpdateProductBinding.productImg3.setVisibility(View.GONE);
        activityUpdateProductBinding.deleteImg3.setVisibility(View.GONE);
        activityUpdateProductBinding.productImg4.setVisibility(View.GONE);
        activityUpdateProductBinding.deleteImg4.setVisibility(View.GONE);
        product3 = data;
        activityUpdateProductBinding.etProductName.setText(data.getTitle());
        if (product.getPrice() != null){
            activityUpdateProductBinding.etProductPrice.setText(data.getPrice()+"");
        }else {
            activityUpdateProductBinding.etProductPrice.setText("");
        }
        activityUpdateProductBinding.etDetails.setText(data.getDetails());
        alboum_id = data.getAlboumIdFk()+"";
        store_id = data.getStoreIdFk()+"";
        product_id = product.getId()+"";
        //Toast.makeText(this, data.getSubImages().size()+"", Toast.LENGTH_SHORT).show();
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+data.getImg()).into(activityUpdateProductBinding.productImg5);
        try {
            if (!data.getSubImages().isEmpty()){
                try {
                    if (data.getSubImages().get(0) != null){
                        //Toast.makeText(this, product.getSubImages().get(0).getImage(), Toast.LENGTH_SHORT).show();
                        activityUpdateProductBinding.productImg.setVisibility(View.VISIBLE);
                        activityUpdateProductBinding.deleteImg1.setVisibility(View.VISIBLE);
                        activityUpdateProductBinding.cameraImg.setVisibility(View.GONE);
                        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+data.getSubImages().get(0).getImage()).into(activityUpdateProductBinding.productImg);
                    }else {
                        activityUpdateProductBinding.productImg.setVisibility(View.GONE);
                        activityUpdateProductBinding.deleteImg1.setVisibility(View.GONE);
                        activityUpdateProductBinding.linearAdd.setVisibility(View.VISIBLE);
                        activityUpdateProductBinding.cameraImg.setVisibility(View.VISIBLE);
                    }
                    if (data.getSubImages().get(1) != null){
                        activityUpdateProductBinding.productImg2.setVisibility(View.VISIBLE);
                        activityUpdateProductBinding.deleteImg2.setVisibility(View.VISIBLE);
                        activityUpdateProductBinding.cameraImg2.setVisibility(View.GONE);
                        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+data.getSubImages().get(1).getImage()).into(activityUpdateProductBinding.productImg2);
                    }else {
                        activityUpdateProductBinding.productImg2.setVisibility(View.GONE);
                        activityUpdateProductBinding.deleteImg2.setVisibility(View.GONE);
                        activityUpdateProductBinding.cameraImg2.setVisibility(View.VISIBLE);
                        activityUpdateProductBinding.linearAdd2.setVisibility(View.VISIBLE);
                    }
                    if (data.getSubImages().get(2) != null){
                        activityUpdateProductBinding.productImg3.setVisibility(View.VISIBLE);
                        activityUpdateProductBinding.deleteImg3.setVisibility(View.VISIBLE);
                        activityUpdateProductBinding.cameraImg3.setVisibility(View.GONE);
                        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+data.getSubImages().get(2).getImage()).into(activityUpdateProductBinding.productImg3);
                    }else {
                        activityUpdateProductBinding.productImg3.setVisibility(View.GONE);
                        activityUpdateProductBinding.deleteImg3.setVisibility(View.GONE);
                        activityUpdateProductBinding.cameraImg3.setVisibility(View.VISIBLE);
                        activityUpdateProductBinding.linearAdd3.setVisibility(View.VISIBLE);
                    }
                    if (data.getSubImages().get(3) != null){
                        //Toast.makeText(this, "llll", Toast.LENGTH_SHORT).show();
                        activityUpdateProductBinding.productImg4.setVisibility(View.VISIBLE);
                        activityUpdateProductBinding.deleteImg4.setVisibility(View.VISIBLE);
                        activityUpdateProductBinding.cameraImg4.setVisibility(View.GONE);
                        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+data.getSubImages().get(3).getImage()).into(activityUpdateProductBinding.productImg4);
                    }else {
                        activityUpdateProductBinding.productImg4.setVisibility(View.GONE);
                        activityUpdateProductBinding.deleteImg4.setVisibility(View.GONE);
                        activityUpdateProductBinding.cameraImg4.setVisibility(View.VISIBLE);
                        activityUpdateProductBinding.linearAdd4.setVisibility(View.VISIBLE);
                    }
                }catch (Exception e){
                    Log.e("lolo",e.getMessage());
                }
            }
        }catch (Exception e){
            Log.e("product_exception",e.getMessage());

        }
    }
}