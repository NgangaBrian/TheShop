package com.example.theshop.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.theshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditProfile extends AppCompatActivity {

    private static final org.apache.commons.logging.Log log = LogFactory.getLog(EditProfile.class);
    public TextView cancel;
    public EditText mobilePhone, address, postalCode;
    public Button save;
    public ImageView backBtn, uploadPic;
    public Uri imagePath;
    public String userId, phoneNo, addresss, profileUrl, postalCodee;
    public ProgressDialog progressDialog, mProgressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        phoneNo = intent.getStringExtra("phoneNo");
        addresss = intent.getStringExtra("address");
        profileUrl = intent.getStringExtra("profileUrl");
        postalCodee = intent.getStringExtra("postalCode");

        progressDialog = new ProgressDialog(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Processing Your Request");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setIndeterminate(true);


        backBtn = findViewById(R.id.back);
        cancel = findViewById(R.id.cancel);
        mobilePhone = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.address);
        postalCode = findViewById(R.id.postalCode);
        save = findViewById(R.id.saveBtn);
        uploadPic = findViewById(R.id.uploadPicture);

        address.setText(addresss);
        mobilePhone.setText(phoneNo);
        postalCode.setText(postalCodee);

        if ( profileUrl!= null && !profileUrl.isEmpty()){
            Glide.with(EditProfile.this).load(profileUrl).into(uploadPic);
            save.setText("Edit Information");
        }


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(EditProfile.this, Profile.class);
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent, 1);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    uploadProfilePicture();
            }
        });
    }

    private void uploadProfilePicture() {
        progressDialog.setTitle("Uploading Profile Picture...");
        progressDialog.show();

        FirebaseStorage.getInstance().getReference("Users' Profile Pictures/"+ UUID.randomUUID().toString()).putFile(imagePath)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            updateProfileDetails(task.getResult().toString());
                        }
                    });
                    Toast.makeText(EditProfile.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfile.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
                mProgressDialog.show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = 100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    }
                });
    }

    private void updateProfileDetails(String profileUrl) {
        String phoneNo = mobilePhone.getText().toString();
        String addresses = address.getText().toString();
        String postalcode = postalCode.getText().toString();

        if (phoneNo.isEmpty() || addresses.isEmpty() || postalcode.isEmpty()){
            Toast.makeText(this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
            mProgressDialog.dismiss();
        } else if (profileUrl.isEmpty()) {
            Toast.makeText(this, "Please upload a profile picture", Toast.LENGTH_SHORT).show();
            mProgressDialog.dismiss();
        } else{

        log.info(profileUrl);

        RequestQueue queue = Volley.newRequestQueue(EditProfile.this);

        String url = "http://192.168.43.233:8080/api/v1/updateuserdetails";

        JSONObject userDetails = new JSONObject();
        try {
            userDetails.put("user_id", userId);
            userDetails.put("mobile_number", phoneNo);
            userDetails.put("address", addresses);
            userDetails.put("postal_code", postalcode);
            userDetails.put("profile_url", profileUrl);
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, userDetails, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String message = response.getString("message");
                    Toast.makeText(EditProfile.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println(error.getMessage());
                Toast.makeText(EditProfile.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
        mProgressDialog.dismiss();
    }}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data!=null){
            imagePath = data.getData();
            getImageInView();
        }
    }

    private void getImageInView() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
        } catch (IOException e){
            e.printStackTrace();
        }
        uploadPic.setImageBitmap(bitmap);
    }
}