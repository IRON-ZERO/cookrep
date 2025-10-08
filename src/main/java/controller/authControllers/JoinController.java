package controller.authControllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Exceptions.AuthExceptions.DuplicateEmailException;
import Exceptions.AuthExceptions.DuplicateNicknameException;
import dto.auth.SignupDTO;
import service.AuthService;
import utils.ValidatorUtils;

@WebServlet("/join")
public class JoinController extends HttpServlet {
	AuthService authService;

	@Override
	public void init() throws ServletException {
		authService = AuthService.getInstance();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/views/auth/join.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		// Params
		String nickname = req.getParameter("nickname");
		String email = req.getParameter("email");
		String firstName = req.getParameter("first_name");
		String lastName = req.getParameter("last_name");
		String country = req.getParameter("country");
		String city = req.getParameter("city");
		String password = req.getParameter("password");
		String check_pass = req.getParameter("check_pass");

		// client error
		boolean hasError = false;
		if (nickname == null || !ValidatorUtils.validateNickname(nickname)) {
			req.setAttribute("error_nickname", "유저이름 방식이 잘못되었습니다. (영문,숫자,-,_ 5~20자)");
			hasError = true;
		}
		if (email == null || !ValidatorUtils.validateEmail(email)) {
			req.setAttribute("error_email", "이메일 방식이 잘못되었습니다.");
			hasError = true;
		}
		if (password == null || check_pass == null || !password.equals(check_pass)) {
			req.setAttribute("error_password", "비밀번호가 일치하지 않습니다.");
			hasError = true;
		}
		if (hasError) {
			req.getRequestDispatcher("/views/auth/join.jsp").forward(req, resp);
			return;
		}

		// server 
		try {
			SignupDTO signup = new SignupDTO(null, nickname, firstName, lastName, country, city, email, password);
			String userId = authService.signUpUser(signup);
			// Session
			HttpSession session = req.getSession();
			session.setAttribute("userId", userId);
			// redirect
			resp.sendRedirect("/index.jsp");
		} catch (DuplicateEmailException e) {
			req.setAttribute("error_email", "이미 사용 중인 이메일입니다.");
			setUserAttribute(req, nickname, email, firstName, lastName, country, city);

			req.getRequestDispatcher("/views/auth/join.jsp").forward(req, resp);
		} catch (DuplicateNicknameException e) {
			req.setAttribute("error_nickname", "이미 사용 중인 유저이름입니다.");
			setUserAttribute(req, nickname, email, firstName, lastName, country, city);

			req.getRequestDispatcher("/views/auth/join.jsp").forward(req, resp);
		} catch (IllegalArgumentException e) {
			req.setAttribute("error_result", "유효하지 않는 입력값입니다.");
			setUserAttribute(req, nickname, email, firstName, lastName, country, city);

			req.getRequestDispatcher("/views/auth/join.jsp").forward(req, resp);
		} catch (RuntimeException e) {
			req.setAttribute("error_result", "회원가입중 오류가 발생했습니다.");
			setUserAttribute(req, nickname, email, firstName, lastName, country, city);

			req.getRequestDispatcher("/views/auth/join.jsp").forward(req, resp);
		} catch (Exception e) {
			req.setAttribute("error_result", "알 수 없는 오류가 발생했습니다.");
			setUserAttribute(req, nickname, email, firstName, lastName, country, city);

			req.getRequestDispatcher("/views/auth/join.jsp").forward(req, resp);
		}
	}

	private void setUserAttribute(HttpServletRequest req, String nickname,
		String email,
		String firstName,
		String lastName,
		String country,
		String city) {
		req.setAttribute("nickname", nickname);
		req.setAttribute("email", email);
		req.setAttribute("first_name", firstName);
		req.setAttribute("last_name", lastName);
		req.setAttribute("country", country);
		req.setAttribute("city", city);
	}
}
