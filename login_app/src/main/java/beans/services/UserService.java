package beans.services;

import beans.converters.UserConverter;
import entities.House;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;
import repository.HouseRepository;
import repository.UserRepository;
import dto.UserDTO;
import entities.User;
import exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;
    private HouseRepository houseRepository;
    private UserConverter converter;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setHouseRepository(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
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

    public UserDTO addNewAccount(UserDTO userDTO) {
        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = new User(userDTO.getEmail(), DigestUtils.md5DigestAsHex(userDTO.getPassword().getBytes()));
        user.setSmartHouse(owner.getSmartHouse());
        user.setRole(userDTO.getRole());
        userRepository.save(user);
        return converter.toDTO(user).orElseThrow(() -> new ServiceException("user wasn't converted"));
    }

    public boolean checkUsernameForExisting(String username) {
        return username != null && userRepository.findUserByLogin(username) != null;
    }

    public User addOwner(UserDTO userDTO, House house) {
        User user = new User(userDTO.getEmail(), userDTO.getPassword());
        user.setRole("ROLE_OWNER");
        user.setSmartHouse(house);
        userRepository.save(user);
        return user;
    }
}
