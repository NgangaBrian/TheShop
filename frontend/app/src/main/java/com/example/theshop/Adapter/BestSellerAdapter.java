package com.example.theshop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.theshop.Activity.DetailActivity;
import com.example.theshop.Model.ItemsModel;
import com.example.theshop.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class BestSellerAdapter extends RecyclerView.Adapter<BestSellerAdapter.ViewHolder> {
    private List<ItemsModel> items;
    private Context context;

    public BestSellerAdapter(List<ItemsModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public BestSellerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_best_seller, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BestSellerAdapter.ViewHolder holder, int position) {
        ItemsModel item = items.get(position);
        holder.titleTV.setText(item.getName());
        holder.priceTV.setText(String.valueOf(item.getPrice()));

        Glide.with(context)
                .load(item.getImageUrl())
                .apply(new RequestOptions().transform(new CenterCrop()))
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("object", item);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return items.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ShapeableImageView imageView;
        public TextView titleTV, priceTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.picBestSeller);
            titleTV = itemView.findViewById(R.id.titleTv);
            priceTV = itemView.findViewById(R.id.priceTV);
        }

    }
}
