package api.backend.Controllers;

import api.backend.Models.*;
import api.backend.Repositories.UserRepository;
import api.backend.Services.RegistrationService;
import api.backend.Services.UserDetailsServices;
import api.backend.Utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class UserController {

    private JwtUtil jwtTokenUtil;
    private UserDetailsServices userDetailsService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private AuthenticationManager authenticationManager;

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ResponseEntity<?> registerNewUser(@RequestHeader(name = "Authorization") String token){
        token = jwtTokenUtil.getUsernameFromRequestToken(token);
        UserModel user = userDetailsService.getUserByUsername(token);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUsers(@RequestHeader(name = "Authorization") String token){
        token = jwtTokenUtil.getUsernameFromRequestToken(token);
        UserModel user = userDetailsService.getUserByUsername(token);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/user", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@RequestHeader(name = "Authorization") String token,
                                        @RequestBody ChangePasswordRequest changePasswordRequest){
        token = jwtTokenUtil.getUsernameFromRequestToken(token);
        UserModel user = userDetailsService.getUserByUsername(token);


        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), changePasswordRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Nieprawidłowe hasło", HttpStatus.BAD_REQUEST);
        }


        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), changePasswordRequest.getNewPassword())
            );
        } catch (BadCredentialsException e) {
            user.setPassword(bCryptPasswordEncoder.encode(changePasswordRequest.getNewPassword()));

            userRepository.save(user);

            final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

            final String jwt = jwtTokenUtil.generateToken(userDetails);

            return ResponseEntity.ok(new LoginResponse(jwt));

        }
        return new ResponseEntity<>("Nowe hasło nie może być takie samo", HttpStatus.BAD_REQUEST);

    }
}
