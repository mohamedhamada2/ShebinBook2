package com.alatheer.shebinbook.trader.addoffer;

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
import android.app.DatePickerDialog;
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
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.allproducts.AllProductsActivity;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.Gender;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.databinding.ActivityAddOfferBinding;
import com.alatheer.shebinbook.home.AskAdapter;
import com.alatheer.shebinbook.home.MenuAdapter;
import com.alatheer.shebinbook.setting.ProfileData;
import com.alatheer.shebinbook.trader.addproduct.AddProductActivity;
import com.alatheer.shebinbook.trader.profile.ProfileActivity;
import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddOfferActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityAddOfferBinding activityAddOfferBinding;
    AddOfferViewModel addOfferViewModel;
    List<Gender> genderList;
    String gender_id,from_date,to_date,title,price_before_offer,price_after_offer,offer_des,user_img,user_name,user_phone,user_id;
    DatePickerDialog.OnDateSetListener date_picker_dialog,date_picker_dialog2;
    Calendar myCalendar,myCalendar2;
    Integer IMG = 1 ,REQUESTCAMERA =2,user_type ;
    Uri filepath;
    Integer trader_id;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Dialog dialog3;
    List<com.alatheer.shebinbook.home.slider.MenuItem> menuItemList;
    AskAdapter askAdapter;
    RecyclerView menu_recycler;
    MenuAdapter menuAdapter;
    RecyclerView.LayoutManager menulayoutmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);
        activityAddOfferBinding = DataBindingUtil.setContentView(this,R.layout.activity_add_offer);
        addOfferViewModel = new AddOfferViewModel(this);
        activityAddOfferBinding.setAddofferviewmodel(addOfferViewModel);
        getSharedPreferenceData();
        addOfferViewModel.getData(user_id);
        addOfferViewModel.get_gender();
        myCalendar = Calendar.getInstance();
        myCalendar2 = Calendar.getInstance();
        date_picker_dialog = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart();

            }

        };
        date_picker_dialog2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, monthOfYear);
                myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart2();

            }

        };
        activityAddOfferBinding.spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender_id = genderList.get(i).getGender_id();
                TextView textView = (TextView) view;
                textView.setTextColor(getResources().getColor(R.color.purple_500));
                textView.setBackground(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        activityAddOfferBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
    }

    private void getSharedPreferenceData() {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getData().getUser().getId()+"";
        trader_id = loginModel.getData().getUser().getTraderId();
        user_img = loginModel.getData().getUser().getUserImg();
        user_name = loginModel.getData().getUser().getName();
        user_phone = loginModel.getData().getUser().getPhone();
        user_type = loginModel.getData().getUser().getRoleIdFk();
        if (user_img != null){
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityAddOfferBinding.userImg);
        }
        activityAddOfferBinding.userName.setText(user_name);
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
        View headerLayout = activityAddOfferBinding.navView.getHeaderView(0);
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


    private void validation() {
        price_before_offer = activityAddOfferBinding.etProductPrice.getText().toString();
        price_after_offer = activityAddOfferBinding.etOfferPrice.getText().toString();
        title = activityAddOfferBinding.etProductName.getText().toString();
        offer_des = activityAddOfferBinding.etDetails.getText().toString();
        if (!TextUtils.isEmpty(title)&&filepath != null&&!TextUtils.isEmpty(offer_des)){
            addOfferViewModel.add_offer(trader_id,title,gender_id,from_date,to_date,price_before_offer,price_after_offer,offer_des,filepath);
        }else {
            if(TextUtils.isEmpty(title)){
                activityAddOfferBinding.etProductName.setError("ادخل اسم المنتج");
            }else {
                activityAddOfferBinding.etProductName.setError(null);
            }
            if(TextUtils.isEmpty(from_date)){
                activityAddOfferBinding.etFromDate.setError("ادخل تاريخ بداية الخصم");
            }else {
                activityAddOfferBinding.etFromDate.setError(null);
            }
            if(TextUtils.isEmpty(from_date)){
                activityAddOfferBinding.etToDate.setError("ادخل تاريخ نهاية الخصم");
            }else {
                activityAddOfferBinding.etToDate.setError(null);
            }
            if(TextUtils.isEmpty(offer_des)){
                activityAddOfferBinding.etDetails.setError("ادخل تفاصيل العرض");
            }else {
                activityAddOfferBinding.etDetails.setError(null);
            }
        }
    }

    private void updateLabelStart2() {
        String myFormat = "dd-MM-yyyy";//In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        to_date = sdf.format(myCalendar2.getTime());
        activityAddOfferBinding.etToDate.setText(to_date);
    }

    private void updateLabelStart() {
        String myFormat = "dd-MM-yyyy";//In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        from_date = sdf.format(myCalendar.getTime());
        activityAddOfferBinding.etFromDate.setText(from_date);
    }

    public void setgender(List<Gender> genderlist) {
        this.genderList = genderlist;
    }

    public void sendgendernameslist(List<String> gender_names) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item,gender_names);
        activityAddOfferBinding.spinnerGender.setAdapter(arrayAdapter);
    }

    public void OpenCalender(View view) {
        new DatePickerDialog(AddOfferActivity.this, date_picker_dialog, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void OpenCalender2(View view) {
        new DatePickerDialog(AddOfferActivity.this, date_picker_dialog2, myCalendar2
                .get(myCalendar2.YEAR), myCalendar2.get(Calendar.MONTH),
                myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void chooseimage(View view) {
        Check_ReadPermission(IMG);
    }

    private void Check_ReadPermission(Integer img) {
        if (ContextCompat.checkSelfPermission(AddOfferActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(AddOfferActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(AddOfferActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Apply for multiple permissions together
            ActivityCompat.requestPermissions(AddOfferActivity.this, new String[]{
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
        AlertDialog.Builder builder = new AlertDialog.Builder(AddOfferActivity.this);
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
                activityAddOfferBinding.linearAdd.setVisibility(View.GONE);
                activityAddOfferBinding.productImg.setVisibility(View.VISIBLE);
                activityAddOfferBinding.productImg.setImageURI(filepath);
            }catch (Exception e){
                filepath = data.getData();
                activityAddOfferBinding.linearAdd.setVisibility(View.GONE);
                activityAddOfferBinding.productImg.setVisibility(View.VISIBLE);
                activityAddOfferBinding.productImg.setImageURI(filepath);
            }
        } else if (requestCode == REQUESTCAMERA && resultCode == Activity.RESULT_OK) {
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
                activityAddOfferBinding.linearAdd.setVisibility(View.GONE);
                activityAddOfferBinding.productImg.setVisibility(View.VISIBLE);
                activityAddOfferBinding.productImg.setImageURI(filepath);
            }catch (Exception e){
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                filepath = Utilities.compressImage(AddOfferActivity.this, path);
                activityAddOfferBinding.linearAdd.setVisibility(View.GONE);
                activityAddOfferBinding.productImg.setVisibility(View.VISIBLE);
                Picasso.get().load(filepath).into(activityAddOfferBinding.productImg);
            }
        }
    }

    public void createsuccessDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.success_dialog, null);
        TextView txt = view.findViewById(R.id.txt);
        txt.setText("تمت الإضافة");
        builder.setView(view);
        dialog3 = builder.create();
        dialog3.show();
        Window window = dialog3.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(450, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog3.isShowing()) {
                    dialog3.dismiss();
                    //productViewModel.getproducts(gallery_id+"");
                    Intent intent = new Intent(AddOfferActivity.this, ProfileActivity.class);
                    //intent.putExtra("gallery_id",product.getAlboumIdFk());
                    startActivity(intent);
                    ProfileActivity.fa.finish();
                    finish();
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

    public void showmenu(View view) {
        activityAddOfferBinding.navView.setNavigationItemSelectedListener(this);
        openDrawer();
    }

    private boolean openDrawer() {
        activityAddOfferBinding.drawerlayout.openDrawer(GravityCompat.END);
        return true;
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
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityAddOfferBinding.userImg);
        }
        activityAddOfferBinding.userName.setText(user_name);
        init_navigation_menu();
    }
}