package com.alatheer.shebinbook.products.offers;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.Utilities.Constants;
import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.comments.CommentModel;
import com.alatheer.shebinbook.home.slider.Slider;
import com.alatheer.shebinbook.home.slider.SliderModel;
import com.alatheer.shebinbook.trader.updateoffer.UpdateOfferActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.OffersHolder> {
    Context context;
    List<Slider> sliderList;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;
    Integer role_id,trader_id;
    String store_name,store_logo,store_id,user_id;
    public OffersAdapter(Context context, List<Slider> sliderList,String store_name,String store_logo,String store_id) {
        this.context = context;
        this.sliderList = sliderList;
        this.store_name = store_name;
        this.store_logo = store_logo;
        this.store_id = store_id;

    }

    @NonNull

    @Override
    public OffersHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.offer_item,parent,false);
        return new OffersHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  OffersAdapter.OffersHolder holder, int position) {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(context);
        role_id = loginModel.getData().getUser().getRoleIdFk();
        trader_id = loginModel.getData().getUser().getTraderId();
        user_id =loginModel.getData().getUser().getId()+"";
        holder.setData(sliderList.get(position));
        holder.message_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (trader_id.equals(sliderList.get(position).getTraderIdFk())){
                        creatTraderOfferDialog(sliderList.get(position));
                    }else {
                        creatrOfferDialog(sliderList.get(position));
                    }
                }catch (Exception e){
                    creatrOfferDialog(sliderList.get(position));
                }
            }
        });
    }

    private void creatrOfferDialog(Slider slider) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.productdialog2, null);
        ImageView product_img = view.findViewById(R.id.product_img);
        TextView product_name = view.findViewById(R.id.product_name);
        TextView product_price = view.findViewById(R.id.product_price);
        TextView product_details = view.findViewById(R.id.product_decription);
        TextView product_price_offer = view.findViewById(R.id.product_price_offer);
        ImageView msg_img = view.findViewById(R.id.msg_img);
        TextView txtstore_name = view.findViewById(R.id.store_name);
        ImageView store_img = view.findViewById(R.id.store_logo);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        if (role_id == 4){
            msg_img.setVisibility(View.GONE);
        }
        product_name.setText(slider.getTitle());
        if (slider.getPriceBeforeOffer() != null){
            product_price.setText(slider.getPriceBeforeOffer()+"LE");
        }else {
            product_price.setText("السعر");
        }
        if (slider.getPriceAfterOffer() != null){
            product_price_offer.setText(slider.getPriceAfterOffer()+"LE");
        }else {
            product_price_offer.setText("الخصم");
        }
        product_details.setText(slider.getDescription());
        Picasso.get().load(Constants.BASE_URL +"public/uploads/images/images/"+store_logo).into(store_img);
        txtstore_name.setText(store_name);
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/advertisement/"+slider.getImg()).into(product_img);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        msg_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                CreateBasketDialog(slider);
            }
        });
    }

    private void CreateBasketDialog(Slider slider) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.basket_dialog, null);
        ImageView product_img = view.findViewById(R.id.product_img);
        EditText et_post = view.findViewById(R.id.et_post);
        ImageView user_img2 = view.findViewById(R.id.user_img);
        AppCompatButton btn_add = view.findViewById(R.id.btn_add);
        TextView txt_store_name = view.findViewById(R.id.store_name);
        ImageView image_store_img = view.findViewById(R.id.store_logo);
        builder.setView(view);
        Dialog dialog2 = builder.create();
        dialog2.show();
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/advertisement/"+slider.getImg()).resize(600,200).into(product_img);
        Picasso.get().load(Constants.BASE_URL +"public/uploads/images/images/"+store_logo).into(image_store_img);
        //Picasso.get().load(Constants.BASE_URL +"public/uploads/images/images/"+user_img).into(user_img2);
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
                    add_message(user_id,slider.getId()+"",slider.getTraderIdFk()+"",store_id,post);
                    dialog2.dismiss();
                }else {
                    et_post.setError("أكتب رسالتك");
                }
            }
        });
    }

    private void add_message(String user_id, String offer_id, String trader_id, String store_id, String post) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.send_offer_message(user_id,offer_id,trader_id,store_id,post);
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

    @Override
    public int getItemCount() {
        return sliderList.size();
    }

    class OffersHolder extends RecyclerView.ViewHolder{
        TextView txt_product_name,txt_offer_price,txt_product_price;
        ImageView slider_img,message_img;
        public OffersHolder(@NonNull  View itemView) {
            super(itemView);
            txt_product_name = itemView.findViewById(R.id.product_name);
            txt_offer_price = itemView.findViewById(R.id.product_price_offer);
            txt_product_price = itemView.findViewById(R.id.product_price);
            slider_img = itemView.findViewById(R.id.offer_img);
            message_img = itemView.findViewById(R.id.message_img);

        }

        public void setData(Slider slider) {
            if (slider.getPriceBeforeOffer()!= null){
                txt_product_price.setText(slider.getPriceBeforeOffer()+"LE");
            }else {
                txt_product_price.setText("السعر");
            }
            if (slider.getPriceAfterOffer()!=null){
                txt_offer_price.setText(slider.getPriceAfterOffer()+"LE");
            }else {
                txt_offer_price.setText("العرض");
            }
            txt_product_name.setText(slider.getTitle());
            Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/advertisement/"+slider.getImg()).into(slider_img);
        }
    }
    private void creatTraderOfferDialog(Slider slider) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.trader_product_dialog2, null);
        ImageView product_img = view.findViewById(R.id.product_img);
        TextView product_name = view.findViewById(R.id.product_name);
        TextView product_price = view.findViewById(R.id.product_price);
        TextView product_details = view.findViewById(R.id.product_details);
        TextView txtstore_name = view.findViewById(R.id.store_name);
        ImageView store_img = view.findViewById(R.id.store_logo);
        TextView product_price_offer = view.findViewById(R.id.product_price_offer);
        ImageView bin_img = view.findViewById(R.id.bin_img);
        ImageView edit_img = view.findViewById(R.id.edit_img);
        builder.setView(view);
        Dialog dialog3 = builder.create();
        dialog3.show();
        product_details.setText(slider.getDescription());
        product_name.setText(slider.getTitle());
        if (slider.getPriceBeforeOffer() != null){
            product_price.setText(slider.getPriceBeforeOffer()+"LE");
        }else {
            product_price.setText("السعر");
        }
        if (slider.getPriceAfterOffer() != null){
            product_price_offer.setText(slider.getPriceAfterOffer()+"LE");
        }else {
            product_price_offer.setText("الخصم");
        }
        Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/advertisement/"+slider.getImg()).into(product_img);
        Picasso.get().load(Constants.BASE_URL +"public/uploads/images/images/"+store_logo).into(store_img);
        txtstore_name.setText(store_name);
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
                delete_offer(slider.getId());
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

    private void delete_offer(Integer id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CommentModel> call = getDataService.delete_offer(id);
            call.enqueue(new Callback<CommentModel>() {
                @Override
                public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getData().getSuccess() == 1){
                            createsuccessdialog();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommentModel> call, Throwable t) {

                }
            });
        }
    }

    private void createsuccessdialog() {
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
        window.setLayout(450, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog3.isShowing()) {
                    dialog3.dismiss();
                    getAdvertisment();
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

    private void getAdvertisment() {
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(context);
        String user_gender = loginModel.getData().getUser().getGender()+"";
        String trader_id = loginModel.getData().getUser().getTraderId()+"";
        Toast.makeText(context, user_gender+"", Toast.LENGTH_SHORT).show();
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<SliderModel> call = getDataService.get_ads_in_store(user_gender+"",5,trader_id,1);
            call.enqueue(new Callback<SliderModel>() {
                @Override
                public void onResponse(Call<SliderModel> call, Response<SliderModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                           // init_sliders(response.body().getData());

                        }
                    }
                }

                @Override
                public void onFailure(Call<SliderModel> call, Throwable t) {
                    Log.d("bug1",t.getMessage());
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void add_offer(List<Slider> sliderList1) {
        for (Slider slider : sliderList1) {
            sliderList.add(slider);
        }
        notifyDataSetChanged();
    }
}
