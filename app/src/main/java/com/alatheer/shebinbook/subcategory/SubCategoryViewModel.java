package com.alatheer.shebinbook.subcategory;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.home.slider.SliderModel;
import com.alatheer.shebinbook.message.Datum;
import com.alatheer.shebinbook.message.MessageAdapter2;
import com.alatheer.shebinbook.message.MessageModel;
import com.alatheer.shebinbook.setting.ProfileData;
import com.alatheer.shebinbook.stores.StoreModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryViewModel {
    Context context;
    SubCategoryActivity subCategoryActivity;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Integer user_gender;
    List<Datum> messagelist;
    MessageAdapter2 messageAdapter2;

    public SubCategoryViewModel(Context context) {
        this.context = context;
        subCategoryActivity = (SubCategoryActivity) context;
    }

    public void getAds(String category_id) {
        try {
            mySharedPreference = MySharedPreference.getInstance();
            loginModel = mySharedPreference.Get_UserData(context);
            user_gender = loginModel.getData().getUser().getGender();
            if (Utilities.isNetworkAvailable(context)){
                GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                Call<CategorySliderModel> call = getDataService.get_Category_slider(3,category_id,1);
                call.enqueue(new Callback<CategorySliderModel>() {
                    @Override
                    public void onResponse(Call<CategorySliderModel> call, Response<CategorySliderModel> response) {
                        if (response.isSuccessful()){
                            if (response.body().getStatus()){
                                subCategoryActivity.init_sliders(response.body().getData().getData());

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CategorySliderModel> call, Throwable t) {
                        Log.d("bug1",t.getMessage());
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }catch (Exception e){
            Log.e("error6",e.getMessage());
        }
    }

    public void getMessages(String trader_id,Integer page) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<MessageModel> call = getDataService.get_messages(trader_id,page);
            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                            messagelist = response.body().getData().getData();
                            messageAdapter2 = new MessageAdapter2(messagelist,context);
                            subCategoryActivity.init_messages_recycler(messageAdapter2);
                        }
                    }
                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {

                }
            });
        }
    }

    public void getUserMessages(String user_id,Integer page) {
        //Toast.makeText(context, user_id, Toast.LENGTH_SHORT).show();
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<MessageModel> call = getDataService.get_user_messages(user_id,page);
            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                            messagelist = response.body().getData().getData();
                            messageAdapter2 = new MessageAdapter2(messagelist,context);
                            subCategoryActivity.init_messages_recycler(messageAdapter2);
                        }
                    }
                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {
                    Log.e("error36",t.getMessage());
                }
            });
        }
    }
    public void TraderPagination(String s, Integer page) {
        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<MessageModel> call = getDataService.get_messages(s,page);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()){
                        if (!response.body().getData().getData().isEmpty()){
                            messagelist = response.body().getData().getData();
                            messageAdapter2.add_message(messagelist);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {

            }
        });
    }

    public void UserPagination(String user_id, Integer page) {
        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<MessageModel> call = getDataService.get_user_messages(user_id,page);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()){
                        if (!response.body().getData().getData().isEmpty()){
                            messagelist = response.body().getData().getData();
                            messageAdapter2.add_message(messagelist);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {

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
                            subCategoryActivity.init_search_recycler(response.body().getData());
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

    public void getData(String user_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ProfileData> call = getDataService.get_user_data(user_id);
            call.enqueue(new Callback<ProfileData>() {
                @Override
                public void onResponse(Call<ProfileData> call, Response<ProfileData> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            subCategoryActivity.setData(response.body());

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

    public void get_subcategories(String category_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<SubCategoryModel> call = getDataService.get_subcategories(category_id);
            call.enqueue(new Callback<SubCategoryModel>() {
                @Override
                public void onResponse(Call<SubCategoryModel> call, Response<SubCategoryModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            subCategoryActivity.initSubCategories(response.body());
                        }
                    }
                }

                @Override
                public void onFailure(Call<SubCategoryModel> call, Throwable t) {

                }
            });
        }
    }
}
