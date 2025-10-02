package controller;

import dto.user.UserDTO;
import exception.UserNotFoundException;
import org.apache.commons.beanutils.BeanUtils;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@WebServlet("/mypage")
public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String view = "";
        switch (action) {
            case "getprofile" :
                view += getProfile(req,resp);
                break;
            case "updateprofile" :
                view += updateProfile(req,resp);
        }
        req.getRequestDispatcher(view).forward(req, resp);
    }

    private String getProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = "2"; // 테스트용
        UserService userService = new UserService();
        try {
            UserDTO user = userService.getProfile(id);
            req.setAttribute("user",user);
            return "/mypagetest.jsp";
        }catch (UserNotFoundException e){
            req.setAttribute("error",e);
            return "/errortest.jsp";
        }
    }
    private String updateProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService userService = new UserService();
        UserDTO userDTO = new UserDTO();
        try {
            BeanUtils.populate(userDTO,req.getParameterMap());
            userService.updateProfile(userDTO);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return "/mypagetest.jsp";
    }
}
