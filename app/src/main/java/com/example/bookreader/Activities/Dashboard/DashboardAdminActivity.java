package com.example.bookreader.Activities.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookreader.Activities.CategoryAddActivity;
import com.example.bookreader.Activities.MainActivity;
import com.example.bookreader.Adapters.CategoryAdapter;
import com.example.bookreader.Models.CategoryModel;
import com.example.bookreader.databinding.ActivityDashboardAdminBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardAdminActivity extends AppCompatActivity {
    private ActivityDashboardAdminBinding binding;

    private FirebaseAuth firebaseAuth;

    private ArrayList<CategoryModel> categoryModels;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        binding.logoutBtn.setOnClickListener(v -> {
            firebaseAuth.signOut();
            checkUser();
        });

        binding.addCategoryBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CategoryAddActivity.class)));

        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    categoryAdapter.getFilter().filter(s);
                } catch (Exception exception) {
                    Toast.makeText(DashboardAdminActivity.this, "" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        checkUser();
        loadCategories();
    }

    private void loadCategories() {
        categoryModels = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Categories");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CategoryModel model = ds.getValue(CategoryModel.class);
                    categoryModels.add(model);
                }

                categoryAdapter = new CategoryAdapter(DashboardAdminActivity.this, categoryModels);
                binding.recyclerView.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {
            String email = firebaseUser.getEmail();

            binding.emailTv.setText(email);
        }
    }
}