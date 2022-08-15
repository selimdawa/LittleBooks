package com.flatcode.littlebooksadmin.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littlebooksadmin.Adapter.EditorsChoiceBookAdapter;
import com.flatcode.littlebooksadmin.Model.Book;
import com.flatcode.littlebooksadmin.R;
import com.flatcode.littlebooksadmin.Unit.DATA;
import com.flatcode.littlebooksadmin.Unit.THEME;
import com.flatcode.littlebooksadmin.databinding.ActivityEditorsChoiceAddBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class EditorsChoiceAddActivity extends AppCompatActivity {

    private ActivityEditorsChoiceAddBinding binding;
    Activity activity;
    Context context = activity = EditorsChoiceAddActivity.this;

    List<String> item;
    ArrayList<Book> list;
    EditorsChoiceBookAdapter adapter;

    String editorsChoiceId, type, oldBookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityEditorsChoiceAddBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        editorsChoiceId = intent.getStringExtra(DATA.EDITORS_CHOICE_ID);
        oldBookId = intent.getStringExtra(DATA.OLD_BOOK_ID);
        int id = Integer.parseInt(editorsChoiceId);

        binding.toolbar.nameSpace.setText(R.string.editors_choice);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        type = DATA.TIMESTAMP;

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

        binding.recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new EditorsChoiceBookAdapter(context, activity, oldBookId, list, id);
        binding.recyclerView.setAdapter(adapter);

        binding.all.setOnClickListener(v -> {
            type = DATA.TIMESTAMP;
            getData(type);
        });
        binding.mostViews.setOnClickListener(v -> {
            type = DATA.VIEWS_COUNT;
            getData(type);
        });
        binding.mostLoves.setOnClickListener(v -> {
            type = DATA.LOVES_COUNT;
            getData(type);
        });
        binding.mostDownloads.setOnClickListener(v -> {
            type = DATA.DOWNLOADS_COUNT;
            getData(type);
        });
        binding.favorites.setOnClickListener(v -> {
            type = DATA.VIEWS_COUNT;
            getFavorites(type);
        });

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        getData(type);
    }

    private void getData(String orderBy) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.BOOKS);
        ref.orderByChild(orderBy).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                int i = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Book item = data.getValue(Book.class);
                    assert item != null;
                    if (item.getId() != null)
                        if (item.getEditorsChoice() == 0) {
                            list.add(item);
                            i++;
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

    private void getFavorites(String orderBy) {
        item = new ArrayList();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.FAVORITES).child(DATA.FirebaseUserUid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                item.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    item.add(snapshot.getKey());
                }
                getBooks(orderBy);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getBooks(String orderBy) {
        Query ref = FirebaseDatabase.getInstance().getReference(DATA.BOOKS);
        ref.orderByChild(orderBy).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Book book = snapshot.getValue(Book.class);
                    for (String id : item) {
                        assert book != null;
                        if (book.getId() != null)
                            if (book.getId().equals(id))
                                if (book.getEditorsChoice() == 0)
                                    list.add(book);
                    }
                }
                binding.progress.setVisibility(GONE);
                if (!(list.isEmpty())) {
                    binding.recyclerView.setVisibility(VISIBLE);
                    binding.emptyText.setVisibility(GONE);
                } else {
                    binding.recyclerView.setVisibility(GONE);
                    binding.emptyText.setVisibility(VISIBLE);
                }
                adapter.notifyDataSetChanged();
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
}