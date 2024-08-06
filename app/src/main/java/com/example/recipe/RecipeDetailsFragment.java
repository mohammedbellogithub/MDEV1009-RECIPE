package com.example.recipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.squareup.picasso.Picasso;
import androidx.navigation.Navigation;

public class RecipeDetailsFragment extends Fragment {

    private static final String ARG_NAME = "recipe_name";
    private static final String ARG_IMAGE = "recipe_image";
    private static final String ARG_CATEGORY = "recipe_category";
    private static final String ARG_DATE = "recipe_date";
    private static final String ARG_DIRECTIONS = "recipe_directions";
    private static final String ARG_INGREDIENTS = "recipe_ingredients";

    private String recipeName;
    private String recipeImage;
    private String recipeCategory;
    private String recipeDate;
    private String recipeDirections;
    private String recipeIngredients;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    public static RecipeDetailsFragment newInstance(String name, String image, String category, String date,
                                                    String directions, String ingredients) {
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_IMAGE, image);
        args.putString(ARG_CATEGORY, category);
        args.putString(ARG_DATE, date);
        args.putString(ARG_DIRECTIONS, directions);
        args.putString(ARG_INGREDIENTS, ingredients);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipeName = getArguments().getString(ARG_NAME);
            recipeImage = getArguments().getString(ARG_IMAGE);
            recipeCategory = getArguments().getString(ARG_CATEGORY);
            recipeDate = getArguments().getString(ARG_DATE);
            recipeDirections = getArguments().getString(ARG_DIRECTIONS);
            recipeIngredients = getArguments().getString(ARG_INGREDIENTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        // Find views
        ImageView imageView = view.findViewById(R.id.recipe_image);
        TextView nameTextView = view.findViewById(R.id.recipe_name);
        TextView categoryTextView = view.findViewById(R.id.recipe_category);
        TextView dateTextView = view.findViewById(R.id.recipe_date);
        TextView directionsTextView = view.findViewById(R.id.recipe_directions);
        TextView ingredientsTextView = view.findViewById(R.id.recipe_ingredients);

        // Find views
        Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            // Use the FragmentManager to handle back navigation
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Set the data
        nameTextView.setText(recipeName);
        categoryTextView.setText(recipeCategory);
        dateTextView.setText(recipeDate);
        directionsTextView.setText(recipeDirections);
        ingredientsTextView.setText(recipeIngredients);
        // Check and load image using Picasso

        if (recipeImage != null && !recipeImage.isEmpty()) {
            Picasso.get()
                    .load(recipeImage)
                    .placeholder(R.drawable.recipe) // Optional placeholder
//                    .error(R.drawable.error_image)  // Optional error image
                    .into(imageView);
        } else {
            // Optionally handle case where recipeImage is null or empty
//            imageView.setImageResource(R.drawable.error_image); // Use a default image
        }
        return view;
    }
}

