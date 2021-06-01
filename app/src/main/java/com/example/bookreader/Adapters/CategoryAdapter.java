package com.example.bookreader.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookreader.CategoryFilter;
import com.example.bookreader.Models.CategoryModel;
import com.example.bookreader.R;
import com.example.bookreader.databinding.RowCategoryBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.HolderCategory> implements Filterable {
    private RowCategoryBinding binding;

    private Context context;
    public ArrayList<CategoryModel> categoryModels, filterList;
    private CategoryFilter filter;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> categoryModels) {
        this.context = context;
        this.categoryModels = categoryModels;
        this.filterList = categoryModels;
    }

    @NonNull
    @Override
    public HolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderCategory(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCategory holder, int position) {
        CategoryModel model = categoryModels.get(position);

        String id = model.getId();
        String category = model.getCategory();
        String uid = model.getUid();
        long timestamp = model.getTimestamp();

        holder.categoryTitleTv.setText(category);

        holder.deleteCategoryBtn.setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
            builder.setTitle(R.string.delete)
                    .setMessage(R.string.are_you_sure_you_want_to_delete_this_category)
                    .setPositiveButton(R.string.confirm, (dialog, which) -> {
                        Toast.makeText(context, R.string.deleting, Toast.LENGTH_SHORT).show();
                        deleteCategory(model, holder);
                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    private void deleteCategory(CategoryModel model, HolderCategory holder) {
        String id = model.getId();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Categories");
        reference.child(id)
                .removeValue()
                .addOnSuccessListener(unused -> Toast.makeText(context, R.string.successfully_deleted, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CategoryFilter(filterList, this);
        }

        return filter;
    }

    class HolderCategory extends RecyclerView.ViewHolder {
        TextView categoryTitleTv;
        ImageButton deleteCategoryBtn;

        public HolderCategory(@NonNull View itemView) {
            super(itemView);

            categoryTitleTv = binding.categoryTitleTv;
            deleteCategoryBtn = binding.deleteCategoryBtn;
        }
    }
}
