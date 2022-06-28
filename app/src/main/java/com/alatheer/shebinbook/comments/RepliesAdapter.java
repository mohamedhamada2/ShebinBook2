package com.alatheer.shebinbook.comments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RepliesAdapter extends RecyclerView.Adapter<RepliesAdapter.RepliesHolder> {
    Context context;
    CommentActivity commentActivity;
    List<RepliesData> replyDataList;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Integer user_id;
    public RepliesAdapter(Context context, List<RepliesData> replyDataList) {
        this.context = context;
        this.replyDataList = replyDataList;
        commentActivity = (CommentActivity) context;
    }

    @NonNull
    @Override
    public RepliesHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reply_item,parent,false);
        return new RepliesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  RepliesAdapter.RepliesHolder holder, int position) {
        holder.setData(replyDataList.get(position));
        holder.bin_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentActivity.delete_reply(replyDataList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return replyDataList.size();
    }

    class RepliesHolder extends RecyclerView.ViewHolder{
        TextView txt_name,txt_reply,fav_num2,msg_num2;
        ImageView fav_img2,comment_img2,userimg2,bin_img;
        public RepliesHolder(@NonNull  View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name2);
            txt_reply = itemView.findViewById(R.id.txt_replay);
            comment_img2 = itemView.findViewById(R.id.comment_img2);
            msg_num2 = itemView.findViewById(R.id.msg_num2);
            userimg2 = itemView.findViewById(R.id.userimg2);
            bin_img = itemView.findViewById(R.id.bin_img);
        }

        public void setData(RepliesData replyData) {
            mySharedPreference = MySharedPreference.getInstance();
            loginModel = mySharedPreference.Get_UserData(context);
            user_id = loginModel.getData().getUser().getId();
            if (replyData.getUserIdFk().equals(user_id)){
                bin_img.setVisibility(View.VISIBLE);
            }
            txt_reply.setText(replyData.getReplayMessage());
            txt_name.setText(replyData.getName()+"  "+replyData.getLastName());
            comment_img2.setVisibility(View.GONE);
            msg_num2.setVisibility(View.GONE);
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+replyData.getUserImg()).into(userimg2);
        }
    }
}
