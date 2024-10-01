package com.example.theshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import javax.annotation.Nullable;

public class Login extends AppCompatActivity {

    public ImageView google, facebook;
    public EditText emailtv, passwordtv;
    public Button loginbtn;
    public ProgressDialog mProgressDialog;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        google = findViewById(R.id.google);
        loginbtn = findViewById(R.id.loginbtn);

        emailtv = findViewById(R.id.email2);
        passwordtv = findViewById(R.id.password2);

        mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setTitle("Processing Your Request");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setIndeterminate(true);

        // Configure Google Sign-In
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Set up the Google sign-in client
        com.google.android.gms.auth.api.signin.GoogleSignInClient mGoogleSignInClient =
                GoogleSignIn.getClient(this, googleSignInOptions);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(mGoogleSignInClient);
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.show();
                checkLoginDetails();
            }
        });


    }

    private void checkLoginDetails() {
        String email = emailtv.getText().toString();
        String password = passwordtv.getText().toString();

        if(email.equals(""))
            emailtv.setError("Required");
        if(password.equals(""))
            passwordtv.setError("Required");

        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);

        String url = "http://192.168.43.233:8080/api/v1/user/login";

        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Get values from the JSON response
                    String fullName = (String) response.get("fullname");
                    String email = (String) response.get("email");
                    Toast.makeText(Login.this, fullName+" "+email, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Login.this, Home.class);
                    intent.putExtra("fullname", fullName);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    mProgressDialog.dismiss();
                    finish();
                } catch (JSONException e){
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println(error.getMessage());
                Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }


    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void signIn(com.google.android.gms.auth.api.signin.GoogleSignInClient mGoogleSignInClient) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void  handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String email = account.getEmail();
            String displayName = account.getDisplayName();

            // Handle the sign-in. Send the token to my backend

            Toast.makeText(this, "Signed in as: " + displayName + " (" + email + ") ", Toast.LENGTH_SHORT).show();
        } catch (ApiException e){
            Log.w("Login", "SignInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "Sign-in failed, please try again", Toast.LENGTH_SHORT).show();
        }
    }
}