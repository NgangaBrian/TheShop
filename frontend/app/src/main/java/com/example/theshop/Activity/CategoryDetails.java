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

import com.example.theshop.Adapter.BestSellerAdapter;
import com.example.theshop.R;
import com.example.theshop.ViewModel.MainViewModel;

public class CategoryDetails extends AppCompatActivity {
    private TextView categoryname, errorMessage;
    public MainViewModel mainViewModel;
    public ProgressBar progressBarSearch;
    public RecyclerView recyclerViewSearch;
    public String userId;
    public ImageView back, errorImage;

    private static final long TIMEOUT_DURATION = 5000;

    private Handler timeoutHandler = new Handler();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("Category_Name");


        categoryname = findViewById(R.id.categoryName);
        progressBarSearch = findViewById(R.id.progressSearch);
        recyclerViewSearch = findViewById(R.id.recyclerSearch);
        back = findViewById(R.id.backBtn);
        errorMessage = findViewById(R.id.errorMessage);
        errorImage = findViewById(R.id.errorImage);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        categoryname.setText(categoryName);

        initSearch(categoryName);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
            BestSellerAdapter adapter = new BestSellerAdapter(items, userId, CategoryDetails.class);
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
                            Toast.makeText(CategoryDetails.this, "The End!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                ((BestSellerAdapter) recyclerViewSearch.getAdapter()).addItems(items, true);
                ((BestSellerAdapter) recyclerViewSearch.getAdapter()).notifyDataSetChanged();
            }
            progressBarSearch.setVisibility(View.GONE);
            errorImage.setVisibility(View.GONE);
            errorMessage.setVisibility(View.GONE);
        });
        mainViewModel.loadSearch(getApplicationContext(), searchTerm, true);
    }
}