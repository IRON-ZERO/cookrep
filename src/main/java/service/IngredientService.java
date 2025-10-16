package service;

import model.Ingredient;
import repository.IngredientDAO;
import repository.RecipeIngredientDAO;
import repository.UserDAO;
import repository.UserIngredientDAO;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IngredientService {
    public void addIngredient(String[] ingredientNames) throws SQLException {
        IngredientDAO ingredientDAO = new IngredientDAO();
        ingredientDAO.addIngredients(ingredientNames);
    }
    public int[] getIdsByNames(String[] ingredientNames) throws SQLException{
        IngredientDAO ingredientDAO = new IngredientDAO();
        List<Ingredient> ingredients = ingredientDAO.findByNames(ingredientNames);
        List<Integer> ingredientIds = new ArrayList<>();
        for(Ingredient ingredient : ingredients){
            ingredientIds.add(ingredient.getIngredientId());
        }
        return ingredientIds.stream().mapToInt(Integer::intValue).toArray();
    }

    // 트랜잭션용. ingredient 테이블에 재료추가 + userIngredient에 재료 추가 (기능 두개 합침)
    public void addIngredientsForUser(String userId, String[] ingredientNames) throws SQLException {
        DBConnection dbConnection = new DBConnection();
        try (Connection conn = dbConnection.open()) {
            IngredientDAO ingredientDAO = new IngredientDAO(conn);
            UserIngredientDAO userIngredientDAO = new UserIngredientDAO(conn);

            ingredientDAO.addIngredients(ingredientNames);
            List<Ingredient> ingredients = ingredientDAO.findByNames(ingredientNames);
            int[] ids = ingredients.stream().mapToInt(Ingredient::getIngredientId).toArray();

            userIngredientDAO.addIngredientsBatch(userId,ids);

            conn.commit(); // ✅ 한 번에 모두 커밋
        }
    }
    public void addIngredientsForRecipe(String recipeId, Map<String, String> ingredientNameCountMap) throws SQLException {
        DBConnection dbConnection = new DBConnection();
        try (Connection conn = dbConnection.open()) {
            conn.setAutoCommit(false); // 트랜잭션 시작

            IngredientDAO ingredientDAO = new IngredientDAO(conn);
            RecipeIngredientDAO recipeIngredientDAO = new RecipeIngredientDAO(conn);

            // 1️⃣ Ingredient 테이블에 재료 추가 (중복 무시)
            String[] ingredientNames = ingredientNameCountMap.keySet().toArray(new String[0]);
            ingredientDAO.addIngredients(ingredientNames);

            // 2️⃣ 재료 ID 조회
            List<Ingredient> ingredients = ingredientDAO.findByNames(ingredientNames);

            // 3️⃣ recipeIngredient에 매핑 (각 이름에 맞는 count 포함)
            for (Ingredient ingredient : ingredients) {
                String ingredientName = ingredient.getName();
                String count = ingredientNameCountMap.get(ingredientName); // 예: "1컵", "2큰술"
                recipeIngredientDAO.addIngredient(recipeId, ingredient.getIngredientId(), count);
            }

            // 4️⃣ 커밋
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
