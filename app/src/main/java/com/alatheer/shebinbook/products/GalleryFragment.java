package com.alatheer.shebinbook.products;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.databinding.FragmentGalleryBinding;

import java.util.List;


public class GalleryFragment extends Fragment {
    FragmentGalleryBinding fragmentGalleryBinding;
    GalleryViewModel galleryViewModel;
    String store_id,store_name,store_image;
    GalleryAdapter galleryAdapter ;
    RecyclerView.LayoutManager layoutManager;
    Integer trader_id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentGalleryBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_gallery, container, false);
        View view = fragmentGalleryBinding.getRoot();
        galleryViewModel = new GalleryViewModel(getActivity(),this);
        fragmentGalleryBinding.setGalleryviewmodel(galleryViewModel);
        store_id = getArguments().getString("store_id");
        store_name = getArguments().getString("store_name");
        store_image = getArguments().getString("store_image");
        trader_id = getArguments().getInt("trader_id");
        galleryViewModel.get_galleries(store_id);
        return view;
    }

    public void init_recycler(List<Gallery> data) {
        fragmentGalleryBinding.productsRecycler.setHasFixedSize(true);
        galleryAdapter = new GalleryAdapter(data,getActivity(),this,store_name,store_image);
        layoutManager = new LinearLayoutManager(getActivity(),GridLayoutManager.VERTICAL,true);
        fragmentGalleryBinding.productsRecycler.setAdapter(galleryAdapter);
        fragmentGalleryBinding.productsRecycler.setLayoutManager(layoutManager);
        fragmentGalleryBinding.productsRecycler.setHasFixedSize(true);
    }
}