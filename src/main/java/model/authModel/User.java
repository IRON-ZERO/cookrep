package model.authModel;

import java.sql.Date;

public class User {
	private long user_id;
	private Date created_at;
	private Date updated_at;
	private String nickname;
	private String first_namel;
	private String last_name;
	private String country;
	private String city;
	private String email;
	private String password;

	public User(long user_id, Date created_at, Date updated_at, String nickname, String first_namel, String last_name,
		String country, String city, String email, String password) {
		super();
		this.user_id = user_id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.nickname = nickname;
		this.first_namel = first_namel;
		this.last_name = last_name;
		this.country = country;
		this.city = city;
		this.email = email;
		this.password = password;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getFirst_namel() {
		return first_namel;
	}

	public void setFirst_namel(String first_namel) {
		this.first_namel = first_namel;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
