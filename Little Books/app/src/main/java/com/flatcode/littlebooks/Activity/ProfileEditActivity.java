package com.flatcode.littlebooks.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littlebooks.R;
import com.flatcode.littlebooks.Unit.DATA;
import com.flatcode.littlebooks.Unit.THEME;
import com.flatcode.littlebooks.Unit.VOID;
import com.flatcode.littlebooks.databinding.ActivityProfileEditBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Objects;

public class ProfileEditActivity extends AppCompatActivity {

    private ActivityProfileEditBinding binding;
    Activity activity;
    Context context = activity = ProfileEditActivity.this;

    private Uri imageUri = null;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityProfileEditBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait...");
        dialog.setCanceledOnTouchOutside(false);

        loadUserInfo();

        binding.toolbar.nameSpace.setText(R.string.edit_profile);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.image.setOnClickListener(v -> VOID.CropImageSquare(activity));
        binding.go.setOnClickListener(v -> validateData());
    }

    private String username = DATA.EMPTY;

    private void validateData() {
        username = binding.nameEt.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(context, "Enter name...", Toast.LENGTH_SHORT).show();
        } else {
            if (imageUri == null) {
                updateProfile(DATA.EMPTY);
            } else {
                uploadImage();
            }
        }
    }

    private void uploadImage() {
        dialog.setMessage("Uploading Image...");
        dialog.show();

        String filePathAndName = "Images/Profile/" + DATA.FirebaseUserUid;

        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context));
        reference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            String uploadedImageUrl = DATA.EMPTY + uriTask.getResult();

            updateProfile(uploadedImageUrl);
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Failed to upload image due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void updateProfile(String imageUrl) {
        dialog.setMessage("Updating user profile...");
        dialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(DATA.USER_NAME, DATA.EMPTY + username);
        if (imageUri != null) {
            hashMap.put(DATA.PROFILE_IMAGE, DATA.EMPTY + imageUrl);
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.USERS);
        reference.child(Objects.requireNonNull(DATA.FirebaseUserUid)).updateChildren(hashMap).addOnSuccessListener(unused -> {
            dialog.dismiss();
            Toast.makeText(context, "Profile updated...", Toast.LENGTH_SHORT).show();
            //finish();
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Failed to update db duo to " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void loadUserInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.USERS);
        reference.child(Objects.requireNonNull(DATA.FirebaseUserUid)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = DATA.EMPTY + snapshot.child(DATA.USER_NAME).getValue();
                String profileImage = DATA.EMPTY + snapshot.child(DATA.PROFILE_IMAGE).getValue();

                VOID.Glide(true, context, profileImage, binding.profileImage);
                binding.nameEt.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = CropImage.getPickImageResultUri(context, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(context, uri)) {
                imageUri = uri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                VOID.CropImageSquare(activity);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                binding.profileImage.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Error! " + error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}