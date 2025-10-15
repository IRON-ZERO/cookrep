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

@WebServlet("/search")
public class SearchController extends HttpServlet {

	SearchService searchService = SearchService.getInstance();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<RecipeSearchDTO> list = searchService.searchRecipeByDefault();
		req.setAttribute("defaultList", list);
		req.getRequestDispatcher("/views/search/search.jsp").forward(req, resp);
	}
}
