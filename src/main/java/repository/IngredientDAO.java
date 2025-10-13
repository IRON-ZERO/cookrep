package repository;

import model.Ingredient;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IngredientDAO {
    private Connection conn = null;

    public IngredientDAO() {
        DBConnection dbConnection = new DBConnection();
        dbConnection.open();
        this.conn = dbConnection.getConnection();
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
}
