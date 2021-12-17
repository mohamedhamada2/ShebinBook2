package com.alatheer.shebinbook.posts;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.comments.CommentModel;

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
    public PostViewModel(Context context) {
        this.context = context;
        postsActivity = (PostsActivity) context;
    }

    public void addpost(String user_id, String gender_id, Uri filepath, String post) {
        if (Utilities.isNetworkAvailable(context)){
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
                            postsActivity.init_recycler(response.body().getData().getData());
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
}
