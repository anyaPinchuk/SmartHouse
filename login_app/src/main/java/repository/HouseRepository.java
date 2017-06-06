package repository;

import entities.House;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseRepository extends JpaRepository<House, Long> {

    @Cacheable(value = "defaultCache", key = "'house' + args[0]", unless = "#result == null")
    House findHouseById(Long id);

    @Cacheable(value = "defaultCache", key = "'house.' + args[0]", unless = "#result == null")
    House findHouseByOwnerLogin(String login);
}
