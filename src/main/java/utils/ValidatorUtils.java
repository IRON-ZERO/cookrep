package utils;

public class ValidatorUtils {
	private static String emailFormat = "^[a-zA-Z0-9-_]{5,20}@[a-zA-Z0-9]+\\.(co|com|net|co\\.kr|kr)$";
	private static String nicknameFormat = "^[a-zA-Z0-9-_]{5,20}$";

	public static boolean validateNickname(String nickname) {
		return nickname.trim().matches(nicknameFormat);

	}

	public static boolean validateEmail(String email) {
		return email.trim().matches(emailFormat);
	}
}
