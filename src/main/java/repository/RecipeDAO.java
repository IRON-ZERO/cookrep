package repository;

import dto.recipe.RecipeDTO;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RecipeDAO {

    private DBConnection db;

    public RecipeDAO(){
        db = new DBConnection();
    }

    public String getLastRecipeId(String datePrefix){
//        오늘 날짜(prefix)로 시작하는 recipe_id를 조회
        String sql = "select recipe_id from Recipe where recipe_id like ? order by recipe_id desc limit 1";

        try (Connection con = db.open();
             PreparedStatement pstmt = con.prepareStatement(sql)){

            pstmt.setString(1, datePrefix + "%");
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return rs.getString("recipe_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; //null값이면 오늘 첫 레시피인 것
    }

    // Recipe 테이블에 삽입
    public void insertRecipe(RecipeDTO recipe) {
        String sql = "INSERT INTO Recipe " +
                "(recipe_id, user_id, title, created_at, updated_at, thumbnail_image_url, views, people_count, prep_time, cook_time, `like`, kcal) " +
                "VALUES (?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = db.open();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

//            pstmt.setString(1, recipe.getRecipeId());
//            pstmt.setString(2, recipe.getUserId());
//            pstmt.setString(3, recipe.getTitle());
//            pstmt.setString(4, recipe.getThumbnailImageUrl());
//            pstmt.setInt(5, recipe.getViews());
//            pstmt.setInt(6, recipe.getPeopleCount());
//            pstmt.setInt(7, recipe.getPrepTime());
//            pstmt.setInt(8, recipe.getCookTime());
//            pstmt.setInt(9, recipe.getLikeCount());
//            pstmt.setInt(10, recipe.getKcal());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

