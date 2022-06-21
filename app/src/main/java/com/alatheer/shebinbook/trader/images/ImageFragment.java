package com.alatheer.shebinbook.trader.images;

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

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.allproducts.AllProductsActivity;
import com.alatheer.shebinbook.databinding.FragmentImage2Binding;
import com.alatheer.shebinbook.trader.images.addimages.AddImagesActivity;

import java.util.ArrayList;
import java.util.List;

public class ImageFragment extends Fragment {
    FragmentImage2Binding fragmentImage2Binding;
    ImagesViewModel imagesViewModel;
    ImageAdapter imageAdapter;
    GridLayoutManager gridLayoutManager;
    String trader_id,store_id;
    List<Integer> gallery_id_list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentImage2Binding = DataBindingUtil.inflate(inflater,R.layout.fragment_image2, container, false);
        imagesViewModel = new ImagesViewModel(getActivity(),this);
        trader_id = getArguments().getString("trader_id");
        store_id = getArguments().getString("store_id");
        imagesViewModel.get_gallery(trader_id);
        View view = fragmentImage2Binding.getRoot();
        gallery_id_list = new ArrayList<>();
        fragmentImage2Binding.btnAddAlboum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddImagesActivity.class);
                intent.putExtra("store_id",store_id);
                startActivity(intent);
            }
        });
        fragmentImage2Binding.btnDeleteFromAlboum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!gallery_id_list.isEmpty()){
                    imagesViewModel.delete_images(gallery_id_list,trader_id);
                }else {
                    Toast.makeText(getActivity(), "قم بتحديد الصور التي تريد حذفها", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    public void init_recycler(List<Image> data) {
        imageAdapter = new ImageAdapter(data,getActivity(),this);
        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        fragmentImage2Binding.imagesRecycler.setLayoutManager(gridLayoutManager);
        fragmentImage2Binding.imagesRecycler.setAdapter(imageAdapter);
        fragmentImage2Binding.imagesRecycler.setHasFixedSize(true);
    }

    public void delete_img(Integer id) {
        //imagesViewModel.delete(id);
        CreateDeleteDialog(id);
    }

    private void CreateDeleteDialog(Integer id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.delete_dialog, null);
        Button btn_delete = view.findViewById(R.id.btn_delete);
        Button btn_skip = view.findViewById(R.id.btn_skip);
        TextView txt = view.findViewById(R.id.txt);
        txt.setText("هل تريد حذف الصورة من المعرض");
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
                imagesViewModel.delete(id,trader_id);
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

    public void get_images_id(List<Integer> gallery_id_list) {
        this.gallery_id_list = gallery_id_list;
    }
}