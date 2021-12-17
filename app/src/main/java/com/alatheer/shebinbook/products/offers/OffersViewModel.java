package com.alatheer.shebinbook.products.offers;

import android.content.Context;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.home.slider.SliderModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OffersViewModel {
    Context context;
    OffersFragment offersFragment;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String gender;
    public OffersViewModel(Context context, OffersFragment offersFragment) {
        this.context = context;
        this.offersFragment = offersFragment;
    }

    public void getOffers(String trader_id) {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(context);
        gender = loginModel.getData().getUser().getGender()+"";
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<SliderModel> call = getDataService.get_ads_in_store(gender,5,trader_id);
            call.enqueue(new Callback<SliderModel>() {
                @Override
                public void onResponse(Call<SliderModel> call, Response<SliderModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            offersFragment.init_recycler(response.body().getData().getData());
                        }
                    }
                }

                @Override
                public void onFailure(Call<SliderModel> call, Throwable t) {

                }
            });
        }
    }
}
