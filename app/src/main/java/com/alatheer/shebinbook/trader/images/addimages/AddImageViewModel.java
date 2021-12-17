package com.alatheer.shebinbook.trader.images.addimages;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.comments.CommentModel;
import com.alatheer.shebinbook.products.ProductsActivity;
import com.alatheer.shebinbook.trader.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddImageViewModel {
    Context context;
    AddImagesActivity addImagesActivity;
    List<MultipartBody.Part> images ;

    public AddImageViewModel(Context context) {
        this.context = context;
        addImagesActivity = (AddImagesActivity) context;
    }

    public void add_gallery(String title, Integer trader_id, List<Uri> all_images,String store_id) {
        ProgressDialog pd = new ProgressDialog(addImagesActivity);
        pd.setMessage("loading ...");
        pd.show();
        images = new ArrayList<>();
        RequestBody rb_trader_id = Utilities.getRequestBodyText(trader_id + "");
        RequestBody rb_store_id = Utilities.getRequestBodyText(store_id + "");
        RequestBody rb_title = Utilities.getRequestBodyText(title);
        for(Uri filePath21 :all_images){
            MultipartBody.Part vimg = Utilities.getMultiPart(context,filePath21,"images[]");
            images.add(vimg);
        }
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.add_gallery(rb_title,rb_trader_id,images,rb_store_id);
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getData().getSuccess()==1){
                            pd.dismiss();
                            Toast.makeText(context, "تم الاضافة بنجاح", Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(addImagesActivity, ProfileActivity.class));
                            ProfileActivity.fa.finish();
                            addImagesActivity.finish();
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
