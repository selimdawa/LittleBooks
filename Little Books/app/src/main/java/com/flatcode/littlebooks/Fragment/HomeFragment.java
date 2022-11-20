package com.flatcode.littlebooks.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.flatcode.littlebooks.Adapter.CategoryAdapter;
import com.flatcode.littlebooks.Adapter.ImageSliderAdapter;
import com.flatcode.littlebooks.Adapter.MainBookAdapter;
import com.flatcode.littlebooks.Model.Book;
import com.flatcode.littlebooks.Model.Category;
import com.flatcode.littlebooks.Unit.CLASS;
import com.flatcode.littlebooks.Unit.DATA;
import com.flatcode.littlebooks.Unit.VOID;
import com.flatcode.littlebooks.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private ArrayList<Book> list, list2, list3, list4, list5;
    private MainBookAdapter adapter, adapter2, adapter3, adapter4, adapter5;
    private Boolean B_one = false, B_two = true, B_three = true, B_four = true, B_five = true;

    int TotalCounts;

    private ArrayList<Category> categoryList;
    private CategoryAdapter categoryAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(LayoutInflater.from(getContext()), container, false);

        VOID.BannerAdTwo(getContext(), binding.adView, DATA.BANNER_SMART_HOME, binding.adView2, DATA.BANNER_SMART_HOME_2);

        binding.showMore.setOnClickListener(v -> VOID.IntentExtra3(getContext(), CLASS.MORE_BOOKS,
                DATA.SHOW_MORE_TYPE, DATA.EDITORS_CHOICE, DATA.SHOW_MORE_NAME,
                binding.name.getText().toString(), DATA.SHOW_MORE_BOOLEAN, DATA.EMPTY + B_one));
        binding.showMore2.setOnClickListener(v -> VOID.IntentExtra3(getContext(), CLASS.MORE_BOOKS,
                DATA.SHOW_MORE_TYPE, DATA.VIEWS_COUNT, DATA.SHOW_MORE_NAME,
                binding.mostViews.getText().toString(), DATA.SHOW_MORE_BOOLEAN, DATA.EMPTY + B_two));
        binding.showMore3.setOnClickListener(v -> VOID.IntentExtra3(getContext(), CLASS.MORE_BOOKS,
                DATA.SHOW_MORE_TYPE, DATA.LOVES_COUNT, DATA.SHOW_MORE_NAME,
                binding.name3.getText().toString(), DATA.SHOW_MORE_BOOLEAN, DATA.EMPTY + B_three));
        binding.showMore4.setOnClickListener(v -> VOID.IntentExtra3(getContext(), CLASS.MORE_BOOKS,
                DATA.SHOW_MORE_TYPE, DATA.DOWNLOADS_COUNT, DATA.SHOW_MORE_NAME,
                binding.name4.getText().toString(), DATA.SHOW_MORE_BOOLEAN, DATA.EMPTY + B_four));
        binding.showMore5.setOnClickListener(v -> VOID.IntentExtra3(getContext(), CLASS.MORE_BOOKS,
                DATA.SHOW_MORE_TYPE, DATA.TIMESTAMP, DATA.SHOW_MORE_NAME,
                binding.name5.getText().toString(), DATA.SHOW_MORE_BOOLEAN, DATA.EMPTY + B_five));

        //RecyclerView Category
        //binding.recyclerCategory.setHasFixedSize(true);
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        binding.recyclerCategory.setAdapter(categoryAdapter);

        //RecyclerView Editor's Choice
        //binding.recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new MainBookAdapter(getContext(), list, true, false, true);
        binding.recyclerView.setAdapter(adapter);

        //RecyclerView Views Count
        //binding.recyclerView2.setHasFixedSize(true);
        list2 = new ArrayList<>();
        adapter2 = new MainBookAdapter(getContext(), list2, false, true, false);
        binding.recyclerView2.setAdapter(adapter2);

        //RecyclerView Loves Count
        //binding.recyclerView3.setHasFixedSize(true);
        list3 = new ArrayList<>();
        adapter3 = new MainBookAdapter(getContext(), list3, false, false, true);
        binding.recyclerView3.setAdapter(adapter3);

        //RecyclerView Downloads Count
        //binding.recyclerView4.setHasFixedSize(true);
        list4 = new ArrayList<>();
        adapter4 = new MainBookAdapter(getContext(), list4, true, false, false);
        binding.recyclerView4.setAdapter(adapter4);

        //RecyclerView New Books
        //binding.recyclerView5.setHasFixedSize(true);
        list5 = new ArrayList<>();
        adapter5 = new MainBookAdapter(getContext(), list5, false, true, true);
        binding.recyclerView5.setAdapter(adapter5);

        FirebaseDatabase.getInstance().getReference(DATA.SLIDER_SHOW).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long counts = snapshot.getChildrenCount();
                TotalCounts = (int) counts;

                binding.imageSlider.setSliderAdapter(new ImageSliderAdapter(getContext(), TotalCounts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        init();

        return binding.getRoot();
    }

    private void init() {
        loadCategories();
        loadPostEditorsChoice(DATA.EDITORS_CHOICE, list, adapter, binding.bar, binding.recyclerView, binding.empty);
        loadPostBy(DATA.VIEWS_COUNT, list2, adapter2, binding.bar2, binding.recyclerView2, binding.empty2);
        loadPostBy(DATA.LOVES_COUNT, list3, adapter3, binding.bar3, binding.recyclerView3, binding.empty3);
        loadPostBy(DATA.DOWNLOADS_COUNT, list4, adapter4, binding.bar4, binding.recyclerView4, binding.empty4);
        loadPostBy(DATA.TIMESTAMP, list5, adapter5, binding.bar5, binding.recyclerView5, binding.empty5);
    }

    private void loadCategories() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.CATEGORIES);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Category category = data.getValue(Category.class);
                    categoryList.add(category);
                }
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadPostBy(String orderBy, ArrayList<Book> list, MainBookAdapter adapter,
                            ProgressBar bar, RecyclerView recyclerView, TextView empty) {
        Query ref = FirebaseDatabase.getInstance().getReference(DATA.BOOKS);
        ref.orderByChild(orderBy).limitToLast(DATA.ORDER_MAIN).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Book item = data.getValue(Book.class);
                    assert item != null;
                    if (!orderBy.equals(DATA.EDITORS_CHOICE))
                        list.add(item);
                }
                adapter.notifyDataSetChanged();
                bar.setVisibility(View.GONE);
                if (!list.isEmpty()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                    if (!orderBy.equals(DATA.EDITORS_CHOICE))
                        Collections.reverse(list);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadPostEditorsChoice(String orderBy, ArrayList<Book> list, MainBookAdapter adapter,
                                       ProgressBar bar, RecyclerView recyclerView, TextView empty) {
        Query ref = FirebaseDatabase.getInstance().getReference(DATA.BOOKS);
        ref.orderByChild(orderBy).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Book item = data.getValue(Book.class);
                    assert item != null;
                    if (orderBy.equals(DATA.EDITORS_CHOICE)) {
                        if (item.getEditorsChoice() == 1 || item.getEditorsChoice() == 2)
                            list.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
                bar.setVisibility(View.GONE);
                if (!list.isEmpty()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                    if (!orderBy.equals(DATA.EDITORS_CHOICE))
                        Collections.reverse(list);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        //init();
        super.onResume();
    }
}