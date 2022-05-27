package api.backend.Services;

import api.backend.Models.LoginRequest;
import api.backend.Models.LoginResponse;
import api.backend.Models.UserModel;
import api.backend.Repositories.UserRepository;
import api.backend.Utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@AllArgsConstructor
public class LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsServices userDetailsServices;

    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody LoginRequest authenticationRequest) throws Exception {
        Optional<UserModel> user = userRepository.findByUsername(authenticationRequest.getUsername());

        if( user == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("User does not exist");
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Incorrect username or password");
        }


        final UserDetails userDetails = userDetailsServices.loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new LoginResponse(jwt));
    }
}
