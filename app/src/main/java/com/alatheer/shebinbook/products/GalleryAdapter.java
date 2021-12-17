package com.alatheer.shebinbook.products;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.allproducts.AllProductsActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryAdapter extends RecyclerView.Adapter <GalleryAdapter.ProductsHolder>{
    List<Gallery> galleryList;
    Context context;
    GalleryFragment galleryFragment;
    String store_name,store_image;


    public GalleryAdapter(List<Gallery> galleryList, Context context,GalleryFragment galleryFragment,String store_name,String store_image) {
        this.galleryList = galleryList;
        this.context = context;
        this.galleryFragment = galleryFragment;
        this.store_name = store_name;
        this.store_image = store_image;

    }

    @NonNull
    @Override
    public ProductsHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_item,parent,false);
        return new GalleryAdapter.ProductsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  GalleryAdapter.ProductsHolder holder, int position) {
        holder.setData(galleryList.get(position));
        holder.txt_see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AllProductsActivity.class);
                intent.putExtra("gallery_id",galleryList.get(position).getId());
                intent.putExtra("trader_id",galleryList.get(position).getTraderIdFk());
                intent.putExtra("store_id",galleryList.get(position).getStoreIdFk());
                context.startActivity(intent);
               // Toast.makeText(context, "gallery", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    class ProductsHolder extends RecyclerView.ViewHolder{
        Button btn_shebin;
        TextView txt_see_all;
        RecyclerView recyclerView;
        public ProductsHolder(@NonNull View itemView) {
            super(itemView);
            btn_shebin = itemView.findViewById(R.id.btn_shebin);
            txt_see_all = itemView.findViewById(R.id.txt_see_all);
            recyclerView = itemView.findViewById(R.id.products_recycler);
        }
        public void setData(Gallery gallery) {
           // Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+category.getImg()).into(category_img);
            //category_txt.setText(category.getName());
            btn_shebin.setText(gallery.getTitle());
            recyclerView.setHasFixedSize(true);
            ProductsAdapter productsAdapter = new ProductsAdapter(gallery.getProducts(),context,store_name,store_image);
            LinearLayoutManager layoutManager = new GridLayoutManager(context,2,GridLayoutManager.VERTICAL,true);
            layoutManager.setReverseLayout(true);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(productsAdapter);
            recyclerView.setLayoutManager(layoutManager);

        }
    }
}
