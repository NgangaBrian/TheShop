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
    private List<OrdersModel> ordersModelList;
    private Context context;

    public OrdersAdapter(List<OrdersModel> ordersModelList) {
        this.ordersModelList = ordersModelList;
    }

    public void setOrdersList(List<OrdersModel> ordersModelList){
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
        OrdersModelItem ordersModelItem = ordersModelList.get(position).getOrdersModel().get(0);
        Log.d("OrdersAdapter", "Binding order: " + ordersModelItem.getOrderId());
        holder.orderDatee.setText(ordersModelItem.getOrderDate());
        holder.amountPaidd.setText(String.valueOf(ordersModelItem.getAmountPaid()));
        holder.orderNumberr.setText(ordersModelItem.getOrderId());

        List<OrderedProductsItem> orderedProductsItems = ordersModelItem.getOrderedProducts();
        OrderedProductsItem firstProduct = orderedProductsItems.get(0);
        holder.itemNamee.setText(firstProduct.getProductName());
        holder.quantityy.setText(firstProduct.getQuantity());

        Glide.with(context)
                .load(firstProduct.getImageUrl())
                .into(holder.imageVieww);

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
