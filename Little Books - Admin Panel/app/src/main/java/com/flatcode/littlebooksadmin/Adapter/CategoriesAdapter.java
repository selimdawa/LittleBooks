package com.flatcode.littlebooksadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flatcode.littlebooksadmin.Filter.CategoriesFilter;
import com.flatcode.littlebooksadmin.Model.Book;
import com.flatcode.littlebooksadmin.Model.Category;
import com.flatcode.littlebooksadmin.Unit.CLASS;
import com.flatcode.littlebooksadmin.Unit.DATA;
import com.flatcode.littlebooksadmin.Unit.VOID;
import com.flatcode.littlebooksadmin.databinding.ItemCategoriesBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;
import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> implements Filterable {

    private ItemCategoriesBinding binding;
    private final Context context;

    public ArrayList<Category> list, filterList;
    private CategoriesFilter filter;

    public CategoriesAdapter(Context context, ArrayList<Category> list) {
        this.context = context;
        this.list = list;
        this.filterList = list;
    }

    @NonNull
    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemCategoriesBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoriesAdapter.ViewHolder holder, final int position) {

        final Category item = list.get(position);
        String categoryId = DATA.EMPTY + item.getId();
        String name = DATA.EMPTY + item.getCategory();
        String image = DATA.EMPTY + item.getImage();

        VOID.Glide(false, context, image, holder.image);

        if (item.getCategory().equals(DATA.EMPTY)) {
            holder.name.setVisibility(View.GONE);
        } else {
            holder.name.setVisibility(View.VISIBLE);
            holder.name.setText(name);
        }

        nrBooks(holder.numberBooks, categoryId);
        holder.more.setOnClickListener(v -> VOID.moreCategories(context, item));

        holder.item.setOnClickListener(view ->
                VOID.IntentExtra2(context, CLASS.CATEGORY_BOOKS, DATA.CATEGORY_ID, categoryId, DATA.CATEGORY_NAME, name));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CategoriesFilter(filterList, this);
        }
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        ImageButton more;
        TextView name, numberBooks;
        LinearLayout item;

        public ViewHolder(View view) {
            super(view);
            image = binding.image;
            name = binding.name;
            more = binding.more;
            numberBooks = binding.numberBooks;
            item = binding.item;
        }
    }

    private void nrBooks(TextView number, String categoryId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(DATA.BOOKS);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Book item = snapshot.getValue(Book.class);
                    assert item != null;
                    if (item.getCategoryId().equals(categoryId))
                        i++;
                }
                number.setText(MessageFormat.format("{0}{1}", DATA.EMPTY, i));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}