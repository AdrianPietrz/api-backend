package api.backend.Testing;

import api.backend.Models.Director;
import api.backend.Models.RegistrationRequest;
import api.backend.Models.UserModel;
import api.backend.Models.Video;
import api.backend.Repositories.DirectorRepository;
import api.backend.Repositories.UserRepository;
import api.backend.Repositories.VideoRepository;
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
    private final VideoRepository videoRepository;
    private  final DirectorRepository directorRepository;

    @RequestMapping(value = "/test/users", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUsers(){
        List<UserModel> response = userRepository.findAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @RequestMapping(value = "/test/videos", method = RequestMethod.GET)
    public ResponseEntity<?> getAllVideos(){
        List<Video> response = videoRepository.findAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/test/directors", method = RequestMethod.GET)
    public ResponseEntity<?> getAllDirectors(){
        List<Director> response = directorRepository.findAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
