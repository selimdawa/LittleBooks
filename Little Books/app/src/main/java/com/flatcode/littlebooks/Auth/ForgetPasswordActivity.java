package com.flatcode.littlebooks.Auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littlebooks.Unit.CLASS;
import com.flatcode.littlebooks.Unit.THEME;
import com.flatcode.littlebooks.Unit.VOID;
import com.flatcode.littlebooks.databinding.ActivityForgetPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private ActivityForgetPasswordBinding binding;
    private Context context = ForgetPasswordActivity.this;

    private FirebaseAuth auth;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        VOID.Logo(getBaseContext(), binding.logo);
        VOID.Intro(getBaseContext(), binding.background, binding.backWhite,binding.backBlack);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setTitle("Please wait...");
        dialog.setCanceledOnTouchOutside(false);

        binding.noAccount.setOnClickListener(v -> {
            VOID.Intent(context, CLASS.REGISTER);
            finish();
        });
        binding.login.setOnClickListener(v -> {
            VOID.Intent(context, CLASS.LOGIN);
            finish();
        });
        binding.go.setOnClickListener(v -> validateDate());
    }

    private String email = "";

    private void validateDate() {
        email = binding.emailEt.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(context, "Enter email...!", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Invalid email format...!", Toast.LENGTH_SHORT).show();
        } else {
            recoverPassword();
        }
    }

    private void recoverPassword() {
        dialog.setMessage("Sending password recovery to instructions to " + email);
        dialog.show();

        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            dialog.dismiss();
            Toast.makeText(context, "Instructions to reset password sent to " + email, Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Failed to send to due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}