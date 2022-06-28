package com.alatheer.shebinbook.products.offers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.databinding.FragmentOffersBinding;
import com.alatheer.shebinbook.home.slider.Slider;
import com.alatheer.shebinbook.home.slider.SliderAdapter;

import java.util.List;


public class OffersFragment extends Fragment {
    FragmentOffersBinding fragmentOffersBinding;
    OffersViewModel offersViewModel;
    String trader_id,store_name,store_logo,store_id;
    OffersAdapter offersAdapter;
    LinearLayoutManager layoutManager;
    private int pastvisibleitem,visibleitemcount,totalitemcount,previous_total=0;
    int view_threshold = 10;
    int page =1;
    private boolean isloading;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentOffersBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_offers, container,false);
        View v = fragmentOffersBinding.getRoot();
        offersViewModel = new OffersViewModel(getActivity(),this);
        fragmentOffersBinding.setOffersviewmodel(offersViewModel);
        trader_id = getArguments().getString("trader_id");
        store_name = getArguments().getString("store_name");
        store_logo = getArguments().getString("store_logo");
        store_id = getArguments().getString("store_id");
        offersViewModel.getOffers(trader_id,1,store_name,store_logo,store_id);
        fragmentOffersBinding.offerRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleitemcount = layoutManager.getChildCount();
                totalitemcount = layoutManager.getItemCount();
                pastvisibleitem = layoutManager.findFirstVisibleItemPosition();
                if(dy<0){
                    if(isloading){
                        if(totalitemcount>previous_total){
                            isloading = false;
                            previous_total = totalitemcount;
                        }
                    }
                    if(!isloading &&(totalitemcount-visibleitemcount)<= pastvisibleitem+view_threshold){
                        page++;
                        offersViewModel.PerformPagination(page,trader_id);
                        isloading = true;
                    }
                }else {
                    page++;
                    offersViewModel.PerformPagination(page,trader_id);
                    //Toast.makeText(getActivity(), "offer", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

    public void init_recycler(OffersAdapter offersAdapter) {
        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,true);
        fragmentOffersBinding.offerRecycler.setAdapter(offersAdapter);
        fragmentOffersBinding.offerRecycler.setLayoutManager(layoutManager);
        fragmentOffersBinding.offerRecycler.setHasFixedSize(true);
    }
}