package com.alatheer.shebinbook.trader.profile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        /*fragmentGalleryBinding.swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fragmentGalleryBinding.swiperefreshlayout.post(new Runnable() {
                    @Override
                    public void run() {
                        galleryViewModel.get_galleries(store_id);
                        fragmentGalleryBinding.swiperefreshlayout.setRefreshing(true);
                    }
                });
            }
        });*/
        /*fragmentGalleryBinding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startRefresh();
            }
        });*/
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

    public void setDeleteAlertDialog(Gallery gallery) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.delete_dialog, null);
        Button btn_delete = view.findViewById(R.id.btn_delete);
        Button btn_skip = view.findViewById(R.id.btn_skip);
        TextView txt = view.findViewById(R.id.txt);
        txt.setText("هل تريد حذف ألبوم");
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
                galleryViewModel.delete_alboum(gallery);
                dialog4.dismiss();
                //CreateBasketDialog(product);
            }
        });
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog4.dismiss();
                //CreateDeleteDialog(product);
            }
        });
    }

}