package com.flatcode.littlebooks.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.flatcode.littlebooks.Adapter.LinearBookAdapter;
import com.flatcode.littlebooks.Model.Book;
import com.flatcode.littlebooks.Unit.DATA;
import com.flatcode.littlebooks.Unit.THEME;
import com.flatcode.littlebooks.Unit.VOID;
import com.flatcode.littlebooks.databinding.ActivityPageLinearBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;
import java.util.ArrayList;

public class MoreBooksActivity extends AppCompatActivity {

    private ActivityPageLinearBinding binding;
    private Context context = MoreBooksActivity.this;

    ArrayList<Book> list;
    LinearBookAdapter adapter;

    String type, name, isReverse;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityPageLinearBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        type = intent.getStringExtra(DATA.SHOW_MORE_TYPE);
        name = intent.getStringExtra(DATA.SHOW_MORE_NAME);
        isReverse = intent.getStringExtra(DATA.SHOW_MORE_BOOLEAN);

        binding.toolbar.nameSpace.setText(name);

        VOID.BannerAd(context, binding.adView, DATA.BANNER_SMART_MORE_BOOKS);

        if (isReverse.equals("true")) {
            recyclerView = binding.recyclerViewReverse;
        } else if (isReverse.equals("false")) {
            recyclerView = binding.recyclerView;
        }

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

        //recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new LinearBookAdapter(context, list, false);
        recyclerView.setAdapter(adapter);

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
    }

    private void getData(String orderBy) {
        Query ref = FirebaseDatabase.getInstance().getReference(DATA.BOOKS);
        ref.orderByChild(orderBy).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                int i = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Book item = data.getValue(Book.class);
                    assert item != null;
                    if (orderBy.equals(DATA.EDITORS_CHOICE)) {
                        if (item.getEditorsChoice() > 0)
                            list.add(item);
                    } else
                        list.add(item);
                    i++;
                }
                binding.toolbar.number.setText(MessageFormat.format("( {0} )", i));
                adapter.notifyDataSetChanged();
                binding.progress.setVisibility(View.GONE);
                if (!list.isEmpty()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    binding.emptyText.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
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
        getData(type);
        super.onResume();
    }

    @Override
    protected void onRestart() {
        getData(type);
        super.onRestart();
    }
}