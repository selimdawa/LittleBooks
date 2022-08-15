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

import com.flatcode.littlebooks.Filter.StaggerdFilter;
import com.flatcode.littlebooks.Model.Book;
import com.flatcode.littlebooks.Unit.CLASS;
import com.flatcode.littlebooks.Unit.DATA;
import com.flatcode.littlebooks.Unit.VOID;
import com.flatcode.littlebooks.databinding.ItemBookStaggeredBinding;

import java.util.ArrayList;

public class StaggeredBookAdapter extends RecyclerView.Adapter<StaggeredBookAdapter.ViewHolder> implements Filterable {

    private ItemBookStaggeredBinding binding;
    private final Context context;

    public ArrayList<Book> list, filterList;
    private StaggerdFilter filter;

    public StaggeredBookAdapter(Context context, ArrayList<Book> list) {
        this.context = context;
        this.list = list;
        this.filterList = list;
    }

    @NonNull
    @Override
    public StaggeredBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemBookStaggeredBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final StaggeredBookAdapter.ViewHolder holder, final int position) {

        final Book item = list.get(position);
        String bookId = DATA.EMPTY + item.getId();
        String title = DATA.EMPTY + item.getTitle();
        String image = DATA.EMPTY + item.getImage();
        String nrLoves = DATA.EMPTY + item.getLovesCount();
        String nrDownloads = DATA.EMPTY + item.getDownloadsCount();

        VOID.Glide(false, context, image, holder.image);

        if (item.getTitle().equals(DATA.EMPTY)) {
            holder.title.setVisibility(View.GONE);
        } else {
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText(title);
        }

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

        holder.item.setOnClickListener(view ->
                VOID.IntentExtra(context, CLASS.BOOK_DETAIL, DATA.BOOK_ID, bookId));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new StaggerdFilter(filterList, this);
        }
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image, favorites, loves;
        TextView title, numberLoves, numberDownloads;
        LinearLayout item;

        public ViewHolder(View view) {
            super(view);
            image = binding.image;
            title = binding.title;
            favorites = binding.favorites;
            loves = binding.loves;
            numberLoves = binding.numberLoves;
            numberDownloads = binding.numberDownloads;
            item = binding.item;
        }
    }
}