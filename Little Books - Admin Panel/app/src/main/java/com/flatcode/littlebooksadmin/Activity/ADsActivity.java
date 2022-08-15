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

import com.flatcode.littlebooksadmin.Adapter.ADsUserAdapter;
import com.flatcode.littlebooksadmin.Model.User;
import com.flatcode.littlebooksadmin.R;
import com.flatcode.littlebooksadmin.Unit.DATA;
import com.flatcode.littlebooksadmin.Unit.THEME;
import com.flatcode.littlebooksadmin.databinding.ActivityAdsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;
import java.util.ArrayList;

public class ADsActivity extends AppCompatActivity {

    private ActivityAdsBinding binding;
    private Context context = ADsActivity.this;

    ArrayList<User> list;
    ADsUserAdapter adapter;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityAdsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        type = DATA.AD_LOAD;

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

        binding.toolbar.nameSpace.setText(R.string.users_ads);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        //binding.recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new ADsUserAdapter(context, list, true);
        binding.recyclerView.setAdapter(adapter);

        binding.name.setOnClickListener(v -> {
            type = DATA.USER_NAME;
            getData(type);
        });
        binding.adLoad.setOnClickListener(v -> {
            type = DATA.AD_LOAD;
            getData(type);
        });
        binding.adClick.setOnClickListener(v -> {
            type = DATA.AD_CLICK;
            getData(type);
        });
        binding.timestamp.setOnClickListener(v -> {
            type = DATA.TIMESTAMP;
            getData(type);
        });

        getData(type);
    }

    private void getData(String orderBy) {
        Query ref = FirebaseDatabase.getInstance().getReference(DATA.USERS);
        ref.orderByChild(orderBy).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                int i = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User item = data.getValue(User.class);
                    assert item != null;
                    if (!(item.getAdLoad() == 0 && item.getAdClick() == 0)){
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