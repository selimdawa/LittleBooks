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

import com.flatcode.littlebooks.Filter.PublisherFilter;
import com.flatcode.littlebooks.Model.User;
import com.flatcode.littlebooks.R;
import com.flatcode.littlebooks.Unit.CLASS;
import com.flatcode.littlebooks.Unit.DATA;
import com.flatcode.littlebooks.Unit.VOID;
import com.flatcode.littlebooks.databinding.ItemPublisherBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;
import java.util.ArrayList;

public class PublisherAdapter extends RecyclerView.Adapter<PublisherAdapter.ViewHolder> implements Filterable {

    private ItemPublisherBinding binding;
    private final Context context;

    public ArrayList<User> list, filterList;
    private PublisherFilter filter;

    public PublisherAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
        this.filterList = list;
    }

    @NonNull
    @Override
    public PublisherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemPublisherBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final PublisherAdapter.ViewHolder holder, final int position) {

        final User item = list.get(position);
        String id = item.getId();

        VOID.Glide(true, context, item.getProfileImage(), holder.image);

        if (item.getUsername().equals(DATA.EMPTY)) {
            holder.username.setVisibility(View.GONE);
        } else {
            holder.username.setVisibility(View.VISIBLE);
            holder.username.setText(item.getUsername());
        }

        if (id.equals(DATA.FirebaseUserUid)) {
            holder.add.setVisibility(View.GONE);
        } else {
            holder.add.setVisibility(View.VISIBLE);
        }

        NrFollowers(holder.numberFollowers, id);
        NrBooks(holder.numberBooks, id);
        isFollowing(holder.add, id);

        holder.add.setOnClickListener(view -> {
            if (holder.add.getTag().equals("add")) {
                FirebaseDatabase.getInstance().getReference().child(DATA.FOLLOW).child(DATA.FirebaseUserUid)
                        .child(DATA.FOLLOWING).child(id).setValue(true);
                FirebaseDatabase.getInstance().getReference().child(DATA.FOLLOW).child(id)
                        .child(DATA.FOLLOWERS).child(DATA.FirebaseUserUid).setValue(true);
            } else {
                FirebaseDatabase.getInstance().getReference().child(DATA.FOLLOW).child(DATA.FirebaseUserUid)
                        .child(DATA.FOLLOWING).child(id).removeValue();
                FirebaseDatabase.getInstance().getReference().child(DATA.FOLLOW).child(id)
                        .child(DATA.FOLLOWERS).child(DATA.FirebaseUserUid).removeValue();
            }
        });

        holder.item.setOnClickListener(view ->
                VOID.IntentExtra(context, CLASS.PROFILE, DATA.PROFILE_ID, id));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new PublisherFilter(filterList, this);
        }
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image, add;
        public TextView username, numberBooks, numberFollowers;
        public LinearLayout item;

        public ViewHolder(View view) {
            super(view);
            this.image = binding.imageProfile;
            this.username = binding.username;
            this.add = binding.add;
            this.numberBooks = binding.numberBooks;
            this.numberFollowers = binding.numberFollowers;
            this.item = binding.item;
        }
    }

    private void isFollowing(final ImageView add, final String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child(DATA.FOLLOW).child(DATA.FirebaseUserUid).child(DATA.FOLLOWING);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userId).exists()) {
                    add.setImageResource(R.drawable.ic_heart_selected);
                    add.setTag("added");
                } else {
                    add.setImageResource(R.drawable.ic_heart_unselected);
                    add.setTag("add");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void NrFollowers(final TextView numberConnected, final String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(DATA.FOLLOW)
                .child(userId).child(DATA.FOLLOWERS);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                numberConnected.setText(MessageFormat.format("{0}", dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void NrBooks(final TextView numberConnected, final String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(DATA.USERS).child(userId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String number = DATA.EMPTY + dataSnapshot.child(DATA.BOOKS_COUNT).getValue();
                numberConnected.setText(number);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}