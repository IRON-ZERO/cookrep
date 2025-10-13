package model;

// 유저가 가진 재료 user_ingredient -> 개수, 냉장고에 언제 넣었는지(추후 속성 추가)
public class UserIngredient {
    private Ingredient ingredient;
    private int ingredientId;
    private String userId;

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserIngredient{" +
            "ingredient=" + ingredient +
            ", ingredientId=" + ingredientId +
            ", userId='" + userId + '\'' +
            '}';
    }
}
