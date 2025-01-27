package com.example.theshop.Activity;

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

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.theshop.BuildConfig;
import com.example.theshop.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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
    public TextView forgotPass;
    public ProgressDialog mProgressDialog;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        google = findViewById(R.id.google);
        loginbtn = findViewById(R.id.loginbtn);
        forgotPass = findViewById(R.id.forgotPassword);

        emailtv = findViewById(R.id.email2);
        passwordtv = findViewById(R.id.password2);

        mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setTitle("Processing Your Request");
        mProgressDialog.setMessage("Please wait...");
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
                mProgressDialog.show();
                signIn(mGoogleSignInClient);
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.show();
                String email = emailtv.getText().toString();
                String password = passwordtv.getText().toString();
                String googleId = "";
                String authType = "password";

                if(email.equals(""))
                    emailtv.setError("Required");
                if(password.equals(""))
                    passwordtv.setError("Required");
                checkLoginDetails(email, password, googleId, authType);
            }
        });
    }
    private void checkLoginDetails(String email, String password, String googleId, String authType) {

        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);

        String url = BuildConfig.BASE_URL + "/api/v1/user/login";

        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("googleId", googleId);
        params.put("authType", authType);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("User details", response.toString());
                try {
                    String status = response.getString("status");

                    if("success".equals(status)) {
                        // Get values from the JSON response
                        JSONObject user = response.getJSONObject("user");
                        String userId = user.getString("id");
                        String fullName = user.getString("fullname");
                        String email = user.getString("email");
                        Toast.makeText(Login.this, fullName + " " + email, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Login.this, Home.class);
                        intent.putExtra("userId", userId);
                        intent.putExtra("fullname", fullName);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        mProgressDialog.dismiss();
                        finish();
                    } else if("error".equals(status)) {
                        String message = response.getString("message");
                        if ("Email does not exist".equals(message)){
                            mProgressDialog.dismiss();
                            Toast.makeText(Login.this, "Please create an account", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, SignUp.class);
                            startActivity(intent);
                            finish();
                        } else if ("Incorrect email or password".equals(message)) {
                            mProgressDialog.dismiss();
                            passwordtv.setError(message);
                            Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                        } else if ("Invalid Google authentication!!!!".equals(message)) {
                            System.out.println("Google Token Tampered with!");
                            Log.e("Tampered!!", "Token Has Been Tampered with, Make sure you enforce your security NOW!!!");
                        } else {
                            Log.e("Check What's wrong!! Because something is wrong. Probably authType", message);
                        }

                    }
                } catch (JSONException e){
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                    Toast.makeText(Login.this, "An error occurred while processing your request.", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
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
            String idToken = account.getIdToken();
            String password = "";
            String authType = "google";

            // Handle the sign-in. Send the token to my backend
            checkLoginDetails(email, password, idToken, authType);

            Toast.makeText(this, displayName + " (" + email + ") ", Toast.LENGTH_SHORT).show();
            mProgressDialog.dismiss();
            // Toast.makeText(this, idToken + " Hello", Toast.LENGTH_SHORT).show();
        } catch (ApiException e){
            Log.w("Login", "SignInResult:failed code=" + e.getStatusCode());
            mProgressDialog.dismiss();
            Toast.makeText(this, "Sign-in failed, please try again", Toast.LENGTH_SHORT).show();
        }
    }
}