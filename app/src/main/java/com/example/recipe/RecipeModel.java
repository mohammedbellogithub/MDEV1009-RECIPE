package com.example.recipe;

public class RecipeModel {
    private String name;
    private String category;
    private String datePublished;
    private String directions;
    private String image;
    private String ingredients;
    private String url;

    // Empty constructor needed for FireStore serialization
    public RecipeModel() {}

    public RecipeModel(String name, String category, String datePublished, String directions, String ingredients, String imageUrl) {
        this.name = name;
        this.category = category;
        this.datePublished = datePublished;
        this.directions = directions;
        this.ingredients = ingredients;
        this.image = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
