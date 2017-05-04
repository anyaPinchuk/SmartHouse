package entities;

import javax.persistence.*;

@Entity
public class House {
    private Long id;
    private String ownerLogin;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_house", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }
}
