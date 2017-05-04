package beans.converters;

import dto.UserDTO;
import entities.User;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Optional;

@Component
public class UserConverter {

    public Optional<User> toEntity(UserDTO dto) {
        if (dto == null) return Optional.empty();
        return Optional.of(new User(dto.getId(), dto.getEmail(),
                dto.getPassword(), new Date(dto.getDateOfRegistration().getTime()), dto.getRole()));
    }

    public Optional<UserDTO> toDTO(User entity) {
        if (entity == null) return Optional.empty();
        return Optional.of(new UserDTO(entity.getId(), entity.getLogin(), entity.getPassword(),
                new java.util.Date(entity.getDateOfRegistration().getTime()), entity.getRole()));
    }
}
