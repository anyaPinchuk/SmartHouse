package repository;

import entities.House;
import entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository  extends JpaRepository<User, Long> {
    User findUserByLogin(String login);

    User findByLoginAndPassword(String email, String password);

    List<User> findAllBySmartHouse(House house);
}
