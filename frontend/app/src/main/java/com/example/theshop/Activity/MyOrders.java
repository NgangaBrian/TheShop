package com.example.theshop.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

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
    public  String userId;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        userId = getIntent().getStringExtra("userId");

        recyclerView = findViewById(R.id.ordersRecyclerView);
        ordersAdapter = new OrdersAdapter(new ArrayList<>());
        recyclerView.setAdapter(ordersAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        mainViewModel.getOrders().observe(this, new Observer<List<OrdersModel>>() {
            @Override
            public void onChanged(List<OrdersModel> orders) {
                if (orders != null){
                    Log.d("MyOrdersActivity", "Orders received: " +orders.size());
                    ordersAdapter.setOrdersList(orders);
                } else {
                    Log.d("MyOrdersAxtivity", "Orders are null");
                }
            }
        });

        mainViewModel.loadOrders(this, userId);

    }
}