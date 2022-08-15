package com.flatcode.littlebooks.Filter;

import android.widget.Filter;

import com.flatcode.littlebooks.Adapter.StaggeredBookAdapter;
import com.flatcode.littlebooks.Model.Book;

import java.util.ArrayList;

public class StaggerdFilter extends Filter {

    ArrayList<Book> list;
    StaggeredBookAdapter adapter;

    public StaggerdFilter(ArrayList<Book> list, StaggeredBookAdapter adapter) {
        this.list = list;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase();
            ArrayList<Book> filter = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTitle().toUpperCase().contains(constraint)) {
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
        adapter.list = (ArrayList<Book>) results.values;
        adapter.notifyDataSetChanged();
    }
}