package com.alatheer.shebinbook.trader.images;

import android.content.Context;
import android.widget.Toast;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.comments.CommentModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImagesViewModel {
    Context context;

    public ImagesViewModel(Context context, ImageFragment imageFragment) {
        this.context = context;
        this.imageFragment = imageFragment;
    }

    ImageFragment imageFragment;

    public void get_gallery(String trader_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ImagesData> call = getDataService.get_gallery(trader_id);
            call.enqueue(new Callback<ImagesData>() {
                @Override
                public void onResponse(Call<ImagesData> call, Response<ImagesData> response) {
                    if (response.isSuccessful()){
                        imageFragment.init_recycler(response.body().getData());
                    }
                }

                @Override
                public void onFailure(Call<ImagesData> call, Throwable t) {

                }
            });
        }
    }

    public void delete(Integer id,String trader_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.delete_from_gallery(id);
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getData().getSuccess() == 1){
                            Toast.makeText(context, "تم الحذف بنجاج", Toast.LENGTH_SHORT).show();
                            get_gallery(trader_id);
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
