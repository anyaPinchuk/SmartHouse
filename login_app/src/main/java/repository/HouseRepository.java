package repository;

import entities.House;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseRepository  extends JpaRepository<House, Long> {
    House findHouseById(Long id);
    House findHouseByOwnerLogin(String login);
}
