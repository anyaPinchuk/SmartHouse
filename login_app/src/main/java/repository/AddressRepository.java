package repository;

import entities.Address;
import entities.House;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository  extends JpaRepository<Address, Long> {
    Address findBySmartHouse(House house);
}
