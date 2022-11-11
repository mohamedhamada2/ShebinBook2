package com.alatheer.shebinbook.trader.addproduct;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.comments.CommentModel;
import com.alatheer.shebinbook.setting.ProfileData;
import com.alatheer.shebinbook.trader.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductViewModel {
    Context context;
    AddProductActivity addProductActivity;
    List<MultipartBody.Part> images ;

    public AddProductViewModel(Context context) {
        this.context = context;
        addProductActivity = (AddProductActivity) context;
    }


    public void addproduct(String trader_id, String store_id, String alboum_id, String product_name, String product_price, String product_details, Uri filepath, List<Uri> all_images) {
        ProgressDialog pd = new ProgressDialog(addProductActivity);
        pd.setMessage("loading ...");
        pd.show();
        images = new ArrayList<>();
        RequestBody rb_trader_id = Utilities.getRequestBodyText(trader_id);
        RequestBody rb_store_id = Utilities.getRequestBodyText(store_id);
        RequestBody rb_alboum_id = Utilities.getRequestBodyText(alboum_id);
        RequestBody rb_product_name = Utilities.getRequestBodyText(product_name);
        RequestBody rb_product_price= Utilities.getRequestBodyText(product_price);
        RequestBody rb_product_details= Utilities.getRequestBodyText(product_details);
        MultipartBody.Part product_img = Utilities.getMultiPart(context, filepath, "img");
        for(Uri filePath21 :all_images){
            MultipartBody.Part vimg = Utilities.getMultiPart(context,filePath21,"image[]");
            images.add(vimg);
        }
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.add_product_to_alboum(rb_trader_id,rb_store_id,rb_alboum_id,rb_product_name,rb_product_price,rb_product_details,product_img,images);
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getData().getSuccess() == 1){
                            pd.dismiss();
                            Toast.makeText(addProductActivity, "تم إضافة المنتج بنجاح", Toast.LENGTH_SHORT).show();
                            addProductActivity.startActivity(new Intent(addProductActivity, ProfileActivity.class));
                            ProfileActivity.fa.finish();
                            addProductActivity.finish();
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
                            addProductActivity.setData(response.body());

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
