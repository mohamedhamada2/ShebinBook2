package com.alatheer.shebinbook.message.reply;

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
import com.alatheer.shebinbook.message.Datum;
import com.alatheer.shebinbook.message.MessageActivity;
import com.alatheer.shebinbook.message.MessageAdapter2;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReplayAdapter  extends RecyclerView.Adapter<ReplayAdapter.MessageHolder> {
    List<Reply> datumList;
    Context context;
    MySharedPreference mySharedPreference;
    Integer trader_id;
    LoginModel loginModel;
    public ReplayAdapter(List<Reply> datumList, Context context) {
        this.datumList = datumList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReplayAdapter.MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_item,parent,false);
        return new ReplayAdapter.MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ReplayAdapter.MessageHolder holder, int position) {
        holder.setData(datumList.get(position));

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

        public void setData(Reply datum) {
            mySharedPreference = MySharedPreference.getInstance();
            loginModel = mySharedPreference.Get_UserData(context);
            trader_id = loginModel.getData().getUser().getTraderId();
            txt_comment.setText(datum.getReplay());
            long dt1 = Long.parseLong(datum.getDate());
            final Date from_dt = new Date((long) (dt1 * 1000));
            final DateFormat f = new SimpleDateFormat("yyyy/MM/dd ", Locale.ENGLISH);
            txt_date.setText(f.format(from_dt));
            /*try {
                if (trader_id.equals(datum.getTraderIdFk())){
                    Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+datum.getLogo()).into(user_img);
                    txt_name.setText(datum.getStoreName());
                }else {
                    Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+datum.getUserImg()).into(user_img);
                    txt_name.setText(datum.getName()+datum.getLastName());
                }
            }catch (Exception e){
                Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+datum.getUserImg()).into(user_img);
                txt_name.setText(datum.getName()+datum.getLastName());
            }*/
            if (datum.getType()==1){
                Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+datum.getLogo()).into(user_img);
                txt_name.setText(datum.getStoreName());
            }else {
                Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+datum.getUserImg()).into(user_img);
                txt_name.setText(datum.getName()+datum.getLastName());
            }

        }
    }
}
