package com.alatheer.shebinbook.trader.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.comments.CommentModel;
import com.alatheer.shebinbook.home.slider.Slider;
import com.alatheer.shebinbook.home.slider.SliderModel;
import com.alatheer.shebinbook.message.Datum;
import com.alatheer.shebinbook.message.MessageAdapter2;
import com.alatheer.shebinbook.message.MessageModel;
import com.alatheer.shebinbook.setting.ProfileData;
import com.alatheer.shebinbook.stores.StoreModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel {
    Context context;
    ProfileActivity profileActivity;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Integer user_gender;
    List<Datum> messagelist;
    MessageAdapter2 messageAdapter2;
    List<Slider> sliderList;
    com.alatheer.shebinbook.trader.profile.SliderAdapter sliderAdapter;
    Integer page2 =1;

    public ProfileViewModel(Context context) {
        this.context = context;
        profileActivity = (ProfileActivity) context;
    }

    public void get_store(String trader_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<StoreModel> call = getDataService.get_trader_store(trader_id);
            call.enqueue(new Callback<StoreModel>() {
                @Override
                public void onResponse(Call<StoreModel> call, Response<StoreModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            if (!response.body().getData().isEmpty()){
                                try {
                                    profileActivity.setData(response.body().getData().get(0));
                                }catch (Exception e){

                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<StoreModel> call, Throwable t) {

                }
            });
        }
    }

    public void getAdvertisment(String trader_id,String store_name,String store_logo) {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(context);
        user_gender = loginModel.getData().getUser().getGender();
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<SliderModel> call = getDataService.get_ads_in_store(user_gender+"",5,trader_id,1);
            call.enqueue(new Callback<SliderModel>() {
                @Override
                public void onResponse(Call<SliderModel> call, Response<SliderModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            sliderList = response.body().getData().getData();
                            if (!sliderList.isEmpty()){
                                if (page2 <= 4){
                                    sliderAdapter = new SliderAdapter(context,sliderList,store_name,store_logo);
                                    profileActivity.init_sliders(sliderAdapter);
                                    page2 = page2+1;
                                    performpagination(trader_id,page2);
                                }
                            }else {
                                profileActivity.setViewpagervisibility();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<SliderModel> call, Throwable t) {
                    Log.d("bug1",t.getMessage());
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void performpagination(String trader_id, Integer page) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<SliderModel> call = getDataService.get_ads_in_store(user_gender+"",5,trader_id,page);
            call.enqueue(new Callback<SliderModel>() {
                @Override
                public void onResponse(Call<SliderModel> call, Response<SliderModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            sliderList = response.body().getData().getData();
                            //Toast.makeText(context, page+"", Toast.LENGTH_SHORT).show();
                            sliderList = response.body().getData().getData();
                            sliderAdapter.add_offer(sliderList);
                            profileActivity.init_sliders(sliderAdapter);

                        }
                    }
                }

                @Override
                public void onFailure(Call<SliderModel> call, Throwable t) {
                    Log.d("bug1",t.getMessage());
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void delete_offer(Integer id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.delete_offer(id);
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getData().getSuccess() == 1){
                            profileActivity.createsuccessdialog();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommentModel> call, Throwable t) {

                }
            });
        }
    }
    public void getMessages(String trader_id,Integer page) {
        //Toast.makeText(context, trader_id, Toast.LENGTH_SHORT).show();
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<MessageModel> call = getDataService.get_messages(trader_id,page);
            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            messagelist = response.body().getData().getData();
                            messageAdapter2 = new MessageAdapter2(messagelist,context);
                            profileActivity.init_messages_recycler(messageAdapter2);
                        }
                    }
                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {

                }
            });
        }
    }

    public void getSearch_stores(String toString) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<StoreModel> call = getDataService.search_store(toString);
            call.enqueue(new Callback<StoreModel>() {
                @Override
                public void onResponse(Call<StoreModel> call, Response<StoreModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            profileActivity.init_search_recycler(response.body().getData());
                        }
                    }
                }

                @Override
                public void onFailure(Call<StoreModel> call, Throwable t) {
                    Log.e("searcherror",t.getMessage());
                }
            });
        }
    }

    public void edit_profile(String store_id, String store_desc, String store_attendance, String store_facebook, String store_what_app, String toString, String store_address, String store_phone, Uri filepath, String governorate, String city, String longMap, String latMap, String offersWords, String offersProducts, String instagram,String store_name) {
        //Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
        if (filepath != null){
            RequestBody rb_store_id = Utilities.getRequestBodyText(store_id);
            RequestBody rb_store_desc = Utilities.getRequestBodyText(store_desc);
            RequestBody rb_store_attendance = Utilities.getRequestBodyText(store_attendance);
            RequestBody rb_store_facebook = Utilities.getRequestBodyText(store_facebook);
            RequestBody rb_store_what_app = Utilities.getRequestBodyText(store_what_app);
            RequestBody rb_mini_describtion = Utilities.getRequestBodyText(toString);
            RequestBody rb_store_address = Utilities.getRequestBodyText(store_address);
            RequestBody rb_store_phone = Utilities.getRequestBodyText(store_phone);
            RequestBody rb_governorate = Utilities.getRequestBodyText("");
            RequestBody rb_city = Utilities.getRequestBodyText("");
            RequestBody rb_longMap= Utilities.getRequestBodyText("");
            RequestBody rb_latMap = Utilities.getRequestBodyText("");
            RequestBody rb_offersWords = Utilities.getRequestBodyText(offersWords);
            RequestBody rb_offersProducts = Utilities.getRequestBodyText(offersProducts);
            RequestBody rb_instagram;
            if (instagram != null){
                 rb_instagram = Utilities.getRequestBodyText(instagram);
            }else {
                 rb_instagram = Utilities.getRequestBodyText("");
            }
            RequestBody rb_store_name = Utilities.getRequestBodyText(store_name);
            MultipartBody.Part logo = Utilities.getMultiPart(context, filepath, "logo");
            if (Utilities.isNetworkAvailable(context)){
                ProgressDialog pd = new ProgressDialog(profileActivity);
                pd.setMessage("loading ...");
                pd.show();
                GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                Call<CommentModel> call = getDataService.update_store_with_img(rb_store_id,rb_store_desc,rb_store_attendance,
                        rb_store_facebook,rb_store_what_app,rb_mini_describtion,rb_store_address,rb_store_phone,logo,rb_governorate,rb_city,rb_longMap,rb_latMap,
                        rb_offersWords,rb_offersProducts,rb_instagram,rb_store_name);
                call.enqueue(new Callback<CommentModel>() {
                    @Override
                    public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                        if (response.isSuccessful()){
                            if (response.body().getData().getSuccess()==1){
                                Toast.makeText(profileActivity, "???? ?????????? ?????????????? ??????????", Toast.LENGTH_SHORT).show();
                                profileActivity.dismiss();
                                pd.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CommentModel> call, Throwable t) {
                        //Log.e("error555",t.getMessage());
                    }
                });
            }
        }else {
            if (Utilities.isNetworkAvailable(context)){
                ProgressDialog pd = new ProgressDialog(profileActivity);
                pd.setMessage("loading ...");
                pd.show();
                GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                Call<CommentModel> call = getDataService.update_store_without_img(store_id,store_desc,store_attendance,store_facebook,store_what_app,
                        toString,store_address,store_phone,"","",longMap,latMap,offersWords,offersProducts,instagram,store_name);
                call.enqueue(new Callback<CommentModel>() {
                    @Override
                    public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                        if (response.isSuccessful()){
                            if (response.body().getData().getSuccess()==1){
                                Toast.makeText(context, "???? ?????????????? ??????????", Toast.LENGTH_SHORT).show();
                                profileActivity.dismiss();
                                pd.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CommentModel> call, Throwable t) {

                    }
                });
            }
        }
    }

    public void TraderPagination(String trader_id, Integer page2) {
        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<MessageModel> call = getDataService.get_messages(trader_id,page2);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()){
                        if (!response.body().getData().getData().isEmpty()){
                            messagelist = response.body().getData().getData();
                            messageAdapter2.add_message(messagelist);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {

            }
        });
    }

    public void getData(String user_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ProfileData> call = getDataService.get_user_data(user_id);
            call.enqueue(new Callback<ProfileData>() {
                @Override
                public void onResponse(Call<ProfileData> call, Response<ProfileData> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            profileActivity.setUserData(response.body());

                        }
                    }
                }

                @Override
                public void onFailure(Call<ProfileData> call, Throwable t) {
                    Log.e("getdata",t.getMessage());
                }
            });
        }
    }
}
