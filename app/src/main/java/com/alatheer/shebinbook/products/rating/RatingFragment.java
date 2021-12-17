package com.alatheer.shebinbook.products.rating;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.databinding.FragmentRatingBinding;

import java.util.List;


public class RatingFragment extends Fragment {
    FragmentRatingBinding fragmentRatingBinding;
    RatingViewModel ratingViewModel;
    String store_id,trader_id;
    RecyclerView.LayoutManager layoutManager;
    RatingAdapter ratingAdapter;

    public  void delete_rating(Integer id) {
        ratingViewModel.delete_rating(id,store_id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentRatingBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_rating, container, false);
        View view = fragmentRatingBinding.getRoot();
        ratingViewModel = new RatingViewModel(getActivity(),this);
        fragmentRatingBinding.setRatingviewmodel(ratingViewModel);
        store_id = getArguments().getString("store_id");
        trader_id = getArguments().getString("trader_id");
        ratingViewModel.getRates(store_id);
        return view;
    }

    public void init_rating_recycler(List<Rating> data) {
        fragmentRatingBinding.ratingRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,true);
        fragmentRatingBinding.ratingRecycler.setLayoutManager(layoutManager);
        ratingAdapter = new RatingAdapter(data,getActivity(),this);
        fragmentRatingBinding.ratingRecycler.setAdapter(ratingAdapter);
    }
}