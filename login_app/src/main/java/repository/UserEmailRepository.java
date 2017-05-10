package repository;

import entities.UserEmail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEmailRepository  extends JpaRepository<UserEmail, Long> {
    UserEmail findByEncodedEmail(String email);
}
