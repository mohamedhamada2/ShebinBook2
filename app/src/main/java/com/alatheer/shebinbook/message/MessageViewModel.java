package com.alatheer.shebinbook.message;

import android.content.Context;
import android.widget.Toast;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.comments.CommentModel;
import com.alatheer.shebinbook.comments.ReplyModel;
import com.alatheer.shebinbook.message.reply.ReplayModel;
import com.alatheer.shebinbook.message.reply.Reply;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageViewModel {
    Context context;
    MessageActivity messageActivity;

    public MessageViewModel(Context context) {
        this.context = context;
        messageActivity = (MessageActivity) context;
    }



    public void addreply(Integer userIdFk, String message, Integer storeIdFk, Integer productIdFk, Integer trader_id, Integer msg_id) {
        //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.send_replay_to_message(userIdFk,message,storeIdFk,productIdFk,trader_id,msg_id);
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getData().getSuccess() ==1){
                            Toast.makeText(context, "تم إرسال ردك بنجاح", Toast.LENGTH_SHORT).show();
                            messageActivity.refresh();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommentModel> call, Throwable t) {

                }
            });
        }
    }

    public void getreplies(Integer message_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ReplayModel> call = getDataService.get_replay_message(message_id);
            call.enqueue(new Callback<ReplayModel>() {
                @Override
                public void onResponse(Call<ReplayModel> call, Response<ReplayModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            messageActivity.init_replies(response.body().getData().getData());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ReplayModel> call, Throwable t) {

                }
            });
        }
    }
}
