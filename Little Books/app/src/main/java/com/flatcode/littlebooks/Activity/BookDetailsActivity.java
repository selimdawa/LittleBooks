package com.flatcode.littlebooks.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.flatcode.littlebooks.Adapter.CommentAdapter;
import com.flatcode.littlebooks.Model.Book;
import com.flatcode.littlebooks.Model.Comment;
import com.flatcode.littlebooks.Model.User;
import com.flatcode.littlebooks.MyApplication;
import com.flatcode.littlebooks.R;
import com.flatcode.littlebooks.Unit.CLASS;
import com.flatcode.littlebooks.Unit.DATA;
import com.flatcode.littlebooks.Unit.THEME;
import com.flatcode.littlebooks.Unit.VOID;
import com.flatcode.littlebooks.databinding.ActivityBookDetailsBinding;
import com.flatcode.littlebooks.databinding.DialogCommentAddBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BookDetailsActivity extends AppCompatActivity {

    private ActivityBookDetailsBinding binding;
    Context context = BookDetailsActivity.this;

    String bookId, bookTitle, bookUrl;

    private ProgressDialog dialog;

    private ArrayList<Comment> list;
    private CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityBookDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        bookId = intent.getStringExtra(DATA.BOOK_ID);

        binding.toolbar.nameSpace.setText(R.string.details_books);
        binding.download.setVisibility(View.GONE);

        dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait...");
        dialog.setCanceledOnTouchOutside(false);

        //RecyclerView New Books
        //binding.recyclerView5.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new CommentAdapter(context, list);
        binding.recyclerView.setAdapter(adapter);

        binding.love.setOnClickListener(v -> VOID.checkLove(binding.love, bookId));
        binding.favorite.setOnClickListener(v -> VOID.checkFavorite(binding.favorite, bookId));
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.read.setOnClickListener(v -> VOID.IntentExtra(context, CLASS.BOOK_VIEW, DATA.BOOK_ID, bookId));

        binding.download.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                VOID.downloadBook(context, DATA.EMPTY + bookId, DATA.EMPTY + bookTitle, DATA.EMPTY + bookUrl);
            } else {
                resultPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        });

        binding.addComment.setOnClickListener(v -> {
            if (DATA.FIREBASE_USER == null) {
                Toast.makeText(context, "You're not logged in...", Toast.LENGTH_SHORT).show();
            } else {
                addCommentDialog();
            }
        });
    }

    private void init() {
        loadBookDetails();
        loadComments();
        VOID.incrementItemCount(DATA.BOOKS, bookId, DATA.VIEWS_COUNT);
        VOID.isLoves(binding.love, bookId);
        VOID.nrLoves(binding.loves, bookId);
        VOID.isFavorite(binding.favorite, bookId, DATA.FirebaseUserUid);
    }

    private void loadComments() {
        list = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.BOOKS);
        ref.child(bookId).child(DATA.COMMENTS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Comment comment = data.getValue(Comment.class);
                    list.add(comment);
                }

                adapter.notifyDataSetChanged();
                if (list.isEmpty())
                    binding.textComment.setVisibility(View.GONE);
                else
                    binding.textComment.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String comment = DATA.EMPTY;

    private void addCommentDialog() {
        DialogCommentAddBinding commentAddBinding = DialogCommentAddBinding.inflate(LayoutInflater.from(this));

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
        builder.setView(commentAddBinding.getRoot());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        commentAddBinding.back.setOnClickListener(v -> alertDialog.dismiss());
        commentAddBinding.submit.setOnClickListener(v -> {
            comment = commentAddBinding.comment.getText().toString().trim();

            if (TextUtils.isEmpty(comment)) {
                Toast.makeText(context, "Enter your comment...", Toast.LENGTH_SHORT).show();
            } else {
                alertDialog.dismiss();
                addComment();
            }
        });
    }

    private void addComment() {
        dialog.setMessage("Adding comment...");
        dialog.show();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.BOOKS);
        String id = ref.push().getKey();
        //setup data to add in db for comment
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(DATA.ID, id);
        hashMap.put(DATA.BOOK_ID, DATA.EMPTY + bookId);
        hashMap.put(DATA.TIMESTAMP, System.currentTimeMillis());
        hashMap.put(DATA.COMMENT, DATA.EMPTY + comment);
        hashMap.put(DATA.PUBLISHER, DATA.EMPTY + DATA.FirebaseUserUid);

        assert id != null;
        ref.child(bookId).child(DATA.COMMENTS).child(id).setValue(hashMap).addOnSuccessListener(unused -> {
            Toast.makeText(context, "Comment Added...", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Failed to add comment duo to  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private final ActivityResultLauncher<String> resultPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            VOID.downloadBook(context, DATA.EMPTY + bookId, DATA.EMPTY + bookTitle, DATA.EMPTY + bookUrl);
        } else {
            Toast.makeText(context, "Permission was denied...", Toast.LENGTH_SHORT).show();
        }
    });

    private void loadBookDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.BOOKS);
        ref.child(bookId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get data
                Book item = snapshot.getValue(Book.class);
                assert item != null;
                bookTitle = DATA.EMPTY + item.getTitle();
                String description = DATA.EMPTY + item.getDescription();
                String categoryId = DATA.EMPTY + item.getCategoryId();
                String viewsCount = DATA.EMPTY + item.getViewsCount();
                String downloadsCount = DATA.EMPTY + item.getDownloadsCount();
                bookUrl = DATA.EMPTY + item.getUrl();
                String timestamp = DATA.EMPTY + item.getTimestamp();
                String image = DATA.EMPTY + item.getImage();
                String publisher = DATA.EMPTY + item.getPublisher();

                binding.download.setVisibility(View.VISIBLE);

                //format date
                String date = MyApplication.formatTimestamp(Long.parseLong(timestamp));

                VOID.loadCategory(DATA.EMPTY + categoryId, binding.category);
                VOID.loadPdfInfo(DATA.EMPTY + bookUrl, binding.size);
                //set data
                VOID.Glide(false, context, image, binding.image);
                VOID.Glide(false, context, image, binding.cover);
                binding.title.setText(bookTitle);
                binding.description.setText(description);
                binding.views.setText(viewsCount);
                binding.downloads.setText(downloadsCount);
                binding.date.setText(date);
                userInfo(publisher);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void userInfo(String userId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.USERS);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get data
                if (snapshot.child(userId).exists()) {
                    User item = snapshot.child(userId).getValue(User.class);
                    assert item != null;
                    String userId = DATA.EMPTY + item.getId();
                    String imageProfile = DATA.EMPTY + item.getProfileImage();
                    String username = DATA.EMPTY + item.getUsername();

                    binding.publisherName.setText(username);
                    VOID.Glide(true, context, imageProfile, binding.publisherImage);

                    binding.userInfo.setOnClickListener(v -> VOID.IntentExtra(context, CLASS.PROFILE, DATA.PROFILE_ID, userId));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        init();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        init();
        super.onRestart();
    }
}