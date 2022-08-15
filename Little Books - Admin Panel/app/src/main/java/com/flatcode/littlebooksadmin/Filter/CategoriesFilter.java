package com.flatcode.littlebooksadmin.Filter;

import android.widget.Filter;

import com.flatcode.littlebooksadmin.Adapter.CategoriesAdapter;
import com.flatcode.littlebooksadmin.Model.Category;

import java.util.ArrayList;

public class CategoriesFilter extends Filter {

    ArrayList<Category> list;
    CategoriesAdapter adapter;

    public CategoriesFilter(ArrayList<Category> list, CategoriesAdapter adapter) {
        this.list = list;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase();
            ArrayList<Category> filter = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getCategory().toUpperCase().contains(constraint)) {
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
        adapter.list = (ArrayList<Category>) results.values;
        adapter.notifyDataSetChanged();
    }
}