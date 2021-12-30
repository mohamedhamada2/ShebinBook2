package com.alatheer.shebinbook.trader.updateoffer;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.authentication.Gender;
import com.alatheer.shebinbook.comments.CommentModel;
import com.alatheer.shebinbook.setting.ProfileData;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateOfferViewModel {
    Context context;
    UpdateOfferActivity updateOfferActivity;

    public UpdateOfferViewModel(Context context) {
        this.context = context;
        updateOfferActivity = (UpdateOfferActivity) context;
    }

    public void getgender() {
        List<Gender> genderlist = new ArrayList<>();
        List<String> gender_names = new ArrayList<>();
        Gender gender_male= new Gender();
        gender_male.setGender_id("1");
        gender_male.setGender_name("ذكر");
        Gender gender_female= new Gender();
        gender_female.setGender_id("3");
        gender_female.setGender_name("أنثي");
        Gender all_gender = new Gender();
        all_gender.setGender_id("2");
        all_gender.setGender_name("للجميع");
        genderlist.add(gender_male);
        genderlist.add(gender_female);
        genderlist.add(all_gender);
        updateOfferActivity.setgender(genderlist);
        for (Gender subcategory : genderlist) {
            gender_names.add(subcategory.getGender_name());
        }
        updateOfferActivity.sendgendernameslist(gender_names);
    }

    public void update_offer_with_img(Integer offer_id, Integer trader_id, String title, String gender_id, String from_date, String to_date, String price_before_offer, String price_after_offer, String offer_des,Uri filepath) {
        RequestBody rb_offer_id = Utilities.getRequestBodyText(offer_id + "");
        RequestBody rb_trader_id = Utilities.getRequestBodyText(trader_id + "");
        RequestBody rb_title = Utilities.getRequestBodyText(title);
        RequestBody rb_gender_id = Utilities.getRequestBodyText(gender_id);
        RequestBody rb_from_date = Utilities.getRequestBodyText(from_date);
        RequestBody rb_to_date = Utilities.getRequestBodyText(to_date);
        RequestBody rb_price_before_offers = Utilities.getRequestBodyText(price_before_offer);
        RequestBody rb_price_after_offer = Utilities.getRequestBodyText(price_after_offer);
        RequestBody rb_offer_des = Utilities.getRequestBodyText(offer_des);
        MultipartBody.Part offer_img = Utilities.getMultiPart(context, filepath, "img");
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.update_offer_with_img(rb_offer_id,rb_title,rb_gender_id,rb_trader_id,rb_from_date,rb_to_date,rb_price_before_offers,rb_price_after_offer,rb_offer_des,offer_img);
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getData().getSuccess() ==1){
                            updateOfferActivity.createsuccessDialog() ;
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommentModel> call, Throwable t) {

                }
            });
        }
    }

    public void update_offer_without_img(Integer offer_id, Integer trader_id, String title, String gender_id, String from_date, String to_date, String price_before_offer, String price_after_offer,String offer_des) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.update_offer_without_img(offer_id+"",title,gender_id,trader_id+"",from_date,to_date,price_before_offer,price_after_offer,offer_des);
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getData().getSuccess() ==1){
                            updateOfferActivity.createsuccessDialog() ;
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommentModel> call, Throwable t) {

                }
            });
        }
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
                            updateOfferActivity.setData(response.body());

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
