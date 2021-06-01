package com.example.bookreader;

import android.widget.Filter;

import com.example.bookreader.Adapters.CategoryAdapter;
import com.example.bookreader.Models.CategoryModel;

import java.util.ArrayList;

public class CategoryFilter extends Filter {
    private ArrayList<CategoryModel> filterList;
    private CategoryAdapter categoryAdapter;

    public CategoryFilter(ArrayList<CategoryModel> filterList, CategoryAdapter categoryAdapter) {
        this.filterList = filterList;
        this.categoryAdapter = categoryAdapter;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();

        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase();

            ArrayList<CategoryModel> filteredModels = new ArrayList<>();
            for (int i = 0; i < filterList.size(); i++) {
                if (filterList.get(i).getCategory().toUpperCase().contains(constraint)) {
                    filteredModels.add(filterList.get(i));
                }
            }

            filterResults.count = filteredModels.size();
            filterResults.values = filteredModels;
        } else {
            filterResults.count = filterList.size();
            filterResults.values = filterList;
        }

        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        categoryAdapter.categoryModels = (ArrayList<CategoryModel>) results.values;

        categoryAdapter.notifyDataSetChanged();
    }
}
