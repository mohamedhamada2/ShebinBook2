package com.alatheer.shebinbook.posts;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.comments.CommentActivity;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AskAdapter extends RecyclerView.Adapter<AskAdapter.AskHolder> {
    List<Post> askModelList;
    Context context;
    PostsActivity postsActivity;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Integer user_id;
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
        holder.bin_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postsActivity.delete_post(askModelList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return askModelList.size();
    }

    class AskHolder extends RecyclerView.ViewHolder {
        TextView txt_comment, txt_name,msg_num,fav_num,txt_date;
        ImageView user_img,post_img,fav_img,bin_img;

        public AskHolder(@NonNull View itemView) {
            super(itemView);
            txt_comment = itemView.findViewById(R.id.txt_comment);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_date = itemView.findViewById(R.id.txt_date);
            user_img = itemView.findViewById(R.id.userimg);
            post_img = itemView.findViewById(R.id.post_img);
            fav_img = itemView.findViewById(R.id.fav_img);
            msg_num = itemView.findViewById(R.id.msg_num);
            fav_num = itemView.findViewById(R.id.fav_num);
            bin_img = itemView.findViewById(R.id.bin_img);

        }

        public void setData(Post askModel) {
            mySharedPreference = MySharedPreference.getInstance();
            loginModel = mySharedPreference.Get_UserData(context);
            user_id = loginModel.getData().getUser().getId();
            txt_comment.setText(askModel.getPost());
            txt_name.setText(askModel.getName());
            msg_num.setText(askModel.getCountComments()+"");
            fav_num.setText(askModel.getLike()+"");
            long dt1 = Long.parseLong(askModel.getDate());
            final DateFormat f = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            final Date dt = new Date((long) (dt1 * 1000));
            txt_date.setText(f.format(dt));
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
            if (askModel.getUserIdFk().equals(user_id)){
                bin_img.setVisibility(View.VISIBLE);
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
