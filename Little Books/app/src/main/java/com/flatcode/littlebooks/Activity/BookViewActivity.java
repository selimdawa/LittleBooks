package com.flatcode.littlebooks.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littlebooks.Unit.DATA;
import com.flatcode.littlebooks.Unit.THEME;
import com.flatcode.littlebooks.databinding.ActivityBookViewBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.MessageFormat;

public class BookViewActivity extends AppCompatActivity {

    private ActivityBookViewBinding binding;
    Context context = BookViewActivity.this;

    String bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityBookViewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        bookId = intent.getStringExtra(DATA.BOOK_ID);

        loadBookDetails();

        binding.toolbar.numberPage.setVisibility(View.VISIBLE);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
    }

    private void loadBookDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.BOOKS);
        ref.child(bookId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String pdfUrl = DATA.EMPTY + snapshot.child(DATA.URL).getValue();
                loadBookFromUrl(pdfUrl);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadBookFromUrl(String pdfUrl) {
        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        reference.getBytes(DATA.MAX_BYTES_PDF).addOnSuccessListener(bytes -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.pdfView.fromBytes(bytes).swipeHorizontal(false).onPageChange((page, pageCount) -> {
                int correctPage = (page + 1);
                binding.toolbar.numberPage.setText(MessageFormat.format("{0}/{1}", correctPage, pageCount));
            }).onError(t -> Toast.makeText(context, DATA.EMPTY + t.getMessage(), Toast.LENGTH_SHORT).show())
                    .onPageError((page, t) -> Toast.makeText(context, "Error on page " + page + DATA.SPACE +
                            t.getMessage(), Toast.LENGTH_SHORT).show()).load();
            binding.progressBar.setVisibility(View.GONE);
        }).addOnFailureListener(e -> {
            binding.progressBar.setVisibility(View.GONE);
        });
    }
}