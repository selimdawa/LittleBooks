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

import com.flatcode.littlebooksadmin.Filter.ADsUserFilter;
import com.flatcode.littlebooksadmin.Model.User;
import com.flatcode.littlebooksadmin.MyApplication;
import com.flatcode.littlebooksadmin.Unit.CLASS;
import com.flatcode.littlebooksadmin.Unit.DATA;
import com.flatcode.littlebooksadmin.Unit.VOID;
import com.flatcode.littlebooksadmin.databinding.ItemAdsUserBinding;

import java.text.MessageFormat;
import java.util.ArrayList;

public class ADsUserAdapter extends RecyclerView.Adapter<ADsUserAdapter.ViewHolder> implements Filterable {

    private ItemAdsUserBinding binding;
    private final Context context;

    public ArrayList<User> list, filterList;
    private ADsUserFilter filter;
    Boolean isUser;
    public int all = 0;

    public ADsUserAdapter(Context context, ArrayList<User> list, Boolean isUser) {
        this.context = context;
        this.list = list;
        this.filterList = list;
        this.isUser = isUser;
    }

    @NonNull
    @Override
    public ADsUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemAdsUserBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final ADsUserAdapter.ViewHolder holder, final int position) {

        final User item = list.get(position);
        String userId = DATA.EMPTY + item.getId();
        String username = DATA.EMPTY + item.getUsername();
        String profileImage = DATA.EMPTY + item.getProfileImage();
        String timestamp = DATA.EMPTY + item.getTimestamp();
        String adLoaded = DATA.EMPTY + item.getAdLoad();
        String adClicked = DATA.EMPTY + item.getAdClick();

        String formattedDate = MyApplication.formatTimestamp(Long.parseLong(timestamp));

        VOID.Glide(true, context, profileImage, holder.profileImage);

        if (username.equals(DATA.EMPTY)) {
            holder.username.setVisibility(View.GONE);
        } else {
            holder.username.setVisibility(View.VISIBLE);
            holder.username.setText(username);
        }

        int First = holder.getPosition();
        int Final = list.size() - First;

        holder.time.setText(formattedDate);
        holder.rank.setText(MessageFormat.format("{0}", Final));
        holder.numberADsLoad.setText(MessageFormat.format("{0}{1}", DATA.EMPTY, adLoaded));
        holder.numberADsClick.setText(MessageFormat.format("{0}{1}", DATA.EMPTY, adClicked));

        //ADsNumber(userId, DATA.AD_LOADED, DATA.AD_LOAD, holder.numberADsLoad);
        //ADsNumber(userId, DATA.AD_CLICKED, DATA.AD_CLICK, holder.numberADsClick);

        holder.item.setOnClickListener(view ->
                VOID.IntentExtra(context, CLASS.ADS_INFO, DATA.PROFILE_ID, userId));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new ADsUserFilter(filterList, this);
        }
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        TextView username, rank, numberADsLoad, numberADsClick, time;
        LinearLayout item;

        public ViewHolder(View view) {
            super(view);
            profileImage = binding.profileImage;
            username = binding.username;
            rank = binding.rank;
            numberADsLoad = binding.numberADsLoad;
            numberADsClick = binding.numberADsClick;
            time = binding.time;
            item = binding.item;
        }
    }

    /*private void ADsNumber(String userId, String type, String hash, TextView numberADs) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.AD_S).child(userId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ADs item = snapshot.getValue(ADs.class);
                    assert item != null;
                    if (type.equals(DATA.AD_LOADED)) {
                        i = i + item.getAdsLoadedCount();
                    } else if (type.equals(DATA.AD_CLICKED)) {
                        i = i + item.getAdsClickedCount();
                    }
                    numberADs.setText(MessageFormat.format("{0}{1}", DATA.EMPTY, i));
                    updateAdsCount(i);
                }
            }

            private void updateAdsCount(int i) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(hash, i);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.USERS);
                reference.child(userId).updateChildren(hashMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/
}