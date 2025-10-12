package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

public class PasswordUtils {
	// 솔트 생성 (16 바이트)
	public static String generateSalt() {
		byte[] salt = new byte[16]; // 1) 바이트 배열(원시 랜덤값 저장)
		new SecureRandom().nextBytes(salt); // 2) 보안성이 좋은 난수 생성
		return Base64.getEncoder().encodeToString(salt); // 3) 바이트를 문자열로 저장하기 위해 Base64 인코딩
	}

	// 솔트와 함께 SHA-256 해시 (입력 문자셋 명시)
	public static String hashPassword(String password, String saltBase64) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256"); // SHA-256 해시 알고리즘
			byte[] salt = Base64.getDecoder().decode(saltBase64); // 저장된 솔트를 다시 바이트로 디코딩
			md.update(salt); // 솔트를 먼저 입력(권장 방식)
			byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8)); // 비밀번호 해시

			// 바이트 배열 -> 16진수 문자열 변환 (사람이 읽을 수 있게)
			StringBuilder sb = new StringBuilder();
			for (byte b : hashedBytes) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException("해시 생성 실패", e);
		}
	}

	// 비밀번호 검증: 입력된 평문 비밀번호를 같은 방식으로 해시하여 비교
	public static boolean verifyPassword(String inputPassword, String storedSaltBase64, String storedHashHex) {
		String inputHash = hashPassword(inputPassword, storedSaltBase64);
		// 타임-상수 시간 비교가 이상적 (타이밍 공격 방지) — 간단한 equals 사용
		return Objects.equals(inputHash, storedHashHex);
	}
}
