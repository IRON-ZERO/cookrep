package controller.authControllers;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dto.auth.LoginDTO;
import service.AuthService;
import utils.ValidatorUtils;

@WebServlet("/login")
public class LoginController extends HttpServlet {

	private AuthService authService;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		authService = AuthService.getInstance();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/views/auth/login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Params
		String action = req.getParameter("action");
		String identifyName = req.getParameter("identifyName");
		String password = req.getParameter("password");

		// Validator
		boolean hasError = false;
		if (action.equals("username-login")) {
			if (identifyName == null || !ValidatorUtils.validateNickname(identifyName)) {
				req.setAttribute("error_nickname", "유저이름 방식이 잘못되었습니다. (영문,숫자,-,_ 5~20자)");
				hasError = true;
			}
		} else if (action.equals("email-login")) {
			if (identifyName == null || !ValidatorUtils.validateEmail(identifyName)) {
				req.setAttribute("error_email", "이메일 방식이 잘못되었습니다.");
				hasError = true;
			}
		}
		// 비밀번호 Validator 구현해야함
		if (hasError) {
			req.getRequestDispatcher("/views/auth/login.jsp").forward(req, resp);
			return;
		}

		try {
			// Variables
			Optional<String> userId = Optional.empty();
			LoginDTO userData = new LoginDTO(identifyName, password);
			HttpSession session = req.getSession();

			// action 분기
			if (action.equals("username-login")) {
				userId = authService.loginUserByNickname(userData);
			} else if (action.equals("email-login")) {
				userId = authService.loginUserByEmail(userData);
			} else {
				req.setAttribute("error_result", "유효하지 않는 로그인 방식입니다.");
				req.getRequestDispatcher("/views/auth/login.jsp").forward(req, resp);
				return;
			}
			// Optional Check
			if (userId.isPresent()) {
				session.setAttribute("userId", userId.get());
				resp.sendRedirect("/index.jsp");
			} else {
				req.setAttribute("error_result", "아이디 혹은 비밀번호가 틀렸습니다.");
				req.getRequestDispatcher("/views/auth/login.jsp").forward(req, resp);
			}
		} catch (Exception e) {
			req.setAttribute("error_result", "로그인 중 오류가 발생했습니다.");
			req.getRequestDispatcher("/views/auth/login.jsp").forward(req, resp);
		}
	}
}
