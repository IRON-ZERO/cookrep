package service;

import dto.auth.LoginDTO;
import dto.auth.SignupDTO;
import repository.AuthDAO;
import utils.PasswordUtils;
import utils.UUIDgenerater;
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
	public boolean signUpUser(SignupDTO userData) {
		// variable
		String nickname = userData.getNickname();
		String email = userData.getEmail();

		// validator
		if (!ValidatorUtils.validateNickname(nickname) || !ValidatorUtils.validateEmail(email)) {
			return false;
		}

		// 유저의 존재 확인
		if (emailExists(email) || nicknameExists(nickname)) {
			return false;
		}

		// 비밀번호 해쉬화
		String password = userData.getPassword();
		String salt = PasswordUtils.generateSalt();
		String hashPassword = PasswordUtils.hashPassword(password, salt);
		userData.setId(UUIDgenerater.generator());
		userData.setPassword(hashPassword + ":" + salt);

		// DB로 데이터 전달
		int success = authRepo.signUpUser(userData);
		return success > 0;
	}

	/**
	 * 닉네임으로 로그인하는 메서드입니다.
	 * 
	 * @param userData
	 * @return user_id
	 */
	public String loginUserByNickname(LoginDTO userData) {
		// db에 데이터를 보내 결과값 리턴
		LoginDTO result = authRepo.loginUserByNickname(userData);
		if (result == null) {
			return null;
		}
		// 결과값 비밀번호 verify 준
		String[] splitPassword = result.getPassword().split(":");

		// 입력된 비밀번호 값과 db에 저장된 비밀번호 비교  
		boolean equals = PasswordUtils.verifyPassword(userData.getPassword(), splitPassword[1], splitPassword[0]);
		return equals ? result.getIdentifyId() : null;
	}

	/**
	 * 이메일로 로그인하는 메서드입니다.
	 * 
	 * @param userData
	 * @return user_id
	 */
	public String loginUserByEmail(LoginDTO userData) {
		// db에 데이터를 보내 결과값 리턴
		LoginDTO result = authRepo.loginUserByEmail(userData);
		if (result == null) {
			return null;
		}
		// 결과값 비밀번호 verify 준
		String[] splitPassword = result.getPassword().split(":");
		// 입력된 비밀번호 값과 db에 저장된 비밀번호 비교  
		boolean equals = PasswordUtils.verifyPassword(userData.getPassword(), splitPassword[1], splitPassword[0]);
		return equals ? result.getIdentifyId() : null;
	}

	/**
	 * 회원탈퇴 메서드입니다. 
	 * @param userId
	 * @return boolean
	 */
	public boolean deleteUser(String userId) {
		return authRepo.deleteUser(userId);
	}

	/* Helper Methods */
	/**
	 * 이메일이 존재하는지 확인합니다.
	 * 
	 * @param email
	 * @return boolean
	 */
	private boolean emailExists(String email) {
		return authRepo.existsUserByEmail(email);
	}

	/**
	 * 유저의 닉네임을 조회합니다.
	 * 
	 * @param nickname
	 * @return boolean
	 */
	private boolean nicknameExists(String nickname) {
		return authRepo.existsUserByNickName(nickname);
	}
}
