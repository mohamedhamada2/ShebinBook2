package com.alatheer.shebinbook.subcategory;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.home.category.SubCate;
import com.alatheer.shebinbook.stores.StoresActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SubCategoryHolder> {
    Context context;
    List<SubCate> subCateList;

    public SubCategoryAdapter(Context context, List<SubCate> subCateList) {
        this.context = context;
        this.subCateList = subCateList;
    }

    @NonNull
    @Override
    public SubCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item2,parent,false);
        return new SubCategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  SubCategoryAdapter.SubCategoryHolder holder, int position) {
        holder.setData(subCateList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StoresActivity.class);
                intent.putExtra("category_id",subCateList.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subCateList.size();
    }

    class SubCategoryHolder extends RecyclerView.ViewHolder {
        ImageView category_img;
        TextView category_txt;
        public SubCategoryHolder(@NonNull  View itemView) {
            super(itemView);
            category_img = itemView.findViewById(R.id.category_img);
            category_txt = itemView.findViewById(R.id.category_name);
        }

        public void setData(SubCate subCate) {
            try {
                Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/subcats_images/"+subCate.getImage()).resize(600, 200).into(category_img);
            }catch (Exception e){
                Log.e("debug",e.getMessage());
            }

            category_txt.setText(subCate.getName());
        }
    }
}
