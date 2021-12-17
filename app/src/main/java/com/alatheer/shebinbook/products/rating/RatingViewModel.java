package com.alatheer.shebinbook.products.rating;

import android.content.Context;
import android.widget.Toast;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.comments.CommentModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingViewModel {
    Context context;
    RatingFragment ratingFragment;

    public RatingViewModel(Context context, RatingFragment ratingFragment) {
        this.context = context;
        this.ratingFragment = ratingFragment;
    }

    public void getRates(String store_id) {
        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<RatingModel> call = getDataService.get_rates(store_id);
        call.enqueue(new Callback<RatingModel>() {
            @Override
            public void onResponse(Call<RatingModel> call, Response<RatingModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()){
                        ratingFragment.init_rating_recycler(response.body().getData().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<RatingModel> call, Throwable t) {

            }
        });
    }

    public void delete_rating(Integer id,String store_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.delete_rating(id);
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getData().getSuccess()==1){
                            Toast.makeText(context, "تم الحذف بنجاح", Toast.LENGTH_SHORT).show();
                            getRates(store_id);
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
