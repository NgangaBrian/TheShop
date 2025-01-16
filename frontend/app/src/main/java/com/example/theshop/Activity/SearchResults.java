package com.example.theshop.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.theshop.Adapter.BestSellerAdapter;
import com.example.theshop.Helper.ManagementCart;
import com.example.theshop.R;
import com.example.theshop.ViewModel.MainViewModel;

public class SearchResults extends AppCompatActivity {
    private TextView keywordTV, back, errorMessage;
    private ImageView cartBtn, errorImage;
    public MainViewModel mainViewModel;
    public ProgressBar progressBarSearch;
    public RecyclerView recyclerViewSearch;
    public String userId;

    private static final long TIMEOUT_DURATION = 5000;

    private Handler timeoutHandler = new Handler();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        keywordTV = findViewById(R.id.keywordTxt);
        back = findViewById(R.id.backBtn);
        cartBtn = findViewById(R.id.toCart);
        progressBarSearch = findViewById(R.id.progressSearch);
        recyclerViewSearch = findViewById(R.id.recyclerSearch);
        errorMessage = findViewById(R.id.errorMessage);
        errorImage = findViewById(R.id.errorImage);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        String keyword = getIntent().getStringExtra("keyword");
        userId = getIntent().getStringExtra("userId");
        keywordTV.setText(keyword);

        initSearch(keyword);


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

    public void initSearch(String searchTerm) {
        progressBarSearch.setVisibility(View.VISIBLE);

        // Start a timeout task to show error after TIMEOUT_DURATION
        timeoutHandler.postDelayed(() -> {
            if (progressBarSearch.getVisibility() == View.VISIBLE){
                progressBarSearch.setVisibility(View.GONE);
                errorImage.setVisibility(View.VISIBLE);
                errorMessage.setVisibility(View.VISIBLE);
            }
        }, TIMEOUT_DURATION);

        mainViewModel.getSearch().observe(this, items ->{
            BestSellerAdapter adapter = new BestSellerAdapter(items, userId, SearchResults.class);
            if(recyclerViewSearch.getAdapter() == null) {

                recyclerViewSearch.setLayoutManager(new GridLayoutManager(this, 2));
                recyclerViewSearch.setAdapter(adapter);

                recyclerViewSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == adapter.getItemCount() - 1) {
                            mainViewModel.loadSearch(getApplicationContext(), searchTerm, true);
                            Toast.makeText(SearchResults.this, "The End!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                ((BestSellerAdapter) recyclerViewSearch.getAdapter()).addItems(items, true);
                ((BestSellerAdapter) recyclerViewSearch.getAdapter()).notifyDataSetChanged();
            }
            progressBarSearch.setVisibility(View.GONE);
        });
        mainViewModel.loadSearch(getApplicationContext(), searchTerm, true);
    }
}