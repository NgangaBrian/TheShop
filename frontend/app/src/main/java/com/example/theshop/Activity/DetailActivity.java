package com.example.theshop.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.theshop.Adapter.PicListAdapter;
import com.example.theshop.Helper.ManagementCart;
import com.example.theshop.Model.ItemsModel;
import com.example.theshop.R;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private ItemsModel item;
    private  int numberOrder=1;
    private ManagementCart managementCart;
    public RecyclerView picListt;
    public ImageView picMainn, back, callSeller, msgSeller;
    public TextView titleTV, priceTV, ratingTV, descriptionTV, addToCart;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        titleTV = findViewById(R.id.titleTxt);
        priceTV = findViewById(R.id.priceTxt);
        ratingTV = findViewById(R.id.ratingTxt);
        descriptionTV = findViewById(R.id.descriptionTxt);
        addToCart = findViewById(R.id.addToCartBtn);
        back = findViewById(R.id.backBtn);
        callSeller = findViewById(R.id.callToSellerBtn);
        msgSeller = findViewById(R.id.msgToSellerBtn);
        picMainn = findViewById(R.id.picMain);
        picListt = findViewById(R.id.picList);

        managementCart = new ManagementCart(this);

        getBundleExtra();
        initList();

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setNumberInCart(numberOrder);
                managementCart.inserItems(item);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // If there is a seller telephone number in the database
//        msgSeller.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//                sendIntent.setData(Uri.Parse("sms:" + item.getSelerTell()));
//                sendIntent.putExtra("sms_body", "type your message");
//                startActivity(sendIntent);
//            }
//        });
//
//        callSeller.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String phone = String.valueOf(item.getSellerNumber());
//                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tell", phone, null));
//                startActivity(intent);
//            }
//        });
    }

    private void initList() {
        ArrayList<String> picList = new ArrayList<>();
        picList.add(item.getImageUrl());

        RequestOptions requestOptions = new RequestOptions().transform(new CenterCrop());
        Glide.with(this)
                .load(picList.get(0))
                .into(picMainn);

        picListt.setAdapter(new PicListAdapter(picList, picMainn));
        picListt.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @SuppressLint("SetTextI18n")
    private void getBundleExtra() {
        item = (ItemsModel) getIntent().getSerializableExtra("object");

        titleTV.setText(item.getName());
        priceTV.setText("Ksh " + String.valueOf(item.getPrice()));
        descriptionTV.setText(item.getDescription());
    }
}