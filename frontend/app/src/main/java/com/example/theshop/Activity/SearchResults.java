package com.example.theshop.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.theshop.Helper.ManagementCart;
import com.example.theshop.R;

public class SearchResults extends AppCompatActivity {
    private TextView keywordTV, back;
    private ImageView cartBtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        keywordTV = findViewById(R.id.keywordTxt);
        back = findViewById(R.id.backBtn);
        cartBtn = findViewById(R.id.toCart);

        String keyword = getIntent().getStringExtra("keyword");
        String userId = getIntent().getStringExtra("userId");
        keywordTV.setText(keyword);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManagementCart managementCart = new ManagementCart(SearchResults.this);
                if (!managementCart.getListCart().isEmpty()){
                    Intent intent = new Intent(SearchResults.this, CartActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                } else {
                    Toast.makeText(SearchResults.this, "Your Cart is Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}