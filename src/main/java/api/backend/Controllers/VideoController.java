package api.backend.Controllers;

import api.backend.Models.*;
import api.backend.Repositories.DirectorRepository;
import api.backend.Repositories.VideoRepository;
import api.backend.Services.UserDetailsServices;
import api.backend.Utils.JwtUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.text.DecimalFormat;
import java.util.*;
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

    @ApiOperation(value = "niepotrzebna na razie")
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
    @ApiOperation(value = "zwraca wszystkie filmy")
    @RequestMapping(value = "/api/video/all", method = RequestMethod.GET)
    public ResponseEntity<?> getAllVideos() {
        List<VideoResponse> videoResponseList = new ArrayList<>();
        List<Video> videoList= videoRepository.findAll();
        for(Video video : videoList){
            VideoResponse temp = new VideoResponse();
            temp.setDescription(video.getDescription());
            temp.setDirector(video.getDirector());
            temp.setCategory(video.getCategory());
            temp.setTitle(video.getTitle());
            temp.setId(video.getId());
            temp.setUrl(video.getUrl());
            temp.setRating(video.getRating());
            videoResponseList.add(temp);
        }
        return new ResponseEntity<>(videoResponseList,HttpStatus.OK);
    }
    @ApiOperation(value = "zwraca film o podanym id")
    @CrossOrigin
    @RequestMapping(value = "/api/video/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getVideoById(@PathVariable Long id) {
        Optional<Video> temp = videoRepository.findById(id);
        Video video = null;
        if(temp.isPresent()){
            video = temp.get();
        } else {
            return new ResponseEntity<>("Not found!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(video, HttpStatus.OK);

    }

    @ApiOperation(value = "zwraca wszytskie kategorie jake wystepuja w bazie")
    @RequestMapping(value = "/api/video/categories", method = RequestMethod.GET)
    public ResponseEntity<?> getAllCategories() {
        List<String> response = new ArrayList<>();
        List<Video> videoList= videoRepository.findAll();
        for(Video video : videoList){
            if(response.contains(video.getCategory())) continue;
            response.add(video.getCategory());
        }

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @ApiOperation(value = "zwraca tutuły wszytskich filmow, np do listy roziwjanej")
    @RequestMapping(value = "/api/video/title", method = RequestMethod.GET)
    public ResponseEntity<?> getAllVideosTitles() {
        List<String> videoResponseList = new ArrayList<>();
        List<Video> videoList= videoRepository.findAll();
        for(Video video : videoList){
            videoResponseList.add(video.getTitle());
        }
        return new ResponseEntity<>(videoResponseList,HttpStatus.OK);
    }

    @RequestMapping(value = "/api/video/filtered/title", method = RequestMethod.POST)
    public ResponseEntity<?> getAllVideosFiltredTitles(@RequestBody String category) {
        List<String> videoResponseList = new ArrayList<>();
        List<Video> videoList= videoRepository.findAll();
        category = category.substring(1,category.length() - 1 );
        for(Video video : videoList){
            if(video.getCategory().equals(category)) videoResponseList.add(video.getTitle());
        }
        return new ResponseEntity<>(videoResponseList,HttpStatus.OK);
    }

    @ApiOperation(value = "zwraca filmy z podanej kategori, kategoria jako string")
    @RequestMapping(value = "/api/video/filtered", method = RequestMethod.POST)
    public ResponseEntity<?> getAllVideosTitles(@RequestBody String category) {
        List<VideoResponse> videoResponseList = new ArrayList<>();
        List<Video> videoList= videoRepository.findAll();
        category = category.substring(1,category.length() - 1 );
        for(Video video : videoList){
            if(!video.getCategory().equals(category)) continue;
            VideoResponse temp = new VideoResponse();
            temp.setDescription(video.getDescription());
            temp.setCategory(video.getCategory());
            temp.setDirector(video.getDirector());
            temp.setId(video.getId());
            temp.setRating(video.getRating());
            temp.setTitle(video.getTitle());
            temp.setUrl(video.getUrl());
            videoResponseList.add(temp);
        }
        return new ResponseEntity<>(videoResponseList,HttpStatus.OK);
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
                        Elements title = doc.getElementsByClass("filmCoverSection__title");
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
                            if(title==null || url==null || category == null || director==null) continue;
                            Video video = new Video();
                            video.setUrl(url);
                            video.setTitle(title.text());
                            video.setDirector(director);
                            video.setCategory(category);
                            video.setDescription(descriptionVidoe);
                            float leftLimit = 1F;
                            float rightLimit = 10F;
                            float generatedFloat = leftLimit + new Random().nextFloat() * (rightLimit - leftLimit);
                            DecimalFormat df = new DecimalFormat("#.#");
                            video.setRating(df.format(generatedFloat));
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