package entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name = "user_email")
public class UserEmail {
    private Long id;
    private String email;
    private String key;
    private Timestamp expireDate;
    private House smartHouse;

    public UserEmail() {
    }

    public UserEmail(String email, String key, Timestamp expireDate) {
        this.email = email;
        this.key = key;
        this.expireDate = expireDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_user_email", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "email", unique = true, nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "unique_key", unique = true, nullable = false)
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Column(name = "expire_date", nullable = false)
    public Timestamp getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Timestamp expireDate) {
        this.expireDate = expireDate;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_house", nullable = false)
    public House getSmartHouse() {
        return smartHouse;
    }

    public void setSmartHouse(House smartHouse) {
        this.smartHouse = smartHouse;
    }
}
