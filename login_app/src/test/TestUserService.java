import beans.converters.UserConverter;
import beans.services.UserService;

import dto.UserDTO;
import entities.House;
import entities.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import repository.HouseRepository;
import repository.UserRepository;

import java.sql.Date;
import java.util.Optional;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestUserService {

    private User owner;

    private UserDTO userDTO;

    @InjectMocks
    private UserService userService = new UserService();

    @InjectMocks
    private UserConverter converter = new UserConverter();

    @Mock
    private UserRepository userRepository;

    @Mock
    private HouseRepository houseRepository;

    @Before
    public void setUp() {
        owner = createOwner();
        userDTO = new UserDTO(2L, "an", "an", "an", new Date(System.currentTimeMillis()), "ROLE");
    }

    @Test
    public void testCreateOwnerIsSaved() {
        UserConverter userConverter = spy(converter);
        userService.setConverter(userConverter);
        when(userConverter.toDTO(owner)).thenReturn(Optional.empty());
        assertThat(userService.createOwner(userDTO).getRole(), equalTo("ROLE_OWNER"));
    }

    private User createOwner() {
       return new User(1L, "ann", "anna", "passme", new Date(System.currentTimeMillis()), "ROLE");
    }

}
