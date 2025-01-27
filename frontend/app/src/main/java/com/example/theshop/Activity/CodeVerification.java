package com.example.theshop.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class CodeVerification extends AppCompatActivity {
    public EditText code1, code2, code3, code4, code5, code6;
    public Button verify;
    public String email;
    public ImageView backBtn;
    public ProgressDialog mProgressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");


        backBtn = findViewById(R.id.back);

        code1 = findViewById(R.id.etDigit1);
        code2 = findViewById(R.id.etDigit2);
        code3 = findViewById(R.id.etDigit3);
        code4 = findViewById(R.id.etDigit4);
        code5 = findViewById(R.id.etDigit5);
        code6 = findViewById(R.id.etDigit6);

        mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setTitle("Processing Your Request");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setIndeterminate(true);

        EditText[] editTexts = {code1, code2, code3, code4, code5, code6};

        editTexts[0].requestFocus();

        for (int i = 0; i < editTexts.length; i++) {
            final int currentIndex = i;

            editTexts[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && currentIndex < editTexts.length - 1) {
                        editTexts[currentIndex + 1].requestFocus(); // Move to the next field
                    } else if (s.length() == 0 && currentIndex > 0) {
                        editTexts[currentIndex - 1].requestFocus(); // Move to the previous field
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }


        verify = findViewById(R.id.verifyCode);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.show();
                String a = code1.getText().toString();
                String b = code2.getText().toString();
                String c = code3.getText().toString();
                String d = code4.getText().toString();
                String e = code5.getText().toString();
                String f = code6.getText().toString();

                String code = a + b + c + d + e + f;

                verifyCode(email, code);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void verifyCode(String email, String code) {

        String url = "http://192.168.43.233:8080/api/v1/verifyCode?email=" + email + "&code=" + code;

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(CodeVerification.this, response, Toast.LENGTH_SHORT).show();
                if (response.equals("Email Verification Successful")){
                    Intent intent = new Intent(CodeVerification.this, ChangeForgotPass.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                    mProgressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(CodeVerification.this, "Failed. Please try again later", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });
        queue.add(stringRequest);
    }
}