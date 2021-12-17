package com.alatheer.shebinbook.home.slider;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.Utilities.Constants;
import com.alatheer.shebinbook.allproducts.AllProductsActivity;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.comments.CommentModel;
import com.alatheer.shebinbook.products.ProductsActivity;
import com.alatheer.shebinbook.trader.profile.ProfileActivity;
import com.alatheer.shebinbook.trader.updateoffer.UpdateOfferActivity;
import com.alatheer.shebinbook.trader.updateproduct.UpdateProductActivity;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SliderAdapter extends PagerAdapter {
    Context context;
    List<Slider> sliderList;
    //ProfileActivity profileActivity;

    public SliderAdapter(Context context, List<Slider> sliderList) {
        this.context = context;
        this.sliderList = sliderList;
        //profileActivity = (ProfileActivity) context;
    }

    @Override
    public int getCount() {
        return sliderList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull  View view, @NonNull  Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull  ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.slide_item_container,null);
        ImageView img = view.findViewById(R.id.slider_img);
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/advertisement/"+sliderList.get(position).getImg()).into(img);
        container.addView(view);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addclick(sliderList.get(position));
                //creatOfferDialog(sliderList.get(position));
            }
        });
        return view;
    }

    private void addclick(Slider slider) {
        MySharedPreference mySharedPreference = MySharedPreference.getInstance();
        LoginModel loginModel = mySharedPreference.Get_UserData(context);
        String user_id = loginModel.getData().getUser().getId()+"";
        Integer trader_id = loginModel.getData().getUser().getTraderId();
        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<CommentModel> call = getDataService.add_click(user_id,slider.getId()+"");
        call.enqueue(new Callback<CommentModel>() {
            @Override
            public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()){
                        if (response.body().getData().getSuccess()==1){
                            Toast.makeText(context, "تم اعجابك بنجاح", Toast.LENGTH_SHORT).show();
                        }
                        if (slider.getTraderIdFk().equals(trader_id)){
                            Intent intent = new Intent(context, ProfileActivity.class);
                            intent.putExtra("slider",slider);
                            intent.putExtra("flag",1);
                            context.startActivity(intent);
                        }else {
                            Intent intent = new Intent(context, ProductsActivity.class);
                            intent.putExtra("slider",slider);
                            intent.putExtra("flag",1);
                            context.startActivity(intent);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CommentModel> call, Throwable t) {

            }
        });

    }

    /*private void creatOfferDialog(Slider slider) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.trader_product_dialog, null);
        ImageView product_img = view.findViewById(R.id.product_img);
        TextView product_name = view.findViewById(R.id.product_name);
        TextView product_price = view.findViewById(R.id.product_price);
        TextView product_details = view.findViewById(R.id.product_details);
        TextView product_price_offer = view.findViewById(R.id.product_price_offer);
        ImageView bin_img = view.findViewById(R.id.bin_img);
        ImageView edit_img = view.findViewById(R.id.edit_img);
        builder.setView(view);
        Dialog dialog3 = builder.create();
        dialog3.show();
        product_name.setText(slider.getTitle());
        product_price.setText(slider.getPriceBeforeOffer()+"LE");
        product_price_offer.setText(slider.getPriceAfterOffer()+"LE");
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/advertisement/"+slider.getImg()).into(product_img);
        Window window = dialog3.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        edit_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateOfferActivity.class);
                intent.putExtra("slider", slider);
                context.startActivity(intent);
                dialog3.dismiss();
                //CreateBasketDialog(product);
            }
        });
        bin_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3.dismiss();
                CreateDeleteDialog(slider);
            }
        });
    }

    /*private void CreateDeleteDialog(Slider slider) {
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
                //profileActivity.delete_offer(slider.getId());
                dialog4.dismiss();
                //CreateBasketDialog(product);
            }
        });
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog4.dismiss();
               // CreateDeleteDialog(product);
            }
        });
    }*/

    @Override
    public void destroyItem(@NonNull  ViewGroup container, int position, @NonNull  Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
