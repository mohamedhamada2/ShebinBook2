package com.alatheer.shebinbook.subcategory;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.categories.CategoryActivity;
import com.alatheer.shebinbook.forgetpassword.NewPassword;
import com.alatheer.shebinbook.home.category.SubCate;
import com.alatheer.shebinbook.stores.StoresActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SubCategoryHolder> {
    Context context;
    List<Datum> subCateList;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Integer user_id;
    public SubCategoryAdapter(Context context, List<Datum> subCateList) {
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
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(context);
        user_id = loginModel.getData().getUser().getId();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)){
                    GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                    Call<NewPassword> call = getDataService.visit_category(subCateList.get(position).getId(),"sub",user_id);
                    call.enqueue(new Callback<NewPassword>() {
                        @Override
                        public void onResponse(Call<NewPassword> call, Response<NewPassword> response) {
                            if (response.isSuccessful()){
                                //Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, StoresActivity.class);
                                intent.putExtra("category_id",subCateList.get(position).getId());
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

        public void setData(Datum subCate) {

            try {
                Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/subcats_images/"+subCate.getImage()).resize(600, 200).into(category_img);
            }catch (Exception e){
                Log.e("debug",e.getMessage());
            }

            category_txt.setText(subCate.getName());
        }
    }
}
