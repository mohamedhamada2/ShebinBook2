package com.alatheer.shebinbook.categories;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.forgetpassword.NewPassword;
import com.alatheer.shebinbook.home.category.Category;
import com.alatheer.shebinbook.subcategory.SubCategoryActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    List<Category> categoryList;
    Context context;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Integer user_id;

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
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(context);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)){
                    GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                    Call<NewPassword> call = getDataService.visit_category(categoryList.get(position).getId(),"main",user_id);
                    call.enqueue(new Callback<NewPassword>() {
                        @Override
                        public void onResponse(Call<NewPassword> call, Response<NewPassword> response) {
                            if (response.isSuccessful()){
                                Intent intent = new Intent(context, SubCategoryActivity.class);
                                intent.putExtra("category",categoryList.get(position));
                                context.startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<NewPassword> call, Throwable t) {

                        }
                    });
                }
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
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+category.getImg()).resize(600,200).into(category_img);
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
