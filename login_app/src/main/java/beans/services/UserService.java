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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private UserRepository userRepository;
    private HouseRepository houseRepository;
    private UserConverter converter;
    private UserEmailRepository emailRepository;
    private MailService mailService;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setHouseRepository(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
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


    public User loadAccountByUsername(String username) {
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

    public String findEmailByKey(String token) {
        return token != null ? emailRepository.findByKey(token).getEmail() : null;
    }

    public boolean checkKeyForExisting(String key) {
        if (key == null) return false;
        UserEmail userEmail = emailRepository.findByKey(key);
        if (userEmail != null) {
            Long current = System.currentTimeMillis();
            Timestamp currentTime = new Timestamp(current);
            Timestamp expire = userEmail.getExpireDate();
            return currentTime.compareTo(expire) < 0;
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

    public void sendConfirm(String email) {
        UserEmail userEmail = emailRepository.findByEmail(email);
        if (userEmail != null) {
            userEmail.setKey(getUniqueKey());
            mailService.sendEmail(email, userEmail.getKey());
            emailRepository.save(userEmail);
        }
    }

    String getUniqueKey() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
