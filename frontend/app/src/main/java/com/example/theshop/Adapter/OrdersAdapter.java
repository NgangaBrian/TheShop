package com.example.theshop.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
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
        List<OrderedProductsItem> orderedProductsItems = ordersModelItem.getOrderedProducts();
        if(ordersModelItem != null){
            Log.d("OrdersAdapter", "Binding order: " + ordersModelItem.getOrderId());
            holder.orderDatee.setText(ordersModelItem.getOrderDate() != null ? ordersModelItem.getOrderDate() : "N/A");
            holder.amountPaidd.setText(String.valueOf(ordersModelItem.getAmountPaid()) != null ?
                    String.valueOf(ordersModelItem.getAmountPaid()) : "0");
            holder.orderNumberr.setText(String.valueOf(ordersModelItem.getOrderId()) != null ?
                String.valueOf(ordersModelItem.getOrderId()) : "N/A");

        if (orderedProductsItems != null && !orderedProductsItems.isEmpty()){
            ProductsOrdersAdapter productsOrdersAdapter = new ProductsOrdersAdapter(context, orderedProductsItems);
            holder.productsView.setLayoutManager(new LinearLayoutManager(context));
            holder.productsView.setAdapter(productsOrdersAdapter);
        }
        else {
            Log.d("Null OrderedProductsItem", "OrdersProductsItem is null");
        }}
        else {
            Log.d("Null OrdersModelItem", "OrdersModelItem is null");
        }
    }

    @Override
    public int getItemCount() {
        return ordersModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView orderDatee, amountPaidd, orderNumberr;
        private RecyclerView productsView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDatee = itemView.findViewById(R.id.orderDate);
            amountPaidd = itemView.findViewById(R.id.amountPaid);
            orderNumberr = itemView.findViewById(R.id.orderNumber);
            productsView = itemView.findViewById(R.id.productsRecyclerView);

        }
    }
}
