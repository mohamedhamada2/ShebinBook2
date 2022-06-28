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
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    Context context;
    List<Comment> commentList;
    CommentActivity commentActivity;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Integer user_id;
    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
        commentActivity = (CommentActivity) context;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentHolder holder, int position) {
        holder.setData(commentList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentActivity.addreplay(commentList.get(position));
            }
        });
        holder.bin_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentActivity.delete_comment(commentList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder {
        TextView txt_comment,txt_name,msg_num;
        RoundedImageView comment_img,user_img;
        ImageView reply_img,bin_img;
        RecyclerView reply_recycler;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            txt_comment = itemView.findViewById(R.id.txt_comment);
            comment_img = itemView.findViewById(R.id.comment_img);
            reply_img = itemView.findViewById(R.id.reply_img);
            txt_name = itemView.findViewById(R.id.txt_name);
            msg_num = itemView.findViewById(R.id.msg_num);
            bin_img = itemView.findViewById(R.id.bin_img);
            reply_recycler = itemView.findViewById(R.id.replies_recycler);
            user_img = itemView.findViewById(R.id.userimg);
        }

        public void setData(Comment comment) {
            mySharedPreference = MySharedPreference.getInstance();
            loginModel = mySharedPreference.Get_UserData(context);
            user_id = loginModel.getData().getUser().getId();
            txt_comment.setText(comment.getComment());
            txt_name.setText(comment.getName()+"  "+comment.getLastName());
            msg_num.setText(comment.getRepliesCount()+"");
            if (comment.getImg().equals("noimage")) {
                comment_img.setVisibility(View.GONE);
            } else {
                Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/" + comment.getImg()).into(comment_img);
            }
            if (comment.getCommentUserIdFk().equals(user_id)){
                bin_img.setVisibility(View.VISIBLE);
            }
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/" + comment.getUserImg()).into(user_img);
            reply_recycler.setHasFixedSize(true);
            RepliesAdapter repliesAdapter = new RepliesAdapter(commentActivity,comment.getReplies().getData());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(commentActivity,LinearLayoutManager.VERTICAL,false);
            reply_recycler.setAdapter(repliesAdapter);
            reply_recycler.setLayoutManager(layoutManager);
        }
    }
}
