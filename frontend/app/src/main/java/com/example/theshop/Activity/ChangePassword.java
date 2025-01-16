package com.example.theshop.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.theshop.R;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePassword extends AppCompatActivity {

    public TextView cancel;
    public EditText oldPword, newPword, confirmPword;
    public Button changePwordBtn;
    public ImageView backBtn;
    String userId;
    public ProgressDialog mProgressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setTitle("Processing Your Request");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setIndeterminate(true);

        oldPword = findViewById(R.id.currentPword);
        newPword = findViewById(R.id.newPassword);
        confirmPword = findViewById(R.id.confirmPassword);
        changePwordBtn = findViewById(R.id.changePasswordBtn);
        cancel = findViewById(R.id.cancelChangePw);
        backBtn = findViewById(R.id.back);

        changePwordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.show();
                String oldPass = oldPword.getText().toString();
                String newPass = newPword.getText().toString();
                String confirmPass = confirmPword.getText().toString();

                if (oldPass.isEmpty()){
                    oldPword.setError("Required");
                } else if (newPass.isEmpty()) {
                    newPword.setError("Required");
                } else if (confirmPass.isEmpty()){
                    confirmPword.setError("Required");
                }

                if(!newPass.equals(confirmPass)){
                    Toast.makeText(ChangePassword.this, "Passwords do not Match", Toast.LENGTH_SHORT).show();
                    confirmPword.setError("Passwords Don't Match");
                } else {
                    /// TODO: 12/21/24 Handle logic to check old pass, if correct update the password.
                    /// TODO: 12/21/24 Send all values to the backend, check old pass, if okay update to new pass, else return error that old pass is not correct.
                    /// TODO: 12/21/24 After the whole operation remember to finish the activity.

                    RequestQueue queue = Volley.newRequestQueue(ChangePassword.this);

                    String url = "http://192.168.43.233:8080/api/v1/user/changepassword";

                    JSONObject passdetails = new JSONObject();
                    try {
                        passdetails.put("id", userId);
                        passdetails.put("oldPassword", oldPass);
                        passdetails.put("newPassword", newPass);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, passdetails, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String message = response.getString("message");
                                Toast.makeText(ChangePassword.this, message, Toast.LENGTH_SHORT).show();
                                mProgressDialog.dismiss();
                                finish();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(ChangePassword.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                    queue.add(jsonObjectRequest);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePassword.this, Profile.class);
                startActivity(intent);
                finish();
            }
        });
    }
}