package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnection {
	private Connection con = null;
	private PreparedStatement pstmt = null;

	String user = System.getenv("DB_USER");
	String password = System.getenv("DB_PASSWORD");

	private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	//    private final String JDBC_URL = "jdbc:mysql://localhost:3306/cookrep?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Seoul";

	String host = "cookrepsql"; // 컨테이너 이름
	String port = "3306";
	String dbName = "cookrep";

	private final String JDBC_URL = "jdbc:mysql://" + host + ":" + port + "/" + dbName +
		"?serverTimezone=Asia/Seoul&useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";

	// DB 접속
	public Connection open() {
		try {
			Class.forName(JDBC_DRIVER);
			con = DriverManager.getConnection(JDBC_URL, user, password);
			//            System.out.println("DB 연결 성공!");
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC 드라이버 로드 실패: " + e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			System.err.println("DB 연결 실패: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("알 수 없는 오류 발생: " + e.getMessage());
			e.printStackTrace();
		}
		return con;
	}

	// DB 종료
	public void close() {
		try {
			if (pstmt != null && !pstmt.isClosed())
				pstmt.close();
			if (con != null && !con.isClosed())
				con.close();
			System.out.println("DB 연결 종료 완료!");
		} catch (SQLException e) {
			System.err.println("DB 종료 중 오류 발생: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("알 수 없는 오류 발생: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// Getter (연결 객체 반환)
	public Connection getConnection() {
		return con;
	}
}

// Local test
//package utils;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//public class DBConnection {
//	private Connection con = null;
//	private PreparedStatement pstmt = null;
//
//	private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
//	private final String JDBC_URL = "jdbc:mysql://localhost:3306/cookrep?serverTimezone=Asia/Seoul";
//
//	// DB 접속
//	public void open() {
//		try {
//			Class.forName(JDBC_DRIVER);
//			con = DriverManager.getConnection(JDBC_URL, "root", "1111");
//			System.out.println("DB 연결 성공!");
//		} catch (ClassNotFoundException e) {
//			System.err.println("JDBC 드라이버 로드 실패: " + e.getMessage());
//			e.printStackTrace();
//		} catch (SQLException e) {
//			System.err.println("DB 연결 실패: " + e.getMessage());
//			e.printStackTrace();
//		} catch (Exception e) {
//			System.err.println("알 수 없는 오류 발생: " + e.getMessage());
//			e.printStackTrace();
//		}
//	}
//
//	// DB 종료
//	public void close() {
//		try {
//			if (pstmt != null && !pstmt.isClosed())
//				pstmt.close();
//			if (con != null && !con.isClosed())
//				con.close();
//			System.out.println("DB 연결 종료 완료!");
//		} catch (SQLException e) {
//			System.err.println("DB 종료 중 오류 발생: " + e.getMessage());
//			e.printStackTrace();
//		} catch (Exception e) {
//			System.err.println("알 수 없는 오류 발생: " + e.getMessage());
//			e.printStackTrace();
//		}
//	}
//
//	// Getter (연결 객체 반환)
//	public Connection getConnection() {
//		return con;
//	}
//}