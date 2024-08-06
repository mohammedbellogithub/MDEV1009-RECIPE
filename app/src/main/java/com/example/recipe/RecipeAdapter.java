package com.example.recipe;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>  {

    private Context context;
    private List<RecipeModel> recipeList;

    public RecipeAdapter(Context context, List<RecipeModel> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_card, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        RecipeModel recipe = recipeList.get(position);
        holder.nameTextView.setText(recipe.getName());
        holder.categoryTextView.setText(recipe.getCategory());
        holder.dateTextView.setText(recipe.getDatePublished());
        Picasso.get().load(recipe.getImage()).into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            // Navigate to RecipeDetailsFragment
            RecipeDetailsFragment fragment = RecipeDetailsFragment.newInstance(
                    recipe.getName(),
                    recipe.getImage(),
                    recipe.getCategory(),
                    recipe.getDatePublished(),
                    recipe.getDirections(),
                    recipe.getIngredients()
            );

            // Use FragmentManager to replace the current fragment
            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.navFragmentView, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameTextView;
        TextView categoryTextView;
        TextView dateTextView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recipe_image);
            nameTextView = itemView.findViewById(R.id.recipe_name);
            categoryTextView = itemView.findViewById(R.id.recipe_category);
            dateTextView = itemView.findViewById(R.id.recipe_date);
        }
    }
}
