package com.example.theshop.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.theshop.Adapter.CartAdapter;
import com.example.theshop.Helper.ChangeNumberItemsListener;
import com.example.theshop.Helper.ManagementCart;
import com.example.theshop.Model.ItemsModel;
import com.example.theshop.R;

import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private static final org.apache.commons.logging.Log log = LogFactory.getLog(CartActivity.class);
    private ManagementCart managementCart;
    private double tax = 0.0;
    public ImageView backBtn;
    public RecyclerView cartView;
    public TextView totalFeeTV, totalTV, taxTV, deliveryFeeTV;
    public Button checkOut;
    public ProgressDialog mProgressDialog;
    public PopupWindow popupWindow;
    public String userId;
    public Long orderId;
    public String phoneNo, amountt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        userId = getIntent().getStringExtra("userId");
        
        managementCart = new ManagementCart(this);
        backBtn = findViewById(R.id.backButton);
        totalFeeTV = findViewById(R.id.totalFeeTxt);
        totalTV = findViewById(R.id.totalTxt);
        taxTV = findViewById(R.id.taxTxt);
        deliveryFeeTV = findViewById(R.id.deliveryTxt);
        cartView = findViewById(R.id.cartView);
        checkOut = findViewById(R.id.checkOutBtn);


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Prossessing your payment");
        mProgressDialog.setMessage("Please wait...");

        
        setVariable();
        initCartList();
        calculatorCart();

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = totalTV.getText().toString();
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.viewholder_popup_window, null);

                popupWindow = new PopupWindow(popupView, 500, ViewGroup.LayoutParams.WRAP_CONTENT, true);

                popupWindow.setBackgroundDrawable(getDrawable(R.drawable.grey_bg_popup));
                popupWindow.setOutsideTouchable(true);
                popupWindow.setTouchable(true);

                popupWindow.showAtLocation(v, Gravity.CENTER, 0, -50);

                View backgroundView = new View(CartActivity.this);
                backgroundView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                backgroundView.setBackgroundColor(Color.parseColor("#90000000"));

                backgroundView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(popupWindow.isShowing()){
                            popupWindow.dismiss();
                            return true;
                        }
                        return false;
                    }
                });

                Button cancelBtn = popupView.findViewById(R.id.cancelBtn);
                Button confirmBtn = popupView.findViewById(R.id.confirmBtn);
                EditText phoneNumber = popupView.findViewById(R.id.phoneTxt);
                TextView amountTv = popupView.findViewById(R.id.amountTxt);

                amountTv.setText(amount);

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                confirmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        phoneNo = phoneNumber.getText().toString();
                        amountt = "1";
                        if(phoneNo.isEmpty()){
                            phoneNumber.setError("Phone Number Required");
                        } else {
                            mProgressDialog.show();
                            saveOrderData();
                        }
                    }
                });
            }
        });
        
    }

    private void pay(String phoneNo, String amount) {
        RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);

        String url = "http://192.168.43.233:8080/mobile-money/stk-transaction-request";

        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("PhoneNumber", phoneNo);
        hashMap.put("Amount", amount);
        hashMap.put("orderId", orderId);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String responseBody = response.toString();
                if(!responseBody.isEmpty()){
                try {
                    String responseCode = response.getString("ResponseCode");
                    if(responseCode.equals("0")){
                        Toast.makeText(CartActivity.this, "Please wait to enter your Mpesa pin", Toast.LENGTH_SHORT).show();
                        managementCart.clearCart();
                        finish();

                    } else {
                        Toast.makeText(CartActivity.this, "Failed. Please try again", Toast.LENGTH_SHORT).show();
                    }
                    mProgressDialog.dismiss();
                    popupWindow.dismiss();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } } else {
                    Toast.makeText(CartActivity.this, "Failed. Please try again", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    popupWindow.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CartActivity.this, "Request Failed. Please try again", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
                popupWindow.dismiss();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
    private void saveOrderData(){
        List<ItemsModel> cartItems = managementCart.getListCart();

        RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);

        String url = "http://192.168.43.233:8080/api/v1/saveOrders";

        JSONObject ordersModel = new JSONObject();
        try {
            ordersModel.put("merchant_request_id", JSONObject.NULL);
            ordersModel.put("customerId", userId);
            ordersModel.put("orderDate", JSONObject.NULL);

            JSONArray productsArray = new JSONArray();
            for (ItemsModel itemsModel : cartItems){
                JSONObject productJson = new JSONObject();
                productJson.put("quantity", itemsModel.getNumberInCart());
                productJson.put("productId", itemsModel.getId());
                productsArray.put(productJson);
            }

            ordersModel.put("products", productsArray);

            log.info(ordersModel);


        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, ordersModel, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String responseBody = response.toString();
                log.info(responseBody);
                try {
                    orderId = response.getLong("message");
                    log.info(orderId);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Log.d("Order Sent", "Order sent Succesfully");
                pay(phoneNo, amountt);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Order Not Sent", "Order not sent");
                error.printStackTrace();
                error.getMessage();
                mProgressDialog.dismiss();
            }
        });

        requestQueue.add(jsonObjectRequest);


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