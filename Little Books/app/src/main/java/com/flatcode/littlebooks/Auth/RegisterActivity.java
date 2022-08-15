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
import com.flatcode.littlebooks.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    Context context = RegisterActivity.this;

    private FirebaseAuth auth;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        VOID.Logo(getBaseContext(), binding.logo);
        VOID.Intro(getBaseContext(), binding.background, binding.backWhite, binding.backBlack);

        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setTitle("Please wait...");
        dialog.setCanceledOnTouchOutside(false);

        binding.login.setOnClickListener(v -> {
            VOID.Intent(context, CLASS.LOGIN);
            finish();
        });
        binding.forget.setOnClickListener(v -> VOID.Intent(context, CLASS.FORGET_PASSWORD));
        binding.go.setOnClickListener(v -> validateData());
    }

    private String name = "", email = "", password = "";

    private void validateData() {

        //get data
        name = binding.nameEt.getText().toString().trim();
        email = binding.emailEt.getText().toString().trim();
        password = binding.passwordEt.getText().toString().trim();
        String cPassword = binding.cPasswordEt.getText().toString().trim();

        //validate data
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(context, "Enter you name...", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Invalid email pattern...!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, "Enter password...!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cPassword)) {
            Toast.makeText(context, "Confirm Password...!", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(cPassword)) {
            Toast.makeText(context, "Password doesn't match...!", Toast.LENGTH_SHORT).show();
        } else {
            createUserAccount();
        }
    }

    private void createUserAccount() {
        //show progress
        dialog.setMessage("Creating account...");
        dialog.show();

        //create user in firebase auth
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> updateUserinfo())
                .addOnFailureListener(e -> {
                    dialog.dismiss();
                    Toast.makeText(context, DATA.EMPTY + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUserinfo() {
        dialog.setMessage("Saving user info...");
        //get current user uid, since user is registered so we can get now
        String id = auth.getUid();

        //setup data to add in db
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(DATA.BOOKS_COUNT, 0);
        hashMap.put(DATA.AD_LOAD, 0);
        hashMap.put(DATA.AD_CLICK, 0);
        hashMap.put(DATA.EMAIL, DATA.EMPTY + email);
        hashMap.put(DATA.ID, DATA.EMPTY + id);
        hashMap.put(DATA.PROFILE_IMAGE, DATA.EMPTY + DATA.BASIC);
        hashMap.put(DATA.TIMESTAMP, System.currentTimeMillis());
        hashMap.put(DATA.USER_NAME, DATA.EMPTY + name);
        hashMap.put(DATA.VERSION, DATA.CURRENT_VERSION);

        //set data to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.USERS);
        assert id != null;
        ref.child(id).setValue(hashMap).addOnSuccessListener(unused -> {
            //data added to db
            dialog.dismiss();
            Toast.makeText(context, "Account created...", Toast.LENGTH_SHORT).show();
            VOID.IntentClear(context, CLASS.MAIN);
            finish();
        }).addOnFailureListener(e -> {
            //data failed adding to db
            Toast.makeText(context, DATA.EMPTY + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}