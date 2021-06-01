package com.example.bookreader.Activities.Authorization;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookreader.Activities.Dashboard.DashboardUserActivity;
import com.example.bookreader.R;
import com.example.bookreader.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    private String name = "", email = "", password = "", cPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.please_wait));
        progressDialog.setCanceledOnTouchOutside(false);

        binding.backBtn.setOnClickListener(v -> onBackPressed());

        binding.registerBtn.setOnClickListener(v -> validateData());
    }

    private void validateData() {
        name = Objects.requireNonNull(binding.nameEt.getText()).toString().trim();
        email = Objects.requireNonNull(binding.emailEt.getText()).toString().trim();
        password = Objects.requireNonNull(binding.passwordEt.getText()).toString().trim();
        cPassword = Objects.requireNonNull(binding.confirmPasswordEt.getText()).toString().trim();

        if (TextUtils.isEmpty(name) & TextUtils.isEmpty(email) & TextUtils.isEmpty(password) & TextUtils.isEmpty(cPassword)) {
            Toast.makeText(this, R.string.fill_in_all_the_fields, Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, R.string.enter_you_name, Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, R.string.enter_you_email, Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, R.string.invalid_email_pattern, Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.enter_password, Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cPassword)) {
            Toast.makeText(this, "Confirm password!", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(cPassword)) {
            Toast.makeText(this, R.string.password_doesnt_match, Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText(this, R.string.the_password_must_contain_at_lest_6_characters, Toast.LENGTH_SHORT).show();
        } else {
            createUser();
        }
    }

    private void createUser() {
        progressDialog.setMessage(getString(R.string.creating_account));
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> updateUserInfo())
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUserInfo() {
        progressDialog.setMessage(getString(R.string.saving_user_info));

        long timestamp = System.currentTimeMillis();
        String uid = firebaseAuth.getUid();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("email", email);
        hashMap.put("name", name);
        hashMap.put("profileImage", "");
        hashMap.put("userType", "user");
        hashMap.put("timestamp", timestamp);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(Objects.requireNonNull(uid))
                .setValue(hashMap)
                .addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, getString(R.string.account_created), Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(), DashboardUserActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}