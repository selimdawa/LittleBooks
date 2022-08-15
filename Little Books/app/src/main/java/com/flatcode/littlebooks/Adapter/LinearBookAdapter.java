package com.flatcode.littlebooks.Adapter;

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

import com.flatcode.littlebooks.Filter.MoreBooksFilter;
import com.flatcode.littlebooks.Model.Book;
import com.flatcode.littlebooks.Unit.CLASS;
import com.flatcode.littlebooks.Unit.DATA;
import com.flatcode.littlebooks.Unit.VOID;
import com.flatcode.littlebooks.databinding.ItemBookLinearBinding;

import java.util.ArrayList;

public class LinearBookAdapter extends RecyclerView.Adapter<LinearBookAdapter.ViewHolder> implements Filterable {

    private ItemBookLinearBinding binding;
    private final Context context;

    public ArrayList<Book> list, filterList;
    private MoreBooksFilter filter;
    Boolean isUser;

    public LinearBookAdapter(Context context, ArrayList<Book> list, Boolean isUser) {
        this.context = context;
        this.list = list;
        this.filterList = list;
        this.isUser = isUser;
    }

    @NonNull
    @Override
    public LinearBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemBookLinearBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final LinearBookAdapter.ViewHolder holder, final int position) {

        final Book item = list.get(position);
        String bookId = DATA.EMPTY + item.getId();
        String title = DATA.EMPTY + item.getTitle();
        String description = DATA.EMPTY + item.getDescription();
        String image = DATA.EMPTY + item.getImage();
        String nrViews = DATA.EMPTY + item.getViewsCount();
        String nrLoves = DATA.EMPTY + item.getLovesCount();
        String nrDownloads = DATA.EMPTY + item.getDownloadsCount();

        if (isUser) {
            holder.more.setVisibility(View.VISIBLE);
        } else {
            holder.more.setVisibility(View.GONE);
        }

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

         /*if (item.getPublisher().equals(DATA.FirebaseUserUid)) {
            holder.favorites.setVisibility(View.GONE);
        } else {
            holder.favorites.setVisibility(View.VISIBLE);
        }*/

        VOID.isFavorite(holder.favorites, item.getId(), DATA.FirebaseUserUid);
        VOID.isLoves(holder.loves, item.getId());
        holder.favorites.setOnClickListener(view -> VOID.checkFavorite(holder.favorites, bookId));
        holder.loves.setOnClickListener(view -> VOID.checkLove(holder.loves, bookId));
        holder.more.setOnClickListener(view -> VOID.moreOptionDialog(context, item));

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
            filter = new MoreBooksFilter(filterList, this);
        }
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image, favorites, loves,more;
        TextView title, description, numberViews, numberLoves, numberDownloads;
        LinearLayout item;

        public ViewHolder(View view) {
            super(view);
            image = binding.image;
            title = binding.title;
            more = binding.more;
            description = binding.description;
            favorites = binding.favorites;
            loves = binding.loves;
            numberViews = binding.numberViews;
            numberLoves = binding.numberLoves;
            numberDownloads = binding.numberDownloads;
            item = binding.item;
        }
    }
}