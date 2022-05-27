package api.backend.Controllers;

import api.backend.Models.LoginRequest;
import api.backend.Services.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



@RestController
@AllArgsConstructor
public class LoginController {

    @Autowired
    LoginService loginService;


    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody LoginRequest authenticationRequest) throws Exception {
        return loginService.createAuthenticationToken(authenticationRequest);
    }







}
