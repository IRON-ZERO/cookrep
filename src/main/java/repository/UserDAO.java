package repository;

import dto.user.UserDTO;
import model.User;
import utils.DBConnection;

import java.sql.*;
import java.util.Optional;

public class UserDAO {
    private Connection conn = null;

    public UserDAO() {
        DBConnection dbConnection = new DBConnection();
        dbConnection.open();
        this.conn = dbConnection.getConnection();
    }

    // 마이 페이지(프로필) 조회할 때 사용
    public Optional<User> findById(String id) throws SQLException {
        User user = new User();
        String sql =  "SELECT * FROM user WHERE user_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            user.setId(rs.getString("user_id"));
            user.setNickname(rs.getString("nickname"));
            user.setFirstName(rs.getString("first_Name"));
            user.setLastName(rs.getString("last_Name"));
            user.setEmail(rs.getString("email"));
            user.setCountry(rs.getString("country"));
            user.setCity(rs.getString("city"));
            user.setPassword(rs.getString("password"));
            user.setCreatedAt(rs.getTimestamp("created_at"));
            user.setUpdatedAt(rs.getTimestamp("updated_at"));
            return Optional.of(user);
        }else{
            return Optional.empty();
        }
    }
}
