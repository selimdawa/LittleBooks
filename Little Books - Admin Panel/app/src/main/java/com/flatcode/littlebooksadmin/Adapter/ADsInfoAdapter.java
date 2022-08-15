package com.flatcode.littlebooksadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flatcode.littlebooksadmin.Filter.ADsInfoFilter;
import com.flatcode.littlebooksadmin.Model.ADs;
import com.flatcode.littlebooksadmin.Unit.DATA;
import com.flatcode.littlebooksadmin.databinding.ItemInfoAdsBinding;

import java.text.MessageFormat;
import java.util.ArrayList;

public class ADsInfoAdapter extends RecyclerView.Adapter<ADsInfoAdapter.ViewHolder> implements Filterable {

    private ItemInfoAdsBinding binding;
    private final Context context;

    public ArrayList<ADs> list, filterList;
    private ADsInfoFilter filter;
    Boolean isUser;

    public ADsInfoAdapter(Context context, ArrayList<ADs> list, Boolean isUser) {
        this.context = context;
        this.list = list;
        this.filterList = list;
        this.isUser = isUser;
    }

    @NonNull
    @Override
    public ADsInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemInfoAdsBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final ADsInfoAdapter.ViewHolder holder, final int position) {

        final ADs item = list.get(position);
        String name = item.getName();
        int adsLoadedCount = item.getAdsLoadedCount();
        int adsClickedCount = item.getAdsClickedCount();

        if (name != null) {
            holder.name.setText(name);
        }
        holder.numberADsLoad.setText(MessageFormat.format("{0}{1}", DATA.EMPTY, adsLoadedCount));
        holder.numberADsClick.setText(MessageFormat.format("{0}{1}", DATA.EMPTY, adsClickedCount));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new ADsInfoFilter(filterList, this);
        }
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView numberADsClick, numberADsLoad, name;
        LinearLayout item;

        public ViewHolder(View view) {
            super(view);
            numberADsClick = binding.numberADsClick;
            numberADsLoad = binding.numberADsLoad;
            name = binding.name;
            item = binding.item;
        }
    }
}