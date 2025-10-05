package Exceptions;

public class AuthExceptions extends Exception {
	public AuthExceptions(String msg) {
		super(msg);
	}

	public static class DuplicateEmailException extends AuthExceptions {
		public DuplicateEmailException(String email) {
			super("이미 사용중인 이메일 입니다. " + email);
		}
	}

	public static class DuplicatieNicknameException extends AuthExceptions {
		public DuplicatieNicknameException(String nickname) {
			super("이미 사용중인 유저이름입니다. " + nickname);
		}
	}
}
