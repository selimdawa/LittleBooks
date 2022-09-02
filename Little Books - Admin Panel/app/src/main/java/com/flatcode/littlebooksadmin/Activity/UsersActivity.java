package com.flatcode.littlebooksadmin.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littlebooksadmin.Adapter.PublisherAdapter;
import com.flatcode.littlebooksadmin.Model.User;
import com.flatcode.littlebooksadmin.R;
import com.flatcode.littlebooksadmin.Unit.DATA;
import com.flatcode.littlebooksadmin.Unit.THEME;
import com.flatcode.littlebooksadmin.databinding.ActivityUsersBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;
import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity {

    private ActivityUsersBinding binding;
    private final Context context = UsersActivity.this;

    ArrayList<User> list;
    PublisherAdapter adapter;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.toolbar.nameSpace.setText(R.string.users);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.toolbar.search.setOnClickListener(v -> {
            binding.toolbar.toolbar.setVisibility(GONE);
            binding.toolbar.toolbarSearch.setVisibility(VISIBLE);
            DATA.searchStatus = true;
        });

        binding.toolbar.close.setOnClickListener(v -> onBackPressed());

        binding.toolbar.textSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapter.getFilter().filter(s);
                } catch (Exception e) {
                    //None
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //binding.recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new PublisherAdapter(context, list);
        binding.recyclerView.setAdapter(adapter);

        binding.all.setOnClickListener(v -> {
            type = DATA.TIMESTAMP;
            getData(DATA.ALL);
        });
        binding.users.setOnClickListener(v -> {
            type = DATA.VIEWS_COUNT;
            getData(DATA.USER);
        });
        binding.publishers.setOnClickListener(v -> {
            type = DATA.LOVES_COUNT;
            getData(DATA.PUBLISHER);
        });
    }

    private void getData(String type) {
        Query ref = FirebaseDatabase.getInstance().getReference(DATA.USERS);
        ref.orderByChild(DATA.USER_NAME).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                int i = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User item = data.getValue(User.class);
                    assert item != null;
                    switch (type) {
                        case "all":
                            list.add(item);
                            i++;
                            break;
                        case "user":
                            if (item.getBooksCount() <= 0) {
                                list.add(item);
                                i++;
                            }
                            break;
                        case "publisher":
                            if (item.getBooksCount() >= 1) {
                                list.add(item);
                                i++;
                            }
                            break;
                    }
                }
                binding.toolbar.number.setText(MessageFormat.format("( {0} )", i));
                adapter.notifyDataSetChanged();
                binding.progress.setVisibility(View.GONE);
                if (!list.isEmpty()) {
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.emptyText.setVisibility(View.GONE);
                } else {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.emptyText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (DATA.searchStatus) {
            binding.toolbar.toolbar.setVisibility(VISIBLE);
            binding.toolbar.toolbarSearch.setVisibility(GONE);
            DATA.searchStatus = false;
            binding.toolbar.textSearch.setText(DATA.EMPTY);
        } else
            super.onBackPressed();
    }

    @Override
    protected void onResume() {
        getData(DATA.ALL);
        super.onResume();
    }

    @Override
    protected void onRestart() {
        getData(DATA.ALL);
        super.onRestart();
    }
}