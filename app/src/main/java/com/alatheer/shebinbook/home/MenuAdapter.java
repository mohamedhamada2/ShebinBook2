package com.alatheer.shebinbook.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.favorite.FavoriteActivity;
import com.alatheer.shebinbook.authentication.login.LoginActivity;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.contactus.ContactUsActivity;
import com.alatheer.shebinbook.home.slider.MenuItem;
import com.alatheer.shebinbook.setting.SettingActivity;
import com.alatheer.shebinbook.trader.profile.ProfileActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder> {
    List<com.alatheer.shebinbook.home.slider.MenuItem> menuItemList;
    Context context;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;

    public MenuAdapter(List<MenuItem> menuItemList, Context context) {
        this.menuItemList = menuItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_item,parent,false);
        return new MenuHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.MenuHolder holder, int position) {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(context);
        holder.setData(menuItemList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuItem menuItem = menuItemList.get(position);
                if (menuItem == menuItemList.get(0)){
                    context.startActivity(new Intent(context,HomeActivity.class));
                }else if (menuItem == menuItemList.get(1)){
                    context.startActivity(new Intent(context, FavoriteActivity.class));
                }else if (menuItem == menuItemList.get(2)){
                    context.startActivity(new Intent(context, SettingActivity.class));
                }else if (menuItem == menuItemList.get(3)){
                    context.startActivity(new Intent(context, ContactUsActivity.class));
                }else if (menuItem == menuItemList.get(4)){
                    mySharedPreference.ClearData(context);
                    context.startActivity(new Intent(context, LoginActivity.class));
                }else if (menuItem == menuItemList.get(5)){
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("flag",2);
                    context.startActivity(intent);
            }
            }
        });

    }

    @Override
    public int getItemCount() {
        return menuItemList.size();
    }

    class MenuHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView txt;
        public MenuHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            txt = itemView.findViewById(R.id.txt);
        }

        public void setData(com.alatheer.shebinbook.home.slider.MenuItem menuItem) {
            img.setImageResource(menuItem.getImage());
            txt.setText(menuItem.getName());
        }
    }
}
