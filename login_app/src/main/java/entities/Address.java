package entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Address {
    private Long id;
    private String country;
    private String city;
    private String street;
    private String house;
    private String flat;
    private House smartHouse;

    public Address() {
    }

    public Address(String country, String city, String street, String house) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.house = house;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_address", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Column(name = "city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "street")
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Column(name = "house")
    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    @Column(name = "flat")
    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_house", nullable = false)
    public House getSmartHouse() {
        return smartHouse;
    }

    public void setSmartHouse(House smartHouse) {
        this.smartHouse = smartHouse;
    }
}
