package com.alatheer.shebinbook.comments;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.message.Datum;
import com.alatheer.shebinbook.message.MessageAdapter2;
import com.alatheer.shebinbook.message.MessageModel;
import com.alatheer.shebinbook.posts.Post;
import com.alatheer.shebinbook.posts.PostModel;
import com.alatheer.shebinbook.setting.ProfileData;
import com.alatheer.shebinbook.stores.StoreModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentViewModel {
    Context context;
    CommentActivity commentActivity;
    MessageAdapter2 messageAdapter2;
    List<Datum> messagelist;

    public CommentViewModel(Context context) {
        this.context = context;
        commentActivity = (CommentActivity) context;
    }

    public void addcomment(String user_id, Uri filepath, Post post) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        }
    }

    public void getComments(String post_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<List<Comment>>call = getDataService.get_comments(post_id);
            call.enqueue(new Callback<List<Comment>>() {
                @Override
                public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                    if (response.isSuccessful()){
                        commentActivity.init_comments(response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<Comment>> call, Throwable t) {

                }
            });

        }
    }

    public void addcomment(String user_id, Uri filepath, String comment, String post_user_id, String post_id) {
        if (Utilities.isNetworkAvailable(context)){
            if (filepath != null){
                RequestBody rb_user_id = Utilities.getRequestBodyText(user_id);
                RequestBody rb_post_user_id = Utilities.getRequestBodyText(post_user_id);
                RequestBody rb_comment = Utilities.getRequestBodyText(comment);
                RequestBody rb_post_id = Utilities.getRequestBodyText(post_id);
                MultipartBody.Part img = Utilities.getMultiPart(context, filepath, "img");
                //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                Call<CommentModel> call = getDataService.add_comment_with_img(rb_user_id,rb_post_user_id,rb_post_id,rb_comment,img);
                call.enqueue(new Callback<CommentModel>() {
                    @Override
                    public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                        if (response.isSuccessful()){
                            if (response.body().getData().getSuccess()==1){
                                Toast.makeText(context, "تم إضافة التعليق بنجاح", Toast.LENGTH_SHORT).show();
                                 commentActivity.refresh();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CommentModel> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("photo error",t.getMessage());
                    }
                });
            }else {
                GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                Call<CommentModel> call = getDataService.add_comment(user_id,post_user_id,post_id,comment);
                call.enqueue(new Callback<CommentModel>() {
                    @Override
                    public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                        if (response.isSuccessful()){
                            if (response.body().getData().getSuccess()==1){
                                Toast.makeText(context, "تم إضافة التعليق بنجاح", Toast.LENGTH_SHORT).show();
                                commentActivity.refresh();
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

    public void addreplay(String post_id, String post_user_id,String comment_id, String commentUserIdFk, String user_id, String reply) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> commentModelCall = getDataService.add_reply(post_id,post_user_id,comment_id,commentUserIdFk,user_id,reply);
            commentModelCall.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            Toast.makeText(context, response.body().getData().getSuccess()+"", Toast.LENGTH_SHORT).show();
                            commentActivity.refresh();
                        }
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
                            commentActivity.init_search_recycler(response.body().getData());
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
                            //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                            messagelist = response.body().getData().getData();
                            messageAdapter2 = new MessageAdapter2(messagelist,context);
                            commentActivity.init_messages_recycler(messageAdapter2);
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
                            //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                            messagelist = response.body().getData().getData();
                            messageAdapter2 = new MessageAdapter2(messagelist,context);
                            commentActivity.init_messages_recycler(messageAdapter2);
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
                            commentActivity.setData(response.body());

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
