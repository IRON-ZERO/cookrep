package service;

import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Logger;

import Exceptions.AuthExceptions.DuplicateEmailException;
import Exceptions.AuthExceptions.DuplicateNicknameException;
import dto.auth.LoginDTO;
import dto.auth.SignupDTO;
import repository.AuthDAO;
import utils.PasswordUtils;
import utils.UUIDGenerater;
import utils.ValidatorUtils;

/**
 * Auth 관련 유저의 생성 / 로그인 / 로그아웃 / 유저삭제와 관련된 
 * service 클래스입니다.
 * 
 * <p>
 * 이 클래스는 다음의 기능을 제공합니다.
 * </p>
 * <ul>
 * <li>유저 생성</li>
 * <li>로그인</li>
 * <li>로그아웃</li>
 * <li>유저삭제</li>
 * </ul>
 * 
 * @author choijoonhuk
 * @since 1.0
 */

public class AuthService {
	// repository
	private AuthDAO authRepo = AuthDAO.getInstance();
	// Logger 
	private Logger log = Logger.getLogger(AuthService.class.getName());

	// constructor
	private AuthService() {}

	// Singleton Holder
	private static class Holder {
		private static final AuthService INSTANCE = new AuthService();
	}

	public static AuthService getInstance() {
		return Holder.INSTANCE;
	}

	/**
	 * 유저의 회원가입서비스 메서드입니다.
	 * 
	 * @param userData
	 * @return boolean
	 * @throws
	 */
	public String signUpUser(SignupDTO userData) throws DuplicateEmailException, DuplicateNicknameException {
		// variable
		String nickname = userData.getNickname();
		String email = userData.getEmail();

		// validator
		if (!ValidatorUtils.validateNickname(nickname) || !ValidatorUtils.validateEmail(email)) {
			throw new IllegalArgumentException("유효하지 않는 입력값입니다.");
		}

		// 유저의 존재 확인
		if (emailExists(email)) {
			throw new DuplicateEmailException(email);
		}
		if (nicknameExists(nickname)) {
			throw new DuplicateNicknameException(nickname);
		}

		// 비밀번호 해쉬화
		String password = userData.getPassword();
		String salt = PasswordUtils.generateSalt();
		String hashPassword = PasswordUtils.hashPassword(password, salt);
		userData.setId(UUIDGenerater.generator());
		userData.setPassword(hashPassword + ":" + salt);

		// DB로 데이터 전달

		try {
			String uid = authRepo.signUpUser(userData);
			return uid;
		} catch (SQLException e) {
			log.severe("AuthService SignUpUser에서 에러 :" + e.getMessage());
			throw new RuntimeException("회원가입 처리 중 오류가 발생했습니다.");
		}
	}

	/**
	 * 닉네임으로 로그인하는 메서드입니다.
	 * 
	 * @param userData
	 * @return user_id 없으면 optional.empty
	 */
	public Optional<String> loginUserByNickname(LoginDTO userData) {
		try {
			// db에 데이터를 보내 결과값 리턴
			LoginDTO result = authRepo.loginUserByNickname(userData);
			if (result == null) {
				return Optional.empty();
			}
			String[] splitPassword = result.getPassword().split(":");
			if (splitPassword.length != 2) {
				log.severe("AuthService Loing 비밀번호 데이터 오류");
				return Optional.empty();
			}
			// 입력된 비밀번호 값과 db에 저장된 비밀번호 비교  
			boolean equals = PasswordUtils.verifyPassword(userData.getPassword(), splitPassword[1], splitPassword[0]);
			// 결과값 비밀번호 verify
			return equals ? Optional.of(result.getIdentifyId()) : Optional.empty();
		} catch (SQLException e) {
			log.severe("AuthService loginUserByNickname에서 에러 :" + e.getMessage());
			return Optional.empty();
		} catch (Exception e) {
			log.severe("AuthService loginUserByNickname에서 에러 :" + e.getMessage());
			return Optional.empty();
		}
	}

	/**
	 * 이메일로 로그인하는 메서드입니다.
	 * 
	 * @param userData
	 * @return user_id 없으면 optional.empty
	 */
	public Optional<String> loginUserByEmail(LoginDTO userData) {
		// db에 데이터를 보내 결과값 리턴

		try {
			LoginDTO result = authRepo.loginUserByEmail(userData);
			if (result == null) {
				return Optional.empty();
			}
			String[] splitPassword = result.getPassword().split(":");
			if (splitPassword.length != 2) {
				log.severe("AuthService Loing 비밀번호 데이터 오류");
				return Optional.empty();
			}
			// 입력된 비밀번호 값과 db에 저장된 비밀번호 비교  
			boolean equals = PasswordUtils.verifyPassword(userData.getPassword(), splitPassword[1], splitPassword[0]);
			return equals ? Optional.of(result.getIdentifyId()) : Optional.empty();
		} catch (SQLException e) {
			log.severe("AuthService loginUserByNickname에서 에러 :" + e.getMessage());
			return Optional.empty();
		} catch (Exception e) {
			log.severe("AuthService loginUserByNickname에서 에러 :" + e.getMessage());
			return Optional.empty();
		}

		// 결과값 비밀번호 verify 준
	}

	/**
	 * 회원탈퇴 메서드입니다. 
	 * @param userId
	 * @return boolean
	 */
	public boolean deleteUser(String userId) {
		try {
			return authRepo.deleteUser(userId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("AuthService deletUser에서 에러 :" + e.getMessage());
			return false;
		}
	}

	/* Helper Methods */
	/**
	 * 이메일이 존재하는지 확인합니다.
	 * 
	 * @param email
	 * @return boolean
	 */
	private boolean emailExists(String email) {
		try {
			return authRepo.existsUserByEmail(email);
		} catch (SQLException e) {
			log.severe("AuthService emailExists에서 에러 :" + e.getMessage());
			return false;
		}
	}

	/**
	 * 유저의 닉네임을 조회합니다.
	 * 
	 * @param nickname
	 * @return boolean
	 */
	private boolean nicknameExists(String nickname) {
		try {
			return authRepo.existsUserByNickName(nickname);
		} catch (SQLException e) {
			log.severe("AuthService nicknameExists에서 에러 :" + e.getMessage());
			return false;
		}
	}
}
