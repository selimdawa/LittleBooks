package com.flatcode.littlebooks.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.flatcode.littlebooks.Model.Category;
import com.flatcode.littlebooks.Unit.CLASS;
import com.flatcode.littlebooks.Unit.DATA;
import com.flatcode.littlebooks.Unit.VOID;
import com.flatcode.littlebooks.databinding.ItemCategoryMainBinding;

import java.util.ArrayList;

public class CategoryMainAdapter extends RecyclerView.Adapter<CategoryMainAdapter.ViewHolder> {

    private ItemCategoryMainBinding binding;
    private final Context context;

    public ArrayList<Category> list;

    public CategoryMainAdapter(Context context, ArrayList<Category> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemCategoryMainBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Category item = list.get(position);
        String id = DATA.EMPTY + item.getId();
        String name = DATA.EMPTY + item.getCategory();
        String image = DATA.EMPTY + item.getImage();

        VOID.Glide(false, context, image, holder.image);
        VOID.GlideBlur(false, context, image, holder.imageBlur, 50);

        if (name.equals(DATA.EMPTY)) {
            holder.name.setVisibility(View.GONE);
        } else {
            holder.name.setVisibility(View.VISIBLE);
            holder.name.setText(name);
        }

        holder.card.setOnClickListener(v ->
                VOID.IntentExtra2(context, CLASS.CATEGORY_BOOKS, DATA.CATEGORY_ID, id, DATA.CATEGORY_NAME, name));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image, imageBlur;
        TextView name;
        CardView card;

        public ViewHolder(View view) {
            super(view);
            image = binding.image;
            imageBlur = binding.imageBlur;
            name = binding.name;
            card = binding.card;
        }
    }
}