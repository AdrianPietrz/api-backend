package api.backend.Controllers;

import api.backend.Models.Director;
import api.backend.Models.DirectorRequest;
import api.backend.Models.Video;
import api.backend.Models.VideoRequest;
import api.backend.Repositories.DirectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DirectorController {

    @Autowired
    private DirectorRepository directorRepository;


    @RequestMapping(value = "/api/director/add", method = RequestMethod.POST)
    public ResponseEntity<?> uploadVideo(
            @RequestBody DirectorRequest directorRequest) {

        if(directorRepository.findByName(directorRequest.getName()).isPresent()){
            return new ResponseEntity<>("Director already exist!", HttpStatus.BAD_REQUEST);
        }

        Director director = new Director(directorRequest.getName());
        directorRepository.save(director);

        return new ResponseEntity<>("Director added!", HttpStatus.CREATED);
    }
}
