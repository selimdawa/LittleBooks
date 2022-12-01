package com.flatcode.littlebooks.Activity;

import android.app.Activity;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littlebooks.R;
import com.flatcode.littlebooks.Unit.DATA;
import com.flatcode.littlebooks.Unit.THEME;
import com.flatcode.littlebooks.Unit.VOID;
import com.flatcode.littlebooks.databinding.ActivityBookAddBinding;
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

public class BookAddActivity extends AppCompatActivity {

    private ActivityBookAddBinding binding;
    Context context = BookAddActivity.this;

    private static final int BOOK_PICK_CODE = 1000;
    private Uri uri = null, imageUri = null;
    private ArrayList<String> titleList, idList;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityBookAddBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        loadBookCategories();

        dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait...");
        dialog.setCanceledOnTouchOutside(false);

        binding.toolbar.nameSpace.setText(R.string.add_new_book);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.image.setOnClickListener(v -> pickImageGallery());
        binding.chooseBook.setOnClickListener(v -> bookPickIntent());
        binding.category.setOnClickListener(v -> categoryPickDialog());
        binding.toolbar.ok.setOnClickListener(v -> validateData());
    }

    private String title = DATA.EMPTY, description = DATA.EMPTY;

    private void validateData() {
        //get data
        title = binding.titleEt.getText().toString().trim();
        description = binding.descriptionEt.getText().toString().trim();

        //validate data
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(context, "Enter Title...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {
            Toast.makeText(context, "Enter Description...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(selectedTitle)) {
            Toast.makeText(context, "Pick Category...", Toast.LENGTH_SHORT).show();
        } else if (uri == null) {
            Toast.makeText(context, "Pick Book...", Toast.LENGTH_SHORT).show();
        } else {
            uploadBookToStorage();
        }
    }

    private void uploadBookToStorage() {
        dialog.setMessage("Uploading Book...");
        dialog.show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.BOOKS);
        String id = ref.push().getKey();

        String filePathAndName = "PDF/Books/" + id;

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(uri, context));
        storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {

            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            String uploadedBookUrl = DATA.EMPTY + uriTask.getResult();

            uploadBookInfoDB(uploadedBookUrl, id, ref);
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Book upload failed due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void uploadBookInfoDB(String uploadedBookUrl, String id, DatabaseReference ref) {
        dialog.setMessage("Uploading book info...");

        //setup data to upload
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(DATA.PUBLISHER, DATA.EMPTY + DATA.FirebaseUserUid);
        hashMap.put(DATA.ID, id);
        hashMap.put(DATA.TITLE, DATA.EMPTY + title);
        hashMap.put(DATA.DESCRIPTION, DATA.EMPTY + description);
        hashMap.put(DATA.CATEGORY_ID, DATA.EMPTY + selectedId);
        hashMap.put(DATA.URL, DATA.EMPTY + uploadedBookUrl);
        hashMap.put(DATA.TIMESTAMP, System.currentTimeMillis());
        hashMap.put(DATA.VIEWS_COUNT, 0);
        hashMap.put(DATA.DOWNLOADS_COUNT, 0);
        hashMap.put(DATA.LOVES_COUNT, 0);
        hashMap.put(DATA.EDITORS_CHOICE, 0);
        hashMap.put(DATA.IMAGE, DATA.EMPTY + DATA.BASIC);

        //db reference: DB > Books
        assert id != null;
        ref.child(id).setValue(hashMap).addOnSuccessListener(unused -> {
            dialog.dismiss();
            Toast.makeText(context, "Successfully uploaded...", Toast.LENGTH_SHORT).show();
        }).addOnCompleteListener(task -> {
            VOID.incrementItemCount(DATA.USERS, DATA.FirebaseUserUid, DATA.BOOKS_COUNT);
            uploadImage(id);
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Failure to upload to db due to :" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void loadBookCategories() {
        titleList = new ArrayList<>();
        idList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.CATEGORIES);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                titleList.clear();
                idList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String categoryId = DATA.EMPTY + data.child(DATA.ID).getValue();
                    String categoryTitle = DATA.EMPTY + data.child(DATA.CATEGORY).getValue();

                    titleList.add(categoryTitle);
                    idList.add(categoryId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateImageBook(String imageUrl, String bookId) {
        dialog.setMessage("Updating Book Image...");
        dialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        if (imageUri != null) {
            hashMap.put(DATA.IMAGE, DATA.EMPTY + imageUrl);
        } else {
            hashMap.put(DATA.IMAGE, DATA.EMPTY + DATA.BASIC);
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.BOOKS);
        reference.child(bookId).updateChildren(hashMap).addOnSuccessListener(unused -> {
            dialog.dismiss();
            Toast.makeText(context, "Image updated...", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Failed to update db duo to " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void uploadImage(String BookId) {
        dialog.setMessage("Updating Image Book");
        dialog.show();

        String filePathAndName = "BookImages/" + DATA.FirebaseUserUid;

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

    private String selectedId, selectedTitle;

    private void categoryPickDialog() {
        String[] categories = new String[titleList.size()];
        for (int i = 0; i < titleList.size(); i++) {
            categories[i] = titleList.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Pick Category").setItems(categories, (dialog, which) -> {
            selectedTitle = titleList.get(which);
            selectedId = idList.get(which);
            binding.category.setText(selectedTitle);

        }).show();
    }

    private void bookPickIntent() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent, BOOK_PICK_CODE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == BOOK_PICK_CODE) {
                assert data != null;
                uri = data.getData();
                binding.book.setBackgroundResource(R.color.green);
                binding.choose.setText(R.string.ok);
            }
        } else {
            binding.book.setBackgroundResource(R.color.red);
            binding.choose.setText(R.string.choose_book);
            Toast.makeText(context, "Cancelled picking book", Toast.LENGTH_SHORT).show();
        }
    }

    private void pickImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }
}