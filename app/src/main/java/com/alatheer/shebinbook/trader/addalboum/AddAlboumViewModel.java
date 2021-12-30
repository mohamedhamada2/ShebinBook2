package com.alatheer.shebinbook.trader.addalboum;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.comments.CommentModel;
import com.alatheer.shebinbook.setting.ProfileData;
import com.alatheer.shebinbook.trader.profile.ProfileActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAlboumViewModel {
     Context context;
     AddAlboumActivity addAlboumActivity;

    public AddAlboumViewModel(Context context) {
        this.context = context;
        this.addAlboumActivity = (AddAlboumActivity) context;
    }

    public void add_alboum(String trader_id,String store_id,String alboum_name) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.add_alboum(trader_id,store_id,alboum_name);
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getData().getSuccess() ==1){
                            Toast.makeText(context, "تم إضافة الألبوم بنجاح", Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(addAlboumActivity,ProfileActivity.class));
                            ProfileActivity.fa.finish();
                            addAlboumActivity.finish();
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
                            addAlboumActivity.setData(response.body());

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
