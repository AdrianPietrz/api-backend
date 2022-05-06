package api.backend.Controllers;

import api.backend.Models.*;
import api.backend.Repositories.DirectorRepository;
import api.backend.Repositories.VideoRepository;
import api.backend.Services.UserDetailsServices;
import api.backend.Utils.JwtUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            temp.setUrl(video.getUrl());
            videoResponseList.add(temp);
        }
        return new ResponseEntity<>(videoResponseList,HttpStatus.OK);
    }

    @RequestMapping(value = "/api/video/title", method = RequestMethod.GET)
    public ResponseEntity<?> getAllVideosTitles() {
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

    @Bean
    void loadSampleVideos(){
        List<Video> videoList = videoRepository.findAll();
        if(!videoList.isEmpty()) return;
        String path = "https://www.filmweb.pl";
        Document document;
        try {
            document = Jsoup.connect("https://www.filmweb.pl/ranking/film").get();
            Elements links = document.select("a[href]");
            for (Element link : links) {
                if(link.attr("href").contains("/film/")){
                    try{
                        Document doc;
                        doc = Jsoup.connect(path + link.attr("href")).get();
                        Elements title = doc.getElementsByClass("fP__title");
                        Elements image = doc.getElementsByClass("dataSource hide");
                        Elements ele = doc.getElementsByClass("filmInfo__info cloneToCast cloneToOtherInfo");
                        Elements description = doc.getElementsByClass("filmPosterSection__plot clamped");

                        List<String> descriptionList = List.of(description.text().split("\\."));
                        String descriptionVidoe = "";
                        for( int i =0;i<descriptionList.size()-1;i++){
                            if(i!=0) descriptionVidoe += " ";
                             descriptionVidoe += descriptionList.get(i);
                        }

                        List<String> list = List.of(ele.text().split(" "));

                        Director director = null;

                        if(list.size()>2){
                            String directorName = list.get(0) + " " + list.get(1);
                            Optional<Director> tempDirector = directorRepository.findByName(directorName);

                            if(!tempDirector.isPresent()){
                                director = new Director(directorName);
                                directorRepository.save(director);
                            } else director = tempDirector.get();
                        }
                        if(director==null) continue;

                        String category = null;
                        Element meta = doc.select("div[itemprop=genre]").first();
                        if(meta!=null) {
                            List<String> lista = List.of(meta.text().split(" "));
                            category = lista.get(0);
                        }




                        //System.out.println(ele.text());

                        //System.out.println(ele.text());


                        String url = "";
                        Pattern p = Pattern.compile("\"imgUrl\".*.jpg");
                        Matcher m = p.matcher(image.text());

                        if(m.find()){


                            Pattern tv = Pattern.compile("tv");
                            Matcher mtv = tv.matcher(m.group());
                            url = m.group().substring(10);
                            if(mtv.find()){
                                url = m.group().substring(10,mtv.start()-5);
                            }
                        }

                        Optional<Video> temp = videoRepository.findByTitle(title.text());
                        if(!temp.isPresent()){
                            Video video = new Video();
                            video.setUrl(url);
                            video.setTitle(title.text());
                            video.setDirector(director);
                            video.setCategory(category);
                            video.setDescription(descriptionVidoe);
                            videoRepository.save(video);

                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}