package com.alatheer.shebinbook.products.images;

import android.content.Context;
import android.util.Log;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.trader.images.ImagesData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageViewModel {
    Context context;
    ImageFragment imageFragment;

    public ImageViewModel(Context context, ImageFragment imageFragment) {
        this.context = context;
        this.imageFragment = imageFragment;
    }

    public void get_gallery(String trader_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ImagesData> call = getDataService.get_gallery(trader_id);
            call.enqueue(new Callback<ImagesData>() {
                @Override
                public void onResponse(Call<ImagesData> call, Response<ImagesData> response) {
                    if (response.isSuccessful()){
                        try {
                            imageFragment.init_recycler(response.body().getData());
                        }catch (Exception e){
                            Log.e("error_msg",e.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ImagesData> call, Throwable t) {

                }
            });
        }
    }

}
