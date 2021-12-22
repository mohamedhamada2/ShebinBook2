package com.alatheer.shebinbook.stores;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.Utilities.Constants;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.products.ProductsActivity;
import com.alatheer.shebinbook.setting.Profile;
import com.alatheer.shebinbook.trader.profile.ProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class StoresAdapter extends RecyclerView.Adapter<StoresAdapter.StoresHolder> {
    Context context;
    List<Store> storeList;
    StoresActivity storesActivity;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Integer role_id,trader_id;
    public StoresAdapter(Context context, List<Store> storeList) {
        this.context = context;
        this.storeList = storeList;
        storesActivity = (StoresActivity) context;
    }

    @NonNull
    @Override
    public StoresHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.store_item,parent,false);
        return new StoresHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  StoresAdapter.StoresHolder holder, int position) {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(context);
        role_id = loginModel.getData().getUser().getRoleIdFk();
        trader_id = loginModel.getData().getUser().getTraderId();
        holder.setData(storeList.get(position));
        holder.fav_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (storeList.get(position).getFavourite() == 0){
                    storesActivity.add_to_fav(storeList.get(position));
                }else {
                    storesActivity.delete_from_fav(storeList.get(position));
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (role_id != 4){
                    Intent intent = new Intent(context, ProductsActivity.class);
                    intent.putExtra("store",storeList.get(position));
                    intent.putExtra("flag",2);
                    context.startActivity(intent);
                }else {
                    if (trader_id.equals(storeList.get(position).getTradeId())){
                        Intent intent = new Intent(context, ProfileActivity.class);
                        context.startActivity(intent);
                    }else {
                        Intent intent = new Intent(context, ProductsActivity.class);
                        intent.putExtra("store",storeList.get(position));
                        intent.putExtra("flag",2);
                        context.startActivity(intent);
                    }
                }
            }
        });
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateImageDialog(storeList.get(position).getLogo());
            }
        });
    }
    private void CreateImageDialog(String product_img) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.image_item, null);
        ImageView img = view.findViewById(R.id.img);
        Picasso.get().load(Constants.BASE_URL +"public/uploads/images/images/"+product_img).into(img);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
        }

        public void setData(Store store) {
            txt_comment.setText(store.getMini_description());
            txt.setText(store.getStoreName());
            Picasso.get().load(Constants.BASE_URL +"public/uploads/images/images/"+store.getLogo()).into(img);
            //img.setImageResource();
            if (store.getFavourite() == 1){
                fav_img_btn.setBackground(storesActivity.getResources().getDrawable(R.drawable.fav3));
            }else {
                fav_img_btn.setBackground(storesActivity.getResources().getDrawable(R.drawable.store_fav));
            }
        }
    }
    public void add_store(List<Store> storeList2){
        for (Store store: storeList2){
            storeList.add(store);
        }
        notifyDataSetChanged();
    }
}
