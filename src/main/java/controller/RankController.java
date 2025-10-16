package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dto.recipe.RecipeSearchDTO;
import service.SearchService;

@WebServlet("/rank")
public class RankController extends HttpServlet {
	private SearchService searchService;

	@Override
	public void init() throws ServletException {
		searchService = SearchService.getInstance();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<RecipeSearchDTO> rank = searchService.searchRecipeForRank();
		req.setAttribute("ranking", rank);
		req.getRequestDispatcher("/views/rank/rank.jsp").forward(req, resp);
	}
}
