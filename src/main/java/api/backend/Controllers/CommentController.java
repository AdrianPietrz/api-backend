package api.backend.Controllers;


import api.backend.Models.Comment;
import api.backend.Models.CommentRequest;
import api.backend.Models.UserModel;
import api.backend.Models.Video;
import api.backend.Repositories.CommentRepository;
import api.backend.Repositories.UserRepository;
import api.backend.Repositories.VideoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class CommentController {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    VideoRepository videoRepository;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/api/comments/video/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getVideoComments(@PathVariable Long id){

        Video video = videoRepository.findById(id).get();
        List<Comment> res = commentRepository.findAllByVideo(video);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/comments/user/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getUserComments(@PathVariable Long id){

        UserModel user = userRepository.findById(id).get();

        List<Comment> res = commentRepository.findAllByUser(user);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/comments/video/{videoId}/user/{userId}", method = RequestMethod.POST)
    public ResponseEntity<?> uploadComment(@PathVariable Long videoId
            , @PathVariable Long userId, @RequestBody CommentRequest request){

        UserModel user = userRepository.getById(userId);
        Video video = videoRepository.getById(videoId);

        Comment comment = new Comment();
        comment.setComment(request.getText());
        comment.setUser(user);
        comment.setVideo(video);
        commentRepository.save(comment);

        return new ResponseEntity<>("Added!", HttpStatus.OK);
    }

    @RequestMapping(value = "/api/comments/video/{videoId}/rating/{rate}", method = RequestMethod.POST)
    public ResponseEntity<?> uploadRate(@PathVariable Long videoId
            , @PathVariable Double rate){

        Video video = videoRepository.getById(videoId);
        Double currentRate = Double.valueOf(video.getRating())*Double.valueOf(video.getRates());
        currentRate = currentRate + rate / Double.valueOf(video.getRates()+1);
        video.setRates(video.getRates()+1);
        video.setRating(String.valueOf(currentRate));

        videoRepository.save(video);

        return new ResponseEntity<>("Updated!", HttpStatus.OK);
    }

}
