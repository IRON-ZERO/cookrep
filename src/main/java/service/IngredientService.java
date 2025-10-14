package service;

import model.Ingredient;
import repository.IngredientDAO;
import repository.UserDAO;
import repository.UserIngredientDAO;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
}
