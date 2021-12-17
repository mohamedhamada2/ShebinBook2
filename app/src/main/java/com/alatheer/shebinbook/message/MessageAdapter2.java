package com.alatheer.shebinbook.message;

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

import java.net.ContentHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter2 extends RecyclerView.Adapter<MessageAdapter2.MessageHolder> {
    List<Datum> datumList;
    Context context;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    int role_id;

    public MessageAdapter2(List<Datum> datumList, Context context) {
        this.datumList = datumList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_item,parent,false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  MessageAdapter2.MessageHolder holder, int position) {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(context);
        role_id = loginModel.getData().getUser().getRoleIdFk();
        holder.setData(datumList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("message",datumList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    class MessageHolder extends RecyclerView.ViewHolder{
        TextView txt_time,txt_date,txt_name,txt_comment;
        ImageView user_img;
        public MessageHolder(@NonNull  View itemView) {
            super(itemView);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_comment = itemView.findViewById(R.id.txt_message);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_time = itemView.findViewById(R.id.txt_time);
            user_img = itemView.findViewById(R.id.userimg);
        }

        public void setData(Datum datum) {
            txt_time.setText(datum.getTime());
            txt_comment.setText(datum.getMessage());
            if (role_id == 4){
                txt_name.setText(datum.getName());
                Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+datum.getUserImg()).into(user_img);
            }else {
                txt_name.setText(datum.getTraderName());
                Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+datum.getTraderImg()).into(user_img);
            }
            long dt1 = Long.parseLong(datum.getDate());
            final Date from_dt = new Date((long) (dt1 * 1000));
            final DateFormat f = new SimpleDateFormat("yyyy/MM/dd ", Locale.ENGLISH);
            txt_date.setText(f.format(from_dt));
            //Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+datum.getImg()).into(user_img);
        }
    }
}
