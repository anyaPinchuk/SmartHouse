package repository;

import entities.Address;
import entities.House;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository  extends JpaRepository<Address, Long> {
    @Cacheable(value = "defaultCache", key = "'address' + args[0].address.id", unless = "#result == null")
    Address findBySmartHouse(House house);
}
