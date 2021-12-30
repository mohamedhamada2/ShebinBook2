package com.alatheer.shebinbook.products.images;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.databinding.FragmentImageBinding;
import com.alatheer.shebinbook.trader.images.Image;

import java.util.List;


public class ImageFragment extends Fragment {
    FragmentImageBinding fragmentImageBinding;
    ImageViewModel imageViewModel;
    String trader_id,store_id;
    ImageAdapter imageAdapter;
    GridLayoutManager gridLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentImageBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_image, container, false);
        imageViewModel = new ImageViewModel(getActivity(),this);
        fragmentImageBinding.setImagesviewmodel(imageViewModel);
        View view = fragmentImageBinding.getRoot();
        trader_id = getArguments().getString("trader_id");
        store_id = getArguments().getString("store_id");
        imageViewModel.get_gallery(trader_id);
        return  view;
    }

    public void init_recycler(List<Image> data) {
        try{
            imageAdapter = new com.alatheer.shebinbook.products.images.ImageAdapter(data,getActivity(),this);
            gridLayoutManager = new GridLayoutManager(getActivity(),2);
            fragmentImageBinding.imagesRecycler.setHasFixedSize(true);
            fragmentImageBinding.imagesRecycler.setAdapter(imageAdapter);
            fragmentImageBinding.imagesRecycler.setLayoutManager(gridLayoutManager);
        }catch (Exception e){
            Log.e("error_msg",e.getMessage());
        }
    }
}