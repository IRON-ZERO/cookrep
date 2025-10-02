package controller.authControllers.loginControllers;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.AuthService;

@WebServlet("/login")
public class LoginViewController extends HttpServlet {

	private AuthService service;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		service = AuthService.getInstance();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		req.getRequestDispatcher("/login.jsp").forward(req, resp);
		//		req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
	}

}
