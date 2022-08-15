package com.flatcode.littlebooks.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littlebooks.R;
import com.flatcode.littlebooks.Unit.DATA;
import com.flatcode.littlebooks.Unit.THEME;
import com.flatcode.littlebooks.Unit.VOID;
import com.flatcode.littlebooks.databinding.ActivityBookEditBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class BookEditActivity extends AppCompatActivity {

    private ActivityBookEditBinding binding;
    Context context = BookEditActivity.this;

    private String bookId;

    private Uri imageUri = null;
    private ProgressDialog dialog;
    private ArrayList<String> categoryTitle, categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityBookEditBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        bookId = getIntent().getStringExtra(DATA.BOOK_ID);

        dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait...");
        dialog.setCanceledOnTouchOutside(false);

        loadCategories();
        loadBooksInfo();

        binding.toolbar.nameSpace.setText(R.string.edit_book);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.image.setOnClickListener(v -> pickImageGallery());
        binding.category.setOnClickListener(v -> categoryDialog());
        binding.toolbar.ok.setOnClickListener(v -> validateData());
    }

    private String selectedId = DATA.EMPTY, selectedTitle = DATA.EMPTY;
    private String title = DATA.EMPTY, description = DATA.EMPTY;

    private void validateData() {
        title = binding.titleEt.getText().toString().trim();
        description = binding.descriptionEt.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(context, "Enter Title...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {
            Toast.makeText(context, "Enter Description...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(selectedId)) {
            Toast.makeText(context, "Pick Category", Toast.LENGTH_SHORT).show();
        } else {
            updateBook();
        }
    }

    private void updateBook() {
        dialog.setMessage("Uploading book info...");
        dialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(DATA.TITLE, DATA.EMPTY + title);
        hashMap.put(DATA.DESCRIPTION, DATA.EMPTY + description);
        hashMap.put(DATA.CATEGORY_ID, DATA.EMPTY + selectedId);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.BOOKS);
        ref.child(bookId).updateChildren(hashMap).addOnSuccessListener(unused -> {
            dialog.dismiss();
            Toast.makeText(context, "Book info updated...", Toast.LENGTH_SHORT).show();
        }).addOnCompleteListener(task -> {
            if (imageUri != null)
                uploadImage(bookId);
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, DATA.EMPTY + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void loadBooksInfo() {
        DatabaseReference refBooks = FirebaseDatabase.getInstance().getReference(DATA.BOOKS);
        refBooks.child(bookId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get book info
                selectedId = DATA.EMPTY + snapshot.child(DATA.CATEGORY_ID).getValue();
                String description = DATA.EMPTY + snapshot.child(DATA.DESCRIPTION).getValue();
                String title = DATA.EMPTY + snapshot.child(DATA.TITLE).getValue();
                String image = DATA.EMPTY + snapshot.child(DATA.IMAGE).getValue();

                VOID.Glide(false, context, image, binding.image);
                binding.titleEt.setText(title);
                binding.descriptionEt.setText(description);

                DatabaseReference refBookCategory = FirebaseDatabase.getInstance().getReference(DATA.CATEGORIES);
                refBookCategory.child(selectedId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get category
                        String category = DATA.EMPTY + snapshot.child(DATA.CATEGORY).getValue();

                        binding.category.setText(category);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void categoryDialog() {
        String[] categoriesArray = new String[categoryTitle.size()];
        for (int i = 0; i < categoryTitle.size(); i++) {
            categoriesArray[i] = categoryTitle.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Category").setItems(categoriesArray, (dialog, which) -> {
            selectedId = categoryId.get(which);
            selectedTitle = categoryTitle.get(which);

            binding.category.setText(selectedTitle);
        }).show();
    }

    private void loadCategories() {
        categoryId = new ArrayList<>();
        categoryTitle = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.CATEGORIES);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryId.clear();
                categoryTitle.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    String id = DATA.EMPTY + data.child(DATA.ID).getValue();
                    String category = DATA.EMPTY + data.child(DATA.CATEGORY).getValue();
                    categoryId.clear();
                    categoryTitle.add(category);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateImageBook(String imageUrl, String bookId) {
        dialog.setMessage("Updating image book...");
        dialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        if (imageUri != null) {
            hashMap.put(DATA.IMAGE, DATA.EMPTY + imageUrl);
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.BOOKS);
        reference.child(bookId).updateChildren(hashMap).addOnSuccessListener(unused -> {
            dialog.dismiss();
            Toast.makeText(context, "Image updated...", Toast.LENGTH_SHORT).show();
            //finish();
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Failed to update db duo to " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void uploadImage(String BookId) {
        dialog.setMessage("Updating Image Book");
        dialog.show();

        String filePathAndName = "Images/Books/" + bookId;

        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context));
        reference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            String uploadedImageUrl = DATA.EMPTY + uriTask.getResult();

            updateImageBook(uploadedImageUrl, BookId);
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Failed to upload image due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    assert data != null;
                    imageUri = data.getData();
                    binding.image.setImageURI(imageUri);
                }
            });

    private void pickImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }
}