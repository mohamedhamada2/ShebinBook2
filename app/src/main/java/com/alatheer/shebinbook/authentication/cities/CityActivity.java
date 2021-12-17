package com.alatheer.shebinbook.authentication.cities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.authentication.signup.SignupActivity;

import java.util.List;

public class CityActivity extends AppCompatActivity {
    RecyclerView city_recycler;
    RecyclerView.LayoutManager layoutManager;
    CityAdapter cityAdapter;
    String title,id;
    String gender_id,city_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        city_recycler = findViewById(R.id.cities_recycler);
        getCities();
    }


    public void sendData(Datum datum) {
        title = datum.getTitle();
        id = datum.getId()+"";
        Intent returnIntent = new Intent();
        returnIntent.putExtra("id",id);
        returnIntent.putExtra("title",title);
        returnIntent.putExtra("flag",2);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
    private void getCities() {
        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<CityModel> cityModelCall = getDataService.get_all_cities("1");
        cityModelCall.enqueue(new Callback<CityModel>() {
            @Override
            public void onResponse(Call<CityModel> call, Response<CityModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()){
                        init_recycler(response.body().getData().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<CityModel> call, Throwable t) {

            }
        });
    }

    private void init_recycler(List<Datum> data) {
        layoutManager = new LinearLayoutManager(this);
        cityAdapter = new CityAdapter(data,this);
        city_recycler.setHasFixedSize(true);
        city_recycler.setAdapter(cityAdapter);
        city_recycler.setLayoutManager(layoutManager);
    }
}