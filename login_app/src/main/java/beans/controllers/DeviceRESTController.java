package beans.controllers;

import beans.services.DeviceService;
import dto.DeviceDTO;
import entities.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/device")
public class DeviceRESTController {
    private DeviceService deviceService;
    private Logger LOG = LoggerFactory.getLogger(DeviceRESTController.class);

    @Autowired
    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole({'ROLE_OWNER', 'ROLE_CHILD', 'ROLE_ADULT', 'ROLE_GUEST'})")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> getAll() {
        LOG.info("handle post request by url /api/device/all");
        return ResponseEntity.ok(deviceService.getAll());
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyRole({'ROLE_OWNER', 'ROLE_CHILD', 'ROLE_ADULT', 'ROLE_GUEST'})")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> update(@RequestBody DeviceDTO deviceDTO, BindingResult bindingResult) {
        LOG.info("handle post request by url /api/device/update");
        if (!bindingResult.hasErrors()) {
            Device device = deviceService.updateDevice(deviceDTO);
            return ResponseEntity.ok(device);
        } else {
            LOG.info("bad request {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
    }


    @GetMapping("/get")
    public ResponseEntity<?> getByUser(@RequestParam String email) {
        if (!"".equals(email)) {
            return ResponseEntity.ok(deviceService.getAllByUser(email));
        } else return ResponseEntity.ok(new ArrayList<>());
    }

    @PostMapping("/saveAll")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> saveAll(@RequestBody List<DeviceDTO> devices, BindingResult bindingResult) {
        LOG.info("handle post request by url /api/device/saveAll");
        if (!bindingResult.hasErrors()) {
            deviceService.updateDevices(devices);
            return ResponseEntity.ok().build();
        } else {
            LOG.info("bad request {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> add(@RequestBody DeviceDTO deviceDTO, BindingResult bindingResult) {
        LOG.info("handle post request by url /api/device/add");
        if (!bindingResult.hasErrors()) {
            Device device = deviceService.saveDevice(deviceDTO);
            return ResponseEntity.ok(device);
        } else {
            LOG.info("bad request {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
    }
}
