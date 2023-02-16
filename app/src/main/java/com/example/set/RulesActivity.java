package com.example.set;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import com.example.set.databinding.ActivityRulesBinding;

public class RulesActivity extends AppCompatActivity {

    private ActivityRulesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRulesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}