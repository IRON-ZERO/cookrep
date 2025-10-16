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
        UserDTO oldUserDTO = getProfile(userDTO.getId());
        if (userDTO.getFirstName() != null && !userDTO.getFirstName().isEmpty()){
            oldUserDTO.setFirstName(userDTO.getFirstName());
        }
        if (userDTO.getLastName() != null && !userDTO.getLastName().isEmpty()){
            oldUserDTO.setLastName(userDTO.getLastName());
        }
        if (userDTO.getCountry() != null && !userDTO.getCountry().isEmpty()){
            oldUserDTO.setCountry(userDTO.getCountry());
        }
        if(userDTO.getCity() != null && !userDTO.getCity().isEmpty()){
            oldUserDTO.setCity(userDTO.getCity());
        }
        userDAO.updateUser(oldUserDTO);

    }
    public void addIngredient(String userId, int ingredientId) throws SQLException {
        UserIngredientDAO userIngredientDAO = new UserIngredientDAO();
        IngredientDAO ingredientDAO = new IngredientDAO();

        userIngredientDAO.addUserIngredient(userId,ingredientId);

    }
    // addIngredient 오버로딩, 배열로 왔을 경우에 해당 재료들 전부 업로드.
    public void addIngredient(String userId, int[] ingredientIds) throws SQLException {
        for(int ingredientId : ingredientIds){
            addIngredient(userId,ingredientId);
        }
    }
    public void removeIngredient(String userId, int ingredientId) throws SQLException {
        UserIngredientDAO userIngredientDAO = new UserIngredientDAO();
        IngredientDAO ingredientDAO = new IngredientDAO();
        // 추후에 필요한 exception 있으면 던지면 됨.
        userIngredientDAO.removeUserIngredient(userId,ingredientId);

    }
    private List<UserIngredient> getIngredients(String userId) throws SQLException {
        UserIngredientDAO userIngredientDAO = new UserIngredientDAO();
        List<UserIngredient> ingredients;
        // 추후에 필요한 exception 있으면 던지면 됨.
        ingredients = userIngredientDAO.findByUserId(userId);
        return  ingredients;
    }
}
