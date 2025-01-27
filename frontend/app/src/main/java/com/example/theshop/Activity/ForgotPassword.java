package com.example.theshop.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.theshop.R;

public class ForgotPassword extends AppCompatActivity {
    public EditText emailET;
    public Button submit;
    public ImageView back;
    public TextView cancel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailET = findViewById(R.id.emailTxt);
        submit = findViewById(R.id.submitEmail);
        back = findViewById(R.id.back);
        cancel = findViewById(R.id.cancel);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                sendVerificationCode(email);
            }
        });
    }

    private void sendVerificationCode(String email) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://192.168.43.233:8080/api/v1/checkUserExists?email=" + email;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ForgotPassword.this, response, Toast.LENGTH_SHORT).show();
                if (response.equals("Verification Code Sent")){
                    Intent intent = new Intent(ForgotPassword.this, CodeVerification.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(ForgotPassword.this, "Failed. Please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }
}