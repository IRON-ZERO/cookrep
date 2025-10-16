package controller;

import service.ScrapService;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/scrap")
public class ScrapController extends HttpServlet {
    private final ScrapService scrapService = new ScrapService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        String action = req.getParameter("action");  // add / remove / check
        String recipeId = req.getParameter("recipeId");

        HttpSession session = req.getSession(false);
        String userId = (String) session.getAttribute("userId");

        try {
            switch (action) {
                case "add":
                    scrapService.addScrap(userId, recipeId);
                    resp.getWriter().write("{\"status\":\"added\"}");
                    break;
                case "remove":
                    scrapService.removeScrap(userId, recipeId);
                    resp.getWriter().write("{\"status\":\"removed\"}");
                    break;
                case "check":
                    boolean scrapped = scrapService.isScrapped(userId, recipeId);
                    resp.getWriter().write("{\"scrapped\":" + scrapped + "}");
                    break;
                default:
                    resp.setStatus(400);
                    resp.getWriter().write("{\"status\":\"invalid_action\"}");
            }
        } catch (SQLException e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }
}
