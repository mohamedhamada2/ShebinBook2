package com.alatheer.shebinbook.posts;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.comments.CommentActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AskAdapter extends RecyclerView.Adapter<AskAdapter.AskHolder> {
    List<Post> askModelList;
    Context context;
    PostsActivity postsActivity;

    public AskAdapter(List<Post> askModelList, Context context) {
        this.askModelList = askModelList;
        this.context = context;
        postsActivity = (PostsActivity) context;
    }

    @NonNull
    @Override
    public AskHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ask_menofia_item,parent,false);
        return new AskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  AskHolder holder, int position) {
        holder.setData(askModelList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("post",askModelList.get(position));
                context.startActivity(intent);
            }
        });
        holder.fav_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (askModelList.get(position).getLike()==0){
                    postsActivity.add_post_to_fav(askModelList.get(position).getId());
                    holder.fav_img.setImageResource(R.drawable.fav);
                    holder.fav_num.setText(askModelList.get(position).getCountLikes()+1+"");

                }else {
                    postsActivity.delete_post_from_fav(askModelList.get(position).getId());
                    holder.fav_img.setImageResource(R.drawable.fav2);
                    holder.fav_num.setText(askModelList.get(position).getCountLikes()-1+"");

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return askModelList.size();
    }

    class AskHolder extends RecyclerView.ViewHolder {
        TextView txt_comment, txt_name,msg_num,fav_num;
        ImageView user_img,post_img,fav_img;

        public AskHolder(@NonNull View itemView) {
            super(itemView);
            txt_comment = itemView.findViewById(R.id.txt_comment);
            txt_name = itemView.findViewById(R.id.txt_name);
            user_img = itemView.findViewById(R.id.userimg);
            post_img = itemView.findViewById(R.id.post_img);
            fav_img = itemView.findViewById(R.id.fav_img);
            msg_num = itemView.findViewById(R.id.msg_num);
            fav_num = itemView.findViewById(R.id.fav_num);

        }

        public void setData(Post askModel) {
            txt_comment.setText(askModel.getPost());
            txt_name.setText(askModel.getName());
            msg_num.setText(askModel.getCountComments()+"");
            fav_num.setText(askModel.getLike()+"");
            //user_img.setImageResource(R.drawable.user2);
            if (askModel.getImg().equals("noimage")){
                post_img.setVisibility(View.GONE);
            }else{
                Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+askModel.getImg()).into(post_img);
            }
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+askModel.getUserImg()).into(user_img);
            if (askModel.getLike() == 0){
                fav_img.setImageResource(R.drawable.fav2);
            }else {
               fav_img.setImageResource(R.drawable.fav);
            }
        }
    }
    public void add_post(List<Post> postList){
        for (Post post:postList){
            askModelList.add(post);
        }
        notifyDataSetChanged();
    }
}
