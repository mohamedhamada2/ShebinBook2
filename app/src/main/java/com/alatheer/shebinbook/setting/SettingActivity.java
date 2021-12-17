package com.alatheer.shebinbook.setting;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

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
import android.widget.Toast;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.cities.CityActivity;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.authentication.signup.SignupActivity;
import com.alatheer.shebinbook.databinding.ActivitySettingBinding;
import com.alatheer.shebinbook.trader.updateoffer.UpdateOfferActivity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class SettingActivity extends AppCompatActivity {
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String user_first_name,user_last_name,user_img,user_phone,title,city_id,gender,old_password,new_password,confirm_password;
    ActivitySettingBinding activitySettingBinding;
    SettingViewModel settingViewModel;
    String user_id;
    int IMG = 1,REQUESTCAMERA =2;
    Uri filepath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        activitySettingBinding = DataBindingUtil.setContentView(this,R.layout.activity_setting);
        settingViewModel = new SettingViewModel(this);
        activitySettingBinding.setSettingviewmodel(settingViewModel);
        getSharedpreferanceData();
        settingViewModel.getData(user_id);
        activitySettingBinding.etCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, CityActivity.class);
                startActivityForResult(intent,5);
            }
        });
        activitySettingBinding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });

    }

    private void validation() {
        user_first_name = activitySettingBinding.etFirstName.getText().toString();
        user_last_name = activitySettingBinding.etLastName.getText().toString();
        user_phone = activitySettingBinding.etPhone.getText().toString();
        title = activitySettingBinding.etCity.getText().toString();
        old_password = activitySettingBinding.etPassword.getText().toString();
        new_password = activitySettingBinding.etNewPassword.getText().toString();
        confirm_password = activitySettingBinding.etConfirmPassword.getText().toString();
        //Toast.makeText(this, gender, Toast.LENGTH_SHORT).show();
        if (!TextUtils.isEmpty(user_first_name)&&!TextUtils.isEmpty(user_phone)
                &&!TextUtils.isEmpty(title)&&new_password.equals(confirm_password)){
            if(filepath != null){
                settingViewModel.updateuserwithImage(user_id,user_first_name,user_last_name,user_phone, new_password, filepath,city_id+"",gender);
            }else {
                settingViewModel.updateuserwithoutImage(user_id,user_first_name,user_last_name,user_phone, new_password,city_id+"",gender);
            }
        }else {
            if (TextUtils.isEmpty(user_first_name)){
                activitySettingBinding.etFirstName.setError("أدخل الإسم الأول");
            }else {
                activitySettingBinding.etFirstName.setError(null);
            }
            if (TextUtils.isEmpty(title)){
                activitySettingBinding.etCity.setError("أدخل المدينة");
            }else {
                activitySettingBinding.etCity.setError(null);
            }
            if (!confirm_password.equals(new_password)){
                activitySettingBinding.etConfirmPassword.setError("تأكيد كلمة المرور غير متطابقة");
            }else {
                activitySettingBinding.etConfirmPassword.setError(null);
            }
        }
    }

    private void getSharedpreferanceData() {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        user_id = loginModel.getData().getUser().getId()+"";
        gender = loginModel.getData().getUser().getGender()+"";

    }

    public void setData(ProfileData loginModel) {
        Toast.makeText(this, "jjjjj", Toast.LENGTH_SHORT).show();
        activitySettingBinding.etFirstName.setText(loginModel.getData().getName());
        activitySettingBinding.etLastName.setText(loginModel.getData().getLastName());
        city_id = loginModel.getData().getCityId()+"";
        activitySettingBinding.etCity.setText(loginModel.getData().getCityId()+"");
        activitySettingBinding.etPhone.setText(loginModel.getData().getPhone());
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+loginModel.getData().getUserImg()).into(activitySettingBinding.userImg);
    }

    public void chooseimage(View view) {
        Check_ReadPermission(IMG);
    }

    private void Check_ReadPermission(Integer img) {
        if (ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Apply for multiple permissions together
            ActivityCompat.requestPermissions(SettingActivity.this, new String[]{
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
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
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
            Picasso.get().load(filepath).into(activitySettingBinding.userImg);
        } else if (requestCode == REQUESTCAMERA && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            final Bitmap bitmap = (Bitmap) bundle.get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
            filepath = data.getData();

            Picasso.get().load(filepath).into(activitySettingBinding.userImg);
        }else if (requestCode == 5 && resultCode == Activity.RESULT_OK){
            title = data.getStringExtra("title");
            city_id = data.getStringExtra("id");
            activitySettingBinding.etCity.setText(title);
        }
    }
}