package com.alatheer.shebinbook.allproducts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alatheer.shebinbook.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ProductImageAdapter extends PagerAdapter {
    Context context;
    List<SubImage> subImageList;

    public ProductImageAdapter(Context context, List<SubImage> subImageList) {
        this.context = context;
        this.subImageList = subImageList;
    }

    @Override
    public int getCount() {
        return subImageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull  View view, @NonNull Object object) {
        return view == object;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.slide_item_container,null);
        ImageView img = view.findViewById(R.id.slider_img);
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+subImageList.get(position).getImage()).into(img);
        container.addView(view);
        return view;
    }
    @Override
    public void destroyItem(@NonNull  ViewGroup container, int position, @NonNull  Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
