package com.alatheer.shebinbook.products.offers;

import android.content.Context;
import android.widget.Toast;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.home.slider.Slider;
import com.alatheer.shebinbook.home.slider.SliderModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OffersViewModel {
    Context context;
    OffersFragment offersFragment;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String gender;
    OffersAdapter offersAdapter;
    List<Slider> offerlist;
    public OffersViewModel(Context context, OffersFragment offersFragment) {
        this.context = context;
        this.offersFragment = offersFragment;
    }

    public void getOffers(String trader_id,Integer page,String store_name,String store_logo,String store_id) {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(context);
        gender = loginModel.getData().getUser().getGender()+"";
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<SliderModel> call = getDataService.get_ads_in_store(gender,5,trader_id,page);
            call.enqueue(new Callback<SliderModel>() {
                @Override
                public void onResponse(Call<SliderModel> call, Response<SliderModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            offerlist = response.body().getData().getData();
                            offersAdapter = new OffersAdapter(context,offerlist,store_name,store_logo,store_id);
                            offersFragment.init_recycler(offersAdapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<SliderModel> call, Throwable t) {

                }
            });
        }
    }

    public void PerformPagination(int page, String trader_id) {
        //Toast.makeText(context, "offer", Toast.LENGTH_SHORT).show();
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<SliderModel> call = getDataService.get_ads_in_store(gender,5,trader_id,page);
            call.enqueue(new Callback<SliderModel>() {
                @Override
                public void onResponse(Call<SliderModel> call, Response<SliderModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            if (!response.body().getData().getData().isEmpty()){
                                offerlist = response.body().getData().getData();
                                offersAdapter.add_offer(offerlist);
                                offersFragment.init_recycler(offersAdapter);
                            }
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
