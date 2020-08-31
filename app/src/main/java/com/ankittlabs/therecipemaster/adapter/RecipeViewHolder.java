package com.ankittlabs.therecipemaster.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.ankittlabs.therecipemaster.OnRecipeListener;
import com.ankittlabs.therecipemaster.R;

public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView title, publisher, socialScore;
    OnRecipeListener recipeListener;
    AppCompatImageView image;

    public RecipeViewHolder(@NonNull View itemView, OnRecipeListener recipeListener) {
        super(itemView);
        this.recipeListener = recipeListener;
        title = itemView.findViewById(R.id.recipe_title);
        publisher = itemView.findViewById(R.id.recipe_publisher);
        socialScore = itemView.findViewById(R.id.recipe_social_score);
        image = itemView.findViewById(R.id.recipe_image);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        recipeListener.onRecipeClick(getAdapterPosition());
    }
}