package entities;

import javax.persistence.*;

@Entity
public class House {
    private Long id;
    private String ownerLogin;
    private Address address;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_house", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "owner_login", nullable = false)
    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "smartHouse", cascade = CascadeType.ALL)
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
