package com.alatheer.shebinbook.products.rating;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingHolder> {
    List<Rating> ratingList;
    Context context;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Integer trader_id;
    RatingFragment ratingFragment;

    public RatingAdapter(List<Rating> ratingList, Context context,RatingFragment ratingFragment) {
        this.ratingList = ratingList;
        this.context = context;
        this.ratingFragment = ratingFragment;
    }

    @NonNull
    @Override
    public RatingHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rating_item,parent,false);
        return new RatingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  RatingAdapter.RatingHolder holder, int position) {
        holder.setData(ratingList.get(position));
        holder.bin_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratingFragment.delete_rating(ratingList.get(position).getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return ratingList.size();
    }

    class RatingHolder extends RecyclerView.ViewHolder{
        TextView txt_user_name;
        ImageView user_img,bin_img;
        RatingBar ratingBar;
        public RatingHolder(@NonNull  View itemView) {
            super(itemView);
            txt_user_name = itemView.findViewById(R.id.txt_name);
            user_img = itemView.findViewById(R.id.userimg);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            bin_img = itemView.findViewById(R.id.bin_img);
        }

        public void setData(Rating rating) {
            mySharedPreference = MySharedPreference.getInstance();
            loginModel = mySharedPreference.Get_UserData(context);
            trader_id = loginModel.getData().getUser().getTraderId();
            try {
                if (trader_id.equals(rating.getTrader_id_fk())) {
                    bin_img.setVisibility(View.VISIBLE);
                }
            }catch (Exception e){

            }
            txt_user_name.setText(rating.getName());
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+rating.getUserImg()).into(user_img);
            ratingBar.setRating(Float.parseFloat(rating.getRate()));
        }
    }
}
