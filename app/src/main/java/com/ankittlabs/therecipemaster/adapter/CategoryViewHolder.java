package com.ankittlabs.therecipemaster.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ankittlabs.therecipemaster.OnRecipeListener;
import com.ankittlabs.therecipemaster.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    OnRecipeListener recipeListener;
    CircleImageView categoryImage;
    TextView categoryTitle;


    public CategoryViewHolder(@NonNull View itemView, OnRecipeListener recipeListener) {
        super(itemView);
        this.recipeListener = recipeListener;
        categoryTitle = itemView.findViewById(R.id.category_title);
        categoryImage = itemView.findViewById(R.id.category_image);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        recipeListener.onCategoryClick(categoryTitle.getText().toString());
    }
}
