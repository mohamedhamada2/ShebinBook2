package com.alatheer.shebinbook.authentication.signup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.authentication.Gender;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.authentication.signup.SignupActivity;
import com.alatheer.shebinbook.home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpViewModel {
    Context context;
    SignupActivity signupActivity;
    MySharedPreference mprefs;
    LoginModel loginModel;

    public SignUpViewModel(Context context) {
        this.context = context;
        signupActivity = (SignupActivity) context;
    }

    public void get_gender() {
        List<Gender> genderlist = new ArrayList<>();
        List<String> gender_names = new ArrayList<>();
        Gender gender_male= new Gender();
        gender_male.setGender_id("1");
        gender_male.setGender_name("ذكر");
        Gender gender_female= new Gender();
        gender_female.setGender_id("2");
        gender_female.setGender_name("أنثي");
        genderlist.add(gender_male);
        genderlist.add(gender_female);
        signupActivity.setgender(genderlist);
        for (Gender subcategory : genderlist) {
            gender_names.add(subcategory.getGender_name());
        }
        signupActivity.sendgendernameslist(gender_names);
    }

    public void sendRegisterRequestwithImage(String first_name, String last_name, String phone, String password, Uri filepath, String city_id, String gender_id) {
        Intent intent = new Intent(signupActivity,VerificationCodeActivity.class);
        intent.putExtra("first_name",first_name);
        intent.putExtra("last_name",last_name);
        intent.putExtra("phone",phone);
        intent.putExtra("password",password);
        intent.putExtra("filepath",filepath.toString());
        intent.putExtra("city_id",city_id);
        intent.putExtra("gender_id",gender_id);
        intent.putExtra("flag",1);
        context.startActivity(intent);
        /*RequestBody rb_first_name = Utilities.getRequestBodyText(first_name+"");
        RequestBody rb_last_name = Utilities.getRequestBodyText(last_name);
        RequestBody rb_phone = Utilities.getRequestBodyText(phone+ "");
        RequestBody rb_password = Utilities.getRequestBodyText(password+"");
        RequestBody rb_city_id= Utilities.getRequestBodyText(city_id+"");
        RequestBody rb_gender= Utilities.getRequestBodyText(gender_id+"");
        MultipartBody.Part img = Utilities.getMultiPart(context, filepath, "user_img");
        if (Utilities.isNetworkAvailable(context)) {
            ProgressDialog pd = new ProgressDialog(signupActivity);
            pd.setMessage("loading ...");
            pd.show();
            mprefs = MySharedPreference.getInstance();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<LoginModel> call = getDataService.add_user_with_img(rb_first_name, rb_last_name, rb_phone, rb_password,rb_gender,rb_city_id,img);
            call.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    if(response.isSuccessful()){
                        if(response.body().getStatus()){
                            Toast.makeText(context, "تم تسجيلك بنجاح", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                            loginModel = response.body();
                            mprefs.Create_Update_UserData(context,loginModel);
                            //Toast.makeText(context, "login successfully", Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context, HomeActivity.class));
                            //Animatoo.animateFade(context);
                            signupActivity.finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {

                }
            });
        }*/
    }

    public void sendRegisterRequestwithoutImage(String first_name, String last_name, String phone, String password, String city_id, String gender_id) {
        Intent intent = new Intent(signupActivity,VerificationCodeActivity.class);
        intent.putExtra("first_name",first_name);
        intent.putExtra("last_name",last_name);
        intent.putExtra("phone",phone);
        intent.putExtra("password",password);
        intent.putExtra("city_id",city_id);
        intent.putExtra("gender_id",gender_id);
        intent.putExtra("flag",2);
        context.startActivity(intent);
        /*if (Utilities.isNetworkAvailable(context)) {
            ProgressDialog pd = new ProgressDialog(signupActivity);
            pd.setMessage("loading ...");
            pd.show();
            mprefs = MySharedPreference.getInstance();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<LoginModel> call = getDataService.add_user(first_name,last_name,phone,password,gender_id,city_id);
            call.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    if(response.isSuccessful()){
                        if(response.body().getStatus()){
                            Toast.makeText(context, "تم تسجيلك بنجاح", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                            loginModel = response.body();
                            mprefs.Create_Update_UserData(context,loginModel);
                            //Toast.makeText(context, "login successfully", Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context, HomeActivity.class));
                            //Animatoo.animateFade(context);
                            signupActivity.finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {

                }
            });
        }*/
    }
}
