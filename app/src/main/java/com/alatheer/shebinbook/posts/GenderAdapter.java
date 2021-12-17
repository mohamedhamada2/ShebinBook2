package com.alatheer.shebinbook.posts;

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

public class GenderAdapter extends RecyclerView.Adapter<GenderAdapter.GenderHolder> {
    Context context;
    List<Gender> genderList;
    GenderClick genderClick;

    public GenderAdapter(Context context, List<Gender> genderList,GenderClick genderClick) {
        this.context = context;
        this.genderList = genderList;
        this.genderClick = genderClick;
    }

    @NonNull
    @Override
    public GenderHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.group_item,parent,false);
        return new GenderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  GenderAdapter.GenderHolder holder, int position) {
        holder.setData(genderList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderClick.onItemClicked(genderList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return genderList.size();
    }

    class GenderHolder extends RecyclerView.ViewHolder{
        ImageView gender_img;
        TextView gender_name;
        public GenderHolder(@NonNull  View itemView) {
            super(itemView);
            gender_img = itemView.findViewById(R.id.group_img);
            gender_name = itemView.findViewById(R.id.group_name);
        }

        public void setData(Gender gender) {
            gender_name.setText(gender.getName());
            gender_img.setImageResource(gender.getImage());
        }
    }
}
