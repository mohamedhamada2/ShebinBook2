package com.alatheer.shebinbook.authentication.favorite;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.comments.CommentModel;
import com.alatheer.shebinbook.message.MessageModel;
import com.alatheer.shebinbook.stores.Store;
import com.alatheer.shebinbook.stores.StoreModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteViewModel {
    Context context;
    FavoriteActivity favoriteActivity;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    String user_id;

    public FavoriteViewModel(Context context) {
        this.context = context;
        favoriteActivity = (FavoriteActivity) context;
    }

    public void delete_fav(Store favorite) {

        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.delete_from_fav(user_id,favorite.getId()+"") ;
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            Toast.makeText(context, "تمت الإزالة من المفضلة بنجاح", Toast.LENGTH_SHORT).show();
                            getStores();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommentModel> call, Throwable t) {

                }
            });
        }
    }

    public void getStores() {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(context);
        user_id = loginModel.getData().getUser().getId()+"";
        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<FavoriteStoreModel> call = getDataService.get_fav_stores(user_id);
        call.enqueue(new Callback<FavoriteStoreModel>() {
            @Override
            public void onResponse(Call<FavoriteStoreModel> call, Response<FavoriteStoreModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()){
                        favoriteActivity.init_recycler(response.body().getData().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<FavoriteStoreModel> call, Throwable t) {

            }
        });
    }

    public void getSearch_stores(String toString) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<StoreModel> call = getDataService.search_store(toString);
            call.enqueue(new Callback<StoreModel>() {
                @Override
                public void onResponse(Call<StoreModel> call, Response<StoreModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            favoriteActivity.init_search_recycler(response.body().getData());
                        }
                    }
                }

                @Override
                public void onFailure(Call<StoreModel> call, Throwable t) {
                    Log.e("searcherror",t.getMessage());
                }
            });
        }
    }
    public void getMessages(Integer trader_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<MessageModel> call = getDataService.get_messages(trader_id+"");
            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                            favoriteActivity.init_messages_recycler(response.body().getData().getData());
                        }
                    }
                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {

                }
            });
        }
    }

    public void getUserMessages(String user_id) {
        //Toast.makeText(context, user_id, Toast.LENGTH_SHORT).show();
        if (Utilities.isNetworkAvailable(context)) {
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<MessageModel> call = getDataService.get_user_messages(user_id);
            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                            favoriteActivity.init_messages_recycler(response.body().getData().getData());
                        }
                    }
                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {
                    Log.e("error36", t.getMessage());
                }
            });
        }
    }

}
