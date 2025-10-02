package dto.user;

import model.User;

import java.sql.Date;

// 프로필 출력에 활용할 DTO입니다.
public class UserDTO {
    private String id;
    private String nickname;
    private String firstName;
    private String lastName;
    private String country;
    private String city;
    private String email;

    public String getId() {
        return id;
    }
    // DTO로 변환
    public static UserDTO from(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setNickname(user.getNickname());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setCountry(user.getCountry());
        userDTO.setCity(user.getCity());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }

    // getter, setter
    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    @Override
    public String toString() {
        return "UserDTO{" +
            "id='" + id + '\'' +
            ", nickname='" + nickname + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", country='" + country + '\'' +
            ", city='" + city + '\'' +
            ", email='" + email + '\'' +
            '}';
    }
}
