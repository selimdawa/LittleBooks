package com.flatcode.littlebooks.Adapter;

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

import com.flatcode.littlebooks.Filter.PdfMainFilter;
import com.flatcode.littlebooks.Model.Book;
import com.flatcode.littlebooks.Unit.CLASS;
import com.flatcode.littlebooks.Unit.DATA;
import com.flatcode.littlebooks.Unit.VOID;
import com.flatcode.littlebooks.databinding.ItemBookMainBinding;

import java.util.ArrayList;

public class MainBookAdapter extends RecyclerView.Adapter<MainBookAdapter.ViewHolder> implements Filterable {

    private ItemBookMainBinding binding;
    private final Context context;

    public ArrayList<Book> list, filterList;
    private PdfMainFilter filter;

    private final Boolean isDownloads, isViews, isLoves;

    public MainBookAdapter(Context context, ArrayList<Book> list, Boolean isDownloads, Boolean isViews, Boolean isLoves) {
        this.context = context;
        this.list = list;
        this.filterList = list;
        this.isDownloads = isDownloads;
        this.isViews = isViews;
        this.isLoves = isLoves;
    }

    @NonNull
    @Override
    public MainBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemBookMainBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final MainBookAdapter.ViewHolder holder, final int position) {

        final Book item = list.get(position);
        String bookId = DATA.EMPTY + item.getId();
        String title = DATA.EMPTY + item.getTitle();
        String image = DATA.EMPTY + item.getImage();
        String viewsCount = DATA.EMPTY + item.getViewsCount();
        String downloadsCount = DATA.EMPTY + item.getDownloadsCount();
        String lovesCount = DATA.EMPTY + item.getLovesCount();

        if (isDownloads) {
            holder.linearDownloads.setVisibility(View.VISIBLE);
        } else {
            holder.linearDownloads.setVisibility(View.GONE);
        }

        if (isLoves) {
            holder.linearLoves.setVisibility(View.VISIBLE);
        } else {
            holder.linearLoves.setVisibility(View.GONE);
        }

        if (isViews) {
            holder.linearViews.setVisibility(View.VISIBLE);
        } else {
            holder.linearViews.setVisibility(View.GONE);
        }

        if (isViews || isLoves || isDownloads) {
            holder.line.setVisibility(View.VISIBLE);
        } else {
            holder.line.setVisibility(View.GONE);
        }

        VOID.Glide(false, context, image, holder.image);

        binding.views.setText(viewsCount);
        binding.downloads.setText(downloadsCount);
        binding.loves.setText(lovesCount);
        binding.name.setText(title);

        VOID.isFavorite(holder.favorites, item.getId(), DATA.FirebaseUserUid);
        holder.favorites.setOnClickListener(view -> VOID.checkFavorite(holder.favorites, bookId));
        holder.itemView.setOnClickListener(v -> VOID.IntentExtra(context, CLASS.BOOK_DETAIL, DATA.BOOK_ID, bookId));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new PdfMainFilter(filterList, this);
        }
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearDownloads, linearLoves, linearViews, linear1, linear2;
        TextView downloads, loves, views;
        ImageView favorites, download, love, view, image;
        LinearLayout item;
        View line;

        public ViewHolder(View itemView) {
            super(itemView);
            image = binding.image;
            linearDownloads = binding.linearDownloads;
            linearLoves = binding.linearLoves;
            linearViews = binding.linearViews;
            favorites = binding.favorites;
            downloads = binding.downloads;
            loves = binding.loves;
            views = binding.views;
            download = binding.download;
            love = binding.love;
            view = binding.view;
            item = binding.item;
            line = binding.line;
            linear1 = binding.linear1;
            linear2 = binding.linear2;
        }
    }
}