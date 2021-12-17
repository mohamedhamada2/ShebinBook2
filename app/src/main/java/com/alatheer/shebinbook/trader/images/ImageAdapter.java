package com.alatheer.shebinbook.trader.images;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.Utilities.Constants;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {
    List<Image> imageList;
    Context context;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Integer trader_id;
    com.alatheer.shebinbook.trader.images.ImageFragment imageFragment;

    public ImageAdapter(List<Image> imageList, Context context, com.alatheer.shebinbook.trader.images.ImageFragment imageFragment) {
        this.imageList = imageList;
        this.context = context;
        this.imageFragment = imageFragment;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.image,parent,false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ImageAdapter.ImageHolder holder, int position) {
        holder.setData(imageList.get(position));
        holder.bin_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageFragment.delete_img(imageList.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    class ImageHolder extends RecyclerView.ViewHolder{
        ImageView img,bin_img;
        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            bin_img = itemView.findViewById(R.id.bin_img);
        }

        public void setData(Image image) {
            mySharedPreference = MySharedPreference.getInstance();
            loginModel = mySharedPreference.Get_UserData(context);
            trader_id = loginModel.getData().getUser().getTraderId();
            try {
                if (trader_id.equals(image.getTraderIdFk())) {
                    bin_img.setVisibility(View.VISIBLE);
                }
            }catch (Exception e){

            }
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+image.getImg()).into(img);
        }
    }
}
