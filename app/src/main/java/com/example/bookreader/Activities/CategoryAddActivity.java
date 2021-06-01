package com.example.bookreader.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookreader.R;
import com.example.bookreader.databinding.ActivityCategoryAddBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class CategoryAddActivity extends AppCompatActivity {
    private ActivityCategoryAddBinding binding;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    private String category = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.please_wait));
        progressDialog.setCanceledOnTouchOutside(false);

        binding.submitBtn.setOnClickListener(v -> validateData());

        binding.backBtn.setOnClickListener(v -> onBackPressed());
    }

    private void validateData() {
        category = Objects.requireNonNull(binding.categoryEt.getText()).toString().trim();

        if (TextUtils.isEmpty(category)) {
            Toast.makeText(this, R.string.please_enter_category, Toast.LENGTH_SHORT).show();
        } else {
            addCategoryFirebase();
        }
    }

    private void addCategoryFirebase() {
        progressDialog.setMessage(getString(R.string.adding_category));
        progressDialog.show();

        long timestamp = System.currentTimeMillis();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", "" + timestamp);
        hashMap.put("category", "" + category);
        hashMap.put("timestamp", timestamp);
        hashMap.put("uid", "" + firebaseAuth.getUid());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Categories");
        reference.child("" + timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(unused -> {
                    binding.categoryEt.setText("");
                    progressDialog.dismiss();
                    Toast.makeText(CategoryAddActivity.this, R.string.category_added_successfully, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(CategoryAddActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}