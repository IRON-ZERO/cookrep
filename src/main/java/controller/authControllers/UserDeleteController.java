package controller.authControllers;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import service.AuthService;

@WebServlet("/deleteAccount")
public class UserDeleteController extends HttpServlet {
	AuthService authService = null;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		authService = AuthService.getInstance();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			HttpSession session = req.getSession(false);
			if (session == null || session.getAttribute("userId") == null) {
				resp.sendRedirect("/views/auth/login.jsp");
			}
			String userId = (String)session.getAttribute("userId");
			authService.deleteUser(userId);
			session.invalidate();
			resp.sendRedirect("/views/auth/login.jsp");
		} catch (Exception e) {
			req.setAttribute("error_msg", "회원탈퇴 중 오류가 발생했습니다.");
			// 여기에 다시 페이지로 돌려보내줘야
		}
	}
}
