package com.alatheer.shebinbook.stores;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.comments.CommentModel;
import com.alatheer.shebinbook.home.slider.SliderModel;
import com.alatheer.shebinbook.message.Datum;
import com.alatheer.shebinbook.message.MessageAdapter2;
import com.alatheer.shebinbook.message.MessageModel;
import com.alatheer.shebinbook.setting.ProfileData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoresViewModel {
    Context context;
    StoresActivity storesActivity;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Integer user_gender;
    String category_id;
    String user_id;
    List<Store> storeList;
    StoresAdapter storesAdapter;
    List<Datum> messagelist;
    MessageAdapter2 messageAdapter2;
    public StoresViewModel(Context context,String category_id) {
        this.context = context;
        storesActivity = (StoresActivity) context;
        this.category_id = category_id;
    }


    public void getAds() {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(context);
        user_gender = loginModel.getData().getUser().getGender();
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<SliderModel> call = getDataService.get_ads(user_gender+"",4);
            call.enqueue(new Callback<SliderModel>() {
                @Override
                public void onResponse(Call<SliderModel> call, Response<SliderModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            storesActivity.init_sliders(response.body().getData().getData());

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

    public void getStores(String user_id,Integer page) {
        this.user_id = user_id;
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<StoreModel> call = getDataService.get_stores(user_id,category_id,page);
            call.enqueue(new Callback<StoreModel>() {
                @Override
                public void onResponse(Call<StoreModel> call, Response<StoreModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            storeList = response.body().getData();
                            storesAdapter = new StoresAdapter(context,storeList);
                            storesActivity.init_recycler(storesAdapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<StoreModel> call, Throwable t) {

                }
            });
        }
    }

    public void add_fav(String user_id, Store store) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.add_to_fav(user_id, store.getId()+"") ;
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            Toast.makeText(context, "تمت الاضافة الي المفضلة بنجاح", Toast.LENGTH_SHORT).show();
                            getStores(user_id,1);
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommentModel> call, Throwable t) {

                }
            });
        }
    }

    public void delete_fav(String user_id, Store store) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.delete_from_fav(user_id, store.getId()+"") ;
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            Toast.makeText(context, "تمت الإزالة من المفضلة بنجاح", Toast.LENGTH_SHORT).show();
                            getStores(user_id,1);
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommentModel> call, Throwable t) {

                }
            });
        }
    }

    public void getMessages(String trader_id,Integer page2) {
        //Toast.makeText(context, trader_id, Toast.LENGTH_SHORT).show();
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<MessageModel> call = getDataService.get_messages(trader_id,page2);
            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            messagelist = response.body().getData().getData();
                            messageAdapter2 = new MessageAdapter2(messagelist,context);
                            storesActivity.init_messages_recycler(messageAdapter2);
                        }
                    }
                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {

                }
            });
        }
    }

    public void getuserMessages(String user_id,Integer page) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<MessageModel> call = getDataService.get_user_messages(user_id,page);
            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            messagelist = response.body().getData().getData();
                            messageAdapter2 = new MessageAdapter2(messagelist,context);
                            storesActivity.init_messages_recycler(messageAdapter2);
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
                            storesActivity.init_search_recycler(response.body().getData());
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

    public void PerformPagination(String user_id,Integer page) {
        this.user_id = user_id;
        if (Utilities.isNetworkAvailable(context)) {
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<StoreModel> call = getDataService.get_stores(user_id,category_id,page);
            call.enqueue(new Callback<StoreModel>() {
                @Override
                public void onResponse(Call<StoreModel> call, Response<StoreModel> response) {
                    if (response.isSuccessful()) {
                        if (!response.body().getData().isEmpty()){
                            if (response.body().getStatus()) {
                                storeList = response.body().getData();
                                storesAdapter.add_store(storeList);
                               // Toast.makeText(context, page+""+"loading", Toast.LENGTH_SHORT).show();
                                //productActivity.init_products(productAdapter);
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<StoreModel> call, Throwable t) {

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
        Call<MessageModel> call = getDataService.get_messages(user_id,page);
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

    public void getData(String user_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ProfileData> call = getDataService.get_user_data(user_id);
            call.enqueue(new Callback<ProfileData>() {
                @Override
                public void onResponse(Call<ProfileData> call, Response<ProfileData> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            storesActivity.setData(response.body());

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
