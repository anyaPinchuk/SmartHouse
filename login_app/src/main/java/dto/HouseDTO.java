package dto;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class HouseDTO {
    private Long id;
    @NotNull
    @Size(max = 20, min = 3)
    private String country;
    @NotNull
    @Size(max = 30, min = 3)
    private String city;
    @NotNull
    @Size(max = 30, min = 4)
    private String street;
    @NotNull
    @Size(max = 10, min = 1)
    private String house;
    @Size(max = 10, min = 1)
    private String flat;
    @Size(max = 30, min = 4)
    @Email(regexp = "^.+@.+\\.[a-z]{2,4}$")
    private String owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
