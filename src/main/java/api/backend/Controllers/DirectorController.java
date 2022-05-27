package api.backend.Controllers;

import api.backend.Models.*;
import api.backend.Repositories.DirectorRepository;
import api.backend.Services.UserDetailsServices;
import api.backend.Utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<?> getDirector(
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

    @RequestMapping(value = "/api/director", method = RequestMethod.GET)
    public ResponseEntity<?> getDirectorsNames() {
        List<Director> directorList = directorRepository.findAll();
        List<DirectorResponse> ret = new ArrayList<>();

        for(Director director : directorList){
            DirectorResponse response =  new DirectorResponse();
            response.setName(director.getName());
            response.setVideos(director.getVideos());
            ret.add(response);
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/director/name", method = RequestMethod.GET)
    public ResponseEntity<?> getDirectors() {
        List<Director> directorList = directorRepository.findAll();
        List<String> ret = new ArrayList<>();

        for(Director director : directorList){
            ret.add(director.getName());
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }


}
