package com.example.theshop.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theshop.Adapter.CartAdapter;
import com.example.theshop.Helper.ChangeNumberItemsListener;
import com.example.theshop.Helper.ManagementCart;
import com.example.theshop.R;

import java.text.NumberFormat;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private ManagementCart managementCart;
    private double tax = 0.0;
    public ImageView backBtn;
    public RecyclerView cartView;
    public TextView totalFeeTV, totalTV, taxTV, deliveryFeeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        
        managementCart = new ManagementCart(this);
        backBtn = findViewById(R.id.backButton);
        totalFeeTV = findViewById(R.id.totalFeeTxt);
        totalTV = findViewById(R.id.totalTxt);
        taxTV = findViewById(R.id.taxTxt);
        deliveryFeeTV = findViewById(R.id.deliveryTxt);
        cartView = findViewById(R.id.cartView);
        
        setVariable();
        initCartList();
        calculatorCart();
        
    }

    private void initCartList() {
        cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        cartView.setAdapter(new CartAdapter(managementCart.getListCart(), this, new ChangeNumberItemsListener() {
            @Override
            public void onChanged() {
                calculatorCart();
            }
        }));
    }

    private void calculatorCart(){
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

        double percentTax = 0.002;
        double delivery = 15.0;
        tax = Math.round(managementCart.getTotalFee() * percentTax * 100) / 100.0;
        double total = Math.round((managementCart.getTotalFee() + tax + delivery) * 100) / 100.0;
        double itemTotal = Math.round(managementCart.getTotalFee() * 100) / 100.0;

        totalFeeTV.setText("Ksh " + numberFormat.format(itemTotal));
        taxTV.setText("Kshs " + numberFormat.format(tax));
        deliveryFeeTV.setText("Kshs " + numberFormat.format(delivery));
        totalTV.setText("Kshs " + numberFormat.format(total));
    }

    private void setVariable() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}