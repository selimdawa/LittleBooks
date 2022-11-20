package com.flatcode.littlebooks.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.flatcode.littlebooks.Adapter.CategoryMainAdapter;
import com.flatcode.littlebooks.Model.Category;
import com.flatcode.littlebooks.Unit.DATA;
import com.flatcode.littlebooks.databinding.FragmentCategoriesBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {

    private FragmentCategoriesBinding binding;

    private ArrayList<Category> list;
    private CategoryMainAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCategoriesBinding.inflate(LayoutInflater.from(getContext()), container, false);

        //binding.recyclerCategory.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new CategoryMainAdapter(getContext(), list);
        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    private void loadItems() {
        Query ref = FirebaseDatabase.getInstance().getReference(DATA.CATEGORIES);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Category item = snapshot.getValue(Category.class);
                    assert item != null;
                        list.add(item);
                }
                binding.bar.setVisibility(View.GONE);
                if (!(list.isEmpty())) {
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.emptyText.setVisibility(View.GONE);
                } else {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.emptyText.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        loadItems();
        super.onResume();
    }
}