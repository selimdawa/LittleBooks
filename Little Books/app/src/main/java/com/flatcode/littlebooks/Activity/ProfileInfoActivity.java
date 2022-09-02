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

import com.flatcode.littlebooks.Adapter.StaggeredBookAdapter;
import com.flatcode.littlebooks.Model.Book;
import com.flatcode.littlebooks.R;
import com.flatcode.littlebooks.Unit.DATA;
import com.flatcode.littlebooks.Unit.THEME;
import com.flatcode.littlebooks.Unit.VOID;
import com.flatcode.littlebooks.databinding.ActivityPageStaggeredSwitchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;

public class ProfileInfoActivity extends AppCompatActivity {

    private ActivityPageStaggeredSwitchBinding binding;
    private Context context = ProfileInfoActivity.this;

    ArrayList<Book> list;
    StaggeredBookAdapter adapter;

    private String type, profileId;
    private Boolean isCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityPageStaggeredSwitchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        profileId = intent.getStringExtra(DATA.PROFILE_ID);

        binding.toolbar.nameSpace.setText(R.string.publishers_books);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        type = DATA.TIMESTAMP;

        VOID.BannerAd(context, binding.adView, DATA.BANNER_SMART_PUBLISHERS_BOOKS);

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
        adapter = new StaggeredBookAdapter(context, list);
        binding.recyclerView.setAdapter(adapter);

        binding.switchBar.all.setOnClickListener(v -> {
            type = DATA.TIMESTAMP;
            getData(type);
        });
        binding.switchBar.name.setOnClickListener(v -> {
            type = DATA.TITLE;
            getData(type);
        });
        binding.switchBar.mostViews.setOnClickListener(v -> {
            type = DATA.VIEWS_COUNT;
            getData(type);
        });
        binding.switchBar.mostLoves.setOnClickListener(v -> {
            type = DATA.LOVES_COUNT;
            getData(type);
        });
        binding.switchBar.mostDownloads.setOnClickListener(v -> {
            type = DATA.DOWNLOADS_COUNT;
            getData(type);
        });
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
                    if (item.getPublisher().equals(profileId)) {
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
                    Collections.reverse(list);
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
        isCheck = true;
        if (isCheck) {
            getData(DATA.TIMESTAMP);
        }
        super.onResume();
    }

    @Override
    protected void onRestart() {
        isCheck = true;
        if (isCheck) {
            getData(DATA.TIMESTAMP);
        }
        super.onRestart();
    }
}