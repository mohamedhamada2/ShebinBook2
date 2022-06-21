package com.alatheer.shebinbook.products;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.allproducts.AllProductsActivity;
import com.alatheer.shebinbook.allproducts.Product;
import com.alatheer.shebinbook.trader.updateproduct.UpdateProductActivity;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductsSlider_for_Trader extends SliderViewAdapter<ProductsSlider_for_Trader.SliderAdapterVH> {

    private Context context;
    List<Product> mSliderItems ;
    private int index;
    String store_name,store_img;
    AllProductsActivity allProductsActivity;

    public ProductsSlider_for_Trader(Context context, List<Product> mSliderItems, String store_name, String store_img) {
        this.context = context;
        this.mSliderItems = mSliderItems;
        this.index = index;
        allProductsActivity = (AllProductsActivity) context;
        this.store_name = store_name;
        this.store_img = store_img;
    }

    public void renewItems(List<Product> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(Product sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public ProductsSlider_for_Trader.SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.product_slider_item, null);
        return new ProductsSlider_for_Trader.SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(ProductsSlider_for_Trader.SliderAdapterVH viewHolder, final int position) {

        Product product = mSliderItems.get(position);
        viewHolder.product_name.setText(product.getTitle());
        viewHolder.txt_store_name.setText(store_name);
        if (product.getPrice() != null){
            viewHolder.product_price_offer.setText(product.getPrice()+"");
        }else {
            viewHolder.product_price_offer.setText("أدخل السعر ان أردت");
        }
        if (product.getDetails() != null){
            viewHolder.product_details.setText(product.getDetails());
        }else {
            viewHolder.product_price.setText("--------");
        }
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+product.getImg()).into(viewHolder.product_img);
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+store_img).into(viewHolder.store_img2);
        viewHolder.edit_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateProductActivity.class);
                intent.putExtra("product", product);
                context.startActivity(intent);
                allProductsActivity.dismiss();
                //CreateBasketDialog(product);
            }
        });
        viewHolder.bin_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allProductsActivity.dismiss();
                allProductsActivity.CreateDeleteDialog(product);
            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        View itemView;
        ImageView product_img;
        TextView product_name;
        TextView product_price;
        TextView product_details;
        TextView product_price_offer;
        ImageView bin_img,edit_img;
        TextView txt_store_name;
        ImageView store_img2;
        public SliderAdapterVH(View itemView) {
            super(itemView);
            product_img = itemView.findViewById(R.id.product_img);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_details = itemView.findViewById(R.id.product_details);
            product_price_offer = itemView.findViewById(R.id.product_price_offer);
            bin_img = itemView.findViewById(R.id.bin_img);
            edit_img = itemView.findViewById(R.id.edit_img);
            txt_store_name = itemView.findViewById(R.id.store_name);
            store_img2 = itemView.findViewById(R.id.store_logo);
            this.itemView = itemView;
        }
    }
}
