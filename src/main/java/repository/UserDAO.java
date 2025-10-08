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

    public int updateUser(UserDTO userDTO) throws SQLException {
        // 비밀번호는 다른 데서 해야하나 모르겠어서 일단.
        String sql = "UPDATE user SET  first_name = ?, last_name = ?, country = ?, city = ? WHERE user_id = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, userDTO.getFirstName());
        pstmt.setString(2, userDTO.getLastName());
        pstmt.setString(3, userDTO.getCountry());
        pstmt.setString(4, userDTO.getCity());
        pstmt.setString(5, userDTO.getId());
        // 영향 받은 row 개수에 대한 int형 정수 반환
        int result = pstmt.executeUpdate();
        return result;
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
