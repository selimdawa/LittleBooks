package com.flatcode.littlebooksadmin.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flatcode.littlebooksadmin.Filter.EditorsChoiceBookFilter;
import com.flatcode.littlebooksadmin.Model.Book;
import com.flatcode.littlebooksadmin.Unit.CLASS;
import com.flatcode.littlebooksadmin.Unit.DATA;
import com.flatcode.littlebooksadmin.Unit.VOID;
import com.flatcode.littlebooksadmin.databinding.ItemEditorsChoiceBinding;

import java.util.ArrayList;

public class EditorsChoiceBookAdapter extends RecyclerView.Adapter<EditorsChoiceBookAdapter.ViewHolder> implements Filterable {

    private ItemEditorsChoiceBinding binding;
    private final Context context;
    private final Activity activity;

    public ArrayList<Book> list, filterList;
    private EditorsChoiceBookFilter filter;
    public int number;
    public String oldBookId;

    public EditorsChoiceBookAdapter(Context context, Activity activity, String oldBookId, ArrayList<Book> list, int number) {
        this.oldBookId = oldBookId;
        this.context = context;
        this.activity = activity;
        this.list = list;
        this.filterList = list;
        this.number = number;
    }

    @NonNull
    @Override
    public EditorsChoiceBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemEditorsChoiceBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final EditorsChoiceBookAdapter.ViewHolder holder, final int position) {

        final Book item = list.get(position);
        String bookId = DATA.EMPTY + item.getId();
        String title = DATA.EMPTY + item.getTitle();
        String description = DATA.EMPTY + item.getDescription();
        String image = DATA.EMPTY + item.getImage();
        String nrViews = DATA.EMPTY + item.getViewsCount();
        String nrLoves = DATA.EMPTY + item.getLovesCount();
        String nrDownloads = DATA.EMPTY + item.getDownloadsCount();

        VOID.Glide(false, context, image, holder.image);

        if (item.getTitle().equals(DATA.EMPTY)) {
            holder.title.setVisibility(View.GONE);
        } else {
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText(title);
        }

        if (item.getDescription().equals(DATA.EMPTY)) {
            holder.description.setVisibility(View.GONE);
        } else {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(description);
        }

        holder.numberViews.setText(nrViews);
        holder.numberLoves.setText(nrLoves);
        holder.numberDownloads.setText(nrDownloads);

        holder.add.setOnClickListener(view -> {
            if (oldBookId != null) {
                VOID.addToEditorsChoice(context, activity, bookId, number);
                VOID.addToEditorsChoice(context, activity, oldBookId, 0);
            } else {
                VOID.addToEditorsChoice(context, activity, bookId, number);
            }
        });

        holder.item.setOnClickListener(view ->
                VOID.IntentExtra(context, CLASS.BOOK_DETAIL, DATA.BOOK_ID, item.getId()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new EditorsChoiceBookFilter(filterList, this);
        }
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image, add;
        TextView title, description, numberViews, numberLoves, numberDownloads;
        LinearLayout item;

        public ViewHolder(View view) {
            super(view);
            image = binding.image;
            title = binding.title;
            description = binding.description;
            add = binding.add;
            numberViews = binding.numberViews;
            numberLoves = binding.numberLoves;
            numberDownloads = binding.numberDownloads;
            item = binding.item;
        }
    }
}