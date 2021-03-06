package com.alatheer.shebinbook.trader.updateoffer;

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
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.allproducts.Product;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.Gender;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.databinding.ActivityUpdateOfferBinding;
import com.alatheer.shebinbook.home.MenuAdapter;
import com.alatheer.shebinbook.home.slider.MenuItem;
import com.alatheer.shebinbook.home.slider.Slider;
import com.alatheer.shebinbook.setting.ProfileData;
import com.alatheer.shebinbook.trader.addoffer.AddOfferActivity;
import com.alatheer.shebinbook.trader.profile.ProfileActivity;
import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UpdateOfferActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Slider slider;
    ActivityUpdateOfferBinding activityUpdateOfferBinding;
    UpdateOfferViewModel updateOfferViewModel;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Integer trader_id,offer_id;
    Integer IMG = 1 ,REQUESTCAMERA =2;
    Uri filepath;
    Calendar myCalendar,myCalendar2;
    DatePickerDialog.OnDateSetListener date_picker_dialog,date_picker_dialog2;
    String to_date,from_date,gender_id;
    List<Gender> genderList;
    String price_before_offer,price_after_offer,title,offer_des,user_name,user_phone,user_img,user_id;
    Dialog dialog3;
    RecyclerView menu_recycler;
    MenuAdapter menuAdapter;
    RecyclerView.LayoutManager menulayoutmanager;
    Integer user_type;
    List<MenuItem> menuItemList;
    Date d1,d2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_offer);
        activityUpdateOfferBinding = DataBindingUtil.setContentView(this,R.layout.activity_update_offer);
        updateOfferViewModel = new UpdateOfferViewModel(this);
        activityUpdateOfferBinding.setUpdateofferviewmodel(updateOfferViewModel);
        updateOfferViewModel.getgender();
        getDataFromIntent();
        getSharedPreferenceData();
        updateOfferViewModel.getData(user_id);
        //init_navigation_menu();
        myCalendar = Calendar.getInstance();
        myCalendar2 = Calendar.getInstance();
        activityUpdateOfferBinding.spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        activityUpdateOfferBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
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
    }

    private void init_navigation_menu() {
        menuItemList = new ArrayList<>();
        menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("????????????????",R.drawable.home2));
        menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("??????????????",R.drawable.fav2));
        menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("??????????????????",R.drawable.setting));
        menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("?????????? ????????",R.drawable.contactus));
        menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("?????????? ????????",R.drawable.logout2));
        if (user_type == 4){
            menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("??????????",R.drawable.store));
        }
        menuAdapter = new MenuAdapter(menuItemList,this);
        menulayoutmanager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        View headerLayout = activityUpdateOfferBinding.navView.getHeaderView(0);
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
        price_before_offer = activityUpdateOfferBinding.etProductPrice.getText().toString();
        price_after_offer = activityUpdateOfferBinding.etOfferPrice.getText().toString();
        title = activityUpdateOfferBinding.etProductName.getText().toString();
        to_date = activityUpdateOfferBinding.etToDate.getText().toString();
        from_date = activityUpdateOfferBinding.etFromDate.getText().toString();
        offer_des = activityUpdateOfferBinding.etDetails.getText().toString();
        if (!TextUtils.isEmpty(title)&&!TextUtils.isEmpty(offer_des)&& Double.parseDouble(price_after_offer)<Double.parseDouble(price_before_offer)&&!TextUtils.isEmpty(price_before_offer)&&!TextUtils.isEmpty(price_after_offer)&&!TextUtils.isEmpty(to_date)&&!TextUtils.isEmpty(from_date)){
            if (filepath != null){
                updateOfferViewModel.update_offer_with_img(offer_id,trader_id,title,gender_id,from_date,to_date,price_before_offer,price_after_offer,offer_des,filepath);
            }else {
                updateOfferViewModel.update_offer_without_img(offer_id,trader_id,title,gender_id,from_date,to_date,price_before_offer,price_after_offer,offer_des);
            }

        }else {
            if(TextUtils.isEmpty(title)){
                activityUpdateOfferBinding.etProductName.setError("???????? ?????? ????????????");
            }else {
                activityUpdateOfferBinding.etProductName.setError(null);
            }
            if(TextUtils.isEmpty(from_date)){
                Toast.makeText(this, "???????? ?????????? ?????????? ??????????", Toast.LENGTH_SHORT).show();
            }else {
                activityUpdateOfferBinding.etFromDate.setError(null);
            }
            if(TextUtils.isEmpty(to_date)){
                Toast.makeText(this, "???????? ?????????? ?????????? ??????????", Toast.LENGTH_SHORT).show();
            }else {
                activityUpdateOfferBinding.etToDate.setError(null);
            }
            if(TextUtils.isEmpty(offer_des)){
                activityUpdateOfferBinding.etDetails.setError("???????? ???????????? ??????????");
            }else {
                activityUpdateOfferBinding.etDetails.setError(null);
            }
            if(TextUtils.isEmpty(price_before_offer)){
                activityUpdateOfferBinding.etProductPrice.setError("???????? ?????????? ?????? ??????????");
            }else {
                activityUpdateOfferBinding.etProductPrice.setError(null);
            }
            if(TextUtils.isEmpty(price_after_offer)){
                activityUpdateOfferBinding.etOfferPrice.setError("???????? ?????????? ?????? ??????????");
            }else {
                activityUpdateOfferBinding.etOfferPrice.setError(null);
            }
            if (!TextUtils.isEmpty(price_after_offer)&&!TextUtils.isEmpty(price_before_offer)) {
                if (Double.parseDouble(price_after_offer) > Double.parseDouble(price_before_offer)) {
                    activityUpdateOfferBinding.etOfferPrice.setError("?????? ?????????? ???????? ???? ???????? ?????? ???? ?????????? ?????? ??????????");
                } else {
                    activityUpdateOfferBinding.etOfferPrice.setError(null);
                }
            }
        }
    }

    private void updateLabelStart2() {
        String myFormat = "dd-MM-yyyy";//In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        to_date = sdf.format(myCalendar2.getTime());
        try {
            d2 = sdf.parse(to_date);
            if (d1.compareTo(d2) < 0){
                activityUpdateOfferBinding.etToDate.setText(to_date);
            }else {
                Toast.makeText(this, "?????????? ?????????? ?????????? ?????? ?????????? ??????????????", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void updateLabelStart() {
        String myFormat = "dd-MM-yyyy";//In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        from_date = sdf.format(myCalendar.getTime());
        activityUpdateOfferBinding.etFromDate.setText(from_date);

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
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityUpdateOfferBinding.userImg);
        }
        activityUpdateOfferBinding.userName.setText(user_name);

    }

    public void chooseimage(View view) {
        Check_ReadPermission(IMG);
    }

    private void Check_ReadPermission(Integer img) {
        if (ContextCompat.checkSelfPermission(UpdateOfferActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(UpdateOfferActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(UpdateOfferActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Apply for multiple permissions together
            ActivityCompat.requestPermissions(UpdateOfferActivity.this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, img);
        } else {
            select_photo(img);
        }
    }

    private void select_photo(Integer img) {
        final CharSequence[] items = {"????????????", "?????????? ??????????", "??????????"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateOfferActivity.this);
        builder.setTitle("?????????? ???????? ?????????? ????????????");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("????????????")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUESTCAMERA);
                } else if (items[which].equals("?????????? ??????????")) {
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
                activityUpdateOfferBinding.linearAdd.setVisibility(View.GONE);
                activityUpdateOfferBinding.productImg.setVisibility(View.VISIBLE);
                activityUpdateOfferBinding.productImg.setImageURI(filepath);
            }catch (Exception e){
                filepath = data.getData();
                activityUpdateOfferBinding.linearAdd.setVisibility(View.GONE);
                activityUpdateOfferBinding.productImg.setVisibility(View.VISIBLE);
                activityUpdateOfferBinding.productImg.setImageURI(filepath);
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
                activityUpdateOfferBinding.linearAdd.setVisibility(View.GONE);
                activityUpdateOfferBinding.productImg.setVisibility(View.VISIBLE);
                activityUpdateOfferBinding.productImg.setImageURI(filepath);
            }catch (Exception e){
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                filepath = Utilities.compressImage(UpdateOfferActivity.this, path);
                activityUpdateOfferBinding.linearAdd.setVisibility(View.GONE);
                activityUpdateOfferBinding.productImg.setVisibility(View.VISIBLE);
                Picasso.get().load(filepath).into(activityUpdateOfferBinding.productImg);
            }
        }
    }


    public void OpenCalender2(View view) {
        new DatePickerDialog(UpdateOfferActivity.this, date_picker_dialog2, myCalendar2
                .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void OpenCalender(View view) {
        new DatePickerDialog(UpdateOfferActivity.this, date_picker_dialog, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void getDataFromIntent() {
        slider = (Slider) getIntent().getSerializableExtra("slider");
        offer_id = slider.getId();
        activityUpdateOfferBinding.etProductName.setText(slider.getTitle());
        if (slider.getPriceBeforeOffer() != null){
            activityUpdateOfferBinding.etProductPrice.setText(slider.getPriceBeforeOffer()+"");
        }else {
            activityUpdateOfferBinding.etProductPrice.setText("");
        }
        if (slider.getPriceAfterOffer() != null){
            activityUpdateOfferBinding.etOfferPrice.setText(slider.getPriceAfterOffer()+"");
        }else {
            activityUpdateOfferBinding.etOfferPrice.setText("");
        }
        activityUpdateOfferBinding.etDetails.setText(slider.getDescription());
        gender_id = slider.getGender()+"";
        if (gender_id.equals("1")){
            activityUpdateOfferBinding.spinnerGender.setSelection(0);
        }else if (gender_id.equals("2")){
            activityUpdateOfferBinding.spinnerGender.setSelection(2);
        }else if(gender_id.equals("3")){
            activityUpdateOfferBinding.spinnerGender.setSelection(1);
        }
        long dt1 = Long.parseLong(slider.getFromDate());
        final Date from_dt = new Date((long) (dt1 * 1000));
        final DateFormat f = new SimpleDateFormat("yyyy/MM/dd ", Locale.ENGLISH);
        activityUpdateOfferBinding.etFromDate.setText(f.format(from_dt));
        long dt2 = Long.parseLong(slider.getToDate());
        final Date to_dt = new Date((long) (dt2 * 1000));
        activityUpdateOfferBinding.etToDate.setText(f.format(to_dt));
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/advertisement/"+slider.getImg()).into(activityUpdateOfferBinding.productImg);


    }

    public void setgender(List<Gender> genderlist) {
        this.genderList = genderlist;
    }

    public void sendgendernameslist(List<String> gender_names) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item,gender_names);
        activityUpdateOfferBinding.spinnerGender.setAdapter(arrayAdapter);
    }

    public void createsuccessDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.success_dialog, null);
        TextView txt = view.findViewById(R.id.txt);
        txt.setText("???? ??????????????");
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
                    Intent intent = new Intent(UpdateOfferActivity.this, ProfileActivity.class);
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
        activityUpdateOfferBinding.navView.setNavigationItemSelectedListener(this);
        openDrawer();
    }

    private boolean openDrawer() {
        activityUpdateOfferBinding.drawerlayout.openDrawer(GravityCompat.END);
        return true;
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
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(activityUpdateOfferBinding.userImg);
        }
        activityUpdateOfferBinding.userName.setText(user_name);
        init_navigation_menu();
    }
}