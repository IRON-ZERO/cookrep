package repository;

import model.Ingredient;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IngredientDAO {
    private Connection conn = null;

    public IngredientDAO() {
        DBConnection dbConnection = new DBConnection();
        dbConnection.open();
        this.conn = dbConnection.getConnection();
    }
    public IngredientDAO(Connection conn){
        this.conn = conn;
    }

    public Ingredient findById(int id) throws SQLException {
        Ingredient ingredient = new Ingredient();
        String sql =  "select * from Ingredient where ingredient_id = ?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    ingredient.setIngredientId(rs.getInt("ingredient_id"));
                    ingredient.setName(rs.getString("name"));
                    ingredient.setCreatedAt(rs.getTimestamp("created_at"));
                }
            }
        }
        return ingredient;
    }

    public void deleteById(int id) throws SQLException {
        String sql = "delete from Ingredient where ingredient_id = ?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }
    public void addIngredient(Ingredient ingredient) throws SQLException {
        String sql = "insert into Ingredient (name) values (?)";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setString(1, ingredient.getName());
            preparedStatement.executeUpdate();
        }
    }
    public void addIngredients(String[] ingredientNames) throws SQLException {
        // MYSQL만 가능함. IGNORE.
        String sql = "INSERT IGNORE INTO Ingredient (name) VALUES (?)";

        try (PreparedStatement ps = this.conn.prepareStatement(sql)) {
            for (String name : ingredientNames) {
                ps.setString(1, name.trim()); // 파라미터 바인딩
                ps.addBatch(); // 배치에 추가
            }
            ps.executeBatch(); // 한 번에 실행
        }
    }
    public List<Ingredient> findByNames(String[] ingredientNames) throws SQLException {
        if (ingredientNames == null || ingredientNames.length == 0) return List.of();

        // "당근,시금치,양파" 형태로 합치기
        String joined = String.join(",", ingredientNames);

        // MySQL에서만 가능함. FIND_IN_SET 메소드
        String sql = "SELECT ingredient_id, name FROM Ingredient WHERE FIND_IN_SET(name, ?)";

        List<Ingredient> ingredients = new ArrayList<>();

        try (PreparedStatement ps = this.conn.prepareStatement(sql)) {
            ps.setString(1, joined);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setIngredientId(rs.getInt("ingredient_id"));
                    ingredient.setName(rs.getString("name"));
                    ingredients.add(ingredient);
                }
            }
        }

        return ingredients;
    }

}
