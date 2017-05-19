package beans.services;

import dto.HouseDTO;
import entities.Address;
import entities.House;
import entities.UserEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.AddressRepository;
import repository.HouseRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class HouseService {
    private HouseRepository houseRepository;
    private AddressRepository addressRepository;
    private UserService userService;
    private MailService mailService;
    private Logger LOG = LoggerFactory.getLogger(HouseService.class);

    @Autowired
    public void setHouseRepository(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    @Autowired
    public void setAddressRepository(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public boolean addHouse(HouseDTO houseDTO) {
        House house = new House();
        if (!userService.checkUsernameForExisting(houseDTO.getOwnerLogin())) {
            house.setOwnerLogin(houseDTO.getOwnerLogin());
        } else return false;
        house = houseRepository.save(house);
        if (house.getId() != null) {
            Address address = new Address(houseDTO.getCountry(), houseDTO.getCity(), houseDTO.getStreet(), houseDTO.getHouse());
            if (!"".equalsIgnoreCase(houseDTO.getFlat())) {
                address.setFlat(houseDTO.getFlat());
            }
            address.setSmartHouse(house);
            addressRepository.save(address);
            Long expireDate = System.currentTimeMillis() + 1209600000;
            Timestamp timestamp = new Timestamp(expireDate);
            UserEmail userEmail = new UserEmail(houseDTO.getOwnerLogin(), userService.getUniqueKey(), timestamp);
            mailService.sendEmail(houseDTO.getOwnerLogin(), userEmail.getKey());
            userEmail.setSmartHouse(house);
            userService.saveUserEmail(userEmail);
            return true;
        }
        LOG.error("house was not saved");
        return false;
    }

    public List<HouseDTO> getAll() {
        List<House> houses = houseRepository.findAll();
        List<HouseDTO> houseDTOS = new ArrayList<>();
        houses.forEach(obj -> {
            Address address = addressRepository.findBySmartHouse(obj);
            HouseDTO dto = new HouseDTO(address.getCountry(), address.getCity(), address.getStreet(), address.getHouse(),
                    address.getFlat(), obj.getOwnerLogin());
            houseDTOS.add(dto);
        });
        return houseDTOS;
    }


}
