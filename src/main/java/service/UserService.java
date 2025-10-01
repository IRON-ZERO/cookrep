package service;

import dto.user.UserDTO;
import exception.UserNotFoundException;
import repository.UserDAO;

import java.sql.SQLException;

public class UserService {
    public UserDTO getProfile(String id) {
        UserDAO userDAO = new UserDAO();
        try {
            return userDAO.findById(id)
                .map(UserDTO::from)
                .orElseThrow(()->new UserNotFoundException(id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
