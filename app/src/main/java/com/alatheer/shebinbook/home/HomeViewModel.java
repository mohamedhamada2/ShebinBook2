package com.alatheer.shebinbook.home;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.comments.CommentModel;
import com.alatheer.shebinbook.home.category.CategoryModel;
import com.alatheer.shebinbook.home.slider.SliderModel;
import com.alatheer.shebinbook.message.MessageModel;
import com.alatheer.shebinbook.posts.PostData;
import com.alatheer.shebinbook.stores.StoreModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel {
    Context context;
    HomeActivity homeActivity;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Integer user_gender;
    public HomeViewModel(Context context) {
        this.context = context;
        homeActivity = (HomeActivity) context;
    }

    public void getAdvertisment() {

        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(context);
        user_gender = loginModel.getData().getUser().getGender();
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<SliderModel> call = getDataService.get_ads(user_gender+"",1);
            call.enqueue(new Callback<SliderModel>() {
                @Override
                public void onResponse(Call<SliderModel> call, Response<SliderModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            if (!response.body().getData().getData().isEmpty()){
                                homeActivity.init_sliders(response.body().getData().getData());
                            }else {
                                homeActivity.setViewPagerGone();
                            }

                        }
                    }
                }

                @Override
                public void onFailure(Call<SliderModel> call, Throwable t) {
                    Log.d("bug1",t.getMessage());
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void getCategories() {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CategoryModel> call = getDataService.get_categories();
            call.enqueue(new Callback<CategoryModel>() {
                @Override
                public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            if (response.body().getData().getData().size()>=2){
                                homeActivity.init_categories(response.body().getData().getData());
                            }else {
                                homeActivity.dismisscategorycart();
                            }


                        }
                    }
                }

                @Override
                public void onFailure(Call<CategoryModel> call, Throwable t) {
                    Log.d("bug1",t.getMessage());
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void getposts(Integer page,String user_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<PostData> call = getDataService.get_all_posts("1",page,user_id);
            call.enqueue(new Callback<PostData>() {
                @Override
                public void onResponse(Call<PostData> call, Response<PostData> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            if (response.body().getData().getData().size()>=3){
                                homeActivity.init_recycler(response.body().getData().getData());
                            }else {
                                homeActivity.init_recycler2(response.body().getData().getData());
                            }

                        }
                    }
                }

                @Override
                public void onFailure(Call<PostData> call, Throwable t) {

                }
            });
        }
    }

    public void getMessages(String trader_id) {
        //Toast.makeText(context, trader_id, Toast.LENGTH_SHORT).show();
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<MessageModel> call = getDataService.get_messages(trader_id);
            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            homeActivity.init_messages_recycler(response.body().getData().getData());
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
        if (Utilities.isNetworkAvailable(context)) {
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<MessageModel> call = getDataService.get_user_messages(user_id);
            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            homeActivity.init_messages_recycler(response.body().getData().getData());
                        }
                    }
                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {

                }
            });
        }
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
                            homeActivity.init_search_recycler(response.body().getData());
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

    public void add_fav(Integer id, String user_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.add_post_to_fav(id+"",user_id);
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getData().getSuccess()==1){
                            Toast.makeText(homeActivity, "تمت أضافة البوست للمفضلة", Toast.LENGTH_SHORT).show();
                            getposts(1,user_id);
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommentModel> call, Throwable t) {

                }
            });
        }
    }

    public void delete_fav(Integer id, String user_id) {
        if (Utilities.isNetworkAvailable(context)) {
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.delete_post_from_fav(id + "", user_id);
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getData().getSuccess() == 1) {
                            Toast.makeText(homeActivity, "تمت إزالة البوست من المفضلة", Toast.LENGTH_SHORT).show();
                            getposts(1,user_id);
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommentModel> call, Throwable t) {

                }
            });
        }
    }

    public void delete_post(Integer id,String user_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.delete_post(id);
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(homeActivity, "تم حذف المنشور بنجاح", Toast.LENGTH_SHORT).show();
                        getposts(1,user_id);
                    }
                }

                @Override
                public void onFailure(Call<CommentModel> call, Throwable t) {

                }
            });
        }
    }
}
