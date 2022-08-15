package com.flatcode.littlebooksadmin.Adapter;

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

import com.flatcode.littlebooksadmin.Filter.TopPublisherFilter;
import com.flatcode.littlebooksadmin.Model.User;
import com.flatcode.littlebooksadmin.R;
import com.flatcode.littlebooksadmin.Unit.CLASS;
import com.flatcode.littlebooksadmin.Unit.DATA;
import com.flatcode.littlebooksadmin.Unit.VOID;
import com.flatcode.littlebooksadmin.databinding.ItemTopPublisherBinding;

import java.text.MessageFormat;
import java.util.ArrayList;

public class TopPublisherAdapter extends RecyclerView.Adapter<TopPublisherAdapter.ViewHolder> implements Filterable {

    private ItemTopPublisherBinding binding;
    private final Context context;

    public ArrayList<User> list, filterList;
    private TopPublisherFilter filter;
    Boolean isUser;

    public TopPublisherAdapter(Context context, ArrayList<User> list, Boolean isUser) {
        this.context = context;
        this.list = list;
        this.filterList = list;
        this.isUser = isUser;
    }

    @NonNull
    @Override
    public TopPublisherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemTopPublisherBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final TopPublisherAdapter.ViewHolder holder, final int position) {

        int all = list.size(), loop = position / 2, round = loop * 2, round2 = round + 1;

        for (int x = 0; x < all && round == position; x++) {
            holder.item.setBackgroundResource(R.drawable.solid1);
        }
        for (int x = 0; x < all && round2 == position; x++) {
            holder.item.setBackgroundResource(R.drawable.solid2);
        }

        final User item = list.get(position);
        String userId = DATA.EMPTY + item.getId();
        String username = DATA.EMPTY + item.getUsername();
        String profileImage = DATA.EMPTY + item.getProfileImage();
        String numberBooks = DATA.EMPTY + item.getBooksCount();

        VOID.Glide(false, context, profileImage, holder.profileImage);

        if (username.equals(DATA.EMPTY)) {
            holder.username.setVisibility(View.GONE);
        } else {
            holder.username.setVisibility(View.VISIBLE);
            holder.username.setText(username);
        }

        int First = holder.getPosition();
        int Final = list.size() - First;

        holder.rank.setText(MessageFormat.format("{0}", Final));

        holder.numberBooks.setText(numberBooks);

         /*if (item.getPublisher().equals(DATA.FirebaseUserUid)) {
            holder.favorites.setVisibility(View.GONE);
        } else {
            holder.favorites.setVisibility(View.VISIBLE);
        }*/

        holder.item.setOnClickListener(view ->
                VOID.IntentExtra(context, CLASS.PROFILE, DATA.PROFILE_ID, userId));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new TopPublisherFilter(filterList, this);
        }
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        TextView username, numberBooks, rank;
        LinearLayout item;

        public ViewHolder(View view) {
            super(view);
            profileImage = binding.imageProfile;
            username = binding.username;
            rank = binding.rank;
            numberBooks = binding.numberBooks;
            item = binding.item;
        }
    }
}