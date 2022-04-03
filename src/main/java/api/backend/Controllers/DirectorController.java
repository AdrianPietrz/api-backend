package api.backend.Controllers;

import api.backend.Models.*;
import api.backend.Repositories.DirectorRepository;
import api.backend.Services.UserDetailsServices;
import api.backend.Utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DirectorController {

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServices userDetailsServices;


    @RequestMapping(value = "/api/director", method = RequestMethod.POST)
    public ResponseEntity<?> uploadVideo(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody DirectorRequest directorRequest) {

        token = jwtUtil.getUsernameFromRequestToken(token);
        UserModel user = userDetailsServices.getUserByUsername(token);

        if(user==null || !user.getRole().equals("ADMIN")){
            return new ResponseEntity<>("Access denied!", HttpStatus.FORBIDDEN);
        }

        if(directorRepository.findByName(directorRequest.getName()).isPresent()){
            return new ResponseEntity<>("Director already exist!", HttpStatus.BAD_REQUEST);
        }

        Director director = new Director(directorRequest.getName());
        directorRepository.save(director);

        return new ResponseEntity<>("Director added!", HttpStatus.CREATED);
    }
}
