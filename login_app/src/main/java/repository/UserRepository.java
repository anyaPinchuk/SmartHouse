package repository;

import entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long> {
    User findUserByLogin(String login);

    User findByLoginAndPassword(String email, String password);
}
