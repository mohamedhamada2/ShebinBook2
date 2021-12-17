package com.alatheer.shebinbook.authentication.cities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alatheer.shebinbook.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityHolder> {
    List<Datum> city_list;
    Context context;
    CityActivity cityActivity;

    public CityAdapter(List<Datum> city_list, Context context) {
        this.city_list = city_list;
        this.context = context;
        cityActivity = (CityActivity) context;
    }

    @NonNull
    @Override
    public CityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.city_item,parent,false);
        return new CityHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  CityAdapter.CityHolder holder, int position) {
        holder.setData(city_list.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityActivity.sendData(city_list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return city_list.size();
    }

    class CityHolder extends RecyclerView.ViewHolder{
        TextView txt_city_name;
        public CityHolder(@NonNull  View itemView) {
            super(itemView);
            txt_city_name = itemView.findViewById(R.id.txt_city_name);
        }

        public void setData(Datum datum) {
            txt_city_name.setText(datum.getTitle());
        }
    }
}
