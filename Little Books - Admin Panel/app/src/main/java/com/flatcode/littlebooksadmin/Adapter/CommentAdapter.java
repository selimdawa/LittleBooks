package com.flatcode.littlebooksadmin.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flatcode.littlebooksadmin.Model.Comment;
import com.flatcode.littlebooksadmin.Model.User;
import com.flatcode.littlebooksadmin.MyApplication;
import com.flatcode.littlebooksadmin.Unit.DATA;
import com.flatcode.littlebooksadmin.Unit.VOID;
import com.flatcode.littlebooksadmin.databinding.ItemCommentBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private final Context context;
    public ArrayList<Comment> list;

    private static ItemCommentBinding binding;

    public CommentAdapter(Context context, ArrayList<Comment> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemCommentBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Comment item = list.get(position);

        String commentId = DATA.EMPTY + item.getId();
        String bookId = DATA.EMPTY + item.getId();
        String comment = DATA.EMPTY + item.getComment();
        String publisher = DATA.EMPTY + item.getPublisher();
        String timestamp = DATA.EMPTY + item.getTimestamp();

        String date = MyApplication.formatTimestamp(Long.parseLong(timestamp));

        holder.date.setText(date);
        holder.comment.setText(comment);

        loadUserDetails(publisher, holder.name);

        holder.itemView.setOnClickListener(v -> {
            if (publisher.equals(DATA.FirebaseUserUid))
                deleteComment(commentId, bookId);
        });
    }

    private void deleteComment(String commentId, String bookId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Comment").setMessage("Are you sure you want to delete this comment?")
                .setPositiveButton("DELETE", (dialog, which) -> {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.BOOKS);
                    ref.child(bookId).child(DATA.COMMENTS).child(commentId).removeValue()
                            .addOnSuccessListener(unused -> Toast.makeText(context,
                                    "Deleted...", Toast.LENGTH_SHORT).show()).addOnFailureListener(e ->
                                    Toast.makeText(context, "Failed to delete duo to " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }).setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss()).show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profile;
        TextView name, comment, date;
        LinearLayout item;

        public ViewHolder(View itemView) {
            super(itemView);
            profile = binding.profile;
            name = binding.name;
            comment = binding.comment;
            date = binding.date;
            item = binding.item;
        }
    }

    private void loadUserDetails(String publisher, TextView name) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.USERS);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(publisher).exists()) {
                    User item = snapshot.child(publisher).getValue(User.class);
                    assert item != null;
                    String username = item.getUsername();
                    String profileImage = item.getProfileImage();

                    VOID.Glide(true, context, profileImage, binding.profile);
                    name.setText(username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}