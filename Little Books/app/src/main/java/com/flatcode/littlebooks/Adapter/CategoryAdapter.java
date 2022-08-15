package com.flatcode.littlebooks.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flatcode.littlebooks.Model.Category;
import com.flatcode.littlebooks.Unit.CLASS;
import com.flatcode.littlebooks.Unit.DATA;
import com.flatcode.littlebooks.Unit.VOID;
import com.flatcode.littlebooks.databinding.ItemCategoryBinding;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final Context context;
    public ArrayList<Category> list;

    private static ItemCategoryBinding binding;

    public CategoryAdapter(Context context, ArrayList<Category> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryAdapter.ViewHolder holder, final int position) {

        final Category item = list.get(position);

        String id = item.getId();
        String name = item.getCategory();
        String image = item.getImage();

        VOID.Glide(false, context, image, binding.image);

        holder.itemView.setOnClickListener(v ->
                VOID.IntentExtra2(context, CLASS.CATEGORY_BOOKS, DATA.CATEGORY_ID, id, DATA.CATEGORY_NAME, name));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = binding.image;
        }
    }
}