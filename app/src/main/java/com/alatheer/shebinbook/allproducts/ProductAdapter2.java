package com.alatheer.shebinbook.allproducts;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.allproducts.Product;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.products.ProductsAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter2 extends RecyclerView.Adapter<ProductAdapter2.CategoryHolder> {
    List<Product> productList;
    Context context;
    AllProductsActivity allProductsActivity;
    Integer user_role;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Integer trader_id;
    List<Integer> product_id_list;

    public ProductAdapter2(List<Product> productList, Context context,Integer user_role) {
        this.productList = productList;
        this.context = context;
        allProductsActivity = (AllProductsActivity) context;
        this.user_role = user_role;
        product_id_list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ProductAdapter2.CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item,parent,false);
        return new ProductAdapter2.CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ProductAdapter2.CategoryHolder holder, int position) {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(context);
        trader_id = loginModel.getData().getUser().getTraderId();
        holder.setData(productList.get(position),trader_id);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_role != 4 ){
                    allProductsActivity.createAlertDialog(productList.get(position));
                }else {
                    if (trader_id.equals(productList.get(position).getTraderIdFk())){
                        allProductsActivity.createTraderDialog(productList.get(position));
                    }else {
                        allProductsActivity.createAlertDialog(productList.get(position));
                    }
                }
            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()){
                    holder.checkBox.setButtonDrawable(R.drawable.ic_checkbox_active);
                    product_id_list.add(productList.get(position).getId());
                    allProductsActivity.get_products_id(product_id_list);
                }else {
                    holder.checkBox.setButtonDrawable(R.drawable.ic_checkbox_svgrepo_com);
                    product_id_list.remove(productList.get(position).getId());
                    allProductsActivity.get_products_id(product_id_list);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder{
        ImageView product_img;
        TextView product_name,product_price;
        CheckBox checkBox;

        public CategoryHolder(@NonNull  View itemView) {
            super(itemView);
            product_img = itemView.findViewById(R.id.product_img);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            checkBox = itemView.findViewById(R.id.checkbox);
        }

        public void setData(Product product,Integer trader_id) {
            try {
                if (trader_id.equals(product.getTraderIdFk())){
                    checkBox.setVisibility(View.VISIBLE);
                }else {
                    checkBox.setVisibility(View.GONE);
                }
            }catch (Exception e){
                checkBox.setVisibility(View.GONE);
            }
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+product.getImg()).into(product_img);
            product_name.setText(product.getTitle());
            if (product.getPrice() != null){
                product_price.setText(product.getPrice()+"");
            }else {
                try {
                    if (trader_id.equals(product.getTraderIdFk())){
                        product_price.setText("أدخل السعر");
                    }else {
                        product_price.setText("أطلب السعر");
                    }
                }catch (Exception e){
                    product_price.setText("أطلب السعر");
                }
            }

        }
    }
}
