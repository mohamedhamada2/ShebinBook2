package com.alatheer.shebinbook.trader.profile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.allproducts.AllProductsActivity;
import com.alatheer.shebinbook.products.Gallery;
import com.alatheer.shebinbook.products.ProductsAdapter;
import com.alatheer.shebinbook.trader.addproduct.AddProductActivity;
import com.alatheer.shebinbook.trader.editalboum.UpdateAlboumActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryAdapter extends RecyclerView.Adapter <GalleryAdapter.ProductsHolder>{
    List<Gallery> galleryList;
    Context context;
    com.alatheer.shebinbook.trader.profile.GalleryFragment galleryFragment;
    String store_name,store_img;

    public GalleryAdapter(List<Gallery> galleryList, Context context, GalleryFragment galleryFragment,String store_name,String store_img) {
        this.galleryList = galleryList;
        this.context = context;
        this.galleryFragment = galleryFragment;
        this.store_name = store_name;
        this.store_img = store_img;
    }

    @NonNull
    @Override
    public ProductsHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_trader_item,parent,false);
        return new ProductsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ProductsHolder holder, int position) {
        holder.setData(galleryList.get(position));
        holder.txt_see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AllProductsActivity.class);
                intent.putExtra("gallery_id",galleryList.get(position).getId());
                intent.putExtra("trader_id",galleryList.get(position).getTraderIdFk());
                intent.putExtra("store_id",galleryList.get(position).getStoreIdFk());
                context.startActivity(intent);
            }
        });
        holder.add_to_alboum_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddProductActivity.class);
                intent.putExtra("gallery_id",galleryList.get(position).getId());
                intent.putExtra("store_id",galleryList.get(position).getStoreIdFk());
                intent.putExtra("trader_id",galleryList.get(position).getTraderIdFk());
                context.startActivity(intent);
            }
        });
        holder.edit_alboum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateAlboumActivity.class);
                intent.putExtra("alboum_id",galleryList.get(position).getId());
                intent.putExtra("store_id",galleryList.get(position).getStoreIdFk());
                intent.putExtra("alboum_name",galleryList.get(position).getTitle());
                context.startActivity(intent);
            }
        });
        if (galleryList.get(position).getProducts().isEmpty()){
            holder.linear_see_all.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    class ProductsHolder extends RecyclerView.ViewHolder{
        TextView btn_shebin;
        TextView txt_see_all;
        RecyclerView recyclerView;
        LinearLayout linear_see_all;
        ImageView add_to_alboum_img,edit_alboum;
        public ProductsHolder(@NonNull View itemView) {
            super(itemView);
            btn_shebin = itemView.findViewById(R.id.btn_shebin);
            txt_see_all = itemView.findViewById(R.id.txt_see_all);
            recyclerView = itemView.findViewById(R.id.products_recycler);
            linear_see_all = itemView.findViewById(R.id.linear_see_all);
            add_to_alboum_img = itemView.findViewById(R.id.add_to_alboum_img);
            edit_alboum = itemView.findViewById(R.id.edit_alboum);
        }
        public void setData(Gallery gallery) {
           // Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+category.getImg()).into(category_img);
            //category_txt.setText(category.getName());
            btn_shebin.setText(gallery.getTitle());
            recyclerView.setHasFixedSize(true);
            ProductsAdapter productsAdapter = new ProductsAdapter(gallery.getProducts(),context,store_name,store_img);
            LinearLayoutManager layoutManager = new GridLayoutManager(context,2,GridLayoutManager.VERTICAL,true);
            layoutManager.setReverseLayout(true);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(productsAdapter);
            recyclerView.setLayoutManager(layoutManager);

        }
    }
}
