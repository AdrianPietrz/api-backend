package api.backend.Testing;

import api.backend.Models.RegistrationRequest;
import api.backend.Models.UserModel;
import api.backend.Repositories.UserRepository;
import api.backend.Services.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class UsersTestingEndpoint {


    private final UserRepository userRepository;

    @RequestMapping(value = "/test/users", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUsers(){
        List<UserModel> response = userRepository.findAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }




}
