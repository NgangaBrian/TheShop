package com.example.theshop.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.theshop.Adapter.SliderAdapter;
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
    private ProgressBar progressBar;
    private TextView name;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        String fullname = intent.getStringExtra("fullname");
        String email = intent.getStringExtra("email");

        viewPager21 = findViewById(R.id.viewPager2);
        dotsIndicator = findViewById(R.id.dotIndicatorBanner);
        progressBar = findViewById(R.id.progressBarBanner);
        name = findViewById(R.id.nametv);
        name.setText(fullname);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        initBanners();

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        View decor = window.getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void initBanners(){
        progressBar.setVisibility(View.VISIBLE);
        mainViewModel.getSlider().observe(this, banners -> {
            updateSlider(banners);
            progressBar.setVisibility(View.GONE);
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