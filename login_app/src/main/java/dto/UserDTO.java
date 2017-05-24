package dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class UserDTO {

    private Long id;

    @NotNull
    @Size(max = 30, min = 4)
    @Email(regexp = "^.+@.+\\.[a-z]{2,4}$")
    private String email;

    @NotNull
    @Size(max = 30, min = 2)
    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 4, max = 32)
    @NotNull
    private String password;
    private Date dateOfRegistration;

    private String role;

    public UserDTO() {
        this.email = "";
    }

    public UserDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserDTO(Long id, String login, String name, String password, Date dateOfRegistration, String role) {
        this.id = id;
        this.email = login;
        this.name = name;
        this.password = password;
        this.dateOfRegistration = dateOfRegistration;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Date getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(Date dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
