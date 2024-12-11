package com.example.theshop.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theshop.Adapter.OrdersAdapter;
import com.example.theshop.Helper.ManagementCart;
import com.example.theshop.Model.OrdersModel;
import com.example.theshop.Model.OrdersModelItem;
import com.example.theshop.R;
import com.example.theshop.ViewModel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyOrders extends AppCompatActivity {
    private MainViewModel mainViewModel;
    private OrdersAdapter ordersAdapter;
    private RecyclerView recyclerView;
    private ManagementCart managementCart;
    public  String userId;
    public TextView back;
    public ImageView cartBtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        userId = getIntent().getStringExtra("userId");

        back = findViewById(R.id.backBtn);
        cartBtn = findViewById(R.id.toCart);
        recyclerView = findViewById(R.id.ordersRecyclerView);
        ordersAdapter = new OrdersAdapter(new ArrayList<>());
        recyclerView.setAdapter(ordersAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        managementCart = new ManagementCart(this);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        mainViewModel.getOrders().observe(this, new Observer<List<OrdersModelItem>>() {
            @Override
            public void onChanged(List<OrdersModelItem> orders) {
                if (orders != null){
                    Log.d("MyOrdersActivity", "Orders received: " +orders.size());
                    ordersAdapter.setOrdersList(orders);
                } else {
                    Log.d("MyOrdersAxtivity", "Orders are null");
                }
            }
        });

        mainViewModel.loadOrders(this, userId);

        // TODO: 11/25/24 Work on orders, if a customer orders multiple items; output the items separately but all containing the same order info

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!managementCart.getListCart().isEmpty()) {
                    Intent intent = new Intent(MyOrders.this, CartActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                } else {
                    Toast.makeText(MyOrders.this, "Your Cart is Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}