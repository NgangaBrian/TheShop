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

import com.example.theshop.R;

public class ChangePassword extends AppCompatActivity {

    public TextView cancel;
    public EditText oldPword, newPword, confirmPword;
    public Button changePwordBtn;
    public ImageView backBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldPword = findViewById(R.id.currentPword);
        newPword = findViewById(R.id.newPassword);
        confirmPword = findViewById(R.id.confirmPassword);
        changePwordBtn = findViewById(R.id.changePasswordBtn);
        cancel = findViewById(R.id.cancelChangePw);
        backBtn = findViewById(R.id.back);

        changePwordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    Toast.makeText(ChangePassword.this, "Password Changed!", Toast.LENGTH_SHORT).show();
                    /// TODO: 12/21/24 Handle logic to check old pass, if correct update the password.
                    /// TODO: 12/21/24 Send all values to the backend, check old pass, if okay update to new pass, else return error that old pass is not correct.
                    /// TODO: 12/21/24 After the whole operation remember to finish the activity.
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