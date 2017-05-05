package beans.controllers;

import beans.services.HouseService;
import dto.HouseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/house")
public class HouseRESTController {
    private HouseService houseService;
    private Logger LOG = LoggerFactory.getLogger(HouseRESTController.class);

    @Autowired
    public void setHouseService(HouseService houseService) {
        this.houseService = houseService;
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> add(@RequestBody HouseDTO houseDTO, BindingResult bindingResult) {
        LOG.info("handle post request by url /api/house/add");
        if (!bindingResult.hasErrors()) {
            return ResponseEntity.ok(houseService.addHouse(houseDTO));
        } else {
            LOG.info("bad request {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> getAll() {
        LOG.info("handle post request by url /api/house/all");
        return ResponseEntity.ok(houseService.getAll());
    }
}
