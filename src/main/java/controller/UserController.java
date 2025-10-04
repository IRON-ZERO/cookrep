package controller;

import dto.user.UserDTO;
import model.UserIngredient;
import org.apache.commons.beanutils.BeanUtils;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(urlPatterns = {"/mypage","/mypage/freezer","/mypage/scrap"})
public class UserController extends HttpServlet {
    String testUserId = "1";
    int testIngredientId = 1;
    int[] testIngredientIds = {1,2,3};

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String path = req.getServletPath();
        String view = "";
        if (action == null) {
            switch (path){
                case "/mypage":
                    view = getProfile(req,resp);
                    break;
                case "/mypage/freezer" :
                    view = getIngredients(req,resp);
                    break;
                case "/mypage/scrap" :
                    break;
            }
        }else {
            switch (action) {
                case "getprofile":
                    view = getProfile(req, resp);
                    break;
                case "getingredients" :
                    view = getIngredients(req,resp);
                    break;
            }
        }
        req.getRequestDispatcher(view).forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String view = "";
        switch (action) {
            case "updateprofile" :
                view = updateProfile(req,resp);
                break;
            case "deleteingredient" :
                view = deleteIngredient(req,resp);
                break;
            case "addingredient" :
                view = addIngredient(req,resp);
                break;
        }
        req.getRequestDispatcher(view).forward(req, resp);
    }

    // return 되는 path는 화면 만들면 수정
    // 유저 조회, 업데이트
    private String getProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService userService = new UserService();
        UserDTO user = null;
        try {
            user = userService.getProfile(testUserId);
            req.setAttribute("status","200");
            req.setAttribute("message","사용자 정보 조회에 성공했습니다.");
            req.setAttribute("user",user);
        } catch (SQLException e) {
            req.setAttribute("status","500");
            req.setAttribute("message","DB 처리 중 오류가 발생했습니다.\n"+e.getMessage());
        }
        return "/views/mypage/mypage.jsp";
    }
    private String updateProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService userService = new UserService();
        UserDTO userDTO = new UserDTO();
        try {
            BeanUtils.populate(userDTO,req.getParameterMap());
            userDTO.setId(testUserId);
            userService.updateProfile(userDTO);
            req.setAttribute("status","200");
            req.setAttribute("message","사용자 정보 업데이트에 성공했습니다.");
        } catch (IllegalAccessException e) {
            req.setAttribute("status","400");
            req.setAttribute("message","요청 파라미터 매핑 중에 오류가 발생했습니다.\n"+e.getMessage());
        } catch (InvocationTargetException e) {
            req.setAttribute("status","400");
            req.setAttribute("message","요청 파라미터 매핑 중에 오류가 발생했습니다.\n"+e.getMessage());
        } catch (SQLException e) {
            req.setAttribute("status","500");
            req.setAttribute("message","DB 처리 중 오류가 발생했습니다.\n"+e.getMessage());
        }
        return getProfile(req,resp);

    }

    // 냉장고(유저가 가진 재료) 추가, 삭제, 조회
    private String deleteIngredient(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService userService = new UserService();
        String userId = req.getParameter("userId");
        int ingredientId = Integer.parseInt(req.getParameter("ingredientId"));
        try {
            userService.removeIngredient(userId, ingredientId);
            req.setAttribute("status","200");
            req.setAttribute("message","재료 삭제에 성공했습니다.");
        }catch (SQLException e){
            req.setAttribute("status","500");
            req.setAttribute("message","DB 처리 중 오류가 발생했습니다.\n"+e.getMessage());
        }
        return getIngredients(req,resp);
    }
    private String addIngredient(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService userService = new UserService();
//        언어수준 7에선 사용 못하는 문법. java 8+ 사용 시 개선
//        int[] ingredientIds = Arrays.stream(req.getParameter("ingredientIds").trim().split(","))
//                                    .mapToInt(Integer::parseInt)
//                                    .toArray();
        String[] stringIngredientIds = req.getParameter("ingredientIds").replaceAll(" ","").split(",");
        int[] ingredientIds = new int[stringIngredientIds.length];
        for(int i = 0; i < stringIngredientIds.length; i++){
            ingredientIds[i] = Integer.parseInt(stringIngredientIds[i]);
        }
        try {
            userService.addIngredient(testUserId,ingredientIds);
            req.setAttribute("status","200");
            req.setAttribute("message","재료 추가에 성공했습니다.");
        } catch (SQLException e) {
            req.setAttribute("status","500");
            req.setAttribute("message","DB 처리 중 오류가 발생했습니다.\n"+e.getMessage());
        }
        return getIngredients(req,resp);
    }

    private String getIngredients(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService userService = new UserService();
        UserDTO user = null;
        try {
            user = userService.getProfile(testUserId);
            req.setAttribute("status","200");
            req.setAttribute("message","재료 조회에 성공했습니다.");
        } catch (SQLException e) {
            req.setAttribute("status","500");
            req.setAttribute("message","DB 처리 중 오류가 발생했습니다.\n"+e.getMessage());
        }
        req.setAttribute("user",user);
        return "/views/mypage/myfreezer.jsp";
    }
}
