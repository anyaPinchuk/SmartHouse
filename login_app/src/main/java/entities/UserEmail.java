package entities;

import javax.persistence.*;

@Entity(name = "user_email")
public class UserEmail {
    private Long id;
    private String email;
    private String encodedEmail;

    public UserEmail() {
    }

    public UserEmail(String email, String encodedEmail) {
        this.email = email;
        this.encodedEmail = encodedEmail;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
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

    @Column(name = "encoded_email", unique = true, nullable = false)
    public String getEncodedEmail() {
        return encodedEmail;
    }

    public void setEncodedEmail(String encodedEmail) {
        this.encodedEmail = encodedEmail;
    }
}
