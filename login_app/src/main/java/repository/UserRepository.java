package repository;

import entities.House;
import entities.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Cacheable(value = "defaultCache", key = "'user.' + args[0]", unless = "#result == null")
    User findUserByLogin(String login);

    @Cacheable(value = "defaultCache", key = "args[0] +'.' + args[1]", unless = "#result == null")
    User findByLoginAndPassword(String email, String password);

    @Cacheable(value = "defaultCache", key = "'users' + args[0].id", unless = "#result == null")
    List<User> findAllBySmartHouse(House house);

    @Cacheable(value = "defaultCache", key = "'user.house.' + args[0].id", unless = "#result == null")
    User findBySmartHouseAndRole(House house, String role);

    User findByToken(String token);
}
