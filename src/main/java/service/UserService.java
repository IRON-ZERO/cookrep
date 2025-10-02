package service;

import dto.user.UserDTO;
import exception.IdNotFoundException;
import model.Ingredient;
import model.UserIngredient;
import repository.IngredientDAO;
import repository.UserDAO;
import repository.UserIngredientDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    public UserDTO getProfile(String id) throws SQLException {
        UserDAO userDAO = new UserDAO();
        UserIngredientDAO userIngredientDAO = new UserIngredientDAO();
        UserDTO userDTO = UserDTO.from(userDAO.findById(id));
        userDTO.setIngredients(getIngredients(id));
        return userDTO;
    }
    public void updateProfile(UserDTO userDTO) throws SQLException{
        UserDAO userDAO = new UserDAO();
        int result = userDAO.updateUser(userDTO);

    }
    public void addIngredient(String userId, int ingredientId) throws SQLException {
        UserIngredientDAO userIngredientDAO = new UserIngredientDAO();
        IngredientDAO ingredientDAO = new IngredientDAO();

        userIngredientDAO.addUserIngredient(userId,ingredientId);

    }
    public void removeIngredient(String userId, int ingredientId) throws SQLException {
        UserIngredientDAO userIngredientDAO = new UserIngredientDAO();
        IngredientDAO ingredientDAO = new IngredientDAO();
        // 추후에 필요한 exception 있으면 던지면 됨.
        userIngredientDAO.removeUserIngredient(userId,ingredientId);

    }
    public List<UserIngredient> getIngredients(String userId) throws SQLException {
        UserIngredientDAO userIngredientDAO = new UserIngredientDAO();
        List<UserIngredient> ingredients;
        // 추후에 필요한 exception 있으면 던지면 됨.
        ingredients = userIngredientDAO.findByUserId(userId);
        return  ingredients;
    }
}
