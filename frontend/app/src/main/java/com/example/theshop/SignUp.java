package com.example.theshop;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.credentials.CredentialManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.BuildConfig;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class SignUp extends AppCompatActivity {

    public ImageView google, facebook;
    public Button signup;
    public ProgressDialog mProgressDialog;
    public EditText fullnametv, emailtv, passwordtv, confirmpwordtv;
    private static final int RC_SIGN_IN = 9001;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        google = findViewById(R.id.google);

        fullnametv = findViewById(R.id.name2);
        emailtv = findViewById(R.id.email2);
        passwordtv = findViewById(R.id.password2);
        confirmpwordtv = findViewById(R.id.confirmPassword2);

        signup = findViewById(R.id.registerbtn);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Processing Your Request");
        mProgressDialog.setTitle("Please Wait...");
        mProgressDialog.setIndeterminate(true);




        // Configure Google Sign-In
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("49701769693-u2l7gbm64nm0vonkja627fanrk7iln8p.apps.googleusercontent.com")
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


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.show();
                String fullname = fullnametv.getText().toString();
                String email = emailtv.getText().toString();
                String password = passwordtv.getText().toString();
                String confirmpwd = confirmpwordtv.getText().toString();
                String googleId = "";
                String authType = "password";

                if(fullname.isEmpty() || email.isEmpty() || password.isEmpty() || confirmpwd.isEmpty()){
                    Toast.makeText(SignUp.this, "All fields are required.", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                } else if(!password.equals(confirmpwd)){
                    confirmpwordtv.setError("Passwords Don't match");
                    mProgressDialog.dismiss();
                } else{
                    storeDetails(fullname, email, password, googleId, authType);
                }
            }
        });

    }

    private void storeDetails(String fullname, String email, String password, String idToken, String authType) {
        RequestQueue queue = Volley.newRequestQueue(SignUp.this);

        String url = "http://192.168.43.233:8080/api/v1/user/register";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equalsIgnoreCase("User already exists")){
                    Toast.makeText(SignUp.this, "User Already Exists. Please login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp.this, Login.class);
                    startActivity(intent);
                    finish();
                    mProgressDialog.dismiss();
                } else if (response.equalsIgnoreCase("User registered successfully")) {
                    Toast.makeText(SignUp.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    // If authType == password, send a verification code(On backend tho) but here, open a verify email page
                    mProgressDialog.dismiss();
                    Intent intent = new Intent(SignUp.this, Login.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignUp.this, "Registration Failed. Please try again", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    System.out.println(response);
                    Log.d("SignUpResponse", response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println(error.getMessage());
                mProgressDialog.dismiss();
                error.printStackTrace();  // Log the stack trace for debugging
                if (error.networkResponse != null) {
                    Log.e("SignUpError", "Error Code: " + error.networkResponse.statusCode);
                    Log.e("SignUpError", "Response Data: " + new String(error.networkResponse.data));
                }
                Toast.makeText(SignUp.this, "Registration Failed!!!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fullname", fullname);
                params.put("email", email);
                params.put("password", password);
                params.put("googleId", idToken);
                params.put("authType", authType);

                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignUp.this, MainActivity.class);
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
            mProgressDialog.show();
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String email = account.getEmail();
            String displayName = account.getDisplayName();
            String googleId = account.getId();
            String idToken = account.getIdToken();
            String password = "";
            String authType = "google";

            // Handle the sign-in. Send the token to my backend

            //Toast.makeText(this, "Signed UP as: " + displayName + " ( " + email + " ) ", Toast.LENGTH_SHORT).show();
            // Toast.makeText(this, idToken, Toast.LENGTH_SHORT).show();
            storeDetails(displayName, email, password, idToken, authType);
        } catch (ApiException e){
            Log.w("Login", "SignInResult:failed code=" + e.getStatusCode());
            e.printStackTrace();
            Toast.makeText(this, "Sign-in failed, please try again", Toast.LENGTH_SHORT).show();
            mProgressDialog.dismiss();
        }
    }
}