package beans.services;

import dto.DeviceDTO;
import dto.HouseDTO;
import entities.Address;
import entities.Device;
import entities.House;
import exceptions.ServiceException;
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
    private Logger LOG = LoggerFactory.getLogger(HouseService.class);

    @Autowired
    public void setHouseRepository(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    @Autowired
    public void setAddressRepository(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public void addHouse(HouseDTO houseDTO) {
        House house = new House();
        house = houseRepository.save(house);
        Address address = new Address(houseDTO.getCountry(), houseDTO.getCity(), houseDTO.getStreet(), houseDTO.getHouse());
        if (!"".equalsIgnoreCase(houseDTO.getFlat())) {
            address.setFlat(houseDTO.getFlat());
        }
        if (house.getId() != null) {
            address.setSmartHouse(house);
            addressRepository.save(address);
        } else {
            LOG.error("house was not saved");
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
