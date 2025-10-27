package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import dto.recipe.RecipeSearchDTO;
import utils.DBConnection;

public class SearchDAO {
	private Logger log = Logger.getLogger(SearchDAO.class.getName());

	private DBConnection db = new DBConnection();

	private SearchDAO() {}

	private static class Holder {
		private static final SearchDAO INSTANCE = new SearchDAO();
	}

	public static SearchDAO getInstance() {
		return Holder.INSTANCE;
	}

	public List<RecipeSearchDTO> searchRecipeByDefault() throws SQLException {
		List<RecipeSearchDTO> result = new ArrayList<>();
		db.open();
		String sql = """
			SELECT recipe_id, title, thumbnail_image_url, views, people_count, prep_time, cook_time, `like`, kcal
			FROM Recipe
			LIMIT 30
			""";
		try (Connection conn = db.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				RecipeSearchDTO newItem = new RecipeSearchDTO(
					resultSet.getString("recipe_id"),
					resultSet.getString("title"),
					resultSet.getString("thumbnail_image_url"),
					resultSet.getInt("views"),
					resultSet.getInt("people_count"),
					resultSet.getInt("prep_time"),
					resultSet.getInt("cook_time"),
					resultSet.getInt("like"),
					resultSet.getInt("kcal"));
				result.add(newItem);
			}
			return result;
		} catch (SQLException e) {
			log.severe("SQLException in searchRecipeByDefault" + e);
			throw e;
		} finally {
			db.close();

		}
	}

	public List<RecipeSearchDTO> searchRecipeForRank() throws SQLException {
		List<RecipeSearchDTO> result = new ArrayList<>();
		db.open();
		String sql = """
			SELECT recipe_id, title, thumbnail_image_url, views, people_count, prep_time, cook_time, `like`, kcal
			FROM Recipe
			ORDER BY `like` DESC
			LIMIT 30
			""";
		try (Connection conn = db.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				RecipeSearchDTO newItem = new RecipeSearchDTO(
					resultSet.getString("recipe_id"),
					resultSet.getString("title"),
					resultSet.getString("thumbnail_image_url"),
					resultSet.getInt("views"),
					resultSet.getInt("people_count"),
					resultSet.getInt("prep_time"),
					resultSet.getInt("cook_time"),
					resultSet.getInt("like"),
					resultSet.getInt("kcal"));
				result.add(newItem);
			}
			return result;
		} catch (SQLException e) {
			log.severe("SQLException in searchRecipeByDefault" + e);
			throw e;
		} finally {
			db.close();

		}
	}

	public List<RecipeSearchDTO> searchRecipeByNames(String[] names) throws SQLException {
		List<RecipeSearchDTO> result = new ArrayList<>();
		db.open();
		StringBuilder sql = new StringBuilder("""
			SELECT recipe_id, title, thumbnail_image_url, views, people_count, prep_time, cook_time, `like`, kcal
			FROM Recipe
			WHERE
			""");
		for (int i = 0; i < names.length; i++) {
			sql.append("title LIKE ?");
			if (i < names.length - 1) {
				sql.append(" OR ");
			}
		}
		try (Connection conn = db.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql.toString());) {
			for (int i = 0; i < names.length; i++) {
				pstmt.setString(i + 1, "%" + names[i].trim() + "%");

			}
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				RecipeSearchDTO newItem = new RecipeSearchDTO(
					resultSet.getString("recipe_id"),
					resultSet.getString("title"),
					resultSet.getString("thumbnail_image_url"),
					resultSet.getInt("views"),
					resultSet.getInt("people_count"),
					resultSet.getInt("prep_time"),
					resultSet.getInt("cook_time"),
					resultSet.getInt("like"),
					resultSet.getInt("kcal"));
				result.add(newItem);
			}
			return result;
		} catch (SQLException e) {
			log.severe("SQLException in searchRecipeByDefault" + e);
			throw e;
		} finally {
			db.close();

		}
	}

}
