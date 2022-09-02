package com.flatcode.littlebooks.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.flatcode.littlebooks.Adapter.LinearBookAdapter;
import com.flatcode.littlebooks.Model.Book;
import com.flatcode.littlebooks.Unit.DATA;
import com.flatcode.littlebooks.Unit.VOID;
import com.flatcode.littlebooks.databinding.FragmentFollowersBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FollowersFragment extends Fragment {

    private FragmentFollowersBinding binding;

    List<String> check;
    ArrayList<Book> list;
    LinearBookAdapter adapter;

    private String type;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFollowersBinding.inflate(LayoutInflater.from(getContext()), container, false);

        type = DATA.TIMESTAMP;

        VOID.BannerAd(getContext(), binding.adView, DATA.BANNER_SMART_FOLLOWERS_BOOKS);

        binding.recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new LinearBookAdapter(getContext(), list, false);
        binding.recyclerView.setAdapter(adapter);

        binding.switchBar.all.setOnClickListener(v -> {
            type = DATA.TIMESTAMP;
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

        return binding.getRoot();
    }

    private void getData(String orderBy) {
        check = new ArrayList();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.FOLLOW)
                .child(DATA.FirebaseUserUid).child(DATA.FOLLOWING);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                check.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    check.add(snapshot.getKey());
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
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Book item = data.getValue(Book.class);
                    for (String id : check) {
                        assert item != null;
                        if (item.getPublisher().equals(id))
                            list.add(item);
                    }
                }
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
    public void onResume() {
        getData(DATA.TIMESTAMP);
        super.onResume();
    }
}