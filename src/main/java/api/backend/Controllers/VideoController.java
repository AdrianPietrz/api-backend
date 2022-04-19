package api.backend.Controllers;

import api.backend.Models.*;
import api.backend.Repositories.DirectorRepository;
import api.backend.Repositories.VideoRepository;
import api.backend.Services.UserDetailsServices;
import api.backend.Utils.JwtUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @RequestMapping(value = "/api/video/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> uploadVideo(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody VideoRequest videoRequest,
            @PathVariable Long id) {


        token = jwtUtil.getUsernameFromRequestToken(token);
        UserModel user = userDetailsServices.getUserByUsername(token);

        if(user==null || !user.getRole().equals("ADMIN")){
            return new ResponseEntity<>("Access denied!", HttpStatus.BAD_REQUEST);
        }

        Video video = new Video();
        video.setTitle(videoRequest.getTitle());
        video.setDescription(videoRequest.getDescription());

        Director director = directorRepository.getById(id);
        video.setDirector(director);

        director.addDirectedVideo(video);

        videoRepository.save(video);
        directorRepository.save(director);

        return new ResponseEntity<>("Video added!", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/video/all", method = RequestMethod.GET)
    public ResponseEntity<?> getAllVideos() {
        List<VideoResponse> videoResponseList = new ArrayList<>();
        List<Video> videoList= videoRepository.findAll();
        for(Video video : videoList){
            VideoResponse temp = new VideoResponse();
            temp.setComments(video.getComments());
            temp.setDescription(video.getDescription());
            temp.setDirector(video.getDirector());
            temp.setTitle(video.getTitle());
            videoResponseList.add(temp);
        }
        return new ResponseEntity<>(videoResponseList,HttpStatus.OK);
    }

    @RequestMapping(value = "/api/video/title", method = RequestMethod.GET)
    public ResponseEntity<?> getAllVideosTitles() {
        System.out.println("aaaa");
        List<String> videoResponseList = new ArrayList<>();
        List<Video> videoList= videoRepository.findAll();
        for(Video video : videoList){
            videoResponseList.add(video.getTitle());
        }
        return new ResponseEntity<>(videoResponseList,HttpStatus.OK);
    }

    @RequestMapping(value = "/api/video/watch/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> watchVideo(@PathVariable Long id) throws IOException {
        String path = "src/main/resources/" + id + ".mp4";
        InputStream in = getClass()
                .getResourceAsStream(path);
        byte[] bytes = IOUtils.toByteArray(in);
        return new ResponseEntity<>(bytes,HttpStatus.OK);
    }


    @RequestMapping(value = "/api/video/{id}/comment", method = RequestMethod.POST)
    public ResponseEntity<?> addCommentToVideo( @RequestHeader(name = "Authorization") String token,
                                                @RequestBody CommentRequest commentRequest,
                                                @PathVariable Long id) {

        token = jwtUtil.getUsernameFromRequestToken(token);
        UserModel user = userDetailsServices.getUserByUsername(token);
        Date date = new Date();
        Comment comment = new Comment(commentRequest.getRating(),commentRequest.getText(),date, user.getId());
        Video video;
        if(videoRepository.findById(id).isPresent()){
            video = videoRepository.findById(id).get();
        } else {
            return new ResponseEntity<>("Video not found!", HttpStatus.FORBIDDEN);
        }
        video.addComment(comment);
        user.addComment(comment);

        return new ResponseEntity<>("Comment added!", HttpStatus.OK);
    }

    @RequestMapping(value = "/api/video/{id}/comment/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteComment( @RequestHeader(name = "Authorization") String token,
                                                @RequestBody CommentRequest commentRequest,
                                                @PathVariable Long id,
                                                @PathVariable Long commentId) {

        token = jwtUtil.getUsernameFromRequestToken(token);
        UserModel user = userDetailsServices.getUserByUsername(token);

        Video video = videoRepository.getById(id);
        if(video == null) return new ResponseEntity<>("Video not found!", HttpStatus.BAD_REQUEST);

        List<Comment> commentList = video.getComments();
        Comment comment = null;
        for(Comment temp : commentList){
            if(temp.getId()==commentId){
                comment = temp;
            }
        }
        if(comment == null) return new ResponseEntity<>("Comment not found!", HttpStatus.BAD_REQUEST);
        if(user.getRole().equals("ADMIN") || user.getId() == comment.getIDofUser()){
            commentList.remove(comment);
            videoRepository.save(video);
            return new ResponseEntity<>("Comment deleted!", HttpStatus.OK);

        } else return new ResponseEntity<>("Comment not found!", HttpStatus.FORBIDDEN);

    }




}