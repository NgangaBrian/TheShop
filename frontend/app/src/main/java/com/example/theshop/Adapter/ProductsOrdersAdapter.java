package com.example.theshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.theshop.Model.OrderedProductsItem;
import com.example.theshop.R;

import java.util.List;

public class ProductsOrdersAdapter extends RecyclerView.Adapter<ProductsOrdersAdapter.ViewHolder> {
    private Context context;
    private List<OrderedProductsItem> productList;

    public ProductsOrdersAdapter(Context context, List<OrderedProductsItem> productList){
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductsOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsOrdersAdapter.ViewHolder holder, int position) {
        OrderedProductsItem product = productList.get(position);
        holder.itemName.setText(product.getProductName() != null ? product.getProductName() : "N/A");
        holder.quantity.setText(product.getQuantity() != null ? String.valueOf(product.getQuantity()) : "0");

        String imageUrl = product.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()){
            Glide.with(context).load(imageUrl).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName, quantity;
        public ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            quantity = itemView.findViewById(R.id.itemQuantity);
            imageView = itemView.findViewById(R.id.productImage);
        }
    }
}
