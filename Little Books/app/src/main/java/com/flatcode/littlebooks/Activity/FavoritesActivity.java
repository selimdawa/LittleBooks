package com.flatcode.littlebooks.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private ActivityPageStaggeredSwitchBinding binding;
    private final Context context = FavoritesActivity.this;

    List<String> item;
    ArrayList<Book> list;
    StaggeredBookAdapter adapter;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityPageStaggeredSwitchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.toolbar.nameSpace.setText(R.string.favorites);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        type = DATA.TIMESTAMP;

        VOID.BannerAd(context, binding.adView, DATA.BANNER_SMART_FAVORITES);

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
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Book pdf = snapshot.getValue(Book.class);
                    for (String id : item) {
                        assert pdf != null;
                        if (pdf.getId() != null)
                            if (pdf.getId().equals(id)) {
                                list.add(pdf);
                                i++;
                            }
                    }
                }
                binding.toolbar.number.setText(MessageFormat.format("( {0} )", i));
                binding.progress.setVisibility(GONE);
                if (!(list.isEmpty())) {
                    binding.recyclerView.setVisibility(VISIBLE);
                    binding.emptyText.setVisibility(GONE);
                    Collections.reverse(list);
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