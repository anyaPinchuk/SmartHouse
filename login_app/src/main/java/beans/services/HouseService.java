package beans.services;

import dto.HouseDTO;
import dto.UserDTO;
import entities.Address;
import entities.House;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.AddressRepository;
import repository.HouseRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class HouseService {
    private HouseRepository houseRepository;
    private AddressRepository addressRepository;
    private UserService userService;
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
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public boolean addHouse(HouseDTO houseDTO) {
        House house = new House();
        UserDTO userDTO = new UserDTO(houseDTO.getOwnerLogin(), houseDTO.getPassword());
        if (!userService.checkUsernameForExisting(userDTO.getEmail())) {
            house.setOwnerLogin(houseDTO.getOwnerLogin());
        } else return false;
        house = houseRepository.save(house);
        if (house.getId() != null) {
            userService.addOwner(userDTO, house);
            Address address = new Address(houseDTO.getCountry(), houseDTO.getCity(), houseDTO.getStreet(), houseDTO.getHouse());
            if (!"".equalsIgnoreCase(houseDTO.getFlat())) {
                address.setFlat(houseDTO.getFlat());
            }
            address.setSmartHouse(house);
            addressRepository.save(address);
            return true;
        } else {
            LOG.error("house was not saved");
            return false;
        }
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
