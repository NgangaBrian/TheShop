package com.example.theshop.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.theshop.R;

import org.json.JSONObject;

public class Profile extends AppCompatActivity {

    public ImageView backBtn, profileImage;
    public TextView changePword, namee, emaill, mobileNumber, addresss, postalcode;
    public Button editProfile;
    public String profileUrl, phoneNo, address, postalCode;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        String fullname = intent.getStringExtra("fullname");
        String email = intent.getStringExtra("email");
        String userId = intent.getStringExtra("userId");

        backBtn = findViewById(R.id.backBtnn);
        profileImage = findViewById(R.id.profilePic);
        changePword = findViewById(R.id.changepw);
        editProfile = findViewById(R.id.editProfile);
        namee = findViewById(R.id.name);
        emaill = findViewById(R.id.email);
        mobileNumber = findViewById(R.id.mobileNo);
        addresss = findViewById(R.id.address);
        postalcode = findViewById(R.id.postalCode);


        namee.setText(fullname);
        emaill.setText(email);

        loadProfileDetails(userId);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, EditProfile.class);
                intent.putExtra("userId", userId);
                intent.putExtra("profileUrl", profileUrl);
                intent.putExtra("phoneNo", phoneNo);
                intent.putExtra("address", address);
                intent.putExtra("postalCode", postalCode);
                startActivity(intent);
                finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        changePword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, ChangePassword.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            }
        });

    }

    private void loadProfileDetails(String userId) {
        RequestQueue queue = Volley.newRequestQueue(Profile.this);

        String url = "http://192.168.43.233:8080/api/v1/getuserdetails/"+userId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    profileUrl = response.getString("profile_url");
                    phoneNo = response.getString("mobile_number");
                    address = response.getString("address");
                    postalCode = response.getString("postal_code");

                    mobileNumber.setText(phoneNo);
                    addresss.setText(address);
                    postalcode.setText(postalCode);

                    Glide.with(Profile.this).load(profileUrl).into(profileImage);


                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("VolleyError", "Error loading orders", error);

            }
        });
        queue.add(jsonObjectRequest);
    }


}