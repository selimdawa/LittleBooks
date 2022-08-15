package com.flatcode.littlebooks.Auth;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littlebooks.Unit.CLASS;
import com.flatcode.littlebooks.Unit.THEME;
import com.flatcode.littlebooks.Unit.VOID;
import com.flatcode.littlebooks.databinding.ActivityAuthBinding;

public class AuthActivity extends AppCompatActivity {

    private ActivityAuthBinding binding;
    Context context = AuthActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        VOID.Logo(getBaseContext(), binding.logo);
        VOID.Intro(getBaseContext(), binding.background, binding.backWhite, binding.backBlack);
        binding.loginBtn.setOnClickListener(v -> VOID.Intent(context, CLASS.LOGIN));
        binding.skipBtn.setOnClickListener(v -> VOID.Intent(context, CLASS.REGISTER));
    }
}