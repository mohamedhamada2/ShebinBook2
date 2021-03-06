package com.alatheer.shebinbook.home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.comments.CommentActivity;
import com.alatheer.shebinbook.posts.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.AskHolder> {
    List<Post> askModelList;
    Context context;
    HomeActivity homeActivity;

    public PostAdapter(List<Post> askModelList, Context context) {
        this.askModelList = askModelList;
        this.context = context;
        homeActivity = (HomeActivity) context;
    }

    @NonNull
    @Override
    public PostAdapter.AskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ask_menofia_item,parent,false);
        return new PostAdapter.AskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  PostAdapter.AskHolder holder, int position) {
        holder.setData(askModelList.get(position));
        holder.fav_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (askModelList.get(position).getLike()==0){
                    homeActivity.add_post_to_fav(askModelList.get(position).getId());
                    holder.fav_img.setImageResource(R.drawable.fav);
                    holder.fav_num.setText(askModelList.get(position).getCountLikes()+1+"");

                }else {
                    homeActivity.delete_post_from_fav(askModelList.get(position).getId());
                    holder.fav_img.setImageResource(R.drawable.fav2);
                    holder.fav_num.setText(askModelList.get(position).getCountLikes()-1+"");

                }
            }
        });
        holder.post_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateImageDialog(askModelList.get(position));
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("post",askModelList.get(position));
                context.startActivity(intent);
            }
        });
    }

    private void CreateImageDialog(Post post) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.image_item2, null);
        ImageView img = view.findViewById(R.id.img);
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+post.getImg()).into(img);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public int getItemCount() {
        return askModelList.size();
    }

    class AskHolder extends RecyclerView.ViewHolder {
        TextView txt_comment, txt_name,msg_num,fav_num;
        ImageView user_img,post_img,fav_img,msg_img;

        public AskHolder(@NonNull View itemView) {
            super(itemView);
            txt_comment = itemView.findViewById(R.id.txt_comment);
            txt_name = itemView.findViewById(R.id.txt_name);
            user_img = itemView.findViewById(R.id.userimg);
            post_img = itemView.findViewById(R.id.post_img);
            msg_num = itemView.findViewById(R.id.msg_num);
            fav_num = itemView.findViewById(R.id.fav_num);
            fav_img = itemView.findViewById(R.id.fav_img);
            msg_img = itemView.findViewById(R.id.comment_img);

        }

        public void setData(Post askModel) {
            txt_comment.setText(askModel.getPost());
            txt_name.setText(askModel.getName());
            user_img.setImageResource(R.drawable.user2);
            msg_num.setText(askModel.getCountComments()+"");
            fav_num.setText(askModel.getLike()+"");
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
}
