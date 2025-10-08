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
    public void updateProfile(UserDTO userDTO) {
        UserDAO userDAO = new UserDAO();
        try {
            // id 없을 때, 유효성 걸릴 때의 예외 아직 안 넣음
            int result = userDAO.updateUser(userDTO);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
