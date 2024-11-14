package com.example.theshop.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.theshop.Model.OrderedProductsItem;
import com.example.theshop.Model.OrdersModel;
import com.example.theshop.Model.OrdersModelItem;
import com.example.theshop.R;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    private List<OrdersModelItem> ordersModelList;
    private Context context;

    public OrdersAdapter(List<OrdersModelItem> ordersModelList) {
        this.ordersModelList = ordersModelList;
    }

    public void setOrdersList(List<OrdersModelItem> ordersModelList){
        this.ordersModelList=ordersModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_my_orders, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.ViewHolder holder, int position) {
        OrdersModelItem ordersModelItem = ordersModelList.get(position);
        if(ordersModelItem != null){
        Log.d("OrdersAdapter", "Binding order: " + ordersModelItem.getOrderId());
        holder.orderDatee.setText(ordersModelItem.getOrderDate() != null ? ordersModelItem.getOrderDate() : "N/A");
            holder.amountPaidd.setText(String.valueOf(ordersModelItem.getAmountPaid()) != null ?
                    String.valueOf(ordersModelItem.getAmountPaid()) : "0");
        holder.orderNumberr.setText(String.valueOf(ordersModelItem.getOrderId()) != null ?
                String.valueOf(ordersModelItem.getOrderId()) : "N/A");

        List<OrderedProductsItem> orderedProductsItems = ordersModelItem.getOrderedProducts();
        if (orderedProductsItems != null && !orderedProductsItems.isEmpty()){
        OrderedProductsItem firstProduct = orderedProductsItems.get(0);
        holder.itemNamee.setText(firstProduct.getProductName() != null ? firstProduct.getProductName() : "N/A");
        holder.quantityy.setText(firstProduct.getQuantity() != null ?
                String.valueOf(firstProduct.getQuantity()) : "0");

            String imageUrl = firstProduct.getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(context).load(imageUrl).into(holder.imageVieww);
            } else {
                 // Use a placeholder if the URL is null
                Log.d("Null Image Url", "Image Url is null");
            }

        } else {
            Log.d("Null OrderedProductsItem", "OrdersProductsItem is null");
        }
        } else {
            Log.d("Null OrdersModelItem", "OrdersModelItem is null");
        }
    }

    @Override
    public int getItemCount() {
        return ordersModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageVieww;
        private TextView itemNamee, orderDatee, quantityy, amountPaidd, orderNumberr;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageVieww = itemView.findViewById(R.id.orderImage);
            itemNamee = itemView.findViewById(R.id.itemName);
            orderDatee = itemView.findViewById(R.id.orderDate);
            quantityy = itemView.findViewById(R.id.quantity);
            amountPaidd = itemView.findViewById(R.id.amountPaid);
            orderNumberr = itemView.findViewById(R.id.orderNumber);

        }
    }
}
