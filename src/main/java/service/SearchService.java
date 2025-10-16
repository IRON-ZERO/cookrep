package service;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import dto.recipe.RecipeSearchDTO;
import repository.SearchDAO;

public class SearchService {

	private SearchDAO searchRepo = SearchDAO.getInstance();

	private Logger log = Logger.getLogger(SearchService.class.getName());

	private SearchService() {}

	private static class Holder {
		private static final SearchService INSTANCE = new SearchService();
	}

	public static SearchService getInstance() {
		return Holder.INSTANCE;
	}

	public List<RecipeSearchDTO> searchRecipeByDefault() {
		try {
			return searchRepo.searchRecipeByDefault();
		} catch (SQLException e) {
			log.severe("SQLException : " + e);
			return Collections.emptyList();
		}
	}

	public List<RecipeSearchDTO> searchRecipeForRank() {
		try {
			return searchRepo.searchRecipeForRank();
		} catch (SQLException e) {
			log.severe("SQLException : " + e);
			return Collections.emptyList();
		}
	}

	public List<RecipeSearchDTO> searchRecipeByNames(String names) {
		String[] split = names.split(",");
		try {
			return searchRepo.searchRecipeByNames(split);
		} catch (SQLException e) {
			log.severe("SQLException : " + e);
			return Collections.emptyList();
		}
	}

}
