package com.alatheer.shebinbook.authentication.signup;

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
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.authentication.Gender;
import com.alatheer.shebinbook.authentication.cities.CityActivity;
import com.alatheer.shebinbook.authentication.cities.Datum;
import com.alatheer.shebinbook.databinding.ActivitySignupBinding;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding activitySignupBinding;
    SignUpViewModel signUpViewModel;
    List<Gender> genderList;
    String gender_id,city_id,title;
    Integer flag;
    Integer IMG = 1;
    Uri filepath;
    int REQUESTCAMERA = 2;
    String first_name,last_name,phone,password;
    public static Activity fa;
    List<String> datumListtitle;
    List<Datum> datumList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        activitySignupBinding = DataBindingUtil.setContentView(this,R.layout.activity_signup);
        signUpViewModel = new SignUpViewModel(this);
        activitySignupBinding.setSignupviewmodel(signUpViewModel);
        fa = this;
        signUpViewModel.get_gender();
        signUpViewModel.get_cities();
        //getDataIntent();
        activitySignupBinding.txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        activitySignupBinding.spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender_id = genderList.get(i).getGender_id();
                TextView textView = (TextView) view;
                try {
                    textView.setTextColor(getResources().getColor(R.color.purple_500));
                    textView.setBackground(null);
                }catch (Exception e){

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        activitySignupBinding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validation();
            }
        });
        activitySignupBinding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                city_id = datumList.get(i).getId()+"";
               // Toast.makeText(SignupActivity.this, city_id, Toast.LENGTH_SHORT).show();
                TextView textView = (TextView) view;
                try {
                    textView.setTextColor(getResources().getColor(R.color.purple_500));
                    textView.setBackground(null);
                }catch (Exception e){

                }
                //citytitlelist.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void Validation() {
        first_name = activitySignupBinding.etFirstName.getText().toString();
        last_name = activitySignupBinding.etLastName.getText().toString();
        phone = activitySignupBinding.etPhone.getText().toString();
        password =activitySignupBinding.etPassword.getText().toString();
        if (!TextUtils.isEmpty(first_name)&&!TextUtils.isEmpty(last_name)&&!TextUtils.isEmpty(phone)
        &&!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(gender_id)) {
            if (filepath != null) {
                //Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                signUpViewModel.sendRegisterRequestwithImage(first_name, last_name, phone, password, filepath, city_id, gender_id);
            } else {
                signUpViewModel.sendRegisterRequestwithoutImage(first_name, last_name, phone, password, city_id, gender_id);
            }
        }else {
            if (TextUtils.isEmpty(first_name)){
                activitySignupBinding.etFirstName.setError("أدخل الإسم الأول");
            }else {
                activitySignupBinding.etFirstName.setError(null);
            }
            if (TextUtils.isEmpty(last_name)){
                activitySignupBinding.etLastName.setError("أدخل الإسم الأخير");
            }else {
                activitySignupBinding.etLastName.setError(null);
            }
            if (TextUtils.isEmpty(phone)){
                activitySignupBinding.etPhone.setError("أدخل رقم الهاتف");
            }else {
                activitySignupBinding.etPhone.setError(null);
            }
            if (TextUtils.isEmpty(password)){
                activitySignupBinding.etPassword.setError("أدخل كلمة المرور");
            }else {
                activitySignupBinding.etPassword.setError(null);
            }
        }
    }

    /*private void getDataIntent() {
        flag = getIntent().getIntExtra("flag",0);
        if (flag == 2){
            title = getIntent().getStringExtra("title");
            city_id = getIntent().getStringExtra("id");
            activitySignupBinding.etCity.setText(title);

        }
    }*/

    public void setgender(List<Gender> genderlist) {
        this.genderList= genderlist;
    }

    public void sendgendernameslist(List<String> gender_names) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item,gender_names);
        activitySignupBinding.spinnerGender.setAdapter(arrayAdapter);
    }

    public void chooseimage(View view) {
        Check_ReadPermission(IMG);
    }

    private void Check_ReadPermission(Integer img) {
        if (ContextCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Apply for multiple permissions together
            ActivityCompat.requestPermissions(SignupActivity.this, new String[]{
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
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
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
                activitySignupBinding.userimg.setImageURI(filepath);
                Picasso.get().load(filepath).into(activitySignupBinding.userimg);
            }catch (Exception e){
                filepath = data.getData();
                activitySignupBinding.cardview.setVisibility(View.VISIBLE);
                Picasso.get().load(filepath).into(activitySignupBinding.userimg);
            }

        }else if (requestCode == REQUESTCAMERA && resultCode == Activity.RESULT_OK){
            Bundle bundle = data.getExtras();
            final Bitmap bitmap = (Bitmap) bundle.get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
            filepath = Utilities.compressImage(SignupActivity.this,path);
            activitySignupBinding.cardview.setVisibility(View.VISIBLE);
            Picasso.get().load(filepath).into(activitySignupBinding.userimg);
        }

    }

    public void setCitiesspinnerData(List<String> datumListtitle, List<Datum> datumList) {
        this.datumListtitle = datumListtitle;
        this.datumList = datumList;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SignupActivity.this,R.layout.spinner_item,datumListtitle);
        activitySignupBinding.spinnerCity.setAdapter(arrayAdapter);
    }

}