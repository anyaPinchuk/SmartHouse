package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
public class User implements Serializable {
    private Long id;
    private String login;
    private String name;
    private String password;
    private Date dateOfRegistration;
    private String role;
    private House smartHouse;
    private String sessionID;
    private String token;

    public User() {

    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.dateOfRegistration = new Date(System.currentTimeMillis());
    }

    public User(Long id, String name, String login, String password, Date dateOfRegistration, String role) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.password = password;
        this.dateOfRegistration = dateOfRegistration;
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_user", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "login", unique = true, nullable = false)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "password", nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "date_of_registration", nullable = false)
    public Date getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(Date dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    @Column(name = "role", nullable = false)
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_house")
    public House getSmartHouse() {
        return smartHouse;
    }

    public void setSmartHouse(House smartHouse) {
        this.smartHouse = smartHouse;
    }

    @Column(name = "session_id")
    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionID() {
        return sessionID;
    }

    @Column(name = "token", unique = true)
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
