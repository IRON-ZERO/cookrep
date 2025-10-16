package controller.authControllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dto.recipe.RecipeApiDTO;
import service.RecipeAPIService;

@WebServlet("/")
public class HomeController extends HttpServlet {

	private RecipeAPIService recipeAPIService;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		recipeAPIService = RecipeAPIService.getInstance();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<RecipeApiDTO> publicAPIRecipeList = recipeAPIService.getPublicAPIRecipeList("1", "20");
		req.setAttribute("recipe", publicAPIRecipeList);
		req.getRequestDispatcher("/views/index.jsp").forward(req, resp);
	}
}
