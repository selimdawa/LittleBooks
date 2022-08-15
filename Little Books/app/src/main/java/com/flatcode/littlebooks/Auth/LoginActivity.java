package com.flatcode.littlebooks.Auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littlebooks.Unit.CLASS;
import com.flatcode.littlebooks.Unit.DATA;
import com.flatcode.littlebooks.Unit.THEME;
import com.flatcode.littlebooks.Unit.VOID;
import com.flatcode.littlebooks.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    Context context = LoginActivity.this;

    private FirebaseAuth auth;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        VOID.Logo(getBaseContext(), binding.logo);
        VOID.Intro(getBaseContext(), binding.background, binding.backWhite, binding.backBlack);

        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setTitle("Please wait...");
        dialog.setCanceledOnTouchOutside(false);

        binding.forget.setOnClickListener(v -> VOID.Intent(context, CLASS.FORGET_PASSWORD));
        binding.noAccount.setOnClickListener(v -> VOID.Intent(context, CLASS.REGISTER));
        binding.loginBtn.setOnClickListener(v -> validateDate());
    }

    private String email = "", password = "";

    private void validateDate() {

        //get data
        email = binding.emailEt.getText().toString().trim();
        password = binding.passwordEt.getText().toString().trim();

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Invalid email pattern...!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, "Enter password...!", Toast.LENGTH_SHORT).show();
        } else {
            loginUser();
        }
    }

    private void loginUser() {
        dialog.setMessage("Logging In...");
        dialog.show();

        try {
            auth.signInWithEmailAndPassword(email, password).addOnCanceledListener(() -> {
                dialog.dismiss();
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
            }).addOnSuccessListener(authResult -> VOID.IntentClear(context, CLASS.MAIN)).addOnFailureListener(e -> {
                dialog.dismiss();
                Toast.makeText(context, DATA.EMPTY + e.getMessage(), Toast.LENGTH_SHORT).show();
            }).addOnCompleteListener(task -> dialog.show());
        } catch (Exception e) {
            dialog.dismiss();
            Toast.makeText(context, DATA.EMPTY + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}