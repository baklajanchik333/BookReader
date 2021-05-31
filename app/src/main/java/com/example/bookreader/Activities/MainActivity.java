package com.example.bookreader.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookreader.Activities.Authorization.LoginActivity;
import com.example.bookreader.Activities.Dashboard.DashboardUserActivity;
import com.example.bookreader.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginBtn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });

        binding.continueWithoutLoginBtn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), DashboardUserActivity.class));
        });
    }
}