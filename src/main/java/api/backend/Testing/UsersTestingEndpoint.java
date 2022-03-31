package api.backend.Testing;

import api.backend.Models.*;
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

import java.util.ArrayList;
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
        List<VideoResponse> response = new ArrayList<>();
        List<Video> videos = videoRepository.findAll();
        for(Video video : videos){
            VideoResponse res = new VideoResponse();
            res.setTitle(video.getTitle());
            res.setDirector(video.getDirector());
            res.setComments(video.getComments());
            res.setDescription(video.getDescription());
            response.add(res);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/test/directors", method = RequestMethod.GET)
    public ResponseEntity<?> getAllDirectors(){
        List<DirectorResponse> ret = new ArrayList<>();
        List<Director> response = directorRepository.findAll();
        for(Director director: response){
            DirectorResponse temp = new DirectorResponse();
            temp.setName(director.getName());
            temp.setVideos(director.getVideos());
            ret.add(temp);
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }



}
