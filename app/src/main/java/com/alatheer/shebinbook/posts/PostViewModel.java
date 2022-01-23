package com.alatheer.shebinbook.posts;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.comments.CommentModel;
import com.alatheer.shebinbook.message.Datum;
import com.alatheer.shebinbook.message.MessageAdapter2;
import com.alatheer.shebinbook.message.MessageModel;
import com.alatheer.shebinbook.setting.ProfileData;
import com.alatheer.shebinbook.stores.StoreModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostViewModel {
    Context context;
    PostsActivity postsActivity;
    List<Post> postList;
    Post post;
    AskAdapter askAdapter;
    String gender_id,user_id;
    Integer page;
    List<Datum> messagelist;
    MessageAdapter2 messageAdapter2;
    public PostViewModel(Context context) {
        this.context = context;
        postsActivity = (PostsActivity) context;
    }

    public void addpost(String user_id, String gender_id, Uri filepath, String post) {
        if (Utilities.isNetworkAvailable(context)){
            ProgressDialog pd = new ProgressDialog(postsActivity);
            pd.setMessage("loading ...");
            pd.show();
            if (filepath != null){
                RequestBody rb_user_id = Utilities.getRequestBodyText(user_id);
                RequestBody rb_gender_id = Utilities.getRequestBodyText(gender_id);
                RequestBody rb_post = Utilities.getRequestBodyText(post);
                MultipartBody.Part img = Utilities.getMultiPart(context, filepath, "img");
                //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                Call<PostModel> call = getDataService.insert_post_with_img(rb_gender_id,rb_user_id,rb_post,img);
                call.enqueue(new Callback<PostModel>() {
                    @Override
                    public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                        if (response.isSuccessful()){
                            if (response.body().getData().getSuccess()==1){
                                pd.dismiss();
                                Toast.makeText(context, "تم إضافة البوست بنجاح", Toast.LENGTH_SHORT).show();
                                getPosts(gender_id,1,user_id);
                               }
                            }
                        }

                        @Override
                        public void onFailure(Call<PostModel> call, Throwable t) {
                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                            //Log.d("photo error",t.getMessage());
                        }
                    });
            }else {
                GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                Call<PostModel> call = getDataService.insert_post(gender_id,user_id,post);
                call.enqueue(new Callback<PostModel>() {
                    @Override
                    public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                        if (response.isSuccessful()){
                            if (response.body().getData().getSuccess()==1){
                                pd.dismiss();
                                Toast.makeText(context, "تم إضافة البوست بنجاح", Toast.LENGTH_SHORT).show();
                                getPosts(gender_id,1,user_id);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PostModel> call, Throwable t) {

                    }
                });
            }
        }
    }

    public void getPosts(String gender_user,Integer page,String user_id) {
        gender_id = gender_user;
        this.user_id = user_id;
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<PostData> call = getDataService.get_all_posts(gender_user,page,user_id);
            call.enqueue(new Callback<PostData>() {
                @Override
                public void onResponse(Call<PostData> call, Response<PostData> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            askAdapter = new AskAdapter(response.body().getData().getData(),postsActivity);
                            postsActivity.init_recycler(askAdapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<PostData> call, Throwable t) {

                }
            });
        }
    }

    public void PerformPagination(String gender_user, int page,String user_id) {
        if (Utilities.isNetworkAvailable(context)) {
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<PostData> call = getDataService.get_all_posts(gender_user, page,user_id);
            call.enqueue(new Callback<PostData>() {
                @Override
                public void onResponse(Call<PostData> call, Response<PostData> response) {
                    if (response.isSuccessful()) {
                        if (page <= response.body().getData().getLastPage()) {
                            //Toast.makeText(context, page+"", Toast.LENGTH_SHORT).show();
                            postList = response.body().getData().getData();
                            askAdapter.add_post(postList);
                            //Toast.makeText(context, "page:"+page+"is loaded", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(context, "no more available", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<PostData> call, Throwable t) {

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
                            Toast.makeText(postsActivity, "تمت أضافة البوست للمفضلة", Toast.LENGTH_SHORT).show();
                            getPosts(gender_id,1,user_id);
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
                            Toast.makeText(postsActivity, "تمت إزالة البوست من المفضلة", Toast.LENGTH_SHORT).show();
                            getPosts(gender_id,1,user_id);
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommentModel> call, Throwable t) {

                }
            });
        }
    }

    public void delete_post(Integer id, String user_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.delete_post(id);
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(postsActivity, "تم حذف المنشور بنجاح", Toast.LENGTH_SHORT).show();
                        getPosts(gender_id,1,user_id);
                    }
                }

                @Override
                public void onFailure(Call<CommentModel> call, Throwable t) {

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
                            postsActivity.init_search_recycler(response.body().getData());
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
    public void getMessages(Integer trader_id,Integer page) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<MessageModel> call = getDataService.get_messages(trader_id+"",page);
            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            messagelist = response.body().getData().getData();
                            messageAdapter2 = new MessageAdapter2(messagelist,context);
                            //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                            postsActivity.init_messages_recycler(messageAdapter2);
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
        if (Utilities.isNetworkAvailable(context)) {
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<MessageModel> call = getDataService.get_user_messages(user_id,page);
            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            messagelist = response.body().getData().getData();
                            messageAdapter2 = new MessageAdapter2(messagelist,context);
                            //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                            postsActivity.init_messages_recycler(messageAdapter2);
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

    public void getData(String user_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ProfileData> call = getDataService.get_user_data(user_id);
            call.enqueue(new Callback<ProfileData>() {
                @Override
                public void onResponse(Call<ProfileData> call, Response<ProfileData> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            postsActivity.setData(response.body());

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
