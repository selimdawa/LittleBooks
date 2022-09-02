package com.flatcode.littlebooks.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littlebooks.Model.Book;
import com.flatcode.littlebooks.R;
import com.flatcode.littlebooks.Unit.CLASS;
import com.flatcode.littlebooks.Unit.DATA;
import com.flatcode.littlebooks.Unit.THEME;
import com.flatcode.littlebooks.Unit.VOID;
import com.flatcode.littlebooks.databinding.ActivityProfileBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    Context context = ProfileActivity.this;

    String profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        profileId = intent.getStringExtra(DATA.PROFILE_ID);

        if (profileId.equals(DATA.FirebaseUserUid)) {
            binding.follow.setVisibility(View.GONE);
            binding.editOrInfo.setImageResource(R.drawable.ic_edit_white);
            binding.editOrInfo.setOnClickListener(v -> VOID.Intent(context, ProfileEditActivity.class));
        } else {
            binding.follow.setVisibility(View.VISIBLE);
            binding.editOrInfo.setImageResource(R.drawable.ic_books);
            binding.editOrInfo.setOnClickListener(v ->
                    VOID.IntentExtra(context, CLASS.PROFILE_INFO, DATA.PROFILE_ID, profileId));
        }

        binding.back.setOnClickListener(v -> onBackPressed());
        isFollowing(binding.follow, profileId);

        binding.follow.setOnClickListener(v -> {
            if (binding.follow.getTag().equals("add")) {
                FirebaseDatabase.getInstance().getReference().child(DATA.FOLLOW).child(DATA.FirebaseUserUid)
                        .child(DATA.FOLLOWING).child(profileId).setValue(true);
                FirebaseDatabase.getInstance().getReference().child(DATA.FOLLOW).child(profileId)
                        .child(DATA.FOLLOWERS).child(DATA.FirebaseUserUid).setValue(true);
            } else {
                FirebaseDatabase.getInstance().getReference().child(DATA.FOLLOW).child(DATA.FirebaseUserUid)
                        .child(DATA.FOLLOWING).child(profileId).removeValue();
                FirebaseDatabase.getInstance().getReference().child(DATA.FOLLOW).child(profileId)
                        .child(DATA.FOLLOWERS).child(DATA.FirebaseUserUid).removeValue();
            }
        });
    }

    private void init() {
        loadUserInfo();
        getNrBooks();
        nrItemFollow(DATA.FOLLOWERS, binding.numberFollowers);
        nrItemFollow(DATA.FOLLOWING, binding.numberFollowing);
        nrItemFavorites();
    }

    private void loadUserInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.USERS);
        reference.child(profileId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //String email = DATA.EMPTY + snapshot.child(DATA.EMAIL).getValue();
                String username = DATA.EMPTY + snapshot.child(DATA.USER_NAME).getValue();
                String profileImage = DATA.EMPTY + snapshot.child(DATA.PROFILE_IMAGE).getValue();
                //String timestamp = DATA.EMPTY + snapshot.child(DATA.TIMESTAMP).getValue();
                //String id = DATA.EMPTY + snapshot.child(DATA.ID).getValue();
                //String version = DATA.EMPTY + snapshot.child(DATA.VERSION).getValue();

                binding.username.setText(username);

                VOID.Glide(true, context, profileImage, binding.profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getNrBooks() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.BOOKS);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Book item = data.getValue(Book.class);
                    assert item != null;
                    if (item.getPublisher().equals(profileId))
                        i++;
                }
                binding.numberBooks.setText(MessageFormat.format("{0}{1}", DATA.EMPTY, i));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void nrItemFollow(String type, TextView number) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child(DATA.FOLLOW).child(profileId).child(type);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                number.setText(MessageFormat.format("{0}", dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void nrItemFavorites() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child(DATA.FAVORITES).child(profileId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                binding.numberFavorites.setText(MessageFormat.format("{0}", dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void isFollowing(final ImageView add, final String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child(DATA.FOLLOW).child(DATA.FirebaseUserUid).child(DATA.FOLLOWING);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userId).exists()) {
                    add.setImageResource(R.drawable.ic_heart_selected);
                    add.setTag("added");
                } else {
                    add.setImageResource(R.drawable.ic_heart_unselected);
                    add.setTag("add");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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