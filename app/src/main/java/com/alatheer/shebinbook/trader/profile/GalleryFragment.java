package com.alatheer.shebinbook.trader.profile;

import android.content.Intent;
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
import com.alatheer.shebinbook.databinding.FragmentGallery2Binding;
import com.alatheer.shebinbook.databinding.FragmentGalleryBinding;
import com.alatheer.shebinbook.products.Gallery;
import com.alatheer.shebinbook.trader.addalboum.AddAlboumActivity;

import java.util.List;

public class GalleryFragment extends Fragment {
    FragmentGallery2Binding fragmentGalleryBinding;
    com.alatheer.shebinbook.trader.profile.GalleryViewModel galleryViewModel;
    String store_id,store_name,store_image;
    com.alatheer.shebinbook.trader.profile.GalleryAdapter galleryAdapter;
    RecyclerView.LayoutManager layoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentGalleryBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_gallery2,container,false);
        View view = fragmentGalleryBinding.getRoot();
        galleryViewModel = new GalleryViewModel(getActivity(),this);
        fragmentGalleryBinding.setTradergalleryviewmodel(galleryViewModel);
        store_id = getArguments().getString("store_id");
        store_name = getArguments().getString("store_name");
        store_image = getArguments().getString("store_image");
        galleryViewModel.get_galleries(store_id);
        fragmentGalleryBinding.btnAddAlboum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddAlboumActivity.class);
                intent.putExtra("store_id",store_id);
                startActivity(intent);
            }
        });
        return view;
    }

    public void init_recycler(List<Gallery> data) {
        fragmentGalleryBinding.productsRecycler.setHasFixedSize(true);
        galleryAdapter = new GalleryAdapter(data,getActivity(),this,store_name,store_image);
        layoutManager = new LinearLayoutManager(getActivity(), GridLayoutManager.VERTICAL,true);
        fragmentGalleryBinding.productsRecycler.setAdapter(galleryAdapter);
        fragmentGalleryBinding.productsRecycler.setLayoutManager(layoutManager);
        fragmentGalleryBinding.productsRecycler.setHasFixedSize(true);
    }
}