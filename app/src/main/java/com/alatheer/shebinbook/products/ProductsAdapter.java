package com.alatheer.shebinbook.products;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.Utilities.Constants;
import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.allproducts.AllProductsActivity;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.comments.CommentModel;
import com.alatheer.shebinbook.home.category.Category;
import com.alatheer.shebinbook.home.category.CategoryAdapter;
import com.alatheer.shebinbook.subcategory.SubCategoryActivity;
import com.alatheer.shebinbook.trader.updateproduct.UpdateProductActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.CategoryHolder> {
    List<Product> productList;
    Context context;
    MySharedPreference mySharedPreference;
    LoginModel loginMode;
    Integer user_role;
    String store_name;
    String store_img;
    Integer trader_id;
    public ProductsAdapter(List<Product> productList, Context context,String store_name,String store_img) {
        this.productList = productList;
        this.context = context;
        this.store_name = store_name;
        this.store_img = store_img;
    }

    @NonNull
    @Override
    public ProductsAdapter.CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item,parent,false);
        return new ProductsAdapter.CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ProductsAdapter.CategoryHolder holder, int position) {
        mySharedPreference = MySharedPreference.getInstance();
        loginMode = mySharedPreference.Get_UserData(context);
        user_role = loginMode.getData().getUser().getRoleIdFk();
        trader_id = loginMode.getData().getUser().getTraderId();
        holder.setData(productList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (trader_id.equals(productList.get(position).getTraderIdFk())){
                        createTraderDialog(productList.get(position));
                    }else {
                        createAlertDialog(productList.get(position));
                    }
                }catch (Exception e){
                    Log.e("error5",e.getMessage());
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    createAlertDialog(productList.get(position));
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder{
        ImageView category_img;
        TextView category_txt;
        CheckBox checkBox;

        public CategoryHolder(@NonNull  View itemView) {
            super(itemView);
            category_img = itemView.findViewById(R.id.category_img);
            category_txt = itemView.findViewById(R.id.category_name);
            checkBox = itemView.findViewById(R.id.checkbox);
            checkBox.setVisibility(View.GONE);
        }

        public void setData(Product product) {
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+product.getImg()).resize(600,200).into(category_img);
            category_txt.setText(product.getTitle());
        }
    }
    private void createTraderDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.trader_product_dialog2, null);
        ImageView product_img = view.findViewById(R.id.product_img);
        TextView product_name = view.findViewById(R.id.product_name);
        TextView product_price = view.findViewById(R.id.product_price);
        TextView product_details = view.findViewById(R.id.product_details);
        TextView product_price_offer = view.findViewById(R.id.product_price_offer);
        //ImageView iv_gif_container = view.findViewById(R.id.iv_gif_container);
        //FrameLayout fl_shadow_container = view.findViewById(R.id.iv_gif_container);
        //TextView txt_product_decription = view.findViewById(R.id.product_decription);
        ImageView bin_img = view.findViewById(R.id.bin_img);
        ImageView edit_img = view.findViewById(R.id.edit_img);
        ImageView image_store_img = view.findViewById(R.id.store_logo);
        TextView txt_store_name = view.findViewById(R.id.store_name);
        product_price.setVisibility(View.GONE);
        //iv_gif_container.setVisibility(View.GONE);
        //fl_shadow_container.setVisibility(View.GONE);
        builder.setView(view);
        Dialog dialog3 = builder.create();
        dialog3.show();
        product_name.setText(product.getTitle());
        if (product.getPrice() != null){
            product_price_offer.setText(product.getPrice()+"");
        }else {
            product_price_offer.setText("أدخل السعر");
        }
        product_details.setText(product.getDetails());
        txt_store_name.setText(store_name);
        Picasso.get().load(Constants.BASE_URL +"public/uploads/images/images/"+store_img).into(image_store_img);
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+product.getImg()).into(product_img);
        Window window = dialog3.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        edit_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateProductActivity.class);
                intent.putExtra("product", product);
                intent.putExtra("flag",2);
                context.startActivity(intent);
                dialog3.dismiss();
                //CreateBasketDialog(product);
            }
        });
        bin_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3.dismiss();
                CreateDeleteDialog(product);
            }
        });
    }

    private void CreateDeleteDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.delete_dialog, null);
        Button btn_delete = view.findViewById(R.id.btn_delete);
        Button btn_skip = view.findViewById(R.id.btn_skip);
        builder.setView(view);
        Dialog dialog4 = builder.create();
        dialog4.show();
        Window window = dialog4.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_product(product.getId());
                dialog4.dismiss();
                //CreateBasketDialog(product);
            }
        });
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog4.dismiss();
                //CreateDeleteDialog(product);
            }
        });
    }

    private void delete_product(Integer id) {
        List<Integer> id_list = new ArrayList<>();
        id_list.add(id);
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.delete_product_from_alboum(id_list);
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getData().getSuccess() ==1){
                            createsuccessDialog();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommentModel> call, Throwable t) {

                }
            });
        }
    }

    public void createsuccessDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.success_dialog, null);
        TextView txt = view.findViewById(R.id.txt);
        txt.setText("تم الحذف");
        builder.setView(view);
        Dialog dialog3 = builder.create();
        dialog3.show();
        Window window = dialog3.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(450,LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog3.isShowing()) {
                    dialog3.dismiss();
                    //getproducts(gallery_id+"");
                    //Intent intent = new Intent(AllProductsActivity.this,AllProductsActivity.class);
                    //intent.putExtra("gallery_id",product.getAlboumIdFk());
                    //startActivity(intent);
                    //finish();
                }
            }
        };
        dialog3.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 3000);

    }
    public void createAlertDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.productdialog2, null);
        ImageView product_img = view.findViewById(R.id.product_img);
        TextView product_name = view.findViewById(R.id.product_name);
        TextView product_price = view.findViewById(R.id.product_price);
        ImageView image_store_img = view.findViewById(R.id.store_logo);
        TextView txt_store_name = view.findViewById(R.id.store_name);
        TextView product_price_offer = view.findViewById(R.id.product_price_offer);
        TextView txt_product_decription = view.findViewById(R.id.product_decription);
        ImageView msg_img = view.findViewById(R.id.msg_img);
        if (user_role == 4){
            msg_img.setVisibility(View.GONE);
        }
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        product_name.setText(product.getTitle());
        if (product.getPrice() != null){
            product_price_offer.setText(product.getPrice()+"");
        }else {
            product_price_offer.setText("أطلب السعر");
        }
        txt_product_decription.setText(product.getDetails());
        product_price.setVisibility(View.GONE);
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+product.getImg()).into(product_img);
        Picasso.get().load(Constants.BASE_URL +"public/uploads/images/images/"+store_img).into(image_store_img);
        txt_store_name.setText(store_name);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        msg_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                CreateBasketDialog(product);
            }
        });
    }

    private void CreateBasketDialog(Product product) {
        MySharedPreference mySharedPreference = MySharedPreference.getInstance();
        LoginModel loginModel = mySharedPreference.Get_UserData(context);
        String userimg = loginModel.getData().getUser().getUserImg();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.basket_dialog, null);
        ImageView product_img = view.findViewById(R.id.product_img);
        EditText et_post = view.findViewById(R.id.et_post);
        ImageView user_img = view.findViewById(R.id.user_img);
        AppCompatButton btn_add = view.findViewById(R.id.btn_add);
        TextView txt_store_name = view.findViewById(R.id.store_name);
        ImageView image_store_img = view.findViewById(R.id.store_logo);
        builder.setView(view);
        Dialog dialog2 = builder.create();
        dialog2.show();
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+product.getImg()).into(product_img);
        Picasso.get().load(Constants.BASE_URL +"public/uploads/images/images/"+store_img).into(image_store_img);
        Picasso.get().load(Constants.BASE_URL +"public/uploads/images/images/"+userimg).into(user_img);
        txt_store_name.setText(store_name);
        Window window = dialog2.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String post = et_post.getText().toString();
                if (!TextUtils.isEmpty(post)){
                    add_message(loginMode.getData().getUser().getId(),product.getId()+"",product.getTraderIdFk()+"",product.getStoreIdFk()+"",post);
                    dialog2.dismiss();
                }else {
                    et_post.setError("أكتب رسالتك");
                }
            }
        });
    }

    private void add_message(Integer id, String s, String s1, String s2, String post) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.send_message(id+"",s,s1,s2,post);
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            Toast.makeText(context, "تم إرسال رسالتك بنجاح", Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<CommentModel> call, Throwable t) {

                }
            });
        }
    }

}
