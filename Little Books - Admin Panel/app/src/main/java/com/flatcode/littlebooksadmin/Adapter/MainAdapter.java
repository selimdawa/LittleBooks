package com.flatcode.littlebooksadmin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flatcode.littlebooksadmin.Model.Main;
import com.flatcode.littlebooksadmin.R;
import com.flatcode.littlebooksadmin.Unit.DATA;
import com.flatcode.littlebooksadmin.databinding.ItemMainBinding;

import java.text.MessageFormat;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ItemMainBinding binding;
    private final Context context;
    public List<Main> list;

    public MainAdapter(Context context, List<Main> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemMainBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Main model = list.get(position);
        int image = model.getImage();
        int number = model.getNumber();
        String name = model.getTitle();
        //String id = list.getId();
        Class c = model.getC();

        if (image != 0) {
            holder.image.setImageResource(image);
        } else {
            holder.image.setImageResource(R.drawable.ic_load);
        }

        if (number != 0) {
            holder.number.setVisibility(View.VISIBLE);
            holder.number.setText(MessageFormat.format("{0}{1}", DATA.EMPTY, number));
        } else {
            holder.number.setVisibility(View.GONE);
        }

        holder.name.setText(name);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, c);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, number;
        ImageView image;
        LinearLayout item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = binding.image;
            name = binding.name;
            number = binding.number;
            item = binding.item;
        }
    }
}