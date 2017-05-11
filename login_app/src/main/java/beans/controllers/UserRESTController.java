package beans.controllers;

import beans.converters.UserConverter;
import beans.services.UserService;
import dto.UserDTO;
import entities.User;
import exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.ArrayList;


@RestController
@RequestMapping("/api/user")
public class UserRESTController {

    private UserService userService;
    private UserConverter userConverter;
    private Logger LOG = LoggerFactory.getLogger(UserRESTController.class);

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUserConverter(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_OWNER')")
    @PostMapping("/reg")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> registration(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        LOG.info("handle post request by url /api/user/reg");
        if (!bindingResult.hasErrors()) {
            if (userService.checkUsernameForExisting(userDTO.getEmail())) {
                return ResponseEntity.ok("user exists");
            } else {
                return ResponseEntity.ok(userService.addNewAccount(userDTO));
            }
        } else {
            LOG.info("bad request {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
    }


    @PreAuthorize("isAuthenticated() and hasRole('ROLE_OWNER')")
    @GetMapping("/all")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> getAll() {
        LOG.info("handle post request by url /api/user/all");
        User user;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!"anonymousUser".equals(auth.getPrincipal())) {
            user = (User) auth.getPrincipal();
            return ResponseEntity.ok(userService.findUsersByHouse(user.getSmartHouse()));
        } else return ResponseEntity.ok(new UserDTO());
    }

    @GetMapping("/checkRights")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> get() {
        LOG.info("handle post request by url /api/user/checkRights");
        UserDTO userDTO;
        User user;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!"anonymousUser".equals(auth.getPrincipal())) {
            user = (User) auth.getPrincipal();
            userDTO = userConverter.toDTO(user).orElseThrow(() -> new ServiceException("user wasn't converted"));
            userDTO.setPassword("");
            return ResponseEntity.ok(userDTO);
        } else return ResponseEntity.ok(new UserDTO());
    }

    @GetMapping("/confirmEmail")
    public RedirectView confirm(@RequestParam String token, @RequestParam Long expire) {
        if (userService.checkEmailForExisting(token, expire))
            return new RedirectView("/user/new?email=" + userService.findEmailByEncodedEmail(token));
        else return new RedirectView("/login");
    }

    @PostMapping("/new")
    public ResponseEntity<?> createAccount(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            if (!userService.checkUsernameForExisting(userDTO.getEmail())) {
                return ResponseEntity.ok(userService.createUserFromEmail(userDTO));
            }
        }
        return ResponseEntity.ok(new UserDTO());
    }
}
