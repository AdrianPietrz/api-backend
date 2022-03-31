package api.backend.Controllers;

import api.backend.Models.*;
import api.backend.Repositories.DirectorRepository;
import api.backend.Repositories.VideoRepository;
import api.backend.Services.UserDetailsServices;
import api.backend.Utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

@RestController
public class VideoController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsServices userDetailsServices;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private DirectorRepository directorRepository;

    @RequestMapping(value = "/api/video/add", method = RequestMethod.POST)
    public ResponseEntity<?> uploadVideo(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody VideoRequest videoRequest) {

        Video video = new Video();
        video.setTitle(videoRequest.getTitle());
        video.setDescription(videoRequest.getDescription());

        Director director = directorRepository.getById(1L);
        video.setDirector(director);

        director.addDirectedVideo(video);

        videoRepository.save(video);
        directorRepository.save(director);

        return new ResponseEntity<>("Video added!", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/video/{id}/comment/add", method = RequestMethod.POST)
    public ResponseEntity<?> addCommentToVideo( @RequestHeader(name = "Authorization") String token,
                                                @RequestBody CommentRequest commentRequest,
                                                @PathVariable Long id) {

        token = jwtUtil.getUsernameFromRequestToken(token);
        UserModel user = userDetailsServices.getUserByUsername(token);
        Date date = new Date();
        Comment comment = new Comment(commentRequest.getRating(),commentRequest.getText(),date);
        Video video;
        if(videoRepository.findById(id).isPresent()){
            video = videoRepository.findById(id).get();
        } else {
            return new ResponseEntity<>("Video not found!", HttpStatus.BAD_REQUEST);
        }
        video.addComment(comment);
        user.addComment(comment);

        return new ResponseEntity<>("Comment added!", HttpStatus.OK);
    }


}