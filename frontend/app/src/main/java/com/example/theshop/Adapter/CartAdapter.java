package com.example.theshop.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.theshop.Helper.ChangeNumberItemsListener;
import com.example.theshop.Helper.ManagementCart;
import com.example.theshop.Model.ItemsModel;
import com.example.theshop.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private ArrayList<ItemsModel> listItemSelected;
    private ManagementCart managementCart;
    private ChangeNumberItemsListener changeNumberItemsListener;

    public CartAdapter(ArrayList<ItemsModel> listItemSelected, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.listItemSelected = listItemSelected;
        this.changeNumberItemsListener = changeNumberItemsListener;
        this.managementCart = new ManagementCart(context);
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ItemsModel item = listItemSelected.get(position);

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

        holder.titleTV.setText(item.getName());
        holder.feeEachItemTV.setText("Kshs " + numberFormat.format(item.getPrice()));
        holder.totalEachItemTV.setText("Kshs " + numberFormat.format(Math.round(item.getNumberInCart() * item.getPrice())));
        holder.numberItemTV.setText(String.valueOf(item.getNumberInCart()));

        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .into(holder.picCart);

        holder.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managementCart.plusItems(listItemSelected, position, new ChangeNumberItemsListener() {
                    @Override
                    public void onChanged() {
                        notifyDataSetChanged();
                        if (changeNumberItemsListener!=null){
                            changeNumberItemsListener.onChanged();
                        }
                    }
                });
            }
        });

        holder.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managementCart.minusItem(listItemSelected, position, new ChangeNumberItemsListener() {
                    @Override
                    public void onChanged() {
                        notifyDataSetChanged();
                        if (changeNumberItemsListener != null){
                            changeNumberItemsListener.onChanged();
                        }

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItemSelected.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTV, feeEachItemTV, totalEachItemTV, numberItemTV, plusBtn, minusBtn;
        public ImageView picCart;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.titleCartTxt);
            feeEachItemTV = itemView.findViewById(R.id.feeEachItem);
            totalEachItemTV = itemView.findViewById(R.id.totalEachItem);
            numberItemTV = itemView.findViewById(R.id.numberItemTxt);
            plusBtn = itemView.findViewById(R.id.plusCartBtn);
            minusBtn = itemView.findViewById(R.id.minusCartBtn);
            picCart = itemView.findViewById(R.id.picCart);
        }
    }
}
