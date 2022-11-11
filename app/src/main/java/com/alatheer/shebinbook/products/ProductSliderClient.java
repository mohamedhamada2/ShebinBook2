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
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.authentication.login.User;
import com.alatheer.shebinbook.trader.updateproduct.UpdateProductActivity;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductSliderClient extends SliderViewAdapter<ProductSliderClient.SliderAdapterVH> {

    private Context context;
    List<com.alatheer.shebinbook.allproducts.Product> mSliderItems ;
    private int index;
    String store_name,store_img;
    AllProductsActivity allProductsActivity;
    MySharedPreference mySharedPreference;
    LoginModel user;
    Integer role_id;
    public ProductSliderClient(Context context, List<com.alatheer.shebinbook.allproducts.Product> mSliderItems, String store_name, String store_img) {
        this.context = context;
        this.mSliderItems = mSliderItems;
        this.index = index;
        allProductsActivity = (AllProductsActivity) context;
        this.store_name = store_name;
        this.store_img = store_img;
    }

    public void renewItems(List<com.alatheer.shebinbook.allproducts.Product> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(com.alatheer.shebinbook.allproducts.Product sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public ProductSliderClient.SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.product_dialog, null);
        return new ProductSliderClient.SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(ProductSliderClient.SliderAdapterVH viewHolder, final int position) {

        Product product = mSliderItems.get(position);
        mySharedPreference  = MySharedPreference.getInstance();
        user = mySharedPreference.Get_UserData(context);
        role_id = user.getData().getUser().getRoleIdFk();
        /*if (user_role == 4){
            viewHolder.msg_img.setVisibility(View.GONE);
        }*/

        viewHolder.product_name.setText(product.getTitle());
        viewHolder.txt_store_name.setText(store_name);
        if (product.getPrice() != null){
            viewHolder.product_price_offer.setText(product.getPrice()+"");
        }else {
            viewHolder.product_price_offer.setText("أطلب السعر");
        }
        if (role_id == 4){
            viewHolder.msg_img.setVisibility(View.GONE);
        }else {
            viewHolder.msg_img.setVisibility(View.VISIBLE);
        }
        if (product.getDetails() != null){
            viewHolder.product_decription.setText(product.getDetails());
        }else {
            viewHolder.product_decription.setText(null);
        }
        viewHolder.product_price.setVisibility(View.GONE);
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+product.getImg()).into(viewHolder.product_img);
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+store_img).into(viewHolder.store_img2);
        viewHolder.msg_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                allProductsActivity.CreateBasketDialog(product);
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
        TextView product_price,product_decription;
        TextView product_details;
        TextView product_price_offer;
        ImageView bin_img,edit_img;
        TextView txt_store_name;
        ImageView store_img2,msg_img;
        public SliderAdapterVH(View itemView) {
            super(itemView);
            product_img = itemView.findViewById(R.id.product_img);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_price_offer = itemView.findViewById(R.id.product_price_offer);
            product_decription = itemView.findViewById(R.id.product_decription);
            txt_store_name = itemView.findViewById(R.id.store_name);
            msg_img = itemView.findViewById(R.id.msg_img);
            store_img2 = itemView.findViewById(R.id.store_logo);
            this.itemView = itemView;
        }
    }
}
