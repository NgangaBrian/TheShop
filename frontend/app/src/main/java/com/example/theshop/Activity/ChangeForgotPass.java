package com.example.theshop.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.theshop.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangeForgotPass extends AppCompatActivity {
    public EditText newPassword, confirmPassword, cancel;
    public Button createPassword;
    public String email;
    public ImageView backBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_forgot_pass);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        createPassword = findViewById(R.id.createPassword);
        backBtn = findViewById(R.id.back);
        cancel = findViewById(R.id.cancelBtn);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        createPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPass = newPassword.getText().toString();
                String confirmPass = confirmPassword.getText().toString();

                if (!newPass.equals(confirmPass)){
                    Toast.makeText(ChangeForgotPass.this, "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
                    confirmPassword.setError("Passwords Do Not Match");
                } else {
                    changeForgotPassword(email, newPass);
                }
            }
        });
    }

    private void changeForgotPassword(String email, String newPass) {
        String url = "http://192.168.43.233:8080/api/v1/changeForgotPass";

        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject passdetails = new JSONObject();
        try {
            passdetails.put("email", email);
            passdetails.put("password", newPass);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, passdetails, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String message = response.getString("message");
                    if (message.equals("Password Changed Successfully")){
                        Toast.makeText(ChangeForgotPass.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
}