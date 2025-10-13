package filter;

import java.io.IOException;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthFilter implements Filter {
	private static final Set<String> NOT_LOGIN_PAGES = Set.of("/", "/login", "/join");
	private static final Set<String> LOGIN_PAGES = Set.of("/", "/rank", "/search","/mypage","/mypage/freezer", "/new-recipe", "/logout");
	private static final Set<String> START_WITH_STATIC_RESOURCE = Set.of("/assets", "/js/", "/images/");
	private static final Set<String> END_WITH_STATIC_RESOURCE = Set.of(".css", ".js", ".png");
    private boolean isStaticResource(String path) {
        return path.startsWith("/assets/")
            || path.startsWith("/js/")
            || path.startsWith("/images/")
            || path.endsWith(".css")
            || path.endsWith(".js")
            || path.endsWith(".png")
            || path.endsWith(".jpg")
            || path.endsWith(".jpeg")
            || path.endsWith(".ico")
            || path.endsWith(".json")
            || path.endsWith(".svg");
    }

    @Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		HttpSession session = req.getSession();
		Object userId = session.getAttribute("userId");
		String path = req.getRequestURI();
		if (isStaticResource(path)) {
			chain.doFilter(request, response);
			return;
		}

		// 로그인 안된 사용자
		if (userId == null) {
			if (NOT_LOGIN_PAGES.contains(path)) {
				chain.doFilter(request, response);
			} else if (LOGIN_PAGES.contains(path)) {
				resp.sendRedirect("/login");
			} else {
				req.getRequestDispatcher("/not-found").forward(request, response);
				return;
			}
		}
		// 로그인 된 사용자
		else {
			if (path.equals("/login") || path.equals("/join")) {
				resp.sendRedirect("/");
			} else if (LOGIN_PAGES.contains(path)) {
				chain.doFilter(request, response);
			} else {
				req.getRequestDispatcher("/not-found").forward(request, response);
				return;
			}
		}
	}
}
