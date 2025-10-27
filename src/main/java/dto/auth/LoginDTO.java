package dto.auth;

public class LoginDTO {
	private String identifyId;
	private String password;

	public LoginDTO(String identifyId, String password) {
		this.identifyId = identifyId;
		this.password = password;
	}

	public String getIdentifyId() {
		return identifyId;
	}

	public void setIdentifyId(String identifyId) {
		this.identifyId = identifyId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
