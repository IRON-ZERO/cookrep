package repository;

import dto.recipe.RecipeDTO;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
            // Recipe 테이블 저장
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

            // Step 테이블 저장
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

    // 사용자 레시피 리스트 조회
    public List<RecipeDTO> getRecipesByUser(String userId) throws SQLException {
        List<RecipeDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Recipe WHERE user_id = ? ORDER BY created_at DESC";

        try(Connection con = db.open();
            PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try(ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    RecipeDTO recipe = new RecipeDTO();
                    recipe.setRecipe_id(rs.getString("recipe_id"));
                    recipe.setUser_id(rs.getString("user_id"));
                    recipe.setTitle(rs.getString("title"));
                    recipe.setThumbnail_image_url(rs.getString("thumbnail_image_url"));
                    recipe.setPeople_count(rs.getInt("people_count"));
                    recipe.setPrep_time(rs.getInt("prep_time"));
                    recipe.setCook_time(rs.getInt("cook_time"));
                    recipe.setViews(rs.getInt("views"));
                    recipe.setLike(rs.getInt("like"));
                    list.add(recipe);
                }
            }
        }
        return list;
    }

    // 레시피 상세 조회 (Recipe + Steps)
    public RecipeDTO getRecipeById(String recipeId) throws SQLException {
        RecipeDTO recipe = null;

        String recipeSql = "SELECT * FROM Recipe WHERE recipe_id = ?";
        String stepsSql = "SELECT * FROM RecipeSteps WHERE recipe_id = ? ORDER BY step_order ASC";

        try (Connection con = db.open()) {
            // Recipe 정보 조회
            try (PreparedStatement pstmt = con.prepareStatement(recipeSql)) {
                pstmt.setString(1, recipeId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        recipe = new RecipeDTO();
                        recipe.setRecipe_id(rs.getString("recipe_id"));
                        recipe.setUser_id(rs.getString("user_id"));
                        recipe.setTitle(rs.getString("title"));
                        recipe.setThumbnail_image_url(rs.getString("thumbnail_image_url"));
                        recipe.setPeople_count(rs.getInt("people_count"));
                        recipe.setPrep_time(rs.getInt("prep_time"));
                        recipe.setCook_time(rs.getInt("cook_time"));
                        recipe.setViews(rs.getInt("views"));
                        recipe.setLike(rs.getInt("like"));
                        recipe.setKcal(rs.getInt("kcal"));
                    }
                }
            }

            if (recipe != null) {
                // Step 정보 조회
                try (PreparedStatement pstmt = con.prepareStatement(stepsSql)) {
                    pstmt.setString(1, recipeId);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        List<RecipeDTO.Step> steps = new ArrayList<>();
                        while (rs.next()) {
                            RecipeDTO.Step step = new RecipeDTO.Step();
                            step.setStepId(rs.getInt("step_id"));
                            step.setStepOrder(rs.getInt("step_order"));
                            step.setContents(rs.getString("contents"));
                            step.setImageUrl(rs.getString("image_url"));
                            steps.add(step);
                        }
                        recipe.setSteps(steps);
                    }
                }
            }
        }

        return recipe;
    }


    // 레시피 삭제 (steps 먼저 삭제)
    public void deleteRecipe(Connection con, String recipeId) throws SQLException {
        String deleteStepsSql = "DELETE FROM recipesteps WHERE recipe_id = ?";
        String deleteRecipeSql = "DELETE FROM recipe WHERE recipe_id = ?";

        // DAO 안에서는 Connection을 닫지 않고, commit/rollback도 Controller에서 처리
        try (PreparedStatement pstmt1 = con.prepareStatement(deleteStepsSql)) {
            pstmt1.setString(1, recipeId);
            pstmt1.executeUpdate();
        }

        try (PreparedStatement pstmt2 = con.prepareStatement(deleteRecipeSql)) {
            pstmt2.setString(1, recipeId);
            pstmt2.executeUpdate();
        }
    }


    // Recipe + Step 수정
    public void updateRecipeWithSteps(RecipeDTO recipe) throws SQLException {
        String updateRecipeSql = "UPDATE Recipe SET title=?, thumbnail_image_url=?, people_count=?, prep_time=?, cook_time=? WHERE recipe_id=?";
        String deleteStepsSql = "DELETE FROM RecipeSteps WHERE recipe_id=?";
        String insertStepSql = "INSERT INTO RecipeSteps(recipe_id, step_order, contents, image_url) VALUES (?, ?, ?, ?)";

        try (Connection con = db.open()) {
            con.setAutoCommit(false);

            // 1. Recipe 테이블 업데이트
            try (PreparedStatement pstmt = con.prepareStatement(updateRecipeSql)) {
                pstmt.setString(1, recipe.getTitle());
                pstmt.setString(2, recipe.getThumbnail_image_url());
                pstmt.setInt(3, recipe.getPeople_count());
                pstmt.setInt(4, recipe.getPrep_time());
                pstmt.setInt(5, recipe.getCook_time());
                pstmt.setString(6, recipe.getRecipe_id());
                pstmt.executeUpdate();
            }

            // 2. 기존 Step 삭제
            try (PreparedStatement pstmt = con.prepareStatement(deleteStepsSql)) {
                pstmt.setString(1, recipe.getRecipe_id());
                pstmt.executeUpdate();
            }

            // 3. Step 재삽입
            if (recipe.getSteps() != null && !recipe.getSteps().isEmpty()) {
                try (PreparedStatement pstmt = con.prepareStatement(insertStepSql)) {
                    for (RecipeDTO.Step step : recipe.getSteps()) {
                        pstmt.setString(1, recipe.getRecipe_id());
                        pstmt.setInt(2, step.getStepOrder());
                        pstmt.setString(3, step.getContents());
                        pstmt.setString(4, step.getImageUrl());
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }
            }

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
