package com.flatcode.littlebooksadmin.Filter;

import android.widget.Filter;

import com.flatcode.littlebooksadmin.Adapter.ADsInfoAdapter;
import com.flatcode.littlebooksadmin.Model.ADs;

import java.util.ArrayList;

public class ADsInfoFilter extends Filter {

    ArrayList<ADs> list;
    ADsInfoAdapter adapter;

    public ADsInfoFilter(ArrayList<ADs> list, ADsInfoAdapter adapter) {
        this.list = list;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase();
            ArrayList<ADs> filter = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getName().toUpperCase().contains(constraint)) {
                    filter.add(list.get(i));
                }
            }
            results.count = filter.size();
            results.values = filter;
        } else {
            results.count = list.size();
            results.values = list;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.list = (ArrayList<ADs>) results.values;
        adapter.notifyDataSetChanged();
    }
}