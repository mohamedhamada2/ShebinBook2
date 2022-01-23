package com.alatheer.shebinbook.products.images;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.trader.images.Image;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter  extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {
    List<Image> imageList;
    Context context;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Integer trader_id;
    com.alatheer.shebinbook.products.images.ImageFragment imageFragment;

    public ImageAdapter(List<Image> imageList, Context context, ImageFragment imageFragment) {
        this.imageList = imageList;
        this.context = context;
        this.imageFragment = imageFragment;
    }

    @NonNull
    @Override
    public com.alatheer.shebinbook.products.images.ImageAdapter.ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image,parent,false);
        return new com.alatheer.shebinbook.products.images.ImageAdapter.ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  com.alatheer.shebinbook.products.images.ImageAdapter.ImageHolder holder, int position) {
        holder.setData(imageList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateImageDialog(imageList.get(position).getImg());
            }
        });

    }
    private void CreateImageDialog(String product_img) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.image_item2, null);
        ImageView img = view.findViewById(R.id.img);
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+product_img).into(img);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
            try {
                Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+image.getImg()).resize(1000, 1000).into(img);
            }catch (Exception e){
                Log.e("error_mag",e.getMessage());
                img.setImageResource(R.drawable.clothes1);
            }

        }
    }
}
