package repository;

import dto.recipe.RecipeDTO;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecipeDAO {

    private final DBConnection db = new DBConnection();

    // Recipe와 Step을 함께 DB에 저장
    public void insertRecipeWithSteps(RecipeDTO recipe) throws SQLException {
        String recipeSql = "INSERT INTO Recipe(recipe_id, user_id, title, thumbnail_image_url, views, people_count, prep_time, cook_time, `like`, kcal) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String stepSql = "INSERT INTO RecipeSteps(recipe_id, step_order, contents, image_url) VALUES (?, ?, ?, ?)";

        try (Connection con = db.open()) {
            con.setAutoCommit(false); // 트랜잭션 시작

            String userId = "u001";
            // 1️⃣ Recipe 테이블 저장
            try (PreparedStatement recipeStmt = con.prepareStatement(recipeSql)) {
                recipeStmt.setString(1, recipe.getRecipe_id());
                recipeStmt.setString(2, userId); // Session에서 가져온 userId
                recipeStmt.setString(3, recipe.getTitle());
                recipeStmt.setString(4, recipe.getThumbnail_image_url()); // 메인 이미지 URL
                recipeStmt.setInt(5, recipe.getViews());
                recipeStmt.setInt(6, recipe.getPeople_count());
                recipeStmt.setInt(7, recipe.getPrep_time());
                recipeStmt.setInt(8, recipe.getCook_time());
                recipeStmt.setInt(9, recipe.getLike());
                recipeStmt.setInt(10, recipe.getKcal());

                recipeStmt.executeUpdate();
            }

            // 2️⃣ Step 테이블 저장
            List<RecipeDTO.Step> steps = recipe.getSteps();
            if (steps != null && !steps.isEmpty()) {
                try (PreparedStatement stepStmt = con.prepareStatement(stepSql)) {
                    for (RecipeDTO.Step step : steps) {
                        stepStmt.setString(1, recipe.getRecipe_id());
                        stepStmt.setInt(2, step.getStepOrder()); // 수정: stepOrder
                        stepStmt.setString(3, step.getContents()); // 수정: contents
                        stepStmt.setString(4, step.getImageUrl()); // 수정: imageUrl
                        stepStmt.addBatch();
                    }
                    stepStmt.executeBatch();
                }
            }

            con.commit(); // 트랜잭션 커밋
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // 호출한 쪽에서 rollback 처리 가능
        }
    }

    //내 recipe 조회

}
