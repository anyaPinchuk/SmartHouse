package beans.services;

import beans.converters.UserConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import repository.UserRepository;
import dto.UserDTO;
import entities.User;
import exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;
    private UserConverter converter;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setConverter(UserConverter converter) {
        this.converter = converter;
    }

    protected UserDTO convertToDTO(User owner) {
        if (owner == null) return null;
        return converter.toDTO(owner).orElseThrow(() -> new ServiceException("account wasn't converted"));
    }

    public UserDTO loadUserByUsername(String username) {
        return convertToDTO(loadAccountByUsername(username));
    }


    protected User loadAccountByUsername(String username) {
        return userRepository.findUserByLogin(username);
    }

    public UserDTO addNewAccount(UserDTO userDTO, String role) {
        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = new User(userDTO.getEmail(), userDTO.getPassword());
        user.setSmartHouse(owner.getSmartHouse());
        if ("adult".equalsIgnoreCase(role)) user.setRole("ROLE_ADULT");
        else if ("child".equalsIgnoreCase(role)) user.setRole("ROLE_CHILD");
        userRepository.save(user);
        return converter.toDTO(user).orElseThrow(() -> new ServiceException("user wasn't converted"));
    }


    public boolean checkUsernameForExisting(String username) {
        return username != null && userRepository.findUserByLogin(username) != null;
    }
}
