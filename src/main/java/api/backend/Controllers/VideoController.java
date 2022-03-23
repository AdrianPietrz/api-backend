package api.backend.Controllers;

import api.backend.Models.Comment;
import api.backend.Models.CommentRequest;
import api.backend.Models.UserModel;
import api.backend.Models.Video;
import api.backend.Repositories.VideoRepository;
import api.backend.Services.UserDetailsServices;
import api.backend.Utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

@RestController
public class VideoController {


    private JwtUtil jwtUtil;
    private UserDetailsServices userDetailsServices;
    private VideoRepository videoRepository;

    @RequestMapping(value = "/api/video/add", method = RequestMethod.POST)
    public ResponseEntity<?> uploadVideo(
            @RequestHeader(name = "Authorization") String token,
            @RequestParam("file") MultipartFile file) {

        String path = "src/main/resources/" + "video" + ".mp4";
        File appFile = new File(path);
        try {
            appFile.createNewFile();
            FileOutputStream fout = new FileOutputStream(appFile);
            fout.write(file.getBytes());
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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