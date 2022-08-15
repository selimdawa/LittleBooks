package com.flatcode.littlebooksadmin.Activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littlebooksadmin.R;
import com.flatcode.littlebooksadmin.Unit.DATA;
import com.flatcode.littlebooksadmin.Unit.THEME;
import com.flatcode.littlebooksadmin.Unit.VOID;
import com.flatcode.littlebooksadmin.databinding.ActivityPrivacyPolicyEditBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PrivacyPolicyEditActivity extends AppCompatActivity {

    private ActivityPrivacyPolicyEditBinding binding;
    Context context = PrivacyPolicyEditActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityPrivacyPolicyEditBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.toolbar.nameSpace.setText(R.string.privacy_policy);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.go.setOnClickListener(v -> validateData());

        VOID.Logo(context,binding.logo);
        privacyPolicy();
    }

    private String description = DATA.EMPTY;

    private void validateData() {
        description = binding.text.getText().toString().trim();

        if (TextUtils.isEmpty(description)) {
            Toast.makeText(context, "Enter Privacy Policy...", Toast.LENGTH_SHORT).show();
        } else {
            update();
        }
    }

    private void update() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(DATA.PRIVACY_POLICY, DATA.EMPTY + description);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.TOOLS);
        ref.updateChildren(hashMap).addOnSuccessListener(unused ->
                Toast.makeText(context, "Privacy Policy updated...", Toast.LENGTH_SHORT).show()).addOnFailureListener(e ->
                Toast.makeText(context, DATA.EMPTY + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void privacyPolicy() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(DATA.TOOLS)
                .child(DATA.PRIVACY_POLICY);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = String.valueOf(dataSnapshot.getValue());

                binding.text.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}