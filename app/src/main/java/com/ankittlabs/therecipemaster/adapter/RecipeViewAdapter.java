package com.ankittlabs.therecipemaster.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ankittlabs.therecipemaster.OnRecipeListener;
import com.ankittlabs.therecipemaster.R;
import com.ankittlabs.therecipemaster.model.Recipe;
import com.ankittlabs.therecipemaster.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class RecipeViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int RECIPE_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    private static final int CATEGORY_TYPE = 3;
    private static final int EXHAUSTED_TYPE = 4;

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
            case CATEGORY_TYPE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_category_list, parent, false);
                return new CategoryViewHolder(view, recipeListener);

            case EXHAUSTED_TYPE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_query_exhausted,parent,false);
                return new SearchExhaustedViewHolder(view);
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
        } else if (itemViewType == CATEGORY_TYPE) {
            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);
            Uri path = Uri.parse("android.resource://com.ankittlabs.therecipemaster/drawable/" + recipes.get(position).getImage_url());
            Glide.with(holder.itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(path)
                    .into(((CategoryViewHolder) holder).categoryImage);

            ((CategoryViewHolder) holder).categoryTitle.setText(recipes.get(position).getTitle());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (recipes.get(position).getSocial_rank() == -1) {
            return CATEGORY_TYPE;
        } else if (recipes.get(position).getTitle().equals("LOADING...")) {
            return LOADING_TYPE;
        } else if (position == recipes.size() - 1
                && position != 0
                && !recipes.get(position).getTitle().equals("EXHAUSTED...")) {
            return LOADING_TYPE;
        } else if(recipes.get(position).getTitle().equals("EXHAUSTED...")){
             return EXHAUSTED_TYPE;
        } else {
            return RECIPE_TYPE;
        }
    }

    public void setQueryExhausted(){
        hideLoading();
        Recipe exhaustedRecipe=new Recipe();
        exhaustedRecipe.setTitle("EXHAUSTED..");
        recipes.add(exhaustedRecipe);
    }

    private void hideLoading(){
        if (isLoading()){
            for (Recipe recipe:recipes){
                if (recipe.getTitle().equals("LOADING...")){
                    recipes.remove(recipe);
                }
            }
        }
    }

    public void displaySearchCategory() {
        List<Recipe> categories = new ArrayList<>();
        for (int i = 0; i < Constants.DEFAULT_SEARCH_CATEGORIES.length; i++) {
            Recipe recipe = new Recipe();
            recipe.setTitle(Constants.DEFAULT_SEARCH_CATEGORIES[i]);
            recipe.setImage_url(Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]);
            recipe.setSocial_rank(-1);
            categories.add(recipe);
        }
        recipes = categories;
        notifyDataSetChanged();
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

    public Recipe getRecipeDetails(int position) {
        if (recipes != null) {
            if (recipes.size() > 0) {
                return recipes.get(position);
            }
        }
        return null;
    }

}
