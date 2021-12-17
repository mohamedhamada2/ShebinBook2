package com.alatheer.shebinbook.comments;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.posts.Post;
import com.alatheer.shebinbook.posts.PostModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentViewModel {
    Context context;
    CommentActivity commentActivity;

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
}
