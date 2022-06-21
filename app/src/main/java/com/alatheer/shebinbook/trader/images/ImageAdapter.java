package com.alatheer.shebinbook.trader.images;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.Utilities.Constants;
import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.products.images.ImagesSlider;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {
    List<Image> imageList;
    Context context;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Integer trader_id;
    private int indexOfHighlightedItem = -1;
    List<Integer> gallery_id_list;

    com.alatheer.shebinbook.trader.images.ImageFragment imageFragment;

    public ImageAdapter(List<Image> imageList, Context context, com.alatheer.shebinbook.trader.images.ImageFragment imageFragment) {
        this.imageList = imageList;
        this.context = context;
        this.imageFragment = imageFragment;
        gallery_id_list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.image,parent,false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ImageAdapter.ImageHolder holder, int position) {
        holder.setData(imageList.get(position));
        holder.bin_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageFragment.delete_img(imageList.get(position).getId());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexOfHighlightedItem = position;
                CreateImageDialog(imageList.get(position));
            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()){
                    holder.checkBox.setButtonDrawable(R.drawable.ic_checkbox_active);
                    gallery_id_list.add(imageList.get(position).getId());
                    imageFragment.get_images_id(gallery_id_list);
                }else {
                    holder.checkBox.setButtonDrawable(R.drawable.ic_checkbox_svgrepo_com);
                    gallery_id_list.remove(imageList.get(position).getId());
                    imageFragment.get_images_id(gallery_id_list);
                }
            }
        });
    }

    private void CreateImageDialog(Image image) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.image_item2, null);
        //ImageView img = view.findViewById(R.id.img);
        //Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+product_img).resize(1000,1000).into(img);

        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ImagesData> call = getDataService.get_gallery(trader_id+"");
            call.enqueue(new Callback<ImagesData>() {
                @Override
                public void onResponse(Call<ImagesData> call, Response<ImagesData> response) {
                    if (response.isSuccessful()){
                        SliderView sliderView = view.findViewById(R.id.imageSlider);
                        sliderView.setSliderAdapter(new ImagesSlider(context,response.body().getData(),1));
                        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                        sliderView.setIndicatorSelectedColor(Color.WHITE);
                        sliderView.setIndicatorUnselectedColor(Color.GRAY);
                        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
                        sliderView.startAutoCycle();
                        for (int i = 0;i<response.body().getData().size();i++){
                            if (image.getId().equals(response.body().getData().get(i).getId())){
                                sliderView.setCurrentPagePosition(i);
                            }
                        }
                    }
                }

                    @Override
                    public void onFailure(Call<ImagesData> call, Throwable t) {

                    }
                });
            }
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
        CheckBox checkBox;
        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            bin_img = itemView.findViewById(R.id.bin_img);
            checkBox = itemView.findViewById(R.id.checkbox);

        }

        public void setData(Image image) {
            mySharedPreference = MySharedPreference.getInstance();
            loginModel = mySharedPreference.Get_UserData(context);
            trader_id = loginModel.getData().getUser().getTraderId();
            try {
                if (trader_id.equals(image.getTraderIdFk())) {
                    bin_img.setVisibility(View.VISIBLE);
                    checkBox.setVisibility(View.VISIBLE);
                }
            }catch (Exception e){

            }
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/"+image.getImg()).resize(1000,800).into(img);
        }
    }
}
