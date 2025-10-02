package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import dto.auth.LoginDTO;
import dto.auth.SignupDTO;
import utils.DBConnection;

/**
 * Auth 관련 유저의 생성 / 로그인 / 로그아웃 / 유저삭제와 관련된 
 * repository 클래스입니다.
 * 
 * <p>
 * 이 클래스는 다음의 기능을 제공합니다.
 * </p>
 * <ul>
 * <li>DB에 유저 생성</li>
 * <li>DB에 유저 확인 후 로그인</li>
 * <li>DB에 유저 확인 후 로그아웃</li>
 * <li>DB에 유저 확인 후 유저삭제</li>
 * </ul>
 * 
 * @author choijoonhuk
 * @since 1.0
 */

public class AuthDAO {
	// Initialized DB
	private DBConnection db = new DBConnection();
	// Logger
	private Logger log = Logger.getLogger(AuthDAO.class.getName());

	// Constructor
	private AuthDAO() {}

	// Singleton Holder 
	private static class Holder {
		private static final AuthDAO INSTANCE = new AuthDAO();
	}

	public static AuthDAO getInstance() {
		return Holder.INSTANCE;
	}

	/**
	 * 유저 회원가입을 하여 DB에 회원을 추가하는 메서드입니다.
	 * 
	 * @param SignupDTO
	 * @return 회원가입 성공시 1 실패시 0
	 * @throws 
	 */
	public int signUpUser(SignupDTO userData) {
		db.open();
		String sql = """
			INSERT INTO User
			(user_id, nickname, first_name, last_name, country, city, email, password)
			VALUES (?,?,?,?,?,?,?,?)
			""";
		try (Connection con = db.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, userData.getId());
			pstmt.setString(2, userData.getNickname());
			pstmt.setString(3, userData.getFirst_name());
			pstmt.setString(4, userData.getLast_name());
			pstmt.setString(5, userData.getCountry());
			pstmt.setString(6, userData.getCity());
			pstmt.setString(7, userData.getEmail());
			pstmt.setString(8, userData.getPassword());
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			log.severe("signUpUser DAO에서 에러 발생 : " + e.getMessage());
			db.close();
			return 0;
		} finally {
			db.close();
		}
	}

	/**
	 * 유저의 nickname을 값을 받아 데이터를 서비스로 넘겨줍니다.
	 * 
	 * @param userData
	 * @return LoginDTO
	 */
	public LoginDTO loginUserByNickname(LoginDTO userData) {
		db.open();
		String sql = """
			SELECT user_id, password
			FROM User
			WHERE nickname = ?
			""";
		try (Connection con = db.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, userData.getIdentifyId());
			ResultSet result = pstmt.executeQuery();
			if (result.next()) {
				LoginDTO data = new LoginDTO(
					result.getString("user_id"),
					result.getString("password"));
				return data;
			}
			return null;
		} catch (SQLException e) {
			log.severe("loginUserByNickName DAO에서 에러 발생 : " + e.getMessage());
			db.close();
			return null;
		} finally {
			db.close();
		}
	}

	/**
	 * 유저의 email을 값을 받아 데이터를 서비스로 넘겨줍니다.
	 * 
	 * @param userData
	 * @return LoginDTO
	 */

	public LoginDTO loginUserByEmail(LoginDTO userData) {
		db.open();
		String sql = """
			SELECT user_id, password
			FROM User
			WHERE email = ?
			""";
		try (Connection con = db.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, userData.getIdentifyId());
			ResultSet result = pstmt.executeQuery();
			if (result.next()) {
				LoginDTO data = new LoginDTO(
					result.getString("user_id"),
					result.getString("password"));
				return data;
			}
			return null;
		} catch (SQLException e) {
			log.severe("loginUserByEmail DAO에서 에러 발생 : " + e.getMessage());
			db.close();
			return null;
		} finally {
			db.close();
		}
	}

	/**
	 * 유저가 로그아웃을 할때 토큰을 삭제하는 메서드입니다.
	 * 
	 *  토큰 사용이 확정되면 구현
	 *  
	 * @return
	 */
	public String logoutUser() {
		return "";
	}

	/**
	 * 유저가 회원탈퇴시 저장된 유저를 삭제하는 메서드입니다.
	 * 
	 * @return
	 */
	public boolean deleteUser(String id) {
		db.open();
		String sql = """
			DELETE FROM User
			WHERE user_id = ?
			""";
		try (Connection con = db.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);) {
			pstmt.setString(1, id);
			int result = pstmt.executeUpdate();
			return result > 0;
		} catch (SQLException e) {
			log.severe("deleteUser DAO에서 에러 발생 : " + e.getMessage());
			db.close();
			return false;
		} finally {
			db.close();
		}
	}

	/* *** Helper Methods *** */

	/**
	 * 유저의 닉네임으로 조회하여 유저의 유무를 확인합니다.
	 * 
	 * @param nickname
	 * @return boolean
	 */
	public boolean existsUserByNickName(String nickname) {
		// db open
		db.open();
		// sql query
		String sql = """
			SELECT user_id
			FROM User
			WHERE nickname = ?
			""";
		// try catch with resource
		try (Connection con = db.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql);) {
			pstmt.setString(1, nickname);
			ResultSet result = pstmt.executeQuery();
			return result.next();
		} catch (SQLException e) {
			log.severe("existsUserByNickName DAO에서 에러 발생 : " + e.getMessage());
			e.printStackTrace();
			db.close();
			return false;
		} finally {
			db.close();
		}
	}

	/**
	 * 유저의 닉네임으로 조회하여 유저의 유무를 확인합니다.
	 * 
	 * @param email
	 * @return boolean
	 */
	public boolean existsUserByEmail(String email) {
		// db open
		db.open();
		// sql query
		String sql = """
			SELECT user_id
			FROM User
			WHERE email = ?
			""";
		// try catch with resource
		try (Connection con = db.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql);) {
			pstmt.setString(1, email);
			ResultSet result = pstmt.executeQuery();
			return result.next();
		} catch (SQLException e) {
			log.severe("existsUserByEmail DAO에서 에러 발생 : " + e.getMessage());
			db.close();
			e.printStackTrace();
			return false;
		} finally {
			db.close();
		}

	}
}
