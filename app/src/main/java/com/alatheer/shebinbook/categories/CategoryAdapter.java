package com.alatheer.shebinbook.categories;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.home.category.Category;
import com.alatheer.shebinbook.subcategory.SubCategoryActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    List<Category> categoryList;
    Context context;

    public CategoryAdapter(List<Category> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item2,parent,false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  CategoryHolder holder, int position) {
        holder.setData(categoryList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SubCategoryActivity.class);
                intent.putExtra("category",categoryList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder{
        ImageView category_img;
        TextView category_txt;

        public CategoryHolder(@NonNull  View itemView) {
            super(itemView);
            category_img = itemView.findViewById(R.id.category_img);
            category_txt = itemView.findViewById(R.id.category_name);
        }

        public void setData(Category category) {
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+category.getImg()).into(category_img);
            category_txt.setText(category.getName());
        }
    }
    public void add_category(List<com.alatheer.shebinbook.home.category.Category> categoryList2){
        for (Category category:categoryList2){
            categoryList.add(category);
        }
        notifyDataSetChanged();
    }
}
