package com.ankittlabs.therecipemaster.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ankittlabs.therecipemaster.OnRecipeListener;
import com.ankittlabs.therecipemaster.R;
import com.ankittlabs.therecipemaster.model.Recipe;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class RecipeViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int RECIPE_TYPE = 1;
    private static final int LOADING_TYPE = 2;

    private List<Recipe> recipes;
    private OnRecipeListener recipeListener;

    public RecipeViewAdapter(OnRecipeListener recipeListener) {
        this.recipeListener = recipeListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;

        switch (viewType) {
            case RECIPE_TYPE: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_recipe_item_list, parent, false);
                return new RecipeViewHolder(view, recipeListener);
            }
            case LOADING_TYPE: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_loading_list_item, parent, false);
                return new LoadingViewHolder(view);
            }
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_recipe_item_list, parent, false);
                return new RecipeViewHolder(view, recipeListener);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int itemViewType = getItemViewType(position);
        if (itemViewType == RECIPE_TYPE) {
            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);
            Glide.with(holder.itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(recipes.get(position).getImage_url())
                    .into(((RecipeViewHolder) holder).image);

            ((RecipeViewHolder) holder).title.setText(recipes.get(position).getTitle());
            ((RecipeViewHolder) holder).publisher.setText(recipes.get(position).getPublisher());
            ((RecipeViewHolder) holder).socialScore.setText(String.valueOf(
                    Math.round(recipes.get(position).getSocial_rank())
            ));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (recipes.get(position).getTitle().equals("LOADING...")) {
            return LOADING_TYPE;
        } else {
            return RECIPE_TYPE;
        }
    }

    public void displayLoading() {
        if (!isLoading()) {
            Recipe recipe = new Recipe();
            recipe.setTitle("LOADING...");
            List<Recipe> loadingList = new ArrayList<>();
            loadingList.add(recipe);
            recipes = loadingList;
            notifyDataSetChanged();
        }
    }

    private boolean isLoading() {
        if (recipes != null) {
            if (recipes.size() > 0) {
                return recipes.get(recipes.size() - 1).getTitle().equals("LOADING...");
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        if (recipes != null) {
            return recipes.size();
        }
        return 0;
    }

    public void setRecipes(List<Recipe> mrecipes) {
        recipes = mrecipes;
        notifyDataSetChanged();
    }

}
