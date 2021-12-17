package com.alatheer.shebinbook.products.offers;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    RecyclerView.LayoutManager layoutManager;
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
        offersViewModel.getOffers(trader_id);
        return v;
    }

    public void init_recycler(List<Slider> data) {
        offersAdapter = new OffersAdapter(getActivity(),data,store_name,store_logo,store_id);
        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,true);
        fragmentOffersBinding.offerRecycler.setAdapter(offersAdapter);
        fragmentOffersBinding.offerRecycler.setLayoutManager(layoutManager);
        fragmentOffersBinding.offerRecycler.setHasFixedSize(true);
    }
}