package com.alatheer.shebinbook.authentication.favorite;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.Utilities.Constants;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.products.ProductsActivity;
import com.alatheer.shebinbook.stores.Store;
import com.alatheer.shebinbook.trader.profile.ProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder> {
    List<Store> favoriteList;
    Context context;
    FavoriteActivity favoriteActivity;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Integer trader_id;
    public FavoriteAdapter(List<Store> favoriteList, Context context) {
        this.favoriteList = favoriteList;
        this.context = context;
        favoriteActivity = (FavoriteActivity) context;
    }

    @NonNull
    @Override
    public FavoriteHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorite_item,parent,false);
        return new FavoriteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  FavoriteAdapter.FavoriteHolder holder, int position) {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(context);
        trader_id = loginModel.getData().getUser().getTraderId();
        holder.setData(favoriteList.get(position));
        holder.fav_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favoriteActivity.delete_from_fav(favoriteList.get(position));
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favoriteList.get(position).getTradeId().equals(trader_id)){
                    Intent intent = new Intent(favoriteActivity, ProfileActivity.class);
                    context.startActivity(intent);
                }else {
                    Intent intent = new Intent(favoriteActivity, ProductsActivity.class);
                    intent.putExtra("store", favoriteList.get(position));
                    intent.putExtra("flag",2);
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    class FavoriteHolder extends RecyclerView.ViewHolder{
        TextView store_name,txt_comment;
        ImageView store_logo;
        ImageButton fav_img_btn;
        public FavoriteHolder(@NonNull  View itemView) {
            super(itemView);
            store_name = itemView.findViewById(R.id.store_name);
            store_logo = itemView.findViewById(R.id.store_logo);
            fav_img_btn = itemView.findViewById(R.id.fav_img_btn);
            txt_comment = itemView.findViewById(R.id.txt_comment);
        }

        public void setData(Store favorite) {
            store_name.setText(favorite.getStoreName());
            txt_comment.setText(favorite.getMini_description());
            Picasso.get().load(Constants.BASE_URL +"public/uploads/images/images/"+favorite.getLogo()).into(store_logo);

        }
    }
    public void add_store(List<Store> storeList2){
        for (Store store: storeList2){
            favoriteList.add(store);
        }
        notifyDataSetChanged();
    }
}
