package com.flatcode.littlebooksadmin.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littlebooksadmin.R;
import com.flatcode.littlebooksadmin.Unit.CLASS;
import com.flatcode.littlebooksadmin.Unit.DATA;
import com.flatcode.littlebooksadmin.Unit.THEME;
import com.flatcode.littlebooksadmin.Unit.VOID;
import com.flatcode.littlebooksadmin.databinding.ActivityPrivacyPolicyBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private ActivityPrivacyPolicyBinding binding;
    Context context = PrivacyPolicyActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityPrivacyPolicyBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.toolbar.nameSpace.setText(R.string.privacy_policy);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.edit.setOnClickListener(v -> VOID.Intent(context, CLASS.PRIVACY_POLICY_EDIT));

        VOID.Logo(context, binding.logo);
        privacyPolicy();
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