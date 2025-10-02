package model;

import java.sql.Timestamp;

public class Ingredient {
    private int ingredientId;
    private String name;
    private Timestamp createdAt;

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
            "ingredientId=" + ingredientId +
            ", name='" + name + '\'' +
            ", createdAt=" + createdAt +
            '}';
    }
}
