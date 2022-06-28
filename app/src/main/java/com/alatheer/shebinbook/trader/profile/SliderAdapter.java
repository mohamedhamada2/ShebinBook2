package com.alatheer.shebinbook.trader.profile;

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

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.Utilities.Constants;
import com.alatheer.shebinbook.home.slider.Slider;
import com.alatheer.shebinbook.trader.updateoffer.UpdateOfferActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {
    Context context;
    List<Slider> sliderList;
    ProfileActivity profileActivity;
    String store_name,store_logo;

    public SliderAdapter(Context context, List<Slider> sliderList,String store_name,String store_logo) {
        this.context = context;
        this.sliderList = sliderList;
        profileActivity = (ProfileActivity) context;
        this.store_name = store_name;
        this.store_logo = store_logo;
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
                creatOfferDialog(sliderList.get(position));
            }
        });
        return view;
    }

    private void creatOfferDialog(Slider slider) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.trader_product_dialog2, null);
        ImageView product_img = view.findViewById(R.id.product_img);
        TextView product_name = view.findViewById(R.id.product_name);
        TextView product_price = view.findViewById(R.id.product_price);
        TextView product_details = view.findViewById(R.id.product_details);
        TextView product_price_offer = view.findViewById(R.id.product_price_offer);
        TextView txt_store_name = view.findViewById(R.id.store_name);
        ImageView store_img = view.findViewById(R.id.store_logo);
        ImageView bin_img = view.findViewById(R.id.bin_img);
        ImageView edit_img = view.findViewById(R.id.edit_img);
        builder.setView(view);
        Dialog dialog3 = builder.create();
        dialog3.show();
        product_name.setText(slider.getTitle());
        if (slider.getPriceBeforeOffer()!= null){
            product_price.setText(slider.getPriceBeforeOffer()+"LE");
        }else {
            product_price.setText("أدخل السعر");
        }
        if (slider.getPriceAfterOffer() != null){
            product_price_offer.setText(slider.getPriceAfterOffer()+"LE");
        }else {
            product_price_offer.setText("أدخل الخصم");
        }
        product_details.setText(slider.getDescription());
        txt_store_name.setText(store_name);
        Picasso.get().load(Constants.BASE_URL +"public/uploads/images/images/"+store_logo).into(store_img);
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

    private void CreateDeleteDialog(Slider slider) {
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
                profileActivity.delete_offer(slider.getId());
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
    }

    @Override
    public void destroyItem(@NonNull  ViewGroup container, int position, @NonNull  Object object) {
       container.removeView((View) object);
    }
    public void add_offer(List<Slider> sliderList1) {
        for (Slider slider : sliderList1) {
            sliderList.add(slider);
        }
        notifyDataSetChanged();
    }
}
