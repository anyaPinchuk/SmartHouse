package beans.services;

import beans.converters.UserConverter;
import entities.House;
import entities.UserEmail;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.DigestUtils;
import repository.HouseRepository;
import repository.UserEmailRepository;
import repository.UserRepository;
import dto.UserDTO;
import entities.User;
import exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;
    private HouseRepository houseRepository;
    private UserConverter converter;
    private UserEmailRepository emailRepository;

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

    @Autowired
    public void setEmailRepository(UserEmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    private UserDTO convertToDTO(User owner) {
        if (owner == null) return null;
        return converter.toDTO(owner).orElseThrow(() -> new ServiceException("user wasn't converted"));
    }

    public UserDTO loadUserByUsername(String username) {
        return convertToDTO(loadAccountByUsername(username));
    }


    private User loadAccountByUsername(String username) {
        return userRepository.findUserByLogin(username);
    }

    public UserDTO addNewAccount(UserDTO userDTO) {
        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = new User(userDTO.getEmail(), DigestUtils.md5DigestAsHex(userDTO.getPassword().getBytes()));
        user.setSmartHouse(owner.getSmartHouse());
        user.setRole(userDTO.getRole());
        userRepository.save(user);
        return convertToDTO(user);
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

    public UserDTO createUserFromEmail(UserDTO userDTO) {
        User user = new User(userDTO.getEmail(), DigestUtils.md5DigestAsHex(userDTO.getPassword().getBytes()));
        user.setSmartHouse(houseRepository.findHouseByOwnerLogin(userDTO.getEmail()));
        user.setRole("ROLE_OWNER");
        userRepository.save(user);
        return convertToDTO(user);
    }

    UserEmail saveUserEmail(UserEmail userEmail) {
        return emailRepository.save(userEmail);
    }

    public String findEmailByEncodedEmail(String encodedEmail) {
        return encodedEmail != null ? emailRepository.findByEncodedEmail(encodedEmail).getEmail() : null;
    }

    public boolean checkEmailForExisting(String encodedEmail, Long expire) {
        if (encodedEmail == null) return false;
        UserEmail userEmail = emailRepository.findByEncodedEmail(encodedEmail);
        if (userEmail != null) {
            Long currentTime = System.currentTimeMillis();
            return expire.equals(userEmail.getExpireDate()) && currentTime >= userEmail.getExpireDate();
        } else return false;
    }

    public List<UserDTO> findUsersByHouse(House house) {
        List<User> users = userRepository.findAllBySmartHouse(house);
        List<UserDTO> userDTOS = new ArrayList<>();

        if (users != null) {
            users.forEach(obj -> {
                if (!obj.getRole().equalsIgnoreCase("ROLE_OWNER")) {
                    UserDTO dto = convertToDTO(obj);
                    dto.setPassword("");
                    userDTOS.add(dto);
                }
            });
            return userDTOS;
        } else return new ArrayList<>();
    }
}
