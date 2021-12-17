package com.alatheer.shebinbook.trader.profile;

import android.content.Context;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.products.GalleryModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryViewModel {
    Context context;
    GalleryFragment galleryFragment;

    public GalleryViewModel(Context context, GalleryFragment galleryFragment) {
        this.context = context;
        this.galleryFragment = galleryFragment;
    }
    public void get_galleries(String store_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<GalleryModel> call = getDataService.get_galleries(store_id);
            call.enqueue(new Callback<GalleryModel>() {
                @Override
                public void onResponse(Call<GalleryModel> call, Response<GalleryModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            galleryFragment.init_recycler(response.body().getData());
                        }
                    }
                }

                @Override
                public void onFailure(Call<GalleryModel> call, Throwable t) {

                }
            });
        }
    }
}
