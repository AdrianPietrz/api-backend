package api.backend.Services;

import api.backend.Models.RegistrationRequest;
import api.backend.Models.UserModel;
import api.backend.Repositories.UserRepository;
import api.backend.Utils.EmailValidation;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final EmailValidation emailValidator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Bean
    private void createAdmin(){
        if(userRepository.findByUsername("Admin").isPresent()) return;
        String encodedPassword = bCryptPasswordEncoder.encode("admin");
        UserModel user = new UserModel("Admin", "admin@admin.admin", encodedPassword, "ADMIN");
        userRepository.save(user);
    }

    public ResponseEntity<?> register(RegistrationRequest request) {
        boolean isValid = emailValidator.patternMatches(request.getEmail());
        if(!isValid){
            return new ResponseEntity<>("Email is not valid", HttpStatus.BAD_REQUEST);
        }
        boolean userExist = userRepository.findByEmail(request.getEmail()).isPresent();
        if(userExist){
            return new ResponseEntity<>("User already exist", HttpStatus.BAD_REQUEST);
        }
        userExist = userRepository.findByUsername(request.getUsername()).isPresent();
        if(userExist){
            return new ResponseEntity<>("User already exist", HttpStatus.BAD_REQUEST);
        }
        String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
        UserModel user = new UserModel (request.getUsername(),request.getEmail(),encodedPassword,"USER");
        userRepository.save(user);
        return new ResponseEntity<>("User registered", HttpStatus.OK);
    }
}
