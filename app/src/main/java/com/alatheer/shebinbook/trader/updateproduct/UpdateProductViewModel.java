package com.alatheer.shebinbook.trader.updateproduct;

import android.content.Context;
import android.net.Uri;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.comments.CommentModel;
import com.alatheer.shebinbook.trader.editalboum.UpdateAlboumActivity;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProductViewModel {
    Context context;
    UpdateProductActivity updateProductActivity;

    public UpdateProductViewModel(Context context) {
        this.context = context;
        updateProductActivity = (UpdateProductActivity) context;
    }

    public void update_product_with_img(String product_id, String trader_id, String store_id, String alboum_id, String product_name, String product_price, String product_details, Uri filepath) {
        RequestBody rb_product_id = Utilities.getRequestBodyText(product_id);
        RequestBody rb_trader_id = Utilities.getRequestBodyText(trader_id);
        RequestBody rb_store_id = Utilities.getRequestBodyText(store_id);
        RequestBody rb_alboum_id = Utilities.getRequestBodyText(alboum_id);
        RequestBody rb_product_name = Utilities.getRequestBodyText(product_name);
        RequestBody rb_product_price= Utilities.getRequestBodyText(product_price);
        RequestBody rb_product_details= Utilities.getRequestBodyText(product_details);
        MultipartBody.Part product_img = Utilities.getMultiPart(context, filepath, "img");
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.update_product_to_alboum_with_img(rb_product_id,rb_trader_id,rb_store_id,rb_alboum_id,rb_product_name,rb_product_price,rb_product_details,product_img);
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getData().getSuccess()==1){
                            updateProductActivity.createsuccessDialog();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommentModel> call, Throwable t) {

                }
            });
        }
    }

    public void update_product_without_img(String productId, String trader_id, String store_id, String alboum_id, String product_name, String product_price, String product_details) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.update_product_to_alboum_without_img(productId,trader_id,store_id,alboum_id,product_name,product_price,product_details);
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getData().getSuccess()==1){
                            updateProductActivity.createsuccessDialog();
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
