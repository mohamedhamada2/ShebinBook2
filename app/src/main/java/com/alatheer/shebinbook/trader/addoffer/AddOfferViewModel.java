package com.alatheer.shebinbook.trader.addoffer;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.authentication.Gender;
import com.alatheer.shebinbook.comments.CommentModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddOfferViewModel {
    Context context;
    AddOfferActivity addOfferActivity;

    public AddOfferViewModel(Context context) {
        this.context = context;
        addOfferActivity = (AddOfferActivity) context;
    }

    public void get_gender() {
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
        addOfferActivity.setgender(genderlist);
        for (Gender subcategory : genderlist) {
            gender_names.add(subcategory.getGender_name());
        }
        addOfferActivity.sendgendernameslist(gender_names);
    }

    public void add_offer(Integer trader_id, String title, String gender_id, String from_date, String to_date, String price_before_offer, String price_after_offer,String offer_des ,Uri filepath) {
        ProgressDialog pd = new ProgressDialog(addOfferActivity);
        pd.setMessage("loading ...");
        pd.show();
        RequestBody rb_trader_id = Utilities.getRequestBodyText(trader_id + "");
        RequestBody rb_title = Utilities.getRequestBodyText(title);
        RequestBody rb_gender_id = Utilities.getRequestBodyText(gender_id);
        RequestBody rb_from_date = Utilities.getRequestBodyText(from_date);
        RequestBody rb_to_date = Utilities.getRequestBodyText(to_date);
        RequestBody rb_price_before_offers = Utilities.getRequestBodyText(price_before_offer);
        RequestBody rb_price_after_offer = Utilities.getRequestBodyText(price_after_offer);
        RequestBody rb_offer_des = Utilities.getRequestBodyText(offer_des);
        MultipartBody.Part offer_img = Utilities.getMultiPart(context, filepath, "img");
        if (Utilities.isNetworkAvailable(context)) {
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.add_offer(rb_title, rb_gender_id, rb_trader_id, rb_from_date,rb_to_date, rb_price_before_offers, rb_price_after_offer,rb_offer_des ,offer_img);
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getData().getSuccess() == 1) {
                            pd.dismiss();
                            addOfferActivity.createsuccessDialog();
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
