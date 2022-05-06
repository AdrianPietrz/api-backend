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
    public ResponseEntity<?> uploadDirector(
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

    @RequestMapping(value = "/api/director/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getDirectors(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable Long id) {

        if(directorRepository.findById(id).isPresent()){
            Director director = directorRepository.getById(id);
            DirectorResponse response = new DirectorResponse();
            response.setName(director.getName());
            response.setVideos(director.getVideos());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>("Director not found!", HttpStatus.BAD_REQUEST);
    }
}
