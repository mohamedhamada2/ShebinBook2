package com.alatheer.shebinbook.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.products.ProductsActivity;
import com.alatheer.shebinbook.products.StoreDetails;
import com.alatheer.shebinbook.products.StoreDetailsAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.StoreDetailsHolder> {
    List<StoreDetails> storeDetailsList;
    Context context;
    Integer selectedItem = 0;
    //ProductsActivity productsActivity;

    public MessageAdapter(List<StoreDetails> storeDetailsList, Context context) {
        this.storeDetailsList = storeDetailsList;
        this.context = context;
        //productsActivity = (ProductsActivity) context;
    }

    @NonNull
    @Override
    public MessageAdapter.StoreDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.store_details_item,parent,false);
        return new MessageAdapter.StoreDetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.StoreDetailsHolder holder, int position) {
        holder.setData(storeDetailsList.get(position));
        holder.constraintLayout.setBackgroundResource(R.drawable.rate_bg);
        holder.txt_title.setTextColor(context.getColor(R.color.white));
        if (selectedItem == position) {
            holder.constraintLayout.setBackgroundResource(R.drawable.add_img_bg);
            holder.txt_title.setTextColor(context.getColor(R.color.purple_500));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousItem = selectedItem;
                selectedItem = position;
                notifyItemChanged(previousItem);
                notifyItemChanged(position);
                //productsActivity.setData(storeDetailsList.get(position));
                //searchActivity.sendData(categoryList.get(position).getId());


            }
        });
    }

    @Override
    public int getItemCount() {
        return storeDetailsList.size();
    }

    class StoreDetailsHolder extends RecyclerView.ViewHolder{
        TextView txt_title;
        ConstraintLayout constraintLayout;
        public StoreDetailsHolder(@NonNull View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.txt_title);
            constraintLayout = itemView.findViewById(R.id.constraint);
        }

        public void setData(StoreDetails storeDetails) {
            txt_title.setText(storeDetails.getName());
        }
    }
}
