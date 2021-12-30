package com.alatheer.shebinbook.trader.editalboum;

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

public class UpdateAlboumViewModel {
    Context context;
    UpdateAlboumActivity updateAlboumActivity;
    public UpdateAlboumViewModel(Context context) {
        this.context = context;
        updateAlboumActivity = (UpdateAlboumActivity) context;
    }

    public void update_alboum(Integer store_id, String alboum_name, Integer alboum_id, Integer trader_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.update_alboum(trader_id+"",store_id+"",alboum_name,alboum_id+"");
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getData().getSuccess()==1){
                            Toast.makeText(context, "تم تعديل الألبوم بنجاح", Toast.LENGTH_SHORT).show();
                            updateAlboumActivity.startActivity(new Intent(updateAlboumActivity, ProfileActivity.class));
                            ProfileActivity.fa.finish();
                            updateAlboumActivity.finish();
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
                            updateAlboumActivity.setData(response.body());

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
