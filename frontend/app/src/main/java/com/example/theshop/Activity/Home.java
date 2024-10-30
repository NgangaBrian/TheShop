package com.example.theshop.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
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
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.theshop.Adapter.BestSellerAdapter;
import com.example.theshop.Adapter.CategoriesAdapter;
import com.example.theshop.Adapter.SliderAdapter;
import com.example.theshop.Helper.ManagementCart;
import com.example.theshop.Model.SliderModel;
import com.example.theshop.R;
import com.example.theshop.ViewModel.MainViewModel;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.List;

public class Home extends AppCompatActivity {

    private ViewPager2 viewPager21;
    private MainViewModel mainViewModel;
    private SliderAdapter sliderAdapter;
    private DotsIndicator dotsIndicator;
    private LinearLayout cartBtn;
    private ProgressBar progressBarBanner, progressBarCategories, progressBarBestSeller;
    private TextView name;
    private RecyclerView recyclerViewCategories, recyclerViewBestSeller;
    public String userId;

    public int currentPage = 0;
    public int  pageSize = 10;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        String fullname = intent.getStringExtra("fullname");
        String email = intent.getStringExtra("email");
        userId = intent.getStringExtra("userId");

        viewPager21 = findViewById(R.id.viewPager2);
        dotsIndicator = findViewById(R.id.dotIndicatorBanner);
        progressBarBanner = findViewById(R.id.progressBarBanner);
        progressBarCategories = findViewById(R.id.progressBarCategory);
        progressBarBestSeller = findViewById(R.id.progressBestSell);
        recyclerViewCategories = findViewById(R.id.recyclerViewCategory);
        recyclerViewBestSeller = findViewById(R.id.recyclerBestSelling);
        name = findViewById(R.id.nametv);
        cartBtn = findViewById(R.id.cartBtn);
        name.setText(fullname);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        initBanners();
        initCategory();
        initBestSeller();

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManagementCart managementCart = new ManagementCart(Home.this);
                if (!managementCart.getListCart().isEmpty()){
                    Intent intent = new Intent(Home.this, CartActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                } else {
                    Toast.makeText(Home.this, "Your Cart is Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        View decor = window.getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void initBestSeller() {
        progressBarBestSeller.setVisibility(View.VISIBLE);
        mainViewModel.getBestSeller().observe(this, items ->{
            BestSellerAdapter adapter = new BestSellerAdapter(items, userId);
            if(recyclerViewBestSeller.getAdapter() == null) {

                recyclerViewBestSeller.setLayoutManager(new GridLayoutManager(this, 2));
                recyclerViewBestSeller.setAdapter(adapter);

                recyclerViewBestSeller.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == adapter.getItemCount() - 1) {
                            currentPage++;
                            mainViewModel.loadBestSeller(getApplicationContext(), currentPage, pageSize, true);
                            Toast.makeText(Home.this, "The End!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                ((BestSellerAdapter) recyclerViewBestSeller.getAdapter()).addItems(items, true);
                ((BestSellerAdapter) recyclerViewBestSeller.getAdapter()).notifyDataSetChanged();
            }
            progressBarBestSeller.setVisibility(View.GONE);
        });
        mainViewModel.loadBestSeller(getApplicationContext(), currentPage, pageSize, true);
    }

    private void initCategory() {
        progressBarCategories.setVisibility(View.VISIBLE);
        mainViewModel.getCategory().observe(this, items -> {
            recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.HORIZONTAL, false));
            recyclerViewCategories.setAdapter(new CategoriesAdapter(items));
            progressBarCategories.setVisibility(View.GONE);
        });
        mainViewModel.loadCategory(getApplicationContext());
    }

    private void initBanners(){
        progressBarBanner.setVisibility(View.VISIBLE);
        mainViewModel.getSlider().observe(this, banners -> {
            updateSlider(banners);
            progressBarBanner.setVisibility(View.GONE);
        });
        mainViewModel.loadSlider(getApplicationContext());
    }

    private void updateSlider(List<SliderModel> images) {
        sliderAdapter = new SliderAdapter(images, viewPager21);
        viewPager21.setAdapter(sliderAdapter);
        viewPager21.setClipChildren(false);
        viewPager21.setClipToPadding(false);
        viewPager21.setOffscreenPageLimit(3);
        viewPager21.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        viewPager21.setPageTransformer(transformer);

        if(images.size() > 1){
            dotsIndicator.setVisibility(View.VISIBLE);
            dotsIndicator.attachTo(viewPager21);
        }

    }
}