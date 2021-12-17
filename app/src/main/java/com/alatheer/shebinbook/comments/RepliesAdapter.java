package com.alatheer.shebinbook.comments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alatheer.shebinbook.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RepliesAdapter extends RecyclerView.Adapter<RepliesAdapter.RepliesHolder> {
    Context context;
    List<RepliesData> replyDataList;

    public RepliesAdapter(Context context, List<RepliesData> replyDataList) {
        this.context = context;
        this.replyDataList = replyDataList;
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

    }

    @Override
    public int getItemCount() {
        return replyDataList.size();
    }

    class RepliesHolder extends RecyclerView.ViewHolder{
        TextView txt_name,txt_reply,fav_num2,msg_num2;
        ImageView fav_img2,comment_img2;
        public RepliesHolder(@NonNull  View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name2);
            txt_reply = itemView.findViewById(R.id.txt_replay);
            fav_img2 = itemView.findViewById(R.id.fav_img2);
            comment_img2 = itemView.findViewById(R.id.comment_img2);
            fav_num2 = itemView.findViewById(R.id.fav_num2);
            msg_num2 = itemView.findViewById(R.id.msg_num2);
        }

        public void setData(RepliesData replyData) {
            txt_reply.setText(replyData.getReplayMessage());
            txt_name.setText(replyData.getName()+replyData.getLastName());
            fav_img2.setVisibility(View.GONE);
            comment_img2.setVisibility(View.GONE);
            fav_num2.setVisibility(View.GONE);
            msg_num2.setVisibility(View.GONE);
        }
    }
}
