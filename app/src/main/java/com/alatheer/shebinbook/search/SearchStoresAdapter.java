package com.alatheer.shebinbook.search;

import android.content.Context;
import android.content.Intent;
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
import com.alatheer.shebinbook.stores.StoresActivity;
import com.alatheer.shebinbook.stores.StoresAdapter;
import com.alatheer.shebinbook.trader.profile.ProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchStoresAdapter extends RecyclerView.Adapter<SearchStoresAdapter.StoresHolder> {
    Context context;
    List<Store> storeList;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Integer trader_id;

    public SearchStoresAdapter(Context context, List<Store> storeList) {
        this.context = context;
        this.storeList = storeList;

    }

    @NonNull
    @Override
    public SearchStoresAdapter.StoresHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.store_item,parent,false);
        return new SearchStoresAdapter.StoresHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  SearchStoresAdapter.StoresHolder holder, int position) {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(context);
        trader_id = loginModel.getData().getUser().getTraderId();
        holder.setData(storeList.get(position));
        /*holder.fav_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (storeList.get(position).getFavourite() == 0){
                    storesActivity.add_to_fav(storeList.get(position));
                }else {
                    storesActivity.delete_from_fav(storeList.get(position));
                }
            }
        });*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (storeList.get(position).getTradeId().equals(trader_id)){
                    Intent intent = new Intent(context, ProfileActivity.class);
                    context.startActivity(intent);
                }else {
                    Intent intent = new Intent(context, ProductsActivity.class);
                    intent.putExtra("store", storeList.get(position));
                    intent.putExtra("flag",2);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    class StoresHolder extends RecyclerView.ViewHolder{
        TextView txt,txt_comment;
        ImageView img;
        ImageButton fav_img_btn;
        public StoresHolder(@NonNull  View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt_name);
            img = itemView.findViewById(R.id.userimg);
            txt_comment = itemView.findViewById(R.id.txt_comment);
            fav_img_btn = itemView.findViewById(R.id.fav_img_btn);
            fav_img_btn.setVisibility(View.GONE);
        }

        public void setData(Store store) {
            txt_comment.setText(store.getStoreAddress());
            txt.setText(store.getStoreName());
            Picasso.get().load(Constants.BASE_URL +"public/uploads/images/images/"+store.getLogo()).into(img);
            //img.setImageResource();
            /*if (store.getFavourite() == 1){
                fav_img_btn.setBackground(storesActivity.getResources().getDrawable(R.drawable.fav3));
            }else {
                fav_img_btn.setBackground(storesActivity.getResources().getDrawable(R.drawable.store_fav));
            }*/
        }
    }
}
